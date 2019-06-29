package io.github.erfangc.sdk.generator.java.general;

import io.github.erfangc.sdk.generator.java.JavaOptions;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.github.erfangc.sdk.generator.IOUtils.readFileFully;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SdkProcessorTest {
    @Test
    public void processApi() {
        JavaOptions options = new JavaOptions()
                .setEmail("erfangc@gmail.com")
                .setName("Erfang Chen")
                .setInputDirectory("src/test/resources")
                .setOperationsPackageName("io.github.erfangc.sdk.operations")
                .setModelsPackageName("io.github.erfangc.sdk.models")
                .setApiPackageName("io.github.erfangc.sdk.apis");
        final SdkProcessor sdkProcessor = new SdkProcessor();
        SDK sdk = sdkProcessor.processSdk(options);
        final List<OutputFile> outputFiles = sdk.getOutputFiles();
        final Map<String, OutputFile> outputFileMap = outputFiles
                .stream()
                .collect(toMap(OutputFile::getPath, Function.identity()));

        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/AccessTokenInterceptor.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/Auth0.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/CredentialFile.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/Credentials.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/CompanySdk.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/PKCEService.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Error.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Label.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Owner.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/OwnerAddress.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Pet.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/LabelType.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Pets.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/operations/PetStore.java"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/resources/pkce-successful.html"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/src/main/resources/pkce-failed.html"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/.gitignore"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/.gitlab-ci.yaml"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/pom.xml"));
        assertTrue(outputFileMap.containsKey("/tmp/company-sdk/README.md"));

        assertEquals(21, outputFiles.size());

        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/apis/AccessTokenInterceptor.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/AccessTokenInterceptor.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/apis/Auth0.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/Auth0.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/apis/CredentialFile.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/CredentialFile.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/apis/Credentials.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/Credentials.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/apis/CompanySdk.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/CompanySdk.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/apis/PKCEService.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/apis/PKCEService.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Error.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Error.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Label.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Label.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Owner.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Owner.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/OwnerAddress.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/OwnerAddress.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Pet.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Pet.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Pets.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/models/Pets.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/operations/PetStore.java"), outputFileMap.get("/tmp/company-sdk/src/main/java/io/github/erfangc/sdk/operations/PetStore.java").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/resources/pkce-successful.html"), outputFileMap.get("/tmp/company-sdk/src/main/resources/pkce-successful.html").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/resources/pkce-failed.html"), outputFileMap.get("/tmp/company-sdk/src/main/resources/pkce-failed.html").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/.gitignore"), outputFileMap.get("/tmp/company-sdk/.gitignore").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/.gitlab-ci.yaml"), outputFileMap.get("/tmp/company-sdk/.gitlab-ci.yaml").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/pom.xml"), outputFileMap.get("/tmp/company-sdk/pom.xml").getContent());
        assertEquals(readFileFully("src/test/petstore-sdk/README.md"), outputFileMap.get("/tmp/company-sdk/README.md").getContent());
    }
}