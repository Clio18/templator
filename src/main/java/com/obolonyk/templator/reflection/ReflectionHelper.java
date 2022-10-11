package com.obolonyk.templator.reflection;

import lombok.SneakyThrows;


public class ReflectionHelper {

    @SneakyThrows
    public static Object getValueFromGetter(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        String getterName = getGetterName(fieldName);
        return clazz.getMethod(getterName).invoke(object);
    }

    private static String getGetterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
