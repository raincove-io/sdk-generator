package io.github.erfangc.sdk.generator.java.operations;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.github.erfangc.sdk.generator.IOUtils;
import io.github.erfangc.sdk.generator.java.Import;
import io.github.erfangc.sdk.generator.java.JavaOptions;
import io.github.erfangc.sdk.generator.java.JavaType;
import io.github.erfangc.sdk.generator.java.JavaTypeResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;

import java.util.*;

import static io.github.erfangc.sdk.generator.java.JavaCaseUtil.toCamelCase;
import static io.github.erfangc.sdk.generator.java.JavaCaseUtil.toPascalCase;
import static io.github.erfangc.sdk.generator.java.JavaTypeResolver.toJavaType;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * {@link OperationsProcessor} processes operations
 */
public class OperationsProcessor {

    private final JavaOptions options;

    public OperationsProcessor(JavaOptions options) {
        this.options = options;
    }

    public Client produceClientFromOperations(OpenAPI openapi) {
        Paths paths = openapi.getPaths();
        Client ret = new Client();
        Set<Import> imports = new HashSet<>();
        Info info = openapi.getInfo();
        String clientName = toPascalCase(info.getTitle());
        ArrayList<InternalOperation> allInternalOperations = new ArrayList<>();
        Context context = new Context();
        for (Map.Entry<String, PathItem> pathItemEntry : paths.entrySet()) {
            //
            // process all the VERB(s)
            //
            List<InternalOperation> internalOperations = processPathItem(pathItemEntry);
            //
            // process imports
            //
            imports.addAll(internalOperations.stream().flatMap(o -> o.getRequiredImports().stream()).collect(toList()));
            allInternalOperations.addAll(internalOperations);
        }
        final String operationsPackageName = options.getOperationsPackageName();
        context.setPackageName(operationsPackageName);
        context.setImports(imports);
        context.setClientName(clientName);
        context.setOperations(allInternalOperations);
        final Template template = Mustache.compiler().compile(IOUtils.readResourceFully("java/operations/template.mustache"));
        String javaCode = template.execute(context);
        ret
                .setContext(context)
                .setJavaCode(javaCode)
                .setClientName(toCamelCase(clientName))
                .setClientType(toPascalCase(clientName))
                .setPackageName(operationsPackageName)
                .setBaseUrl(getBaseUrl(openapi));
        return ret;
    }

    private String getBaseUrl(OpenAPI openapi) {
        String baseUrl = openapi.getServers().stream().findFirst().map(Server::getUrl).orElse("http://localhost:8080");
        if (options.getServiceEndpoint() != null) {
            baseUrl = options.getServiceEndpoint() + baseUrl;
        }
        return baseUrl;
    }

    private List<InternalOperation> processPathItem(Map.Entry<String, PathItem> pathItemEntry) {
        List<InternalOperation> ret = new ArrayList<>();
        final PathItem pathItem = pathItemEntry.getValue();
        if (pathItem.getGet() != null) {
            ret.add(processSingleItem(pathItemEntry, pathItem.getGet(), PathItem.HttpMethod.GET));
        }
        if (pathItem.getDelete() != null) {
            ret.add(processSingleItem(pathItemEntry, pathItem.getDelete(), PathItem.HttpMethod.DELETE));
        }
        if (pathItem.getPatch() != null) {
            ret.add(processSingleItem(pathItemEntry, pathItem.getPatch(), PathItem.HttpMethod.PATCH));
        }
        if (pathItem.getPost() != null) {
            ret.add(processSingleItem(pathItemEntry, pathItem.getPost(), PathItem.HttpMethod.POST));
        }
        if (pathItem.getPut() != null) {
            ret.add(processSingleItem(pathItemEntry, pathItem.getPut(), PathItem.HttpMethod.PUT));
        }
        return ret;
    }

    private InternalOperation processSingleItem(Map.Entry<String, PathItem> pathItemEntry,
                                                io.swagger.v3.oas.models.Operation operation,
                                                PathItem.HttpMethod method) {
        List<Param> operationParams = getOperationParams(operation);
        String path = pathItemEntry.getKey();
        JavaType reponseType = getReponseType(operation);
        JavaType requestBody = getRequestBody(operation);
        List<Import> requiredImports = new ArrayList<>();
        if (requestBody != null) {
            requiredImports.addAll(requestBody.getRequiredImports());
        }
        requiredImports.addAll(reponseType.getRequiredImports());
        return new InternalOperation()
                .setName(getOperationName(operation))
                .setParams(operationParams)
                .setRequestBody(requestBody)
                .setResponseType(reponseType)
                .setRequestLine(getRequestLine(path, method, operation))
                .setRequiredImports(requiredImports);
    }

    private JavaType getReponseType(io.swagger.v3.oas.models.Operation operation) {
        return operation
                .getResponses()
                .entrySet()
                .stream()
                .filter(entry -> {
                    final int statusCode = parseInt(entry.getKey());
                    return statusCode >= 200 && statusCode < 300;
                })
                .findFirst()
                .map(entry -> {
                    final ApiResponse apiResponse = entry.getValue();
                    final Content content = apiResponse.getContent();
                    if (content == null) {
                        return JavaType.Void;
                    }
                    final MediaType applicationJSON = content.get("application/json");
                    if (applicationJSON == null) {
                        return JavaType.Void;
                    }
                    final Schema schema = applicationJSON.getSchema();
                    return toJavaType(schema, options.getModelsPackageName());
                })
                .orElse(JavaType.Void);
    }

    private String getRequestLine(String path, PathItem.HttpMethod method, io.swagger.v3.oas.models.Operation operation) {
        final List<Param> params = getOperationParams(operation);
        final String url = method.toString() + " " + path;
        final List<Param> queryParams = params
                .stream()
                .filter(param -> !param.isPathVariable())
                .collect(toList());
        if (!queryParams.isEmpty()) {
            return url + "?" + queryParams
                    .stream()
                    .map(param -> param.getName() + "={" + param.getName() + "}")
                    .collect(joining("&"));
        } else {
            return url;
        }
    }

    private JavaType getRequestBody(io.swagger.v3.oas.models.Operation operation) {
        final RequestBody requestBody = operation.getRequestBody();
        if (requestBody == null) {
            return null;
        }
        final Content content = requestBody.getContent();
        if (content == null) {
            return null;
        }
        final MediaType applicationJSON = content.get("application/json");
        if (applicationJSON == null) {
            return null;
        }
        return JavaTypeResolver.toJavaType(applicationJSON.getSchema(), options.getModelsPackageName());
    }

    private List<Param> getOperationParams(io.swagger.v3.oas.models.Operation operation) {
        final List<Parameter> parameters = operation.getParameters();
        if (parameters == null) {
            return Collections.emptyList();
        }
        List<Param> ret = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            JavaType javaType = toJavaType(parameter.getSchema(), options.getModelsPackageName());
            Param param = new Param();
            param.setName(parameter.getName());
            param.setType(javaType.getTypeName());
            param.setLast(i == parameters.size() - 1);
            param.setPathVariable(parameter.getIn().equals("path"));
            ret.add(param);
        }
        return ret;
    }

    private String getOperationName(io.swagger.v3.oas.models.Operation operation) {
        final String operationId = operation.getOperationId();
        return toCamelCase(operationId);
    }

}
