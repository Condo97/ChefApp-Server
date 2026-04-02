package com.pantrypro;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.config.EnvConfig;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.Server;
import com.pantrypro.database.FlywayMigrator;
import com.pantrypro.exceptions.AuthTokenExpiredException;
import com.pantrypro.exceptions.RateLimitedException;
import com.pantrypro.exceptions.ResponseStatusException;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.endpoints.*;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.ResponseStatus;
import com.pantrypro.networking.server.request.*;
import com.pantrypro.networking.server.response.*;

import com.pantrypro.util.PersistentLogger;
import com.pantrypro.util.StartupHealthCheck;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

public class Main {

    private static final int MAX_THREADS = 4;
    private static final int MIN_THREADS = 1;
    private static final int TIMEOUT_MS = -1; //30000;

    private static final int DEFAULT_PORT = 800;

    public static void main(String... args) throws SQLException {
        // Initialize persistent logging FIRST
        PersistentLogger.initialize();
        PersistentLogger.info(PersistentLogger.SERVER, "Starting PantryPro Server — threads=" + MAX_THREADS + ", port=" + EnvConfig.SERVER_PORT);

        // Set up MySQL Driver
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            PersistentLogger.error(PersistentLogger.SERVER, "Failed to register MySQL driver", e);
        }

        // Resolve DB credentials: env vars take priority, fall back to Keys
        String mysqlUrl = EnvConfig.MYSQL_URL;
        String mysqlUser = EnvConfig.MYSQL_USER != null ? EnvConfig.MYSQL_USER : Keys.MYSQL_USER;
        String mysqlPass = EnvConfig.MYSQL_PASS != null ? EnvConfig.MYSQL_PASS : Keys.MYSQL_PASS;

        // Run Flyway database migrations
        try {
            FlywayMigrator.migrate(mysqlUrl, mysqlUser, mysqlPass);
            PersistentLogger.info(PersistentLogger.SERVER, "Flyway migrations completed successfully.");
        } catch (Exception e) {
            PersistentLogger.error(PersistentLogger.SERVER, "Flyway migration failed — continuing startup. Error: " + e.getMessage(), e);
        }

        // Configure web sockets
        configureWebSockets();

        // Set up SQLConnectionPoolInstance
        SQLConnectionPoolInstance.create(mysqlUrl, mysqlUser, mysqlPass, MAX_THREADS * 4);

        // Run startup health checks
        StartupHealthCheck.runAll(mysqlUrl, mysqlUser, mysqlPass);

        // Register shutdown hook for logger cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> PersistentLogger.shutdown()));

        // Schedule rate limiter cleanup every 5 minutes
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "rate-limiter-cleanup");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(com.pantrypro.core.RateLimiter::cleanup, 5, 5, TimeUnit.MINUTES);

        // Set up Spark thread pool and port
