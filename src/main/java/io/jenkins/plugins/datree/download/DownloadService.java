package io.jenkins.plugins.datree.download;

import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService {
    public static URL getDownloadUrlForDatree(String version) throws MalformedURLException {
        return new URL(String.format("https://github.com/datreeio/datree/releases/download/0.13.7/datree-cli_%s_Linux_x86_64.zip", version));
    }
}
