package io.jenkins.plugins.datree;

import hudson.model.Run;
import jenkins.model.RunAction2;

public class DatreeAction implements RunAction2 {
    private transient Run<?, ?> run;

    @Override
    public void onAttached(Run<?, ?> run) {
        this.run = run;
    }

    @Override
    public void onLoad(Run<?, ?> run) {
        this.run = run;
    }

    @SuppressWarnings("unused")
    public Run<?, ?> getRun() {
        return run;
    }

    @Override
    public String getIconFileName() {
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        return "Datree";
    }

    @Override
    public String getUrlName() {
        return "datree";
    }
}
