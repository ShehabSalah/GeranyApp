package com.shehabsalah.locationlib;

import android.net.Uri;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shehab Salah on 8/20/17.
 * This Class responsible on build the url's with it's parameters and values
 */
public class UriBuilder {
    // The url scheme : http:// or https://
    private String SCHEME;
    // The main path that contain the apis pages
    private String BASE_URL;
    // The page that the application will communicate with
    private String PHP_PAGE_URL;
    // List of all url parameters
    private ArrayList<String> PARAMS = new ArrayList<>();
    // List of all url values
    private ArrayList<String> VALUES = new ArrayList<>();
    // List of parameters and values: using only in POST requests
    Map<String, String> map;
    /**
     * UriBuilder initialize GET variables
     * @param SCHEME        the url scheme as string.
     * @param BASE_URL      the main path that contain the apis pages.
     * @param PHP_PAGE_URL  the page that the application will communicate with.
     * */
    public UriBuilder(String SCHEME, String BASE_URL, String PHP_PAGE_URL) {
        this.SCHEME = SCHEME;
        this.BASE_URL = BASE_URL;
        this.PHP_PAGE_URL = PHP_PAGE_URL;
    }
    /**
     * UriBuilder initialize POST variable with new object of HashMap.
     * */
    public UriBuilder(){
        map = new HashMap<>();
    }
    /**
     * setParam responsible on add sequence of the url params as strings to PARAMS ArrayList.
     * @param params sequence of the url params.
     * */
    public void setParam(String... params){
        if (params.length > 0){
            Collections.addAll(PARAMS, params);
        }
    }
    /**
     * setValues responsible on add sequence of the url values as strings to VALUES ArrayList
     * @param values sequence of the url values
     * */
    public void setValues(String... values){
        if (values.length > 0){
            Collections.addAll(VALUES, values);
        }
    }
    /**
     * getURL responsible on building the uri's using the PARAMS ArrayList and VALUES ArrayList with
     * the SCHEME, BASE_URL and PHP_PAGE_URL to build the URL that the application will communicate
     * with, with its parameters and values.
     * @return URL: the final url that the application will communicate with
     * */
    public URL getURL(){
        try {
            if (SCHEME != null && BASE_URL !=null && PHP_PAGE_URL != null){
                Uri uri;
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(SCHEME)
                        .encodedAuthority(BASE_URL)
                        .appendEncodedPath(PHP_PAGE_URL);
                if (PARAMS.size() == VALUES.size()){
                    for (int i = 0; i < PARAMS.size(); i++) {
                        builder.appendQueryParameter(PARAMS.get(i), VALUES.get(i));
                    }
                }
                uri = builder.build();
                return new URL(uri.toString());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * getMAP responsible on converting the PARAMS and VALUES to HashMap and return this HashMap.
     * @return Map\<String, String>: the final HashMap that contain the parameters and values that
     * will be send to the backend with POST Request
     * */
    public Map<String, String> getMAP(){
        if (PARAMS.size() == VALUES.size() && map != null){
            for (int i = 0; i < PARAMS.size(); i++) {
                map.put(PARAMS.get(i), VALUES.get(i));
            }
            return map;
        }
        return null;
    }
}
