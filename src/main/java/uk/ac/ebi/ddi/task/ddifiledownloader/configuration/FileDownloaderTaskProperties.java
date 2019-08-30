package uk.ac.ebi.ddi.task.ddifiledownloader.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("downloader")
public class FileDownloaderTaskProperties {
    private String originalFileURL;
    private String targetFileName;

    public String getOriginalFileURL() {
        return originalFileURL;
    }

    public void setOriginalFileURL(String originalFileURL) {
        this.originalFileURL = originalFileURL;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }
}
