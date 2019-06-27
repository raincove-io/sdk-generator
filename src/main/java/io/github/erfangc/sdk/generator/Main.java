package io.github.erfangc.sdk.generator;

import com.beust.jcommander.JCommander;
import io.github.erfangc.sdk.generator.java.JavaOptions;
import io.github.erfangc.sdk.generator.java.general.OutputFile;
import io.github.erfangc.sdk.generator.java.general.SDK;
import io.github.erfangc.sdk.generator.java.general.SdkProcessor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final JavaOptions javaOptions = new JavaOptions();
        final JCommander commander = JCommander.newBuilder()
                .addObject(javaOptions)
                .build();
        commander.parse(args);
        if (javaOptions.isHelp()) {
            commander.usage();
            System.exit(1);
        }
        final SdkProcessor sdkProcessor = new SdkProcessor();
        final SDK sdk = sdkProcessor.processSdk(javaOptions);

        //
        // write the files in the SDK
        //
        sdk.getOutputFiles().forEach(Main::writeFile);
    }

    private static void writeFile(OutputFile outputFile) {
        final String path = outputFile.getPath();
        final File file = new File(path);
        final File directory = file.getParentFile();
        try {
            logger.info("Writing file {}", path);
            directory.mkdirs();
            final FileWriter fileWriter = new FileWriter(path);
            IOUtils.write(outputFile.getContent(), fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Failed to write {}, the error was: {}", path, e.getMessage());
        }
    }
}
