# {{sdkName}}

This is the SDK (Software Development Kit) for Petstore. The SDK handles authentication and makes API calls on your behalf to the hosted service

# Authentication

When using the SDK, you may be authenticated through the following means:

 - If the environment variable `CLIENT_ID` and `CLIENT_SECRET` exists, these will be used in an OAuth 2.0 Client Credential Grant flow with the aim of obtaining an JWT access token
 - If a file at `~/{{credentialsFilePath}}` exists and contains valid access tokens, the contents of the files will be used
 - If the credentials file does not exist we will launch a browser window and authenticate you. The access token retrieved here will be saved to the `~/{{credentialsFilePath}}` location for future use
 
Note that most JWT access tokens are short-lived, therefore we will automatically re-authenticate you once the tokens expire 