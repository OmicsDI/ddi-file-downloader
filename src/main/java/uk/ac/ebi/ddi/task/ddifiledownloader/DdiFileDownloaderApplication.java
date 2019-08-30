package uk.ac.ebi.ddi.task.ddifiledownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.ac.ebi.ddi.ddifileservice.DdiFileServiceApplication;
import uk.ac.ebi.ddi.ddifileservice.services.IFileSystem;
import uk.ac.ebi.ddi.task.ddifiledownloader.configuration.FileDownloaderTaskProperties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class DdiFileDownloaderApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DdiFileServiceApplication.class);

    @Autowired
    private FileDownloaderTaskProperties taskProperties;

    @Autowired
    private IFileSystem fileSystem;

    public static void main(String[] args) {
        SpringApplication.run(DdiFileDownloaderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            LOGGER.error("Exception occurred, ", e);
        }

        URL url = new URL(taskProperties.getOriginalFileURL());
        URLConnection connection = url.openConnection();
        File tmpOutput = File.createTempFile("ddi", "downloader.tmp");
        try (InputStream is = connection.getInputStream();
             FileOutputStream fileOutput = new FileOutputStream(tmpOutput)) {

            byte[] buffer = new byte[2048];
            int bufferLength; //used to store a temporary size of the buffer

            while ((bufferLength = is.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
        }

        fileSystem.copyFile(tmpOutput, taskProperties.getTargetFileName());
    }
}
