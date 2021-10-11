package io.jenkins.plugins.datree;

import hudson.AbortException;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.function.Supplier;

public class DatreeTestStepFlow {

    public static void perform(DatreeConfig config, Supplier<DatreeContext> contextSupplier) throws AbortException {
        boolean foundIssues = false;
        Exception cause = null;
        DatreeContext context = null;

        try {
            context = contextSupplier.get();
            foundIssues = DatreeTestStepFlow.testFile(context, config);
        } catch (IOException | InterruptedException | RuntimeException ex) {
            if (context != null) {
                TaskListener listener = context.getTaskListener();
                if (ex instanceof IOException) {
                    Util.displayIOException((IOException) ex, listener);
                }
                ex.printStackTrace(listener.fatalError("Datree scan failed."));
            }
            cause = ex;
        }

        if (foundIssues) {
            throw new AbortException("Datree found issues.");
        }
    }

    private static boolean testFile(DatreeContext context, DatreeConfig config) throws IOException, InterruptedException {
        DatreeTest.Result testResult = DatreeTest.testFile(context, config);

        addSidebarLink(context);

        return testResult.foundIssues;
    }

    private static void addSidebarLink(DatreeContext context) {
        Run<?, ?> run = context.getRun();
        if (run.getActions(DatreeAction.class).isEmpty()) {
            run.addAction(new DatreeAction());
        }
    }
}
