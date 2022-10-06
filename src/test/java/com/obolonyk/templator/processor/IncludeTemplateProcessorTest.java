package com.obolonyk.templator.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class IncludeTemplateProcessorTest {
    @Test
    @DisplayName("test IncludeTemplateProcessor")
    void testIncludeTemplateProcessor(){
        String template = """
                <!DOCTYPE html>
                <html lang="en">
                <#include "_head.html">
                <body style="margin-left: 40px">
                                
                <h1>Product</h1>
                """;
        String include = """
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
                          rel="stylesheet"
                          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
                          crossorigin="anonymous">
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
                            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
                            crossorigin="anonymous"></script>
                                
                    <title>Online Shop</title>
                </head>
                """;

        String expectedTemplate = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
                          rel="stylesheet"
                          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
                          crossorigin="anonymous">
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
                            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
                            crossorigin="anonymous"></script>
                                
                    <title>Online Shop</title>
                </head>
                <body style="margin-left: 40px">
                                
                <h1>Product</h1>
                """;
        IncludeTemplateProcessor includeTemplateProcessor = new IncludeTemplateProcessor("src/test/resources/templates/");
        String resultTemplate = includeTemplateProcessor.process(template, Collections.emptyMap()).replace("\n", "").replace(" ", "");
        assertEquals(expectedTemplate.replace("\n", "").replace(" ", ""), resultTemplate);
    }

}