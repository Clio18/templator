package com.obolonyk.templator.processor;

import com.obolonyk.templator.reflection.ObjectInvoker;

import java.util.*;

public class ForEachTemplateProcessor implements TemplateProcessor {
    @Override
    public String process(String template, Map<String, Object> params) {
        if (params.isEmpty()){
            return template;
        }
        List<String> forEachItems = getForEachItems(template);
        for (String forEachItem : forEachItems) {

            Map<String, String> mapWithParamsAndRepeatedItems = getMapWithParamsAndRepeatedItems(forEachItem);
            Set<String> keySet = mapWithParamsAndRepeatedItems.keySet();

            for (String key : keySet) {
                String repeatedPart = mapWithParamsAndRepeatedItems.get(key);
                //here I am sure that it will be the List
                List<Object> objects = (List<Object>) params.get(key);
                StringBuilder stringBuilder = new StringBuilder();
                for (Object object : objects) {
                    String filledTemplatePart = fillTemplate(object, repeatedPart);
                    stringBuilder.append(filledTemplatePart);
                }

                String forReplace = "<#foreach" + forEachItem + "#foreach>";
                template = template.replace(forReplace, stringBuilder.toString());
            }
        }
        return template;
    }

    private String fillTemplate(Object object, String template) {
        List<String> paramsFromTemplate = new ArrayList<>();
        String[] splitStart = template.split("\\$\\{");
        for (int i = 1; i < splitStart.length; i++) {
            String[] splitEnd = splitStart[i].split("\\}");
            paramsFromTemplate.add(splitEnd[0]);
        }

        for (String paramFromTemplate : paramsFromTemplate) {
            String[] split = paramFromTemplate.split("\\.");

            if (split.length == 1) {
                String forReplace = "${" + paramFromTemplate + "}";
                String replace = object.toString();
                template = template.replace(forReplace, replace);

            } else {
                Object newObj = getObject(object, split);
                String forReplace = "${" + paramFromTemplate + "}";
                String replace = newObj.toString();
                template = template.replace(forReplace, replace);
            }
        }
        return template;
    }

    private Object getObject(Object object, String[] split) {
        Object objectFromMap = object;
        for (int i = 1; i < split.length; i++) {
            objectFromMap = ObjectInvoker.proceedObject(objectFromMap, split[i]);
        }
        return objectFromMap;
    }

    private Map<String, String> getMapWithParamsAndRepeatedItems(String forEachItem) {
        Map<String, String> mapOfRepeatedPartAndParamName = new HashMap<>();
        String[] tags = forEachItem.split(">");
        String info = tags[0];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < tags.length - 1; i++) {
            stringBuilder.append(tags[i]).append(">");
        }
        String repeatedPart = stringBuilder.toString();
        String[] splitInfo = info.split(" ");
        String paramsName = splitInfo[splitInfo.length - 1];
        mapOfRepeatedPartAndParamName.put(paramsName, repeatedPart);
        return mapOfRepeatedPartAndParamName;
    }

    private List<String> getForEachItems(String template) {
        String[] split = template.split("#foreach");
        List<String> forEachItems = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (i % 2 != 0) {
                forEachItems.add(split[i]);
            }
        }
        return forEachItems;
    }
}
