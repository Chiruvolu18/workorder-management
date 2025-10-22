# STEP 1: Use a minimal Java Runtime Environment (JRE) as the base image.
# This provides the necessary environment to run your JAR and keeps the image small.
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# STEP 2: Expose the port your Spring Boot app runs on. 
# Assuming WorkOrderService runs on the default 8080 port.
# If you configured a different port (e.g., in application.properties), change this number.
EXPOSE 8080

# STEP 3: Copy the executable JAR file from the host's 'target/' folder into the container.
# NOTE: Ensure you run 'mvn clean package' before building the Docker image.
# Replace 'WorkOrderService-0.0.1-SNAPSHOT.jar' with the actual file name if it differs.
COPY target/WorkOrderService-0.0.1-SNAPSHOT.jar app.jar

# STEP 4: Define the command to run the application when the container starts.
# This executes the Java application.
ENTRYPOINT ["java", "-jar", "app.jar"]