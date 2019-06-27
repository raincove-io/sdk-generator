package io.github.erfangc.sdk.apis;

import feign.FeignException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.erfangc.sdk.apis.AccessTokenInterceptor.audience;
import static io.github.erfangc.sdk.apis.CredentialFile.saveCredentials;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

class PKCEService {

    private static final Logger logger = LoggerFactory.getLogger(PKCEService.class);
    private int port = 8085;
    private String redirectUri = "http://localhost:" + port + "/oauth/callback";
    //
    // this is the SDK's clientId not to be confused with arbitrary clientId/clientSecret pairs
    // that could be present on the program's ENV, which would trigger a access token request by
    // Client Credentials Grant
    //
    // in this case (PKCE), the access token obtained will assert the identity and consent of end-user (john.doe@company.com)
    //
    private String clientId = "testClientId";

    Credentials runPKCE() {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        //
        // open the user's browser to the authUrl
        //
        openBrowser(codeChallenge);
        try (ServerSocket socket = new ServerSocket(port)) {
            logger.info("Waiting for user to login via the browser ...");
            Socket connection = socket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintStream response = new PrintStream(new BufferedOutputStream(connection.getOutputStream()));
            String request = in.readLine();
            while (true) {
                String ignore = in.readLine();
                if (ignore == null || ignore.length() == 0) break;
            }
            Credentials credentials = null;
            String newLine = "\r\n";
            if (!request.startsWith("GET /oauth/callback") ||
                    !(request.endsWith(" HTTP/1.0") || request.endsWith(" HTTP/1.1"))) {
                logger.error("Invalid callback requested, terminating, request={}", request);
                response.print("HTTP/1.0 400 Bad Request" + newLine + newLine);
            } else {
                logger.info("Processing authorization code");
                final URL url = new URL("http://localhost:" + port + request.split("\\s+")[1]);
                final String query = url.getQuery();
                List<String> queryParams = asList(query.split("&"));
                final Map<String, String> queryParamsMap = queryParams.stream().collect(toMap(p -> p.split("=")[0], p -> p.split("=")[1]));
                try {
                    String code = queryParamsMap.get("code");
                    logger.info("Exchanging authorization code");
                    credentials = Auth0
                            .getInstance()
                            .exchangeCode(
                                    "authorization_code",
                                    clientId,
                                    codeVerifier,
                                    code,
                                    redirectUri
                            );
                    saveCredentials(credentials);
                    final String html = pkceSuccessful();
                    response.print(
                            "HTTP/1.0 200 OK" + newLine +
                                    "Content-Type: text/html" + newLine +
                                    "Content-length: " + html.length() + newLine + newLine +
                                    html
                    );
                    response.close();
                    logger.info("Successfully exchanged authorization code");
                } catch (FeignException e) {
                    final String html = pkceFailed();
                    response.print("HTTP/1.0 500 Internal Server Error" + newLine +
                            "Content-Type: text/html" + newLine +
                            "Content-Length: " + html.length() + newLine + newLine +
                            html);
                    response.close();
                    logger.info("Failed to exchanged authorization code");
                    throw new RuntimeException(e.contentUTF8(), e);
                }
            }
            return credentials;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateCodeChallenge(String codeVerifier) {
        String codeChallenge;
        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes, 0, bytes.length);
            byte[] digest = md.digest();
            codeChallenge = Base64.encodeBase64URLSafeString(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return codeChallenge;
    }

    private String generateCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] codeBytes = new byte[32];
        sr.nextBytes(codeBytes);
        return Base64.encodeBase64URLSafeString(codeBytes);
    }

    private String pkceSuccessful() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PKCEService.class.getClassLoader().getResourceAsStream("pkce-successful.html"))));
        final String html = reader.lines().collect(joining("\n"));
        reader.close();
        return html;
    }

    private String pkceFailed() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PKCEService.class.getClassLoader().getResourceAsStream("pkce-failed.html"))));
        final String html = reader.lines().collect(joining("\n"));
        reader.close();
        return html;
    }

    private void openBrowser(String codeChallenge) {
        final String authUrl = "https://auth.company.com/authorize?" +
                "audience=" + audience + "&" +
                "scope=openid email profile offline_access&" +
                "response_type=code&" +
                "client_id=" + clientId + "&" +
                "code_challenge=" + codeChallenge + "&" +
                "code_challenge_method=S256&" +
                "redirect_uri=" + redirectUri;
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.contains("win")) {
                // this doesn't support showing urls in the form of "page.html#nameLink"
                rt.exec("rundll32 url.dll,FileProtocolHandler " + authUrl);
            } else if (os.contains("mac")) {
                rt.exec(new String[]{"open", authUrl});
            } else if (os.contains("nix") || os.contains("nux")) {
                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};
                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(authUrl).append("\" ");
                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}