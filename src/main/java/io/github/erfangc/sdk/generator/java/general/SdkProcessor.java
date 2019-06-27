package io.github.erfangc.sdk.generator.java.general;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samskivert.mustache.Mustache;
import io.github.erfangc.sdk.generator.IOUtils;
import io.github.erfangc.sdk.generator.java.JavaOptions;
import io.github.erfangc.sdk.generator.java.models.Model;
import io.github.erfangc.sdk.generator.java.models.ModelsProcessor;
import io.github.erfangc.sdk.generator.java.operations.Client;
import io.github.erfangc.sdk.generator.java.operations.OperationsProcessor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

import static io.github.erfangc.sdk.generator.java.JavaCaseUtil.toPascalCase;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class SdkProcessor {
    private static Logger logger = LoggerFactory.getLogger(SdkProcessor.class);

    public SDK processSdk(JavaOptions options) {
        final OperationsProcessor operationsProcessor = new OperationsProcessor(options);
        //
        // process all the components
        //
        SDK ret = new SDK();
        final ArrayList<OutputFile> outputFiles = new ArrayList<>();
        ret.setOutputFiles(outputFiles);
        final String outputDirectory = options.getSdkOutputDirectory();
        final File inputDirectory = new File(options.getInputDirectory());
        final ArrayList<Client> clients = new ArrayList<>();
        final Context context = new Context()
                .setArtifactId(options.getArtifactId())
                .setGroupId(options.getGroupId())
                .setVersion(options.getVersion())
                .setRepository(options.getRepository())
                .setRepositoryId(options.getRepositoryId())
                .setSnapshotRepository(options.getSnapshotRepository())
                .setSnapshotRepositoryId(options.getSnapshotRepositoryId())
                .setAuthorizationServer(options.getAuthorizationServer())
                .setClients(clients)
                .setSdkClientId(options.getSdkClientId())
                .setPackageName(options.getApiPackageName())
                .setCredentialsFilePath(options.getCredentialsFilePath())
                .setSdkAudience(options.getSdkAudience())
                .setSdkName(toPascalCase(options.getSdkName()));
        try {
            logger.info("Running Java SDK generator with the following options");
            logger.info("{}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(options));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        //
        // process each openapi spec found on the input directory
        //
        Map<String, Schema> schemas = new HashMap<>();
        for (File file : Objects.requireNonNull(inputDirectory.listFiles((dir, name) -> name.endsWith(".yaml")))) {
            final ParseOptions parseOptions = new ParseOptions();
            final OpenAPI openAPI = new OpenAPIV3Parser().read(file.getAbsolutePath(), emptyList(), parseOptions);
            schemas.putAll(openAPI.getComponents().getSchemas());

            if (!options.getIgnoreFiles().contains(file.getName())) {
                final Client client = operationsProcessor.produceClientFromOperations(openAPI);
                final String pkgDir = options.getOperationsPackageName().replaceAll("\\.", "/");
                final OutputFile operationsOutputFile = new OutputFile()
                        .setContent(client.getJavaCode())
                        .setPath(new File(outputDirectory, "/src/main/java/" + pkgDir + "/" + client.getContext().getClientName() + ".java").getAbsolutePath());
                clients.add(client);
                outputFiles.add(operationsOutputFile);
            }
        }

        outputFiles.addAll(getModelOutputFiles(schemas, options));
        outputFiles.addAll(apiFiles(options, context));
        outputFiles.add(getSdkOutputFile(options, context));

        if (options.getPkceSuccessfulFile() != null) {
            outputFiles.add(new OutputFile().setPath(outputDirectory + "/src/main/resources/pkce-successful.html").setContent(IOUtils.readFileFully(options.getPkceSuccessfulFile())));
        } else {
            outputFiles.add(new OutputFile().setPath(outputDirectory + "/src/main/resources/pkce-successful.html").setContent(IOUtils.readResourceFully("java/pkce-successful.html")));
        }
        if (options.getPkceFailedFile() != null) {
            outputFiles.add(new OutputFile().setPath(outputDirectory + "/src/main/resources/pkce-failed.html").setContent(IOUtils.readFileFully(options.getPkceFailedFile())));
        } else {
            outputFiles.add(new OutputFile().setPath(outputDirectory + "/src/main/resources/pkce-failed.html").setContent(IOUtils.readResourceFully("java/pkce-failed.html")));
        }

        outputFiles.add(new OutputFile().setPath(outputDirectory + "/.gitlab-ci.yaml").setContent(IOUtils.readResourceFully("java/.gitlab-ci.yaml")));
        outputFiles.add(new OutputFile().setPath(outputDirectory + "/README.md").setContent(Mustache.compiler().compile(IOUtils.readResourceFully("java/README.md")).execute(context)));
        outputFiles.add(new OutputFile().setPath(outputDirectory + "/pom.xml").setContent(Mustache.compiler().compile(IOUtils.readResourceFully("java/pom.xml")).execute(context)));
        outputFiles.add(new OutputFile().setPath(outputDirectory + "/.gitignore").setContent(".idea\n*.iml\ntarget"));

        return ret;
    }

    private OutputFile getSdkOutputFile(JavaOptions options, Context context) {
        //
        // process the main SDK java file
        //
        final String javaCode = Mustache.compiler().compile(IOUtils.readResourceFully("java/api/templates.mustache")).execute(context);
        final String apiPkgDir = options.getApiPackageName().replaceAll("\\.", "/");
        return new OutputFile().setContent(javaCode).setPath(options.getSdkOutputDirectory() + "/src/main/java/" + apiPkgDir + "/" + context.getSdkName() + ".java");
    }

    private List<OutputFile> getModelOutputFiles(Map<String, Schema> schemas, JavaOptions options) {
        ModelsProcessor modelsProcessor = new ModelsProcessor(options);
        String modelsPackageName = options.getModelsPackageName();
        String outputDirectory = options.getSdkOutputDirectory();
        final List<Model> models = schemas
                .entrySet()
                .stream()
                .flatMap(schemaEntry -> modelsProcessor.processSchema(schemaEntry.getValue(), schemaEntry.getKey()).stream())
                .distinct()
                .collect(toList());
        return models
                .stream()
                .map(model -> {
                            final String modelsPath = modelsPackageName.replaceAll("\\.", "/");
                            final File outputFile = new File(outputDirectory, "src/main/java/" + modelsPath + "/" + model.getId() + ".java");
                            return new OutputFile()
                                    .setContent(model.getJavaCode())
                                    .setPath(outputFile.getAbsolutePath());
                        }
                )
                .collect(toList());
    }

    private List<OutputFile> apiFiles(JavaOptions options, Context context) {
        //
        // compile and substitute the API and authentication files
        //
        final Mustache.Compiler compiler = Mustache.compiler();
        return Stream.of(
                "java/api/AccessTokenInterceptor.mustache",
                "java/api/Auth0.mustache",
                "java/api/CredentialFile.mustache",
                "java/api/Credentials.mustache",
                "java/api/PKCEService.mustache"
        )
                .map(templateFile -> {
                    final String javaCode = compiler.compile(IOUtils.readResourceFully(templateFile)).execute(context);
                    final String[] split = templateFile.split("/");
                    final String fileName = split[split.length - 1];
                    final String pkgDir = options.getApiPackageName().replaceAll("\\.", "/");
                    String outputDirectory = options.getSdkOutputDirectory();
                    return new OutputFile()
                            .setPath(outputDirectory + "/src/main/java/" + pkgDir + "/" + fileName.replace(".mustache", ".java"))
                            .setContent(javaCode);
                })
                .collect(toList());
    }
}
