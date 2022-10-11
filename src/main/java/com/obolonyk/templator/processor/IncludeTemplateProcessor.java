package com.obolonyk.templator.processor;

import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;

public class IncludeTemplateProcessor implements TemplateProcessor {
    private static final Pattern VALUE_CLOSE_TAG = Pattern.compile(">");
    private static final Pattern VALUE_SPLIT_PATTERN = Pattern.compile("\"");
    private static final String INCLUDE_PATTERN = "<#include";

    private String pathToDir;

    public IncludeTemplateProcessor(String pathToDir) {
        this.pathToDir = pathToDir;
    }


    @SneakyThrows
    @Override
    public String process(String template, Map<String, Object> params) {
        String resultTemplate = template;
        String[] lines = VALUE_CLOSE_TAG.split(template);
        for (String line : lines) {
            String strip = line.strip();
            if (strip.startsWith(INCLUDE_PATTERN)) {
                String[] words = VALUE_SPLIT_PATTERN.split(line);
                String fileName = words[1];
                InputStream resourceAsStream = IncludeTemplateProcessor.class.getClassLoader()
                        .getResourceAsStream(String.valueOf(new File(pathToDir + File.separator + fileName)));
                String include = new String(resourceAsStream.readAllBytes());
                String substring = StringEscapeUtils.escapeHtml4(include.substring(0, include.length() - 1));
                resultTemplate = resultTemplate.replace(line, substring);
            }
        }
        return resultTemplate;
    }
}
