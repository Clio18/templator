package com.obolonyk.templator.processor;

import com.obolonyk.templator.reflection.ReflectionHelper;

import java.util.*;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

@Slf4j
public class ForEachTemplateProcessor implements TemplateProcessor {
    private static final Pattern VALUE_PREFIX = Pattern.compile("\\$\\{");
    private static final Pattern VALUE_SUFFIX = Pattern.compile("\\}");
    private static final Pattern VALUE_DOT = Pattern.compile("\\.");
    private static final Pattern VALUE_CLOSE_TAG = Pattern.compile(">");
    private static final Pattern VALUE_BLANK = Pattern.compile(" ");
    private static final Pattern VALUE_FOR_EACH = Pattern.compile("#foreach");

    @Override
    public String process(String template, Map<String, Object> params) {
        log.trace("Starting ForEachTemplateProcessor processing");
        if (params.isEmpty()) {
            log.trace("ForEachTemplateProcessor finished processing: params value was empty");
            return template;
        }
        List<String> forEachItemsPieces = getForEachItems(template);
        for (String forEachItem : forEachItemsPieces) {

            Map<String, String> mapWithParamsAndRepeatedItems = getMapWithParamsAndRepeatedItems(forEachItem);
            Set<String> keySet = mapWithParamsAndRepeatedItems.keySet();

            for (String key : keySet) {
                String repeatedPart = mapWithParamsAndRepeatedItems.get(key);
                //here I am sure that it will be the List
                List<Object> objects = (List<Object>) params.get(key);
                StringBuilder stringBuilder = new StringBuilder();

                //fill the template by repeated forEach pieces
                for (Object object : objects) {
                    String filledTemplatePart = fillTemplate(object, repeatedPart);
                    stringBuilder.append(filledTemplatePart);
                }

                String forReplace = "<#foreach" + forEachItem + "#foreach>";
                template = template.replace(forReplace, stringBuilder.toString());
            }
        }
        log.trace("ForEachTemplateProcessor finished processing: template was updated");
        return template;
    }

    private String fillTemplate(Object object, String template) {
        log.trace("ForEachTemplateProcessor: filling template by repeated pieces");
        List<String> paramsFromTemplate = new ArrayList<>();
        String[] splitStart = VALUE_PREFIX.split(template);
        for (int i = 1; i < splitStart.length; i++) {
            String[] splitEnd = VALUE_SUFFIX.split(splitStart[i]);
            paramsFromTemplate.add(splitEnd[0]);
        }

        for (String paramFromTemplate : paramsFromTemplate) {
            String[] split = VALUE_DOT.split(paramFromTemplate);
            String forReplace = "${" + paramFromTemplate + "}";
            Object replace;
            if (split.length == 1) {
                replace = object;
            } else {
                replace = getObject(object, split);
            }
            String replaceString = StringEscapeUtils.escapeHtml4(replace.toString());
            template = template.replace(forReplace, replaceString);
        }
        log.trace("ForEachTemplateProcessor: template was filled");
        return template;
    }

    private Object getObject(Object object, String[] split) {
        Object objectFromMap = object;
        for (int i = 1; i < split.length; i++) {
            objectFromMap = ReflectionHelper.getValueFromGetter(objectFromMap, split[i]);
        }
        return objectFromMap;
    }

    private Map<String, String> getMapWithParamsAndRepeatedItems(String forEachItem) {
        Map<String, String> mapOfRepeatedPartAndParamName = new HashMap<>();
        String[] tags = VALUE_CLOSE_TAG.split(forEachItem);
        String info = tags[0];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < tags.length - 1; i++) {
            stringBuilder.append(tags[i]).append(">");
        }
        String repeatedPart = stringBuilder.toString();
        String[] splitInfo = VALUE_BLANK.split(info);
        String paramsName = splitInfo[splitInfo.length - 1];
        mapOfRepeatedPartAndParamName.put(paramsName, repeatedPart);
        return mapOfRepeatedPartAndParamName;
    }

    private List<String> getForEachItems(String template) {
        String[] split = VALUE_FOR_EACH.split(template);
        List<String> forEachItems = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (i % 2 != 0) {
                forEachItems.add(split[i]);
            }
        }
        return forEachItems;
    }
}
