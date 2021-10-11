package io.jenkins.plugins.datree.installer;

import hudson.Extension;
import hudson.FilePath;
import hudson.Functions;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolInstallerDescriptor;
import io.jenkins.plugins.datree.download.DownloadService;
import jenkins.security.MasterToSlaveCallable;
import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import static hudson.Util.fixEmptyAndTrim;
import static java.lang.String.format;

public class DatreeInstaller extends ToolInstaller {
    private static final Logger LOG = LoggerFactory.getLogger(DatreeInstaller.class.getName());

    private final String version;

    @DataBoundConstructor
    public DatreeInstaller(String label, String version) {
        super(label);
        this.version = version;
    }

    @Override
    public FilePath performInstallation(ToolInstallation tool, Node node, TaskListener listener) throws IOException, InterruptedException {
        FilePath expected = preferredLocation(tool, node);
        PrintStream logger = listener.getLogger();

//        if (isUpToDate(expected)) {
//            return expected;
//        }

        logger.println("Installing Datree (" + fixEmptyAndTrim(version) + ")...");
        return downloadDatreeBinaries(expected, node);
    }

    private FilePath downloadDatreeBinaries(FilePath expected, Node node) {
        try {
            LOG.info("Installing Datree '{}' on Build Node '{}'", version, node.getDisplayName());

            final VirtualChannel nodeChannel = node.getChannel();

            if (nodeChannel == null) {
                throw new IOException(format("Build Node '%s' is offline.", node.getDisplayName()));
            }

            URL datreeDownloadUrl = DownloadService.getDownloadUrlForDatree(version);
            nodeChannel.call(new Downloader(datreeDownloadUrl, expected.child("datree")));

            expected.mkdirs();

            return expected;
        } catch (RuntimeException | IOException | InterruptedException ex) {
            throw new RuntimeException("Failed to install Snyk.", ex);
        }
    }

    @Extension
    public static final class DatreeInstallerDescriptor extends ToolInstallerDescriptor<DatreeInstaller> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Install from datree.io";
        }

        @Override
        public boolean isApplicable(Class<? extends ToolInstallation> toolType) {
            return toolType == DatreeInstallation.class;
        }
    }

    private static class Downloader extends MasterToSlaveCallable<Void, IOException> {
        private static final long serialVersionUID = 1L;

        private final URL downloadUrl;
        private final FilePath output;

        Downloader(URL downloadUrl, FilePath output) {
            this.downloadUrl = downloadUrl;
            this.output = output;
        }

        @Override
        public Void call() throws IOException {
            final File downloadedFile = new File(output.getRemote());
            FileUtils.copyURLToFile(downloadUrl, downloadedFile, 10000, 10000);
            // set execute permission
            if (!Functions.isWindows() && downloadedFile.isFile()) {
                boolean result = downloadedFile.setExecutable(true, false);
                if (!result) {
                    throw new RuntimeException(format("Failed to set file as executable. (%s)", downloadedFile.getAbsolutePath()));
                }
            }
            return null;
        }
    }
}
