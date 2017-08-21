package com.shehabsalah.locationlib;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shehab Salah on 8/20/17.
 * This class responsible on connecting the application with POST web services and return Json File.
 */
public abstract class POST_Connector implements GetInfo {
    public static final String BASE_URL                 = "www.platformhouse.com";
    //map variables that contain post params and values
    Map<String, String> map;
    //Backend web page that the application will communicate with
    String web_page;
    //Application context
    Context context;

    /**
     * POST_Connector is a Constructor
     * @param context   the main application context
     * @param map       the Map that contain the parameters and values
     * @param web_page  the web page that the application will communicate with
     * */
    public POST_Connector(Context context,Map<String, String> map, String web_page) {
        this.context = context;
        this.map = map;
        this.web_page = web_page;
    }
    /**
     * implementing the getInfo method
     * @return JSONObject the backend response
     * */
    @Override
    public JSONObject getInfo() {
        // Building String request
        StringRequest request = new StringRequest(Request.Method.POST, "http://"+ BASE_URL + "/" + web_page,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //When server return the response, send the response to onResult
                            onResult(new JSONObject(response));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i(LOG_TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        request.setShouldCache(false);
        requestQueue.add(request);
        try {
            synchronized (this) {
                wait(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * onResult: This abstract method will be called automatic after the getInfo method execute and
     * return the response from the backend.
     * @param response: the backend response as JSONObject
     * */
    public abstract void onResult(JSONObject response);
}

