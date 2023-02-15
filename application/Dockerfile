#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /build

COPY . ./m2w-challenge
RUN --mount=type=cache,target=/root/.m2 mvn -f ./m2w-challenge clean install

#
# Package stage
#
FROM openjdk:17-oracle
WORKDIR /app

COPY --from=build /build/m2w-challenge/target/*.jar ./app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]