package com.challenge.utils.json;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Collections of useful methods for convert from Object to json String and vice versa
 */
public class JSONUtils {


    public static final <T extends Serializable> String convertToJsonString(T thePojo, Class clazz) {

        return JSONUtils.getGsonConverter(clazz).getObjectToJson(thePojo, false);
    }


    public static <T extends Serializable> T convertJsonStringToObject(String theJson, final Class<T> clazz)
    {
        return JSONUtils.getGsonConverter(clazz).getObjectFromJson(theJson);
    }

    private static <T extends Serializable> GsonConverterBase<T> getGsonConverter(final Class<T> clazz) {

        return new  GsonConverterBase<T>() {
            @Override
            public T getObjectFromJson(String theJson) {
                Gson gson = createGson(false);
                return gson.fromJson(theJson, clazz);
            }

            @Override
            public String getObjectToJson(T theObject, boolean prettyPrintFormat) {
                Gson gson = createGson(prettyPrintFormat);
                String json =  gson.toJson(theObject, clazz);
                return json;
            }
        };

    }

}


