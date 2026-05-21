# 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

#Copy required files to download dependencies
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Wrapper execution permission and download dependencies
RUN chmod +x gradlew && ./gradlew --version

# Copy src and build Jar files
COPY src src
RUN ./gradlew bootJar --no-daemon


# 2: Run (Optimized execution environment)

#Using JRE (smaller than JDK)
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="wcampos-dev"
LABEL description="ToDo List API - Optimized for Free Hosting"

WORKDIR /app

# Create a non-root user for safety
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy only Jar previously created in the prior step #1
COPY --from=build /app/build/libs/*.jar app.jar

# Optimized JVM configurations for instances with low memory
# -Xmx: Limits Heap Memory to prevent the container from being killed by OOM (Out of Memory)
# -Xss: Reduces the stack size of each thread to save RAM
# -Djava.security.egd: Speeds up Tomcat startup in Linux environments
ENV JAVA_OPTS="-Xmx300M -Xms200M -Xss512K -Djava.security.egd=file:/dev/./urandom"

#Expose Spring Boot default port
EXPOSE 8080

#Initializing command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]






