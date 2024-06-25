package com.pantrypro.core.conversiontracking.pinterest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.pantrypro.Constants;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.client.pinterest.conversionsapi.request.PinterestSendConversionRequest;
import httpson.Httpson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

public class PinterestConversionLogger {

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofMinutes(Constants.AI_TIMEOUT_MINUTES)).build();

    private static final String adAccountID = Keys.pinterestAdAccountID;
    private static final String accessToken = Keys.pinterestAccessToken;
    private static final String defaultIOSAppActionSource = "app_ios";

    private static final URI fullPinterestSendConversionsURI = URI.create(
            Constants.PINTEREST_API_BASE_URI
            + Constants.PINTEREST_V5_URI
            + Constants.PINTEREST_AD_ACCOUNTS_URI
            + "/" + adAccountID
            + Constants.PINTEREST_EVENTS_URI
    );

    private static final URI fullPinterestSendConversionsTestURI = URI.create(
            Constants.PINTEREST_API_BASE_URI
                    + Constants.PINTEREST_V5_URI
                    + Constants.PINTEREST_AD_ACCOUNTS_URI
                    + "/" + adAccountID
                    + Constants.PINTEREST_EVENTS_URI
                    + "?" + Constants.PINTEREST_TEST_URI_ARGUMENT
    );


    public static void logPinterestConversion(PinterestSendConversionRequest.Data.EventNames eventName, String idfa, String eventID, Boolean test) throws IOException, InterruptedException {
        logPinterestConversion(
                eventName,
                defaultIOSAppActionSource,
                eventID,
                idfa,
                test
        );
    }

    public static void logPinterestConversion(PinterestSendConversionRequest.Data.EventNames eventName, String actionSource, String eventID, String idfa, Boolean test) throws IOException, InterruptedException {
        // Hash IDFA
        String hashedIDFA = Hashing.sha256().hashString(idfa, StandardCharsets.UTF_8).toString();

        // Create PinterestConversionAPIRequest
        PinterestSendConversionRequest pscRequest = new PinterestSendConversionRequest(
                List.of(
                        new PinterestSendConversionRequest.Data(
                                eventName,
                                actionSource,
                                System.currentTimeMillis() / 1000, // Pinterest asks for it in seconds lol
                                eventID,
                                new PinterestSendConversionRequest.Data.UserData(
                                        List.of(hashedIDFA)
                                )
                            )
                )
        );

        // Add accessToken to HttpRequest with builder
        Consumer<HttpRequest.Builder> httpRequestBuilder = (r) -> {
            r.setHeader("Authorization", "Bearer " + accessToken);
        };

        // Send PinterestConversionAPIRequest
        JsonNode pscResponse = Httpson.sendPOST(
                pscRequest,
                httpClient,
                test == null || !test ? fullPinterestSendConversionsURI : fullPinterestSendConversionsTestURI,
                httpRequestBuilder
        );

        // Print pscResponse
        System.out.println("Logged Pinterest Conversion: " + new ObjectMapper().writeValueAsString(pscResponse));
    }

}