//        threadPool(MAX_THREADS, MIN_THREADS, TIMEOUT_MS);
        port(EnvConfig.SERVER_PORT);

        // Set up Policy static file location
        staticFiles.location("/policies");

        // Set up SSL (disabled when behind reverse proxy like Cloudflare Tunnel)
        if (EnvConfig.SSL_ENABLED) {
            String sslKeystore = EnvConfig.SSL_KEYSTORE;
            String sslPassword = EnvConfig.SSL_PASSWORD != null ? EnvConfig.SSL_PASSWORD : Keys.sslPassword;
            secure(sslKeystore, sslPassword, null, null);
            PersistentLogger.info(PersistentLogger.SERVER, "SSL enabled with keystore: " + sslKeystore);
        } else {
            PersistentLogger.info(PersistentLogger.SERVER, "SSL disabled — expecting reverse proxy to handle TLS");
        }

        // Health check endpoint (no auth required, outside versioned paths)
        get("/health", (req, res) -> {
            res.type("application/json");
            return HealthCheckEndpoint.check();
        });

        // Set up https v1 path
        path("/v1", () -> configureHttpEndpoints());

        // Set up https dev path
        path("/dev", () -> configureHttpEndpoints(true));

        // Set up legacy / path, though technically I think configureHttp() can be just left plain there in the method without the path call
        configureHttpEndpoints();

        // Exception Handling
        exception(AuthTokenExpiredException.class, (e, req, res) -> {
            PersistentLogger.warn(PersistentLogger.SERVER, "AuthTokenExpiredException: " + e.getResponseStatus() + " - " + e.getResponseMessage());
            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(e.getResponseStatus(), e.getResponseMessage()));
        });

        exception(RateLimitedException.class, (e, req, res) -> {
            PersistentLogger.warn(PersistentLogger.SERVER, "RateLimitedException: " + e.getResponseStatus() + " - " + e.getResponseMessage());
            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(e.getResponseStatus(), e.getResponseMessage()));
        });

        exception(ResponseStatusException.class, (e, req, res) -> {
            PersistentLogger.warn(PersistentLogger.SERVER, "ResponseStatusException: " + e.getResponseStatus() + " - " + e.getResponseMessage());
            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(e.getResponseStatus(), e.getResponseMessage()));
        });

        exception(JsonMappingException.class, (error, req, res) -> {
            PersistentLogger.error(PersistentLogger.SERVER, "JsonMappingException on " + req.uri() + ": " + error.getMessage(), error);
            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.JSON_ERROR, "Invalid request format"));
        });

        exception(OpenAIGPTException.class, (error, req, res) -> {
            PersistentLogger.error(PersistentLogger.SERVER, "OpenAIGPTException on " + req.uri() + ": " + error.getMessage(), error);
            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.OAIGPT_ERROR, "AI generation failed, please try again"));
        });

        exception(IllegalArgumentException.class, (error, req, res) -> {
            PersistentLogger.error(PersistentLogger.SERVER, "IllegalArgumentException on " + req.uri() + ": " + error.getMessage(), error);
            String message = error.getMessage() != null ? error.getMessage() : "Illegal argument";
            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.ILLEGAL_ARGUMENT, message));
        });

        exception(Exception.class, (error, req, res) -> {
            PersistentLogger.error(PersistentLogger.SERVER, "Unhandled exception on " + req.uri() + ": " + error.getMessage(), error);
            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.UNHANDLED_ERROR, "An unexpected error occurred"));
        });

        // Handle not found (404)
        notFound((req, res) -> {
            PersistentLogger.warn(PersistentLogger.SERVER, "404 Not Found: " + req.uri() + " (active threads: " + activeThreadCount() + ")");
            res.status(404);
            return Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.UNHANDLED_ERROR, "Endpoint not found");
        });
    }

    private static void configureWebSockets() {
        // TODO: Do constants and make this better :O
        /* v1 */
        final String v1Path = "/v1";

        /* dev */
        final String devPath = "/dev";

    }

    private static void configureHttpEndpoints() {
        configureHttpEndpoints(false);
    }

    private static void configureHttpEndpoints(boolean dev) {
        // POST Functions
        post(Constants.URIs.ADD_OR_REMOVE_LIKE_OR_DISLIKE, (req, res) -> Server.respond(req, AddOrRemoveLikeOrDislikeRequest.class, new AddOrRemoveLikeOrDislikeEndpoint()));
        post(Constants.URIs.GET_AND_DUPLICATE_RECIPE, (req, res) -> Server.respond(req, GetAndDuplicateRecipeRequest.class, new GetAndDuplicateRecipeEndpoint()));
        post(Constants.URIs.REGISTER_APNS, (req, res) -> Server.respond(req, APNSRegistrationRequest.class, new APNSRegistrationEndpoint()));
        post(Constants.URIs.SEND_PUSH_NOTIFICATION, (req, res) -> Server.respond(req, SendPushNotificationRequest.class, new SendPushNotificationEndpoint()));
        post(Constants.URIs.SEARCH_IMAGES, (req, res) -> Server.respond(req, SearchImagesRequest.class, new SearchImagesEndpoint()));
        post(Constants.URIs.UPDATE_RECIPE_IMAGE_URL, (req, res) -> Server.respond(req, UpdateRecipeImageURLRequest.class, new UpdateRecipeImageURLEndpoint()));

        post(Constants.URIs.TIK_API_GET_VIDEO_INFO, (req, res) -> Server.respond(
                req,
                TikAPIGetVideoInfoRequest.class,
                new TikAPIGetVideoInfoEndpoint()
        ));

        post(Constants.URIs.TIK_TOK_SEARCH, (req, res) -> Server.respond(
                req,
                TikTokSearchRequest.class,
                new TikTokSearchEndpoint()
        ));

        post(Constants.URIs.TRANSCRIBE_SPEECH, (req, res) -> Server.respond(
                req,
                TranscribeSpeechRequest.class,
                new TranscribeSpeechEndpoint()
        ));

        post(Constants.URIs.CATEGORIZE_INGREDIENTS, (req, res) -> Server.respond(req, CategorizeIngredientsRequest.class, new CategorizeIngredientsEndpoint()));
        post(Constants.URIs.CREATE_RECIPE_IDEA, (req, res) -> Server.respond(req, CreateIdeaRecipeRequest.class, new CreateRecipeIdeaEndpoint()));
        post(Constants.URIs.MAKE_RECIPE_FROM_IDEA, (req, res) -> Server.respond(req, MakeRecipeRequest.class, new MakeRecipeEndpoint()));
        post(Constants.URIs.PARSE_PANTRY_ITEMS_URI, (req, res) -> Server.respond(req, ParsePantryItemsRequest.class, new ParsePantryItemsEndpoint()));
        post(Constants.URIs.REGENERATE_RECIPE_DIRECTIONS_AND_UPDATE_MEASURED_INGREDIENTS, (req, res) -> Server.respond(req, RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest.class, new RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsEndpoint()));
        post(Constants.URIs.TAG_RECIPE_IDEA, (req, res) -> Server.respond(req, TagRecipeRequest.class, new TagRecipeEndpoint()));
        post(Constants.URIs.LOG_PINTEREST_CONVERSION, (req, res) -> Server.respond(req, LogPinterestConversionRequest.class, new LogPinterestConversionEndpoint()));

        post(Constants.URIs.GET_ALL_TAGS_URI, Server::getAllTags);
        post(Constants.URIs.GET_IS_PREMIUM_URI, Server::getIsPremium);
        post(Constants.URIs.GET_REMAINING_URI, Server::getRemainingIdeaRecipes);
        post(Constants.URIs.REGISTER_TRANSACTION_URI, Server::registerTransaction);
        post(Constants.URIs.REGISTER_USER_URI, Server::registerUser);
        post(Constants.URIs.VALIDATE_AUTH_TOKEN_URI, Server::validateAuthToken);


        post("/printDidTapWeeklyPrice", (req, res) -> {
            PersistentLogger.info(PersistentLogger.SERVER, "Did tap weekly price");
            return "";
        });

        // Get Constants
        post(Constants.URIs.GET_CREATE_PANELS, (req, res) -> new ObjectMapper().writeValueAsString(new BodyResponse(ResponseStatus.SUCCESS, new GetCreatePanelsResponse())));
        post(Constants.URIs.GET_IMPORTANT_CONSTANTS_URI, (req, res) -> new ObjectMapper().writeValueAsString(new BodyResponse(ResponseStatus.SUCCESS, new GetImportantConstantsResponse())));
        post(Constants.URIs.GET_IAP_STUFF_URI, (req, res) -> new ObjectMapper().writeValueAsString(new BodyResponse(ResponseStatus.SUCCESS, new GetIAPStuffResponse())));

        // GET routes (additive — POST routes above remain for backward compatibility with iOS client)

        // No-auth read-only endpoints
        get(Constants.URIs.GET_ALL_TAGS_URI, (req, res) -> {
            BodyResponse bodyResponse = GetAllTagsEndpoint.getAllTags();
            return new ObjectMapper().writeValueAsString(bodyResponse);
        });
        get(Constants.URIs.GET_CREATE_PANELS, (req, res) -> new ObjectMapper().writeValueAsString(new BodyResponse(ResponseStatus.SUCCESS, new GetCreatePanelsResponse())));
        get(Constants.URIs.GET_IMPORTANT_CONSTANTS_URI, (req, res) -> new ObjectMapper().writeValueAsString(new BodyResponse(ResponseStatus.SUCCESS, new GetImportantConstantsResponse())));
        get(Constants.URIs.GET_IAP_STUFF_URI, (req, res) -> new ObjectMapper().writeValueAsString(new BodyResponse(ResponseStatus.SUCCESS, new GetIAPStuffResponse())));

        // Auth-required read-only endpoints (authToken via query parameter)
        get(Constants.URIs.GET_IS_PREMIUM_URI, (req, res) -> {
            AuthRequest authRequest = new AuthRequest(req.queryParams("authToken"));
            IsPremiumResponse ipResponse = GetIsPremiumEndpoint.getIsPremium(authRequest);
            BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(ipResponse);
            return new ObjectMapper().writeValueAsString(br);
        });
        get(Constants.URIs.GET_REMAINING_URI, (req, res) -> {
            AuthRequest authRequest = new AuthRequest(req.queryParams("authToken"));
            BodyResponse bodyResponse = GetRemainingIdeaRecipesEndpoint.getRemaining(authRequest);
            return new ObjectMapper().writeValueAsString(bodyResponse);
        });
        get(Constants.URIs.VALIDATE_AUTH_TOKEN_URI, (req, res) -> {
            AuthRequest authRequest = new AuthRequest(req.queryParams("authToken"));
            ValidateAuthTokenResponse vatr = ValidateAuthTokenEndpoint.validateAuthToken(authRequest);
            BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(vatr);
            return new ObjectMapper().writeValueAsString(br);
        });

        // dev functions
        if (dev) {

        }
    }


}
