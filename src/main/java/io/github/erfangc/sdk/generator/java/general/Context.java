package io.github.erfangc.sdk.generator.java.general;

import io.github.erfangc.sdk.generator.java.operations.Client;

import java.util.List;

public class Context {
    private String version;
    private String groupId;
    private String artifactId;
    private String repository;
    private String repositoryId;
    private String snapshotRepository;
    private String snapshotRepositoryId;
    private String packageName;
    private String sdkClientId;
    private String authorizationServer;
    private String credentialsFilePath;
    private String sdkName;
    private String sdkAudience;
    private List<Client> clients;
    private Scm scm;
    private License license;
    private String name;
    private String email;
    private String url;

    public Scm getScm() {
        return scm;
    }

    public Context setScm(Scm scm) {
        this.scm = scm;
        return this;
    }

    public License getLicense() {
        return license;
    }

    public Context setLicense(License license) {
        this.license = license;
        return this;
    }

    public String getName() {
        return name;
    }

    public Context setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Context setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Context setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getRepository() {
        return repository;
    }

    public Context setRepository(String repository) {
        this.repository = repository;
        return this;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public Context setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        return this;
    }

    public String getSnapshotRepository() {
        return snapshotRepository;
    }

    public Context setSnapshotRepository(String snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
        return this;
    }

    public String getSnapshotRepositoryId() {
        return snapshotRepositoryId;
    }

    public Context setSnapshotRepositoryId(String snapshotRepositoryId) {
        this.snapshotRepositoryId = snapshotRepositoryId;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public Context setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public Context setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public Context setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getSdkClientId() {
        return sdkClientId;
    }

    public Context setSdkClientId(String sdkClientId) {
        this.sdkClientId = sdkClientId;
        return this;
    }

    public String getAuthorizationServer() {
        return authorizationServer;
    }

    public Context setAuthorizationServer(String authorizationServer) {
        this.authorizationServer = authorizationServer;
        return this;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public Context setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
        return this;
    }

    public String getSdkName() {
        return sdkName;
    }

    public Context setSdkName(String sdkName) {
        this.sdkName = sdkName;
        return this;
    }

    public String getSdkAudience() {
        return sdkAudience;
    }

    public Context setSdkAudience(String sdkAudience) {
        this.sdkAudience = sdkAudience;
        return this;
    }

    public List<Client> getClients() {
        return clients;
    }

    public Context setClients(List<Client> clients) {
        this.clients = clients;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Context setVersion(String version) {
        this.version = version;
        return this;
    }
}
