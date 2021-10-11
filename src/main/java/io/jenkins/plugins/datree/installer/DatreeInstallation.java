package io.jenkins.plugins.datree.installer;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentSpecific;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.slaves.NodeSpecific;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolProperty;
import io.jenkins.plugins.datree.DatreeStepBuilder;
import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DatreeInstallation extends ToolInstallation implements EnvironmentSpecific<DatreeInstallation>, NodeSpecific<DatreeInstallation> {

    @DataBoundConstructor
    public DatreeInstallation(String name, String home, List<? extends ToolProperty<?>> properties) {
        super(name, home, properties);
    }

    @Override
    public DatreeInstallation forEnvironment(EnvVars environment) {
        return new DatreeInstallation(getName(), environment.expand(getHome()), getProperties().toList());
    }

    @Override
    public DatreeInstallation forNode(@NonNull Node node, TaskListener log) throws IOException, InterruptedException {
        return new DatreeInstallation(getName(), translateFor(node, log), getProperties().toList());
    }

    @Extension
    @Symbol("datree")
    public static class DatreeInstallationDescriptor extends ToolDescriptor<DatreeInstallation> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Datree";
        }

        @Override
        public List<? extends ToolInstaller> getDefaultInstallers() {
            return Collections.singletonList(new DatreeInstaller("Datree 0.13.7", "0.13.7"));
        }

        @Override
        public DatreeInstallation[] getInstallations() {
            return Jenkins.get().getDescriptorByType(DatreeStepBuilder.DatreeStepBuilderDescriptor.class).getInstallations();
        }

        @Override
        public void setInstallations(DatreeInstallation... installations) {
            Jenkins.get().getDescriptorByType(DatreeStepBuilder.DatreeStepBuilderDescriptor.class).setInstallations(installations);
        }
    }
}
