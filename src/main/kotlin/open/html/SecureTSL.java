package open.html;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


public class SecureTSL {


    public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

//    public static synchronized SSLSocketFactory initUnSecureTSL(final URL url) throws IOException {
//
//        if (sslSocketFactory == null) {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
//
//                }
//
//                public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
//
//                }
//
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//            }};
//
//            final SSLContext sslContext;
//            try {
//                sslContext = SSLContext.getInstance("TLS");
//                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//                SSLParameters sslParameters = new SSLParameters();
//
//                List sniHostNames = new ArrayList(1);
//
//                System.out.println("urlhost: " + url.getHost());
//
//                sniHostNames.add(new SNIHostName(url.getHost()));
//
//                sslParameters.setServerNames(sniHostNames);
//
//                SSLSocketFactory sslSocketFactory = new SSLSocketFactoryWrapper(sslContext.getSocketFactory(), sslParameters);
//                return sslSocketFactory;
//
//            } catch (NoSuchAlgorithmException e) {
//
//                throw new IOException("Can't create unsecure trust manager");
//
//            } catch (KeyManagementException e) {
//
//                throw new IOException("Can't create unsecure trust manager");
//
//            }
//        }
//    }
}
