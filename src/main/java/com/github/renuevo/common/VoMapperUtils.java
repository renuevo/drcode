package com.github.renuevo.common;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * @className : VoMapperUtils
 * @author : Deokhwa.Kim
 * @since : 2018-08-03
 * @version : 1.2
 * @update : 2018-11-28
 * @description : 상속 메소드 기능 추가
 * </pre>
 */
public class VoMapperUtils {

    public static <T> Map<String, Method> getFieldMehtods(Class<T> classType, String methodType) {
        return getFieldMehtods(classType, methodType, "default");
    }

    /**
     * <pre>
     *  @methodName : getFieldMehtods
     *  @author : Deokhwa.Kim
     *  @since : 2019-09-26 오후 1:59
     *  @summary : create key method map
     *  @param : [classType, methodType, type]
     *  @return : java.util.Map<java.lang.String,java.lang.reflect.Method>
     * </pre>
     */
    public static <T> Map<String, Method> getFieldMehtods(Class<T> classType, String methodType, String keyType) {
        List<String> typeList = Arrays.asList("lower", "upper", "underUpper", "upperUnder"); //key type check
        Map<String, Method> methodsMap = new ConcurrentHashMap<>();
        Method[] methods = classType.getDeclaredMethods();
        Set<String> keySet = new HashSet<>();
        String methodName;
        String key;
        try {
            for (Method method : methods) {
                keySet.clear();
                if (method.getName().startsWith(methodType)) {
                    methodName = method.getName();
                    //create method key
                    if (keyType.equals("all")) {
                        for (String type : typeList) {
                            key = getKey(methodName.substring(methodType.length()), type);
                            keySet.add(key);
                        }
                    } else {
                        keySet.add(getKey(methodName.substring(methodType.length()), keyType));
                    }

                    //key duplication check
                    for(String mapKey : keySet){
                        if (!methodsMap.containsKey(mapKey)) methodsMap.put(mapKey, method);
                        else throw new Exception(mapKey + " this key duplication!");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodsMap;
    }

    /**
     * <pre>
     *  @methodName : getKey
     *  @author : Deokhwa.Kim
     *  @since : 2019-09-26 오후 1:59
     *  @summary : create key
     *  @param : [key, keyType]
     *  @return : java.lang.String
     * </pre>
     */
    private static String getKey(String key, String keyType) {
        int index;
        switch (keyType) {
            case "lower":
                key = key.toLowerCase();
                break;
            case "upper":
                key = key.toUpperCase();
                break;
            case "underUpper":
                //setCompany_name_str -> companyNameStr
                key = key.substring(0, 1).toLowerCase() + key.substring(1);
                if (key.substring(key.length() - 1).equals("_"))
                    key = key.substring(0, key.length() - 1);

                while ((index = key.indexOf("_")) != -1) {
                    key = key.substring(0, index) + key.substring(index + 1, index + 2).toUpperCase() + key.substring(index + 2);
                }
                break;
            case "upperUnder":
                //setCompanyNameStr -> company_name_str
                key = key.substring(0, 1).toLowerCase() + key.substring(1);
                while ((index = regIndexOf(key, "[A-Z]")) != -1) {
                    key = key.substring(0, index) + "_" + key.substring(index, index + 1).toLowerCase() + key.substring(index + 1);
                }
                break;
            default:
                key = key.substring(0, 1).toLowerCase() + key.substring(1);
                break;
        }
        return key;
    }

    /**
     * <pre>
     *  @methodName : getMethods
     *  @author : Deokhwa.Kim
     *  @update : 2018-11-28 오후 1:33
     *  @since : 2018-08-03 오후 2:07
     *  @param classType, methodType
     *  @return java.util.Map<java.lang.String, java.lang.reflect.Method>
     * </pre>
     */
    public <T> Map<String, Method> getMethods(Class<T> classType, String methodType) {
        return createMethodMap(classType.getDeclaredMethods(), methodType);
    }

    /**
     * <pre>
     *  @methodName : createMethodMap
     *  @author : Deokhwa.Kim
     *  @since : 2018-11-28 오후 1:33
     *  @summary : field에대한 method Map 생성
     *  @param : methods, methodType
     *  @return : java.util.Map<java.lang.String,java.lang.reflect.Method>
     * </pre>
     */
    private Map<String, Method> createMethodMap(Method[] methods, String methodType) {

        Map<String, Method> methodsMap = new ConcurrentHashMap<>();
        Set<String> methodsSet = new HashSet<>();
        String key = null;
        int index = 0;

        try {
            for (Method method : methods) {
                methodsSet.clear();
                if (method.getName().startsWith(methodType)) {

                    key = method.getName().substring(methodType.length()).toLowerCase();

                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);
                    methodsSet.add(key);

                    key = method.getName().substring(methodType.length()).toUpperCase();
                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);
                    methodsSet.add(key);

                    key = method.getName().substring(methodType.length(), methodType.length() + 1).toLowerCase() + method.getName().substring(methodType.length() + 1);
                    if (key.substring(key.length() - 1).equals("_"))
                        key = key.substring(0, key.length() - 1);

                    //setCompany_name_str -> companyNameStr
                    while ((index = key.indexOf("_")) != -1) {
                        key = key.substring(0, index) + key.substring(index + 1, index + 2).toUpperCase() + key.substring(index + 2);
                    }
                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);
                    methodsSet.add(key);

                    //setCompanyNameStr -> company_name_str
                    key = method.getName().substring(methodType.length(), methodType.length() + 1).toLowerCase() + method.getName().substring(methodType.length() + 1);
                    while ((index = regIndexOf(key, "[A-Z]")) != -1) {
                        key = key.substring(0, index) + "_" + key.substring(index, index + 1).toLowerCase() + key.substring(index + 1);
                    }
                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);
                    methodsSet.add(key);


                    //setStringIdx -> stringIdx
                    key = method.getName().substring(methodType.length(), methodType.length() + 1).toLowerCase() + method.getName().substring(4);
                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodsMap;
    }

    /**
     * <pre>
     *  @methodName : getMethodsSuper
     *  @author : Deokhwa.Kim
     *  @since : 2018-11-28 오후 1:30
     *  @param classType, methodType
     *  @return java.util.Map<java.lang.String, java.lang.reflect.Method>
     * </pre>
     */
    public <T> Map<String, Method> getMethodsSuper(Class<T> classType, String methodType) {
        List<Method> methodList = new ArrayList<>(Arrays.asList(classType.getDeclaredMethods()));
        methodList.addAll(Arrays.asList(classType.getSuperclass().getDeclaredMethods()));
        return createMethodMap(methodList.toArray(new Method[0]), methodType);
    }

    private boolean inputChk(Set<String> methodsKeySet, Set<String> keySet, String key) throws Exception {
        if (methodsKeySet.contains(key) && !keySet.contains(key)) {
            throw new Exception(key + " this key is ambiguous");
        }
        return false;
    }

    private static int regIndexOf(String key, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(key);
        if (matcher.find())
            return matcher.start();
        return -1;
    }

}