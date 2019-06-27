package io.github.erfangc.sdk.generator.java.general;

import java.util.List;

public class SDK {
    private String projectName;
    private String projectOutputDirectory;
    private List<OutputFile> outputFiles;

    public String getProjectName() {
        return projectName;
    }

    public SDK setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getProjectOutputDirectory() {
        return projectOutputDirectory;
    }

    public SDK setProjectOutputDirectory(String projectOutputDirectory) {
        this.projectOutputDirectory = projectOutputDirectory;
        return this;
    }

    public List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    public SDK setOutputFiles(List<OutputFile> outputFiles) {
        this.outputFiles = outputFiles;
        return this;
    }
}
