package com.shehabsalah.locationlib;

import org.json.JSONObject;

/**
 * Created by Shehab Salah on 8/20/17.
 * Get info interface must implement in classes that will communicate with the backend
 */
public interface GetInfo {
    /**
     * getInfo Abstract method responsible on the communication with the backend
     * @return JSONObject: the backend response
     * */
    JSONObject getInfo();
}
