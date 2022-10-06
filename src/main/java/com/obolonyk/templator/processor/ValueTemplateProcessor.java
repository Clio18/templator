package com.obolonyk.templator.processor;

import com.obolonyk.templator.reflection.ObjectInvoker;

import java.util.*;

public class ValueTemplateProcessor implements TemplateProcessor {
    @Override
    public String process(String template, Map<String, Object> params) {
        if (params.isEmpty()){
            return template;
        }
        List<String> paramsFromTemplate = new ArrayList<>();
        String[] splitStart = template.split("\\$\\{");
        for (int i = 1; i < splitStart.length; i++) {
            String[] splitEnd = splitStart[i].split("\\}");
            paramsFromTemplate.add(splitEnd[0]);
        }

        for (String paramFromTemplate : paramsFromTemplate) {
            String[] split = paramFromTemplate.split("\\.");
            //if param is one word ---> looking at map
            if (split.length == 1) {
                Object paramValue = params.get(paramFromTemplate);
                String forReplace = "${" + paramFromTemplate + "}";
                String replace = paramValue.toString();
                template = template.replace(forReplace, replace);

            } else {
                //if > 1 (contains dot): split ---> first one - object(looking in map), other ---> getters
                Object objectFromMap = getObject(params.get(split[0]), split);
                String forReplace = "${" + paramFromTemplate + "}";
                String replace = objectFromMap.toString();
                template = template.replace(forReplace, replace);
            }
        }
        return template;
    }

    //in case if getter returns the object and another getter will call
    private Object getObject(Object object, String[] split) {
        Object objectFromMap = object;
        for (int i = 1; i < split.length; i++) {
            objectFromMap = ObjectInvoker.proceedObject(objectFromMap, split[i]);
        }
        return objectFromMap;
    }
}
