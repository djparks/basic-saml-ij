package com.example.basicsaml.security;

import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class ClientService {
    @Autowired
    private ClientSSLContext sslContext;

    public HttpURLConnection configureRawHttpConnection(String requestMethod, String extension) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        URL url = new URL("http://localhost:8080/test" + extension);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("Authorization", "Bearer stuff");
        if("POST".equalsIgnoreCase(requestMethod)) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json" );
            conn.setChunkedStreamingMode(4096);
        }
        conn.setRequestMethod(requestMethod);
        conn.setConnectTimeout(50);
        if(conn instanceof HttpsURLConnection connection) {
            connection.setSSLSocketFactory(sslContext.getSSLContext().getSocketFactory());
        }
        return conn;
    }
}
