# Basic SAML Application

A Spring Boot 3.2 application with SAML 2.0 authentication support.

## Requirements

- Java 17 or higher
- Maven (or use the included Maven wrapper)

## Technologies

- Spring Boot 3.2
- Spring Security
- Spring Security SAML2
- Maven

## Project Structure

The project follows the standard Spring Boot application structure:

```
basic-saml/
├── .mvn/wrapper/                  # Maven wrapper files
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/basicsaml/
│   │   │       ├── config/        # Configuration classes
│   │   │       ├── controller/    # Web controllers
│   │   │       └── BasicSamlApplication.java
│   │   └── resources/
│   │       └── application.yml
├── mvnw                           # Maven wrapper script for Unix
├── mvnw.cmd                       # Maven wrapper script for Windows
├── pom.xml                        # Project dependencies and build configuration
└── README.md                      # This file
```

## Getting Started

1. Clone the repository
2. Configure your SAML Identity Provider details in `application.yml`
3. Run the application:

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

4. Access the application at http://localhost:8080

## SAML Configuration

To configure SAML authentication, update the following configuration in `application.yml`:

```yaml
spring:
  security:
    saml2:
      relyingparty:
        registration:
          idpname:
            identityprovider:
              entity-id: https://idp.example.com/idp
              sso-url: https://idp.example.com/idp/profile/SAML2/Redirect/SSO
              verification:
                credentials:
                  certificate-location: classpath:saml/idp-certificate.crt
```

You'll also need to add your Identity Provider's certificate to the `src/main/resources/saml/` directory.

## Endpoints

- `/` - Public home page
- `/secured` - Protected page that requires authentication
- `/login` - Login page

## License

This project is licensed under the MIT License.
