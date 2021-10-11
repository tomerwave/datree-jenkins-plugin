package io.jenkins.plugins.datree;

public interface DatreeConfig {
    boolean isIgnoreMissingSchemas();

    String getOutput();

    String getPolicy();

    String getSchemaVersion();

    String getTargetFile();
}
