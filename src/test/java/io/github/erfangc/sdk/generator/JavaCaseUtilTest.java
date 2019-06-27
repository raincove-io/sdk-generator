package io.github.erfangc.sdk.generator;

import io.github.erfangc.sdk.generator.java.JavaCaseUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaCaseUtilTest {

    @Test
    public void toPascalCase() {
        assertEquals("HelloWorld", JavaCaseUtil.toPascalCase("Hello World"));
        assertEquals("HelloWorld", JavaCaseUtil.toPascalCase("HelloWorld"));
        assertEquals("HelloWorld", JavaCaseUtil.toPascalCase("helloWorld"));
    }

    @Test
    public void toCamelCase() {
        assertEquals("helloWorld", JavaCaseUtil.toCamelCase("Hello World"));
        assertEquals("helloWorld", JavaCaseUtil.toCamelCase("HelloWorld"));
        assertEquals("helloWorld", JavaCaseUtil.toCamelCase("helloWorld"));
    }
}