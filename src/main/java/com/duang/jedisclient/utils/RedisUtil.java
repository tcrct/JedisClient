package com.duang.jedisclient.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.duang.jedisclient.core.Redis;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisUtil {

    private static SerializeConfig jsonConfig = new SerializeConfig();
    public static SerializerFeature[] serializerFeatureArray = {
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.WriteNonStringKeyAsString,
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.NotWriteRootClassName,
            SerializerFeature.WriteDateUseDateFormat
    };

    private static SerializerFeature[] serializerFeatureArray2 = {
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteNonStringKeyAsString,
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.NotWriteRootClassName
    };

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj, jsonConfig, serializerFeatureArray);
    }

    public static void log(Logger logger, String message) {
        System.out.println(message);
//        logger.warn(message);
    }

    /***
     * 判断传入的对象是否为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,为空或等于0时返回true
     */
    public static boolean isEmpty(Object obj) {
        return checkObjectIsEmpty(obj, true);
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
    }

    @SuppressWarnings("rawtypes")
    private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
        if (null == obj) {
            return bool;
        }
        else if (obj == "" || "".equals(obj)) {
            return bool;
        }
        else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            try {
                Double.parseDouble(obj + "");
            } catch (Exception e) {
                return bool;
            }
        } else if (obj instanceof String) {
            if (((String) obj).length() <= 0) {
                return bool;
            }
            if ("null".equalsIgnoreCase(obj+"")) {
                return bool;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0) {
                return bool;
            }
        }
        return !bool;
    }

    private static Set<String> buildExcludedMethodName() {
        Set<String> excludedMethodName = new HashSet<String>();
        Method[] methods = Object.class.getDeclaredMethods();
        for (Method m : methods) {
            excludedMethodName.add(m.getName());
        }
        return excludedMethodName;
    }
    public static void main(String[] args) {
        Method[] methods = Redis.class.getDeclaredMethods();
        Set<String> excludedMethodName = buildExcludedMethodName();
        StringBuilder methodCodeStr = new StringBuilder();
        for (Method method : methods) {
            // 过滤掉Object.class里的公用方法及静态方法
            if (excludedMethodName.contains(method.getName()) || Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            Type type = method.getGenericReturnType();
            String returnName = type.getTypeName();
            returnName = returnName.replace("java.lang.", "").replace("java.util.", "");
            if (returnName.contains("<T>")) {
                returnName = "<T> " + returnName;
            }
            methodCodeStr.append(returnName).append(" ").append(method.getName()).append("(");
            Parameter[] parameters = method.getParameters();
            StringBuilder parameterStr = new StringBuilder();
            String parameterString = "";
            for (int i=0; i<parameters.length; i++) {
                Parameter p = parameters[i];
                String typeName = p.getType().getSimpleName();
                String paramName = p.getName();

                if ("CacheKeyModel".equalsIgnoreCase(typeName)) {
                    paramName = "mode";
                }
                if ("Class".equalsIgnoreCase(typeName)) {
                    typeName = "Class<T>";
                    paramName = "type";
                }
                parameterStr.append("final ").append(typeName);
                parameterStr.append(" ").append(paramName).append(", ");
            }
            if (parameterStr.length()>2) {
                parameterString = parameterStr.substring(0, parameterStr.length() - 2);
            }

            methodCodeStr.append(parameterString).append(");").append("\n");
//            Class<?>[] parametersCls = method.getParameterTypes();
//            for (int i=0; i<parameters.length; i++) {
//                Class<?> parameter = parametersCls[i];
//                parameter.getName()
//
//            }
        }
        System.out.println(methodCodeStr);
    }
}
