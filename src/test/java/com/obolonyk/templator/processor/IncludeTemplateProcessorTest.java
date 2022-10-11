package com.obolonyk.templator.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class IncludeTemplateProcessorTest {
    @Test
    @DisplayName("test IncludeTemplateProcessor")
    void testIncludeTemplateProcessor() {
        String template = """
                <!DOCTYPE html>
                <html lang="en">
                <#include "_head.html">
                <body style="margin-left: 40px">
                                
                <h1>Product</h1>
                """;

        IncludeTemplateProcessor includeTemplateProcessor = new IncludeTemplateProcessor("templates");
        String resultTemplate = includeTemplateProcessor.process(template, Collections.emptyMap());
        assertTrue(resultTemplate.contains("Online Shop"));
        assertTrue(resultTemplate.contains("sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"));
    }

}