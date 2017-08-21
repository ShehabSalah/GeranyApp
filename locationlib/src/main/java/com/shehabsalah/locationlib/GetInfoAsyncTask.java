package com.shehabsalah.locationlib;

import android.os.AsyncTask;
import org.json.JSONObject;

/**
 * Created by Shehab Salah on 8/20/17.
 * This class responsible on retrieving information's from the server side
 */
public abstract class GetInfoAsyncTask extends AsyncTask<GetInfo,Long,JSONObject> {
    @Override
    protected JSONObject doInBackground(GetInfo... params) {
        return params[0].getInfo();
    }

    @Override
    protected abstract void onPreExecute();

    @Override
    protected abstract void onPostExecute(JSONObject jsonObject);

    @Override
    protected abstract void onProgressUpdate(Long... values);
}
