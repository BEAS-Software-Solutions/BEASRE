# Use official OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the JAR file
COPY target/rule-engine-*.jar app.jar

# Change ownership to spring user
RUN chown spring:spring app.jar

# Switch to spring user
USER spring

# Expose the application port
EXPOSE 8090

# Set JVM options for better performance
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
