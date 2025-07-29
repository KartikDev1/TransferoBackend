# Use minimal Java 17 image
FROM openjdk:17-jdk-slim

# Create directory
WORKDIR /app

# Copy JAR from target folder
COPY target/Transfero-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
