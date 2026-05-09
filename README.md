# Digital Wallet Backend

This repository contains the Spring Boot backend for the Digital Wallet application.

The backend is built with:

- Spring Boot 3.2.4
- Java 21
- Spring Web
- Spring Data MongoDB
- Spring Security with OAuth2 resource server support
- JWT authentication
- Validation support

## Prerequisites

- Java 21 JDK installed
- Gradle wrapper available via `./gradlew` or `gradlew.bat`
- MongoDB running locally or accessible through a configured connection string

## Local configuration

By default, the backend connects to MongoDB at:

```yaml
spring.data.mongodb.uri: mongodb://localhost:27017/digital-wallet
```

JWT properties are defined in `src/main/resources/application.yml` and should be provided using environment variables:

- `JWT_SECRET` - secret key used to sign JWT tokens
- `JWT_EXPIRATION` - token expiration in milliseconds (default `86400000`)

Admin credentials should also be provided through environment variables:

- `APP_ADMIN_USERNAME` - default `admin`
- `APP_ADMIN_PASSWORD` - admin password (required)
- `APP_ADMIN_EMAIL` - default `admin@digitalwallet.com`

> Do not commit secrets or production credentials to source control.

## Run locally

Start the backend using the Gradle wrapper from this repository root:

```powershell
./gradlew bootRun
```

or on Windows:

```powershell
gradlew.bat bootRun
```

The API listens on port `8080` by default.

## Build

Build the JAR with:

```powershell
./gradlew build
```

The resulting artifact will be in `build/libs`.

## Tests

Run unit and integration tests with:

```powershell
./gradlew test
```

## API base URL

During local development, the frontend expects the backend API to be available at:

- `http://localhost:8080/api`

## Notes

- The UI and Terraform infrastructure repositories are located in `../digital-wallet-ui` and `../digital-wallet-iac`.
- For Azure deployment, the backend can be published to App Service and configured to use the Cosmos DB MongoDB connection string.
