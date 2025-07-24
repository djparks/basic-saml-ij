# Gateway MVC Logging Configuration

## Overview
This document describes the changes made to implement logging for Spring Cloud Gateway MVC calls, including headers and status information.

## Implementation Details

### Updated Logging Configuration in application.yml

Added DEBUG level logging for Spring Cloud Gateway MVC components in the application.yml file:

```yaml
# Logging configuration
logging:
  level:
    root: INFO
    org:
      springframework:
        security: DEBUG
        security.saml2: DEBUG
        cloud.gateway: DEBUG
        web.reactive.function.client: DEBUG
        http.server.reactive: DEBUG
```

## How It Works

1. `org.springframework.cloud.gateway: DEBUG` - Enables detailed logging for Spring Cloud Gateway components, including route matching, filter execution, and general gateway operations.

2. `org.springframework.web.reactive.function.client: DEBUG` - Logs HTTP client requests and responses, which are used by the gateway to forward requests to the target services. This includes request/response headers and status codes.

3. `org.springframework.http.server.reactive: DEBUG` - Logs HTTP server requests and responses at the reactive layer, providing information about incoming requests and outgoing responses.

## Expected Logging Output

When a request is made to a gateway route (e.g., `/gate/**`), the logs will include detailed information such as:

- Request headers sent to the gateway
- Request headers forwarded to the target service
- Response status code from the target service
- Response headers from the target service
- Filter execution details
- Route matching information

Example log entries:

```
DEBUG o.s.c.g.h.RoutePredicateHandlerMapping : Route matched: google-route
DEBUG o.s.c.g.h.RoutePredicateHandlerMapping : Mapping [Exchange: GET http://localhost:8080/gate/search] to Route{id='google-route', uri=https://www.google.com, order=0, predicate=Paths: [/gate/**], gatewayFilters=[]}
DEBUG o.s.w.r.f.client.ExchangeFunctions     : [3c01c7c3] HTTP GET https://www.google.com/search
DEBUG o.s.w.r.f.client.ExchangeFunctions     : [3c01c7c3] Request headers: [Host:"www.google.com", User-Agent:"ReactorNetty/1.1.12", Accept:"*/*"]
DEBUG o.s.w.r.f.client.ExchangeFunctions     : [3c01c7c3] Response status: 200 OK
DEBUG o.s.w.r.f.client.ExchangeFunctions     : [3c01c7c3] Response headers: [Content-Type:"text/html; charset=ISO-8859-1", Cache-Control:"private, max-age=0", Date:"Wed, 23 Jul 2025 12:24:00 GMT", Server:"gws", Content-Length:"12345"]
```

## Testing the Configuration

To verify the logging configuration:

1. Start the application with `./mvnw spring-boot:run`
2. Make a request to a gateway route, e.g., `http://localhost:8080/gate/search`
3. Check the application logs for detailed information about the gateway request/response, including headers and status codes

## Troubleshooting

If you don't see the expected logging output:

1. Verify that the application.yml file has been updated correctly
2. Ensure that the application is using the correct configuration file
3. Try setting the root logging level to DEBUG temporarily for more verbose output
4. Check that the gateway route is correctly configured and being used