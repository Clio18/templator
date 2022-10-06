package com.obolonyk.templator.processor;

import com.obolonyk.templator.reader.ResourceReader;
import java.util.Map;

public class IncludeTemplateProcessor implements TemplateProcessor{
    private static final String SPLIT_TAG_PATTERN = ">";
    private static final String SPLIT_PATTERN = "\"";
    private static final String INCLUDE_PATTERN = "<#include";

    private String path;

    public IncludeTemplateProcessor(String path) {
        this.path = path;
    }

    @Override
    public String process(String template, Map<String, Object> params) {
        String resultTemplate = template;
        String[] lines = template.split(SPLIT_TAG_PATTERN);
        for (String line : lines) {
            String strip = line.strip();
            if(strip.startsWith(INCLUDE_PATTERN)){
                String[] words = line.split(SPLIT_PATTERN);
                String partPath = words[1];
                String include = ResourceReader.getStringFromFile(path + partPath);
                String substring = include.substring(0, include.length() - 1);
                resultTemplate = resultTemplate.replace(line, substring);
            }
        }
        return resultTemplate;
    }
}
