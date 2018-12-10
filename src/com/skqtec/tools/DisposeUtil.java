package com.skqtec.tools;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class DisposeUtil {


    public static JSONObject dispose(Object object) {
        Field[] field = object.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        JSONObject jsonObject = new JSONObject();
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String nameOrigon = field[j].getName(); // 获取属性的名字
                String name = nameOrigon.substring(0, 1).toUpperCase() + nameOrigon.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = object.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(object); // 调用getter方法获取属性值
                    jsonObject.put(nameOrigon,value);
                    if(value==null){
                        m = object.getClass().getMethod("set" + name, String.class);
                        m.invoke(object, value);
                    }
                }
                if (type.equals("int")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(object);
                    if(value!=null){
                        double doubleValue = value.intValue();
                        jsonObject.put(nameOrigon,doubleValue);
                    }
                    if (value == null) {
                        m = object.getClass().getMethod("set" + name, Integer.class);
                        m.invoke(object, 1);
                    }
                }
                if (type.equals("double")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Double value = (Double) m.invoke(object);
                    if(value!=null){
                        double doubleValue = value.doubleValue();
                        jsonObject.put(nameOrigon,doubleValue);
                    }

                    if (value == null) {
                        m = object.getClass().getMethod("set" + name, Integer.class);
                        m.invoke(object, 1);
                    }
                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(object);
                    jsonObject.put(nameOrigon,value);
                    if (value == null) {
                        m = object.getClass().getMethod("set" + name, Integer.class);
                        m.invoke(object, 1);
                    }
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Boolean value = (Boolean) m.invoke(object);
                    jsonObject.put(nameOrigon,value);
                    if (value == null) {
                        m = object.getClass().getMethod("set" + name, Boolean.class);
                        m.invoke(object, false);
                    }
                }
                if (type.equals("class java.util.Date")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Date value = (Date) m.invoke(object);
                    jsonObject.put(nameOrigon,value);
                    if (value == null) {
                        m = object.getClass().getMethod("set" + name, Date.class);
                        m.invoke(object, new Date());
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}

