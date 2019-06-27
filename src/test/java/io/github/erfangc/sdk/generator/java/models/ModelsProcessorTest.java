package io.github.erfangc.sdk.generator.java.models;

import io.github.erfangc.sdk.generator.java.JavaOptions;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.github.erfangc.sdk.generator.IOUtils.readFileFully;
import static org.junit.Assert.assertEquals;

public class ModelsProcessorTest {

    private Map<String, Schema> schemas;
    private ModelsProcessor modelsProcessor;

    @Test
    public void processSchema() {
        final Schema schema = schemas.get("Owner");
        final List<Model> models = modelsProcessor.processSchema(schema, "Owner");
        assertEquals(2, models.size());
        final Model ownerAddress = models.get(0);
        final Model owner = models.get(1);
        assertEquals("OwnerAddress", ownerAddress.getContext().getJavaType().getTypeName());
        assertEquals("Owner", owner.getContext().getJavaType().getTypeName());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Owner.java"), owner.getJavaCode());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models//OwnerAddress.java"), ownerAddress.getJavaCode());
    }

    @Test
    public void processSchemaPets() {
        final Schema pets = schemas.get("Pets");
        final List<Model> models = modelsProcessor.processSchema(pets, "Pets");
        assertEquals(1, models.size());
        assertEquals("Pets", models.get(0).getContext().getJavaType().getTypeName());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Pets.java"), models.get(0).getJavaCode());
    }

    @Before
    public void setUp() throws Exception {
        JavaOptions options = new JavaOptions().setModelsPackageName("io.github.erfangc.sdk.models");
        modelsProcessor = new ModelsProcessor(options);
        OpenAPI openapi = new OpenAPIV3Parser().read("petstore.yaml");
        schemas = openapi
                .getComponents()
                .getSchemas();
    }

    @Test
    public void processSchemaPet() {
        final Schema pet = schemas.get("Pet");
        final List<Model> models = modelsProcessor.processSchema(pet, "Pet");
        assertEquals(1, models.size());
        assertEquals("Pet", models.get(0).getContext().getJavaType().getTypeName());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Pet.java"), models.get(0).getJavaCode());
    }

    @Test
    public void processSchemaLabel() {
        final Schema label = schemas.get("Label");
        final List<Model> models = modelsProcessor.processSchema(label, "Label");
        assertEquals(1, models.size());
        assertEquals("Label", models.get(0).getContext().getJavaType().getTypeName());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Label.java"), models.get(0).getJavaCode());
    }

    @Test
    public void processSchemaError() {
        JavaOptions options = new JavaOptions().setModelsPackageName("io.github.erfangc.sdk.models");
        final ModelsProcessor modelsProcessor = new ModelsProcessor(options);
        OpenAPI openapi = new OpenAPIV3Parser().read("petstore.yaml");
        final Schema pet = openapi
                .getComponents()
                .getSchemas()
                .get("Error");
        final List<Model> models = modelsProcessor.processSchema(pet, "Error");
        assertEquals(1, models.size());
        assertEquals("Error", models.get(0).getContext().getJavaType().getTypeName());
        assertEquals(readFileFully("src/test/petstore-sdk/src/main/java/io/github/erfangc/sdk/models/Error.java"), models.get(0).getJavaCode());
    }

}