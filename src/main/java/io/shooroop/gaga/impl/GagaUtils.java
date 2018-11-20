package io.shooroop.gaga.impl;

import java.lang.reflect.Constructor;

import static org.springframework.util.ReflectionUtils.makeAccessible;

public abstract class GagaUtils {
    public static boolean isPrimitiveType(Class<?> type) {
        return (type.isPrimitive() && type != void.class) ||
                type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }

    public static Class<?> getClassByName(String className) {
        if ("boolean".equals(className)) return boolean.class;
        if ("byte".equals(className)) return byte.class;
        if ("char".equals(className)) return char.class;
        if ("short".equals(className)) return short.class;
        if ("int".equals(className)) return int.class;
        if ("long".equals(className)) return long.class;
        if ("float".equals(className)) return float.class;
        if ("double".equals(className)) return double.class;
        try {
            return Class.forName(skipGenerics(className));
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String skipGenerics(String className) {
        int indexOfGeneric = className.indexOf("<");
        if (indexOfGeneric > -1) {
            return className.substring(0, indexOfGeneric);
        } else {
            return className;
        }
    }

    public static String getGenericClass(String className) {
        int indexOfGeneric = className.indexOf("<");
        if (indexOfGeneric > -1) {
            return className.substring(indexOfGeneric + 1, className.length() - 1);
        } else {
            return null;
        }
    }

    public static <T> T instantiate(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Constructor<T>[] ctors = (Constructor<T>[]) clazz.getDeclaredConstructors();
        Constructor<T> ctor = null;
        for (Constructor<T> constructor : ctors) {
            ctor = constructor;
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        if (ctor == null) {
            return null;
        }
        makeAccessible(ctor);
        try {
            return ctor.newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

}
