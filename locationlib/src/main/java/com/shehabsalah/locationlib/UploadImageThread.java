package com.shehabsalah.locationlib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

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
       if (file.length() > 2000000){
           Log.e("FILE_SIZE", file.length()+"");
           file = getReducedImage(file);
       }
        Log.e("FILE_SIZE", file.length()+"");
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
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
        return "http://www.platformhouse.com/ireport/upload/"+imageName+".jpg";

    }

    public File getReducedImage(File mediaFile){
        Bitmap b = decodeFileWithRotationIfNecessary(mediaFile);
        return getfileFromBitmap(b, mediaFile.getPath());
    }

    private File getfileFromBitmap(Bitmap b, String path) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        Date d = new Date();
        CharSequence dateSeq = DateFormat.format("yyyy-MM-dd-hh-mm-ss", d.getTime());

        // you can create a new file name "test.jpg" in sdcard folder.
        File folder = new File("sdcard/DCIM/Gerany");
        if(!folder.exists()){
            folder.mkdir();
        }
        File f = new File(folder, dateSeq+".jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            // remember close de FileOutput
            fo.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // write the bytes in file
    }



    private Bitmap decodeFileWithRotationIfNecessary(File f) {
        final int IMAGE_MAX_SIZE = 800;
        Bitmap b = null;
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);

            BitmapFactory.decodeStream(fis, null, o);

            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        Bitmap bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                b.getHeight(), getMatrix(f), true);

        return bMapRotate;
    }

    private Matrix getMatrix(File f) {
        Matrix mat = new Matrix();
        mat.postRotate(90);
        try {
            ExifInterface exif = new ExifInterface(f.getPath());

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(90);

                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    mat.postRotate(180);

                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    mat.postRotate(270);

                    break;
                default:
                    //  mat.postRotate(0);

                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mat;
    }
}
