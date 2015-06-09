package com.challenge.utils.json;

import com.google.gson.Gson;


interface GsonConverter<T> {

	/**
	 * Get Object From JSON
	 * @param theJson
	 * @return
	 */
	public abstract T getObjectFromJson(String theJson);

	/**
	 * Get Object To JSON
	 * @param theObject
	 * @return
	 */
	public abstract String getObjectToJson(T theObject, boolean prettyPrintFormat);

	public abstract Gson createGson(boolean prettyPrintFormat);

}