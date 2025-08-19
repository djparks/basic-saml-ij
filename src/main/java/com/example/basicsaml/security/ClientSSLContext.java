package com.example.basicsaml.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.Security;

/**
 * Creates an {@link SSLContext} for outbound HTTPS clients using Spring Boot 3.1+ SSL Bundles.
 * <p>
 * Why SSL Bundles?
 * Instead of manually loading keystores/truststores (e.g., via Apache HttpClient's SSLContextBuilder),
 * Spring Boot's {@link SslBundles} allows central, property-driven configuration of TLS material (key and trust stores)
 * that can be reused by both server and client components. This class retrieves a named bundle and turns it
 * into a JDK {@link SSLContext} suitable for use with {@link javax.net.ssl.HttpsURLConnection} or other HTTP clients.
 * </p>
 * <p>
 * Configuration:
 * - The bundle name is read from the property {@code client.ssl.bundle-name} and defaults to {@code my-ssl}.
 * - See application.yml under {@code spring.ssl.bundle.jks} for an example of how to define a bundle.
 * </p>
 */
@Component
public class ClientSSLContext {

    /**
     * The container of SSL bundles configured through application properties (application.yml).
     */
    private final SslBundles sslBundles;

    /**
     * Name of the SSL bundle to load from {@link SslBundles}. Defaults to "my-ssl".
     * Configure by setting "client.ssl.bundle-name" in application properties or environment.
     */
    @Value("${client.ssl.bundle-name:my-ssl}")
    private String bundleName;

    public ClientSSLContext(SslBundles sslBundles) {
        this.sslBundles = sslBundles;
    }

    /**
     * Returns an {@link SSLContext} created from the configured Spring SSL bundle.
     *
     * Notes:
     * - We set the JCE crypto policy to "unlimited" to ensure strong cipher suites are available
     *   on older JVM distributions that might restrict cryptography by default. Modern JVMs usually
     *   enable unlimited policy out of the box; setting it here is a safe no-op in that case.
     * - If the bundle is not found or an error occurs, this method logs to stdout and returns null.
     *   Callers should handle a null return (e.g., by skipping TLS customization or failing fast).
     */
    public SSLContext getSSLContext() throws IOException {
        // Ensure the strongest crypto policy is enabled for TLS operations
        Security.setProperty("crypto.policy", "unlimited");
        try {
            // Look up the SSL bundle defined in application.yml (spring.ssl.bundle...)
            SslBundle bundle = this.sslBundles.getBundle(this.bundleName);
            if (bundle != null) {
                // Convert the Spring SSL bundle into a standard JDK SSLContext
                return bundle.createSslContext();
            } else {
                System.out.println("No SSL bundle named '" + this.bundleName + "' was found. Returning null SSLContext.");
            }
        } catch (Exception e) {
            // Intentionally simple logging to avoid bringing a logger just for this example.
            System.out.println("Failed to create SSLContext from Spring SSL bundle '" + this.bundleName + "': " + e);
        }
        return null;
    }
}
