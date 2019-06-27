package io.github.erfangc.sdk.generator;

import java.io.*;
import java.util.stream.Collectors;

public class IOUtils {
    public static String readResourceFully(String fullPath) {
        final InputStream inputStream = IOUtils.class.getClassLoader().getResourceAsStream(fullPath);
        assert inputStream != null;
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final String content = bufferedReader.lines().collect(Collectors.joining("\n"));
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static String readFileFully(String fullpath) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(fullpath));
            final String content = bufferedReader.lines().collect(Collectors.joining("\n"));
            bufferedReader.close();
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
