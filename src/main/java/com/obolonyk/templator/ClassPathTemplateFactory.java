package com.obolonyk.templator;

import com.obolonyk.templator.processor.ForEachTemplateProcessor;
import com.obolonyk.templator.processor.IncludeTemplateProcessor;
import com.obolonyk.templator.processor.TemplateProcessor;
import com.obolonyk.templator.processor.ValueTemplateProcessor;
import com.obolonyk.templator.reader.ResourceReader;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClassPathTemplateFactory implements TemplateFactory {
    private String pathToFile;

    public ClassPathTemplateFactory(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    @Override
    public String getPage(String path, Map<String, Object> params) {
        String template = ResourceReader.getStringFromFile(pathToFile + path);

        ForEachTemplateProcessor forEachTemplateProcessor = new ForEachTemplateProcessor();
        IncludeTemplateProcessor includeTemplateProcessor = new IncludeTemplateProcessor(pathToFile);
        ValueTemplateProcessor valueTemplateProcessor = new ValueTemplateProcessor();

        List <TemplateProcessor> templateProcessorList = List.of(forEachTemplateProcessor, includeTemplateProcessor, valueTemplateProcessor);
        for (TemplateProcessor templateProcessor : templateProcessorList) {
            template = templateProcessor.process(template, params);
        }
        return template;
    }

    @Override
    public String getPage(String path) {
        return getPage(path, Collections.emptyMap());
    }
}
