package com.shehabsalah.locationlib;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ShehabSalah on 8/23/17.
 *
 */

public abstract class UploadImageThread extends AsyncTask<String,Integer,String> {
    @Override
    protected String doInBackground(String... params) {
        String sourceFileUri = params[0];
        String currentDate = params[1];
        return upload(sourceFileUri, currentDate);

    }

    @Override
    protected abstract void onPreExecute();

    @Override
    protected abstract void onProgressUpdate(Integer... values);

    @Override
    protected abstract void onPostExecute(String s);


    private String upload(String imagePath, String imageName){
        final String url= "http://www.platformhouse.com/ireport/upload_video.php";
        File file = new File(imagePath);
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = null;
        //  String fileName = file.getName();

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            String boundary = "---------------------------boundary";
            String tail = "\r\n--" + boundary + "--\r\n";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            String metadataPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                    + "" + "\r\n";

            String fileHeader1 = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"file\"; filename=\""
                    + imageName + ".jpg\"" + "\"\r\n"
                    + "Content-Type: application/octet-stream\r\n"
                    + "Content-Transfer-Encoding: binary\r\n";

            long fileLength = file.length() + tail.length();
            String fileHeader2 = "Content-length: " + fileLength + "\r\n";
            String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
            String stringData = metadataPart + fileHeader;

            long requestLength = stringData.length() + fileLength;
            connection.setRequestProperty("Content-length", "" + requestLength);
            connection.setFixedLengthStreamingMode((int) requestLength);
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(stringData);
            out.flush();

            int progress = 0;
            int bytesRead;
            byte buf[] = new byte[1024];
            BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file));
            while ((bytesRead = bufInput.read(buf)) != -1) {
                // write output
                out.write(buf, 0, bytesRead);
                out.flush();
                progress += bytesRead;
                // update progress bar
                publishProgress((int) ((progress * 100) / (file.length())));
                //  publishProgress(progress);
            }

            // Write closing boundary and close stream
            out.writeBytes(tail);
            out.flush();
            out.close();
            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {

            }
        } catch (Exception e) {
            // Exception
        } finally {
            if (connection != null) connection.disconnect();
        }
        return "http://www.platformhouse.com/ireport/upload/"+imageName+".jpg";

    }
}
