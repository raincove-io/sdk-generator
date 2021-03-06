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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static io.github.erfangc.sdk.generator.java.JavaCaseUtil.toCamelCase;
import static io.github.erfangc.sdk.generator.java.JavaCaseUtil.toPascalCase;
import static io.github.erfangc.sdk.generator.java.JavaTypeResolver.toJavaType;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * {@link OperationsProcessor} processes operations
 */
public class OperationsProcessor {

    private final JavaOptions options;
    private static final Logger logger = LoggerFactory.getLogger(OperationsProcessor.class);

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
        return openapi
                .getServers()
                .stream()
                .findFirst()
                .map(Server::getUrl)
                .orElse("/");
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
        JavaType responseType = getResponseType(operation);
        JavaType requestBody = getRequestBody(operation);
        List<Import> requiredImports = new ArrayList<>();
        if (requestBody != null) {
            requiredImports.addAll(requestBody.getRequiredImports());
        }
        requiredImports.addAll(responseType.getRequiredImports());
        List<Header> headers = operationParams
                .stream()
                .filter(Param::isHeaderVariable)
                .map(param -> {
                    Header header = new Header();
                    header
                            .setName("{" + param.getName() + "}")
                            .setRawName(param.getOrigName());
                    return header;
                })
                .collect(toList());
        return new InternalOperation()
                .setName(getOperationName(operation))
                .setParams(operationParams)
                .setRequestBody(requestBody)
                .setResponseType(responseType)
                .setRequestLine(getRequestLine(path, method, operation))
                .setHeaders(headers)
                .setRequiredImports(requiredImports);
    }

    private JavaType getResponseType(io.swagger.v3.oas.models.Operation operation) {
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
                .filter(param -> !param.isPathVariable() && !param.isHeaderVariable())
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
            final Parameter parameter = parameters.get(i);
            if (isValidParameter(parameter)) {
                JavaType javaType = toJavaType(parameter.getSchema(), options.getModelsPackageName());
                Param param = new Param();
                param.setOrigName(parameter.getName());
                param.setName(getName(parameter));
                param.setType(javaType.getTypeName());
                param.setLast(i == parameters.size() - 1);
                param.setPathVariable(parameter.getIn().equals("path"));
                param.setHeaderVariable(parameter.getIn().equals("header"));
                ret.add(param);
            }
        }
        return ret;
    }

    private boolean isValidParameter(Parameter parameter) {
        final List<String> blacklist = asList("Authorization");
        if (blacklist.contains(parameter.getName())) {
            logger.warn("Skipping generation of forbidden parameter {}", parameter.getName());
            return false;
        }
        return true;
    }

    private String getName(Parameter parameter) {
        if (parameter.getIn().equals("header")) {
            // headers can take the form X-Word-X
            return toCamelCase(parameter.getName());
        } else {
            return parameter.getName();
        }
    }

    private String getOperationName(io.swagger.v3.oas.models.Operation operation) {
        final String operationId = operation.getOperationId();
        return toCamelCase(operationId);
    }

}
