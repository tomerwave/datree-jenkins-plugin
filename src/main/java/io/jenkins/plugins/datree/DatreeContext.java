package io.jenkins.plugins.datree;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.io.PrintStream;

public class DatreeContext {
    private final FilePath yamlFilePath;
    private final Launcher launcher;
    private final EnvVars envVars;
    private final Run<?, ?> run;
    private final TaskListener taskListener;

    private DatreeContext(
            FilePath yamlFilePath,
            Launcher launcher,
            EnvVars envVars,
            Run<?, ?> run,
            TaskListener taskListener
    ) {
        this.yamlFilePath = yamlFilePath;
        this.launcher = launcher;
        this.envVars = envVars;
        this.run = run;
        this.taskListener = taskListener;
    }

    public FilePath getYamlFilePath() {
        return yamlFilePath;
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public EnvVars getEnvVars() {
        return envVars;
    }

    public Run<?, ?> getRun() {
        return run;
    }

    public TaskListener getTaskListener() {
        return taskListener;
    }

    public PrintStream getLogger() {
        return taskListener.getLogger();
    }

    public static DatreeContext forFreestyleProject(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener) {
        try {
            return new DatreeContext(
                    workspace,
                    launcher,
                    build.getEnvironment(listener),
                    build,
                    listener
            );
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

