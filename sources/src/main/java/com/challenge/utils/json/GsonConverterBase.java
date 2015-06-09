package com.challenge.utils.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


abstract class GsonConverterBase<T> implements GsonConverter<T> {
    @Override
    final public Gson createGson(boolean prettyPrintFormat){
        Gson gson = null;
        if (prettyPrintFormat)
            gson = new GsonBuilder().serializeSpecialFloatingPointValues()
                    .disableHtmlEscaping()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                    .setPrettyPrinting().create();
        else
            gson = new GsonBuilder().create();

        return gson;
    }
}
