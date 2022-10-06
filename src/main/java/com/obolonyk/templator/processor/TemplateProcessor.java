package com.obolonyk.templator.processor;

import java.util.Map;

public interface TemplateProcessor {
    String process (String template, Map<String, Object> params);
}
