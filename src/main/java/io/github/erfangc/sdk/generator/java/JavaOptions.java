package io.github.erfangc.sdk.generator.java;

import com.beust.jcommander.Parameter;

import java.util.List;

import static java.util.Collections.singletonList;

public class JavaOptions {
    @Parameter(names = "--help", help = true)
    private boolean help;
    @Parameter(names = "--groupId")
    private String groupId = "com.company";
    @Parameter(names = "--artifactId")
    private String artifactId = "sdk";
    @Parameter(names = "--version")
    private String version = "0.0.1-SNAPSHOT";
    @Parameter(names = "--ignoreFiles")
    private List<String> ignoreFiles = singletonList("common.yaml");
    @Parameter(names = "--inputDirectory")
    private String inputDirectory = "./";
    @Parameter(names = "--modelsPackageName")
    private String modelsPackageName = "com.company.sdk.models";
    @Parameter(names = "--operationsPackageName")
    private String operationsPackageName = "com.company.sdk.operations";
    @Parameter(names = "--apiPackageName")
    private String apiPackageName = "com.company.sdk.apis";
    @Parameter(names = "--sdkName")
    private String sdkName = "company-sdk";
    @Parameter(names = "--sdkAudience")
    private String sdkAudience = "https://api.company.com";
    @Parameter(names = "--sdkOutputDirectory")
    private String sdkOutputDirectory = "/tmp/company-sdk";
    @Parameter(names = "--sdkClientId")
    private String sdkClientId = "testClientId";
    @Parameter(names = "--authorizationServer")
    private String authorizationServer = "https://auth.company.com";
    @Parameter(names = "--credentialsFilePath")
    private String credentialsFilePath = ".sdk/credentials.json";
    @Parameter(names = "--serviceEndpoint")
    private String serviceEndpoint = "http://localhost:8080";
    @Parameter(names = "--repository")
    private String repository = "https://oss.sonatype.org/service/local/staging/deploy/maven2";
    @Parameter(names = "--repositoryId")
    private String repositoryId = "ossrh";
    @Parameter(names = "--snapshotRepository")
    private String snapshotRepository = "https://oss.sonatype.org/content/repositories/snapshots";
    @Parameter(names = "--snapshotRepositoryId")
    private String snapshotRepositoryId = "ossrh";
    @Parameter(names = "--pkceSuccessfulFile")
    private String pkceSuccessfulFile;
    @Parameter(names = "--pkceFailedFile")
    private String pkceFailedFile;
    @Parameter(names = "--scmConnection")
    private String scmConnection;
    @Parameter(names = "--scmDeveloperConnection")
    private String scmDeveloperConnection;
    @Parameter(names = "--scmUrl")
    private String scmUrl;
    @Parameter(names = "--name")
    private String name = System.getProperty("user.name");
    @Parameter(names = "--email")
    private String email = System.getProperty("user.name") + "@gmail.com";
    @Parameter(names = "--url")
    private String url = "https://api.company.com";
    @Parameter(names = "--license")
    private String license = "apache2";

    public String getScmConnection() {
        return scmConnection;
    }

    public JavaOptions setScmConnection(String scmConnection) {
        this.scmConnection = scmConnection;
        return this;
    }

    public String getScmDeveloperConnection() {
        return scmDeveloperConnection;
    }

    public JavaOptions setScmDeveloperConnection(String scmDeveloperConnection) {
        this.scmDeveloperConnection = scmDeveloperConnection;
        return this;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public JavaOptions setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
        return this;
    }

    public String getPkceSuccessfulFile() {
        return pkceSuccessfulFile;
    }

    public JavaOptions setPkceSuccessfulFile(String pkceSuccessfulFile) {
        this.pkceSuccessfulFile = pkceSuccessfulFile;
        return this;
    }

    public String getPkceFailedFile() {
        return pkceFailedFile;
    }

    public JavaOptions setPkceFailedFile(String pkceFailedFile) {
        this.pkceFailedFile = pkceFailedFile;
        return this;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public JavaOptions setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        return this;
    }

    public String getSnapshotRepositoryId() {
        return snapshotRepositoryId;
    }

    public JavaOptions setSnapshotRepositoryId(String snapshotRepositoryId) {
        this.snapshotRepositoryId = snapshotRepositoryId;
        return this;
    }

    public String getRepository() {
        return repository;
    }

    public JavaOptions setRepository(String repository) {
        this.repository = repository;
        return this;
    }

    public String getSnapshotRepository() {
        return snapshotRepository;
    }

    public JavaOptions setSnapshotRepository(String snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public JavaOptions setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public JavaOptions setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getSdkClientId() {
        return sdkClientId;
    }

    public JavaOptions setSdkClientId(String sdkClientId) {
        this.sdkClientId = sdkClientId;
        return this;
    }

    public String getAuthorizationServer() {
        return authorizationServer;
    }

    public JavaOptions setAuthorizationServer(String authorizationServer) {
        this.authorizationServer = authorizationServer;
        return this;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public JavaOptions setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
        return this;
    }

    public String getModelsPackageName() {
        return modelsPackageName;
    }

    public JavaOptions setModelsPackageName(String modelsPackageName) {
        this.modelsPackageName = modelsPackageName;
        return this;
    }

    public String getOperationsPackageName() {
        return operationsPackageName;
    }

    public JavaOptions setOperationsPackageName(String operationsPackageName) {
        this.operationsPackageName = operationsPackageName;
        return this;
    }

    public String getApiPackageName() {
        return apiPackageName;
    }

    public JavaOptions setApiPackageName(String apiPackageName) {
        this.apiPackageName = apiPackageName;
        return this;
    }

    public String getSdkName() {
        return sdkName;
    }

    public JavaOptions setSdkName(String sdkName) {
        this.sdkName = sdkName;
        return this;
    }

    public String getSdkOutputDirectory() {
        return sdkOutputDirectory;
    }

    public JavaOptions setSdkOutputDirectory(String sdkOutputDirectory) {
        this.sdkOutputDirectory = sdkOutputDirectory;
        return this;
    }

    public String getSdkAudience() {
        return sdkAudience;
    }

    public JavaOptions setSdkAudience(String sdkAudience) {
        this.sdkAudience = sdkAudience;
        return this;
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public JavaOptions setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
        return this;
    }

    public List<String> getIgnoreFiles() {
        return ignoreFiles;
    }

    public JavaOptions setIgnoreFiles(List<String> ignoreFiles) {
        this.ignoreFiles = ignoreFiles;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public JavaOptions setVersion(String version) {
        this.version = version;
        return this;
    }

    public boolean isHelp() {
        return help;
    }

    public JavaOptions setHelp(boolean help) {
        this.help = help;
        return this;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public JavaOptions setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
        return this;
    }

    public String getName() {
        return name;
    }

    public JavaOptions setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public JavaOptions setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public JavaOptions setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLicense() {
        return license;
    }

    public JavaOptions setLicense(String license) {
        this.license = license;
        return this;
    }

}
