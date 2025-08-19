package com.example.basicsaml.security;


import org.springframework.stereotype.Component;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Component
public class ClientSSLContext {

    public SSLContext getSSLContext() throws IOException {
        Security.setProperty("crypto.policy", "unlimited");
        String keystorePath = System.getProperty("user.home") + "/.keystore";
        SSLContext sslContext = null;

        try {
            if(keystorePath!=null && keystorePath.startsWith("/")) {
                sslContext = new SSLContextBuilder()
                        .loadKeyMaterial(new File(keystorePath), "password".toCharArray(), "password".toCharArray())
                        .loadTrustMaterial(new File(keystorePath), "password".toCharArray())
                        .build();
            } else {
                URL url = ClassLoader.getSystemResource(keystorePath).toURI().toURL();
                sslContext = new SSLContextBuilder()
                        .loadKeyMaterial(url,"password".toCharArray(), "password".toCharArray())
                        .loadTrustMaterial(url, "password".toCharArray())
                        .build();
            }
        } catch (Exception e) {
            System.out.println("Outbound SSL Configuration Is BAD! " + e);
        }

        return sslContext;
    }

}
