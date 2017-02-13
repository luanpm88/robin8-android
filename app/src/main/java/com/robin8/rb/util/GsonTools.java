package com.robin8.rb.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GsonTools {

    public GsonTools() {
    }

    /**
     * 只导出有标注的
     * @param list
     * @return
     */
    public static String listToJsonByAnnotation(List list){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation()
                .create();
        String s = gson.toJson(list);
        return s;
    }
    public static String listToJson(List list) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .create();
        String s = gson.toJson(list);
        return s;
    }

    public static String mapToJson(HashMap map) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .create();
        String s = gson.toJson(map);
        return s;
    }


        public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        try{
            T t = gson.fromJson(gsonString, cls);
            return t;
        }catch (Exception e){
            e.printStackTrace();
             return null;
        }
    }

    public static <T> List<T> jsonToList(String gsonString,
                                         Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(gsonString,
                new TypeToken<List<T>>() {
                }.getType());
        return list;
    }


    public static <T> List<Map<String, T>> jsonToListMaps(
            String gsonString) {
        List<Map<String, T>> list = null;
        Gson gson = new Gson();
        list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    public static <T> Map<String, T> jsonToMaps(String gsonString) {
        Map<String, T> map = null;
        Gson gson = new Gson();
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

    public static String beanToJson(Object object) {
        if (object == null)
            return "";
        Gson gson = new Gson();
        return gson.toJson(object);
    }

}
