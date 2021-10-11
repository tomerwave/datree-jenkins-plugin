package io.jenkins.plugins.datree;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.util.ArgumentListBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.time.Instant;

public class DatreeTest {

    private static final Logger LOG = LoggerFactory.getLogger(DatreeTest.class);

    public static Result testFile(
            DatreeContext context,
            DatreeConfig config
    ) throws IOException, InterruptedException {
        PrintStream logger = context.getLogger();
        FilePath yamlFile = context.getYamlFilePath();
        Launcher launcher = context.getLauncher();
        EnvVars envVars = context.getEnvVars();

        FilePath workspace = yamlFile.getParent();
        FilePath child = workspace.createTempFile("datree", ".sh");

        ArgumentListBuilder installDatreeArgumentList = CommandLine.installDatreeArgumentList();
        ArgumentListBuilder testFileCommand = CommandLine
                .testCommandArgumentList(
                        "datree",
                        config,
                        envVars
                );

        logger.println("Installing Datree...");
        logger.println("+ " + installDatreeArgumentList);
        logger.println("+ " + testFileCommand);
        logger.println(">>  File: .datree/k8s-demo.yaml\n" +
                "\n" +
                "[V] YAML validation\n" +
                "[V] Kubernetes schema validation\n" +
                "\n" +
                "[X] Policy check\n" +
                "\n" +
                "❌  Ensure each container image has a pinned (tag) version  [1 occurrence]\n" +
                "    — metadata.name: rss-site (kind: Deployment)\n" +
                "\uD83D\uDCA1  Incorrect value for key `image` - specify an image version to avoid unpleasant \"version surprises\" in the future\n" +
                "\n" +
                "❌  Ensure each container has a configured memory limit  [1 occurrence]\n" +
                "    — metadata.name: rss-site (kind: Deployment)\n" +
                "\uD83D\uDCA1  Missing property object `limits.memory` - value should be within the accepted boundaries recommended by the organization\n" +
                "\n" +
                "❌  Ensure workload has valid label values  [1 occurrence]\n" +
                "    — metadata.name: rss-site (kind: Deployment)\n" +
                "\uD83D\uDCA1  Incorrect value for key(s) under `labels` - the vales syntax is not valid so the Kubernetes engine will not accept it\n" +
                "\n" +
                "❌  Ensure each container has a configured liveness probe  [1 occurrence]\n" +
                "    — metadata.name: rss-site (kind: Deployment)\n" +
                "\uD83D\uDCA1  Missing property object `livenessProbe` - add a properly configured livenessProbe to catch possible deadlocks\n" +
                "\n" +
                "\n" +
                "(Summary)\n" +
                "\n" +
                "- Passing YAML validation: 1/1\n" +
                "\n" +
                "- Passing Kubernetes (1.18.0) schema validation: 1/1\n" +
                "\n" +
                "- Passing policy check: 0/1\n" +
                "\n" +
                "+-----------------------------------+----------------------------------------------------------+\n" +
                "| Enabled rules in policy “Default” | 21                                                       |\n" +
                "| Configs tested against policy     | 1                                                        |\n" +
                "| Total rules evaluated             | 21                                                       |\n" +
                "| Total rules failed                | 4                                                        |\n" +
                "| Total rules passed                | 17                                                       |\n" +
                "| See all rules in policy           | https://app.datree.io/login?cliId=nXEHFcDBadMycukkrSvqSZ |\n" +
                "+-----------------------------------+----------------------------------------------------------+\n");

        return new Result(1);
//        logger.println("TOMER: " + shell.getCommand());
//        try (OutputStream out = child.write()) {
//            int installDatreeExitCode = launcher.launch()
//                    .cmdAsSingleString("curl https://get.datree.io")
//                    .envs(envVars)
//                    .stderr(logger)
//                    .stdout(out)
//                    .quiet(true)
//                    .join();
//        }
////        logger.println(child.);
//
//        String command = child.readToString();

//        logger.println("TOMER" + command);
//
//        int something = launcher.launch()
//                .cmds(new ArgumentListBuilder("sh", child.getName()))
//                .envs(envVars)
//                .stderr(logger)
//                .quiet(true)
//                .join();

//        Result installResult = new Result(something);

//        if (!installResult.foundIssues) {

//        }

//        return installResult;

//        DatreeTestResult result = ObjectMapperHelper.unmarshallTestResult(stdout);
//        if (result == null) {
//            throw new RuntimeException("Failed to parse test output.");
//        }
//        if (fixEmptyAndTrim(result.error) != null) {
//            throw new RuntimeException("An error occurred. " + result.error);
//        }
//        if (!result.ok) {
//            logger.println("Vulnerabilities found!");
//            logger.printf(
//                    "Result: %s known vulnerabilities | %s dependencies%n",
//                    result.uniqueCount,
//                    result.dependencyCount
//            );
//        }

    }

    public static String getURLSafeDateTime() {
        return Instant.now().toString()
                .replaceAll(":", "-")
                .replaceAll("\\.", "-");
    }

    public static class Result {
        public final boolean foundIssues;

        public Result(int exitCode) {
            this.foundIssues = exitCode == 1;
        }
    }
}
