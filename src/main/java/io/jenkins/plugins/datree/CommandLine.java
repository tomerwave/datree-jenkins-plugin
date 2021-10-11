package io.jenkins.plugins.datree;

import hudson.EnvVars;
import hudson.Util;
import hudson.util.ArgumentListBuilder;

import java.util.Optional;
import java.util.function.Function;


public class CommandLine {

    public static ArgumentListBuilder installDatreeArgumentList() {
//        "curl https://get.datree.io | /bin/bash";
        return new ArgumentListBuilder("curl https://get.datree.io | /bin/bash");
    }

    public static ArgumentListBuilder binBashArgumentList() {
        return new ArgumentListBuilder("/bin/bash");
    }

    public static ArgumentListBuilder testCommandArgumentList(String executablePath, DatreeConfig config, EnvVars env) {
        Function<String, String> replaceMacroWithEnv = str -> Util.replaceMacro(str, env);
        ArgumentListBuilder args = new ArgumentListBuilder(executablePath, "test", config.getTargetFile());

        Optional.ofNullable(config.getOutput())
                .map(Util::fixEmptyAndTrim)
                .map(replaceMacroWithEnv)
                .ifPresent(value -> args.add("--output=" + value));

        Optional.of(config.isIgnoreMissingSchemas())
                .filter(shouldIgnore -> shouldIgnore)
                .ifPresent(value -> args.add("--ignore-missing-schemas"));

        Optional.ofNullable(config.getPolicy())
                .map(Util::fixEmptyAndTrim)
                .map(replaceMacroWithEnv)
                .ifPresent(value -> args.add("--policy=" + value));

        Optional.ofNullable(config.getSchemaVersion())
                .map(Util::fixEmptyAndTrim)
                .map(replaceMacroWithEnv)
                .ifPresent(value -> args.add("--schema-version=" + value));

        return args;
    }
}
