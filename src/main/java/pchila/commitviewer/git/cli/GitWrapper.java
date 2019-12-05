package pchila.commitviewer.git.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.stream.Stream;

public class GitWrapper {

    private final static Logger logger = LoggerFactory.getLogger(GitWrapper.class);
    private File tempDir;

    private static void checkGitProcessReturnValue(Process gitProcess) throws GitWrapperException {
        try {
            gitProcess.waitFor();
        } catch (InterruptedException e) {
            throw new GitWrapperException("Interrupted while waiting for the git process " + gitProcess.pid() + " to finish", e);
        }
        if (gitProcess.exitValue() != 0) {
            // collect stderr output useful and attach it to the exception
            String stderr_output = null;
            try {
                stderr_output = collectOutputFromProcessStream(gitProcess.getErrorStream());
            } catch (IOException e) {
                throw new GitWrapperException(
                        "Error while collecting stderr output for abnormally terminated process "
                                + gitProcess.pid(), e
                );
            }
            throw new GitWrapperException(
                    "Git process " + gitProcess.pid() + " terminated abnormally: " + stderr_output
            );
        }
    }

    private static String collectOutputFromProcessStream(InputStream is) throws IOException {
        var builder = new StringBuilder();

        try (var reader = new BufferedReader(new InputStreamReader(is), 65535)) {
            int c = reader.read();
            while (c != -1) {
                builder.append((char) c);
                c = reader.read();
            }
        }
        return builder.toString();
    }

    public void fetch(URL repositoryUrl) throws GitWrapperException {
        try {
            this.tempDir = Files.createTempDirectory("commitviewer").toFile();
            this.tempDir.deleteOnExit();
            logger.debug("Cloning {} into {}", repositoryUrl, this.tempDir.getAbsolutePath());
            Process gitProcess = null;
            gitProcess = new ProcessBuilder("git", "clone", "--bare", repositoryUrl.toExternalForm(), tempDir.getAbsolutePath()).start();
            gitProcess.waitFor();
            logger.debug("Clone command completed!");
            checkGitProcessReturnValue(gitProcess);
        } catch (IOException | InterruptedException e) {
            throw new GitWrapperException("Error cloning repository", e);
        }
    }

    public Stream<String> log(String logFormat, String delimiter, int page, int pageSize) throws GitWrapperException {
        Process gitProcess;
        try {
            gitProcess = new ProcessBuilder(
                    "git",
                    "--git-dir=" + this.tempDir.getAbsolutePath(),
                    "log",
                    "--format=" + logFormat,
                    "-n", Integer.toString(pageSize),
                    "--skip", Integer.toString((page-1)*pageSize)).start();
            logger.debug("Launched git process with pid {} with arguments {}", gitProcess.pid(), gitProcess.info().commandLine());
            gitProcess.onExit().thenAccept( p -> {
                // TODO this should be handled in a different way: we should warn the user if still possible (i.e. we did not complete the output yet)
                try {
                    checkGitProcessReturnValue(p);
                } catch (GitWrapperException e) {
                    logger.error("Git process " + p.pid() + " terminated abnormally during log.", e);
                }
            });
        } catch (IOException e) {
            throw new GitWrapperException("Unable to start git process.", e);
        }

        return new Scanner(gitProcess.getInputStream()).useDelimiter(delimiter).tokens();
    }

}
