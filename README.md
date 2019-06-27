# SDK Generator

This is the generator that uses `swagger-parser` to parse openapi 3.0 specs into client SDK libraries

# Motivation

SDKs ease adoption of APIs and hosted-services. The world's fastest moving technology companies enable customers and employees to access their capabilities through APIs, usually implemented as REST APIs

While making raw HTTP REST API calls is not difficult, the amount of orchestrations involved is still not a native experience for developers and data scientist alike. Accessing APIs using SDKs written in the programming language and framework of the user's preference is more ideal and more convenient than making raw HTTP calls

## What's Unique Here?

This library focus on being opinionated and ready to use out of the box. Specifically, this SDK generator defines a pattern for authenticating SDKs with services that is both convenient and transparent to the consumer

# Authentication

The SDK generated using this generator seeks to leverage an opinionated authentication stack  

The generated SDK will authenticate the caller via the following hierarchy

- If a `CLIENT_ID` and `CLIENT_SECRET` is defined the SDK will use a [Client Credentials Grant flow](https://www.oauth.com/oauth2-servers/access-tokens/client-credentials/) to obtain an access token. This logs the user in as a Machine User. This should be used when using the SDK in a production service to service context
- If a credentials file defined via the `--credentialsFilePath` when running the generator exists, then that credentials file's content will be read
- Launch a browser and obtain an access token via the [Authorization Code exchange with PKCE](https://www.oauth.com/oauth2-servers/pkce/). This will log the user in as themselves 

# Installing and Running

```bash
mvn verify

#
# note the generator accepts a --inputDirectory and will seek out any YAML files in that directory
# each YAML file found is converted into an independent API interface along with all the operations declared
# in the file. For example: foo.yaml -> Foo interface
#
java -jar target/sdk-generator.jar --help
```

