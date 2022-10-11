package com.obolonyk.templator.processor;

import com.obolonyk.templator.reflection.ReflectionHelper;

import java.util.*;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

@Slf4j
public class ValueTemplateProcessor implements TemplateProcessor {
    private static final Pattern VALUE_PREFIX = Pattern.compile("\\$\\{");
    private static final Pattern VALUE_SUFFIX = Pattern.compile("\\}");
    private static final Pattern VALUE_DOT = Pattern.compile("\\.");

    @Override
    public String process(String template, Map<String, Object> params) {
        log.trace("Starting ValueTemplateProcessor processing");
        if (params.isEmpty()) {
            log.trace("ValueTemplateProcessor finished processing: params value was empty");
            return template;
        }
        List<String> paramsFromTemplate = new ArrayList<>();
        String[] splitStart = VALUE_PREFIX.split(template);
        for (int i = 1; i < splitStart.length; i++) {
            String[] splitEnd = VALUE_SUFFIX.split(splitStart[i]);
            paramsFromTemplate.add(splitEnd[0]);
        }

        for (String paramFromTemplate : paramsFromTemplate) {
            String[] split = VALUE_DOT.split(paramFromTemplate);

            String forReplace = "${" + paramFromTemplate + "}";
            Object value;

            //if param is one word ---> looking at map
            if (split.length == 1) {
                value = params.get(paramFromTemplate);
            } else {
                //if > 1 (contains dot): split ---> first one - object(looking in map), other ---> getters
                value = processGetterChain(params.get(split[0]), split);
            }

            String replace = StringEscapeUtils.escapeHtml4(value.toString());
            template = template.replace(forReplace, replace);
        }
        log.trace("ValueTemplateProcessor finished processing: template was updated");
        return template;
    }

    //in case if getter returns the object and another getter will call
    Object processGetterChain(Object object, String[] fieldsName) {
        log.trace("ValueTemplateProcessor: processing with getters chain");
        Object currentValue = object;
        //avoid first field name, because it is already our object
        for (int i = 1; i < fieldsName.length; i++) {
            currentValue = ReflectionHelper.getValueFromGetter(currentValue, fieldsName[i]);
        }
        return currentValue;
    }
}
