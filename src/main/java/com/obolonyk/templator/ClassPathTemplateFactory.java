package com.obolonyk.templator;

import com.obolonyk.templator.processor.ForEachTemplateProcessor;
import com.obolonyk.templator.processor.IncludeTemplateProcessor;
import com.obolonyk.templator.processor.TemplateProcessor;
import com.obolonyk.templator.processor.ValueTemplateProcessor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClassPathTemplateFactory implements TemplateFactory {
    private String pathToDir;

    public ClassPathTemplateFactory(String pathToDir) {
        this.pathToDir = pathToDir;
    }

    @SneakyThrows
    @Override
    public String getPage(String path, Map<String, Object> params) {
        InputStream resourceAsStream = ClassPathTemplateFactory.class.getClassLoader()
                .getResourceAsStream(pathToDir + File.separator + path);
        String template = new String(resourceAsStream.readAllBytes());

        ForEachTemplateProcessor forEachTemplateProcessor = new ForEachTemplateProcessor();
        IncludeTemplateProcessor includeTemplateProcessor = new IncludeTemplateProcessor(pathToDir);
        ValueTemplateProcessor valueTemplateProcessor = new ValueTemplateProcessor();

        List<TemplateProcessor> templateProcessorList = List.of(forEachTemplateProcessor,
                includeTemplateProcessor, valueTemplateProcessor);

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
