package com.github.renuevo.es;

import java.lang.reflect.Field;
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
        Map<String, Method> methodsMap = new ConcurrentHashMap<>();
        Field[] fields = classType.getDeclaredFields();
        try {
            for (Field field : fields) {
                switch (methodType) {
                    case "set":
                        methodsMap.put(field.getName(), classType.getDeclaredMethod(methodType + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), String.class));
                        break;
                    case "get":
                        methodsMap.put(field.getName(), classType.getDeclaredMethod(methodType + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodsMap;
    }

    /**
     * <pre>
     *  @methodName : getSetMethods
     *  @author : Deokhwa.Kim
     *  @since : 2018-01-30 오후 4:02
     *  @param classType, type
     *  @description : data와 매핑될 vo의 set메소드 매핑
     *  @return java.util.Map<java.lang.String, java.lang.reflect.Method>
     * </pre>
     */
    public <T> Map<String, Method> getMethods(Class<T> classType, String methodType, String type) throws Exception {

        Map<String, Method> methodsMap = new ConcurrentHashMap<>();
        Method[] methods = classType.getDeclaredMethods();
        String key = null;
        int index = 0;

        for (Method method : methods) {
            if (method.getName().startsWith(methodType)) {
                switch (type) {
                    case "lower":
                        key = method.getName().substring(3).toLowerCase();
                        break;
                    case "upper":
                        key = method.getName().substring(3).toUpperCase();
                        break;
                    case "underUpper":
                        //setCompany_name_str -> companyNameStr
                        key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);

                        if (key.substring(key.length() - 1).equals("_"))
                            key = key.substring(0, key.length() - 1);

                        while ((index = key.indexOf("_")) != -1) {
                            key = key.substring(0, index) + key.substring(index + 1, index + 2).toUpperCase() + key.substring(index + 2);
                        }
                        break;
                    case "upperUnder":
                        //setCompanyNameStr -> company_name_str
                        key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                        while ((index = regIndexOf(key, "[A-Z]")) != -1) {
                            key = key.substring(0, index) + "_" + key.substring(index, index + 1).toLowerCase() + key.substring(index + 1);
                        }
                        break;
                    default:
                        //setStringIdx -> stringIdx
                        key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                        break;
                }

                if (!methodsMap.containsKey(key)) methodsMap.put(key, method);
                else throw new Exception(key + " this key duplication!");
            }
        }
        return methodsMap;
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

                    key = method.getName().substring(3).toLowerCase();

                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);
                    methodsSet.add(key);

                    key = method.getName().substring(3).toUpperCase();
                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);
                    methodsSet.add(key);

                    key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
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
                    key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    while ((index = regIndexOf(key, "[A-Z]")) != -1) {
                        key = key.substring(0, index) + "_" + key.substring(index, index + 1).toLowerCase() + key.substring(index + 1);
                    }
                    if (inputChk(methodsMap.keySet(), methodsSet, key))
                        throw new Exception();
                    methodsMap.put(key, method);
                    methodsSet.add(key);


                    //setStringIdx -> stringIdx
                    key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
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

    private boolean inputChk(Set<String> methodsKeySet, Set<String> keySet, String key) throws Exception{
        if (methodsKeySet.contains(key) && !keySet.contains(key)) {
            throw new Exception(key + " this key is ambiguous");
        }
        return false;
    }

    private int regIndexOf(String key, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(key);
        if (matcher.find())
            return matcher.start();
        return -1;
    }

}
