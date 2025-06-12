# Basic SAML Application Development Guidelines

This document provides essential information for developers working on the Basic SAML application. It includes build/configuration instructions, testing information, and additional development details.

## Build/Configuration Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.9.5+ (or use the included Maven wrapper)

### Building the Application
```bash
# Using Maven wrapper
./mvnw clean install

# Or using Maven directly
mvn clean install
```

### Running the Application
```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

### SAML Configuration
To configure SAML authentication, update the following properties in `src/main/resources/application.properties`:

```properties
spring.security.saml2.relyingparty.registration.idpname.identityprovider.entity-id=https://your-idp.example.com/idp
spring.security.saml2.relyingparty.registration.idpname.identityprovider.sso-url=https://your-idp.example.com/idp/profile/SAML2/Redirect/SSO
spring.security.saml2.relyingparty.registration.idpname.identityprovider.verification.credentials.certificate-location=classpath:saml/your-idp-certificate.crt
```

You'll need to:
1. Replace `idpname` with a unique identifier for your Identity Provider
2. Update the entity-id and sso-url with your IdP's values
3. Add your IdP's certificate to `src/main/resources/saml/` directory
4. Update the certificate-location property to point to your certificate file

## Testing Information

### Test Configuration
The application uses a separate test configuration profile that disables SAML authentication for testing purposes. This is defined in:
- `src/test/resources/application-test.properties`
- `src/test/java/com/example/basicsaml/config/TestSecurityConfig.java`

### Running Tests
```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=HomeControllerTest

# Run a specific test method
./mvnw test -Dtest=HomeControllerTest#testHomeEndpoint
```

### Test Types

#### 1. Integration Tests
The `BasicSamlApplicationTests` class verifies that the Spring application context loads successfully with all dependencies.

#### 2. Controller Tests
The `HomeControllerTest` class demonstrates how to test REST controllers using `@WebMvcTest` and `MockMvc`.

#### 3. Unit Tests
The `StringUtilsTest` class shows how to write simple unit tests for utility classes.

### Adding New Tests

#### Creating a Controller Test
```java
@WebMvcTest(YourController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class YourControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testYourEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/your-endpoint"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.property").value("expectedValue"));
    }
}
```

#### Creating a Unit Test
```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class YourServiceTest {

    @Test
    public void testYourMethod() {
        YourService service = new YourService();
        assertEquals("expected", service.yourMethod("input"));
    }
}
```

## Additional Development Information

### Code Style
- Follow standard Java coding conventions
- Use 4 spaces for indentation
- Use camelCase for variables and methods
- Use PascalCase for class names
- Add Javadoc comments for public methods and classes

### Project Structure
- Controllers should be placed in `com.example.basicsaml.controller`
- Configuration classes should be placed in `com.example.basicsaml.config`
- Service classes should be placed in `com.example.basicsaml.service`
- Utility classes should be placed in `com.example.basicsaml.util`

### Security Considerations
- The application uses Spring Security with SAML 2.0 authentication
- The security configuration is defined in `SecurityConfig.java`
- For production, ensure all endpoints except the home page require authentication
- Always use HTTPS in production environments
- Keep SAML certificates secure and don't commit them to version control

### Debugging Tips
- Enable DEBUG logging for Spring Security and SAML components in application.properties:
  ```properties
  logging.level.org.springframework.security=DEBUG
  logging.level.org.springframework.security.saml2=DEBUG
  ```
- Use Spring Boot Actuator for monitoring and health checks
- For SAML issues, check the SAML response from the IdP using browser developer tools

### Common Issues
- **SAML Certificate Issues**: Ensure the certificate is in the correct format and accessible on the classpath
- **SAML Configuration**: Double-check entity IDs and SSO URLs match exactly with your IdP configuration
- **Context Path**: If deploying with a context path, update the SAML configuration accordingly