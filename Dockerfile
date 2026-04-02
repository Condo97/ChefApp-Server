FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY lib/out/PantryPro_Server.jar app.jar
COPY chitchatserver.com.jks chitchatserver.com.jks
EXPOSE 800
ENTRYPOINT ["java", "-jar", "app.jar"]
