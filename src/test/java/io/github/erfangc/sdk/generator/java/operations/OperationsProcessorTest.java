package io.github.erfangc.sdk.generator.java.operations;

import io.github.erfangc.sdk.generator.java.JavaOptions;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.junit.Test;

import static io.github.erfangc.sdk.generator.IOUtils.readFileFully;
import static org.junit.Assert.assertEquals;

public class OperationsProcessorTest {
    @Test
    public void processOperation() {
        OpenAPI openapi = new OpenAPIV3Parser().read("petstore.yaml");
        JavaOptions options = new JavaOptions()
                .setModelsPackageName("io.github.erfangc.sdk.models")
                .setOperationsPackageName("io.github.erfangc.sdk.operations");
        final OperationsProcessor operationsProcessor = new OperationsProcessor(options);
        final Client client = operationsProcessor.produceClientFromOperations(openapi);
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/operations/PetStore.java"), client.getJavaCode());
    }
}