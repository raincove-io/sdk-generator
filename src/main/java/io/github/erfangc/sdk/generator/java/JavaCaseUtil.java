package io.github.erfangc.sdk.generator.java;

import org.apache.commons.text.CaseUtils;

public class JavaCaseUtil {

    public static String toPascalCase(String input) {
        if (input == null) {
            return null;
        } else if (input.contains(" ")) {
            return CaseUtils.toCamelCase(input, true);
        } else if (input.contains("-")) {
            return CaseUtils.toCamelCase(input.replaceAll("-", " "), true);
        } else if (input.equals(input.toUpperCase())) {
            return input;
        } else {
            final char c = input.charAt(0);
            if (Character.isUpperCase(c)) {
                return input;
            } else {
                final char upperC = Character.toUpperCase(c);
                return upperC + input.substring(1);
            }
        }
    }

    public static String toCamelCase(String input) {
        if (input == null) {
            return null;
        } else if (input.contains(" ")) {
            return CaseUtils.toCamelCase(input, false);
        } else if (input.contains("-")) {
            return CaseUtils.toCamelCase(input.replaceAll("-", " "), false);
        } else if (input.equals(input.toUpperCase())) {
            return input.toLowerCase();
        } else {
            final char c = input.charAt(0);
            if (Character.isLowerCase(c)) {
                return input;
            } else {
                final char lowerC = Character.toLowerCase(c);
                return lowerC + input.substring(1);
            }
        }
    }
}
