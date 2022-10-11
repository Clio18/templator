package com.obolonyk.templator;

import java.util.Map;

public interface TemplateFactory {
    String getPage(String path, Map<String, Object> params);

    String getPage(String path);
}
