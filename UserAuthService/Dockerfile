# STEP 1: Use a minimal Java Runtime Environment (JRE) for the final image.
# This reduces image size.
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Define the port your Spring Boot application runs on. 
# Assuming UserAuthService runs on port 8081 (a common choice for microservices).
# Check your application.properties/yml for the correct port.
EXPOSE 8081 

# STEP 2: Copy the JAR file from the host's target folder into the container.
# The 'target' folder is relative to the directory where you run 'docker build'.
COPY target/UserAuthService-0.0.1-SNAPSHOT.jar app.jar

# STEP 3: Define the command to execute the application when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]