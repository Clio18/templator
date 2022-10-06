package com.obolonyk.templator.reflection;

import lombok.SneakyThrows;


public class ObjectInvoker {

    @SneakyThrows
    public static Object proceedObject(Object objectFromMap, String getName) {
        Class<?> clazz = objectFromMap.getClass();
        String getter = getGetterName(getName);
        return clazz.getMethod(getter).invoke(objectFromMap);
    }
    private static String getGetterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
