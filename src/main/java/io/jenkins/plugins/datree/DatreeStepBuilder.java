package io.jenkins.plugins.datree;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.*;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import io.jenkins.plugins.datree.installer.DatreeInstallation;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;

public class DatreeStepBuilder extends Builder implements SimpleBuildStep, DatreeConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DatreeStepBuilder.class.getName());

    private boolean ignoreMissingSchemas = false;
    private String output;
    private String policy;
    private String schemaVersion;
    private String targetFile;

    @Override
    public void perform(
            @NonNull Run<?, ?> run,
            @NonNull FilePath workspace,
            @NonNull EnvVars env,
            @NonNull Launcher launcher,
            @NonNull TaskListener listener
    ) throws InterruptedException, IOException {
        DatreeTestStepFlow.perform(this, () -> DatreeContext.forFreestyleProject(run, workspace, launcher, listener));
    }

    @DataBoundConstructor
    public DatreeStepBuilder() {
    }

    @Override
    public boolean isIgnoreMissingSchemas() {
        return this.ignoreMissingSchemas;
    }

    @Override
    public String getOutput() {
        return this.output;
    }

    @Override
    public String getPolicy() {
        return this.policy;
    }

    @Override
    public String getSchemaVersion() {
        return this.schemaVersion;
    }

    @Override
    public String getTargetFile() {
        return this.targetFile;
    }

    @DataBoundSetter
    public void setTargetFile(String targetFile) {
        this.targetFile = targetFile;
    }

    @DataBoundSetter
    public void setOutput(String output) {
        this.output = output;
    }

    @DataBoundSetter
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    @DataBoundSetter
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    @DataBoundSetter
    public void setIgnoreMissingSchemas(boolean ignoreMissingSchemas) {
        this.ignoreMissingSchemas = ignoreMissingSchemas;
    }

    @Extension
    public static class DatreeStepBuilderDescriptor extends BuildStepDescriptor<Builder> {

        @CopyOnWrite
        private volatile DatreeInstallation[] installations = new DatreeInstallation[0];

        public DatreeStepBuilderDescriptor() {
            load();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Invoke Datree CLI";
        }

        @Override
        public String getId() {
            return super.getId();
        }


        @SuppressFBWarnings("EI_EXPOSE_REP")
        public DatreeInstallation[] getInstallations() {
            return installations;
        }

        public void setInstallations(DatreeInstallation... installations) {
            this.installations = installations;
            save();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
//TODO these are form validations that needs to be done.
//        public FormValidation doCheckSeverity(@QueryParameter String value, @QueryParameter String additionalArguments) {
//            if (fixEmptyAndTrim(value) == null || fixEmptyAndTrim(additionalArguments) == null) {
//                return FormValidation.ok();
//            }
//
//            if (additionalArguments.contains("--severity-threshold")) {
//                return FormValidation.warning("Option '--severity-threshold' is overridden in additional arguments text area below.");
//            }
//            return FormValidation.ok();
//        }
//
//        public FormValidation doCheckTargetFile(@QueryParameter String value, @QueryParameter String additionalArguments) {
//            if (fixEmptyAndTrim(value) == null || fixEmptyAndTrim(additionalArguments) == null) {
//                return FormValidation.ok();
//            }
//
//            if (additionalArguments.contains("--file")) {
//                return FormValidation.warning("Option '--file' is overridden in additional arguments text area below.");
//            }
//            return FormValidation.ok();
//        }
//
//        public FormValidation doCheckOrganisation(@QueryParameter String value, @QueryParameter String additionalArguments) {
//            if (fixEmptyAndTrim(value) == null || fixEmptyAndTrim(additionalArguments) == null) {
//                return FormValidation.ok();
//            }
//
//            if (additionalArguments.contains("--org")) {
//                return FormValidation.warning("Option '--org' is overridden in additional arguments text area below.");
//            }
//            return FormValidation.ok();
//        }
//
//        public FormValidation doCheckProjectName(@QueryParameter String value, @QueryParameter String monitorProjectOnBuild, @QueryParameter String additionalArguments) {
//            if (fixEmptyAndTrim(value) == null || fixEmptyAndTrim(monitorProjectOnBuild) == null) {
//                return FormValidation.ok();
//            }
//
//            List<FormValidation> findings = new ArrayList<>(2);
//            if ("false".equals(fixEmptyAndTrim(monitorProjectOnBuild))) {
//                findings.add(FormValidation.warning("Project name will be ignored, because the project is not monitored on build."));
//            }
//            if (fixNull(additionalArguments).contains("--project-name")) {
//                findings.add(FormValidation.warning("Option '--project-name' is overridden in additional arguments text area below."));
//            }
//            return FormValidation.aggregate(findings);
//        }
    }
}
