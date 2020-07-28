package com.mobile.stu.base.net.security;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.Nullable;

/**
 * author: dourl
 * created on: 2019/2/13 5:20 PM
 * description:
 */
public class SSLContextFactory {

    private SSLContextFactory() {
    }

    /**
     * Creates an SSLContext with the client and server certificates
     * @param clientCertFile A File imput stream containing the client certificate
     * @param clientCertPassword Password for the client certificate
     * @param caCertString A String input stream containing the server certificate, pem format
     * @return An initialized SSLContext
     * @throws Exception
     */
    public static SSLContext makeContext(InputStream clientCertFile, String clientCertPassword, InputStream caCertString) throws Exception {
        final KeyStore keyStore = loadPKCS12KeyStore(clientCertFile, clientCertPassword);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, clientCertPassword.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();

        TrustManager[] trustManagers = new TrustManager[]{loadTrustManagers(caCertString)};

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    }

    /**
     * Creates an SSLContext with the client and server certificates
     * @param clientCertFile A File imput stream containing the client certificate
     * @param clientCertPassword Password for the client certificate
     * @param trustManagers A Array of trust managers
     * @return An initialized SSLContext
     * @throws Exception
     */
    public static SSLContext makeContext(InputStream clientCertFile, String clientCertPassword, TrustManager[] trustManagers) throws Exception {
        final KeyStore keyStore = loadPKCS12KeyStore(clientCertFile, clientCertPassword);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, clientCertPassword.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    }

    /**
     * Creates an SSLContext with the client and server certificates
     * @param clientCertFile A File imput stream containing the client certificate
     * @param clientCertPassword Password for the client certificate
     * @param trustManager trust manager
     * @return An initialized SSLContext
     * @throws Exception
     */
    public static SSLContext makeContext(InputStream clientCertFile, String clientCertPassword, TrustManager trustManager) throws Exception {
        final KeyStore keyStore = loadPKCS12KeyStore(clientCertFile, clientCertPassword);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, clientCertPassword.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, new TrustManager[]{trustManager}, null);

        return sslContext;
    }

    public static X509TrustManager loadTrustManagers(InputStream caCertString) throws Exception {
        final KeyStore trustStore = loadPEMTrustStore(caCertString);
        if (trustStore == null) {
            return null;
        }
        return new CustomTrustManager(trustStore);
    }

    /**
     * Produces a KeyStore from a String containing a PEM certificate (typically, the server's CA certificate)
     * @param certificateString A String containing the PEM-encoded certificate
     * @return a KeyStore (to be used as a trust store) that contains the certificate
     * @throws Exception
     */
    private static @Nullable
    KeyStore loadPEMTrustStore(InputStream certificateString) throws Exception {
        if (certificateString == null) {
            return null;
        }
        byte[] der = loadPemCertificate(certificateString);
        ByteArrayInputStream derInputStream = new ByteArrayInputStream(der);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
        String alias = cert.getSubjectX500Principal().getName();

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry(alias, cert);

        return trustStore;
    }

    /**
     * Produces a KeyStore from a PKCS12 (.p12) certificate file, typically the client certificate
     * @param certificateFile A file input stream containing the client certificate
     * @param clientCertPassword Password for the certificate
     * @return A KeyStore containing the certificate from the certificateFile
     * @throws Exception
     */
    private static KeyStore loadPKCS12KeyStore(InputStream certificateFile, String clientCertPassword) throws Exception {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(certificateFile, clientCertPassword.toCharArray());
        } finally {
            try {
                if(certificateFile != null) {
                    certificateFile.close();
                }
            } catch(IOException ex) {
                // ignore
            }
        }
        return keyStore;
    }

    /**
     * Reads and decodes a base-64 encoded DER certificate (a .pem certificate), typically the server's CA cert.
     * @param certificateStream an InputStream from which to read the cert
     * @return a byte[] containing the decoded certificate
     * @throws IOException
     */
    static byte[] loadPemCertificate(InputStream certificateStream) throws IOException {

        byte[] der = null;
        BufferedReader br = null;

        try {
            StringBuilder buf = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(certificateStream));

            String line = br.readLine();
            while(line != null) {
                if(!line.startsWith("--")){
                    buf.append(line);
                }
                line = br.readLine();
            }

            String pem = buf.toString();
            der = Base64.decode(pem, Base64.DEFAULT);

        } finally {
            if(br != null) {
                br.close();
            }
        }

        return der;
    }
}
