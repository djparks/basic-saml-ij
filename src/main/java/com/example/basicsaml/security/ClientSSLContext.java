package com.example.basicsaml.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.Security;

@Component
public class ClientSSLContext {

    private final SslBundles sslBundles;

    @Value("${client.ssl.bundle-name:my-ssl}")
    private String bundleName;

    public ClientSSLContext(SslBundles sslBundles) {
        this.sslBundles = sslBundles;
    }

    public SSLContext getSSLContext() throws IOException {
        Security.setProperty("crypto.policy", "unlimited");
        try {
            SslBundle bundle = this.sslBundles.getBundle(this.bundleName);
            if (bundle != null) {
                return bundle.createSslContext();
            } else {
                System.out.println("No SSL bundle named '" + this.bundleName + "' was found. Returning null SSLContext.");
            }
        } catch (Exception e) {
            System.out.println("Failed to create SSLContext from Spring SSL bundle '" + this.bundleName + "': " + e);
        }
        return null;
    }
}
