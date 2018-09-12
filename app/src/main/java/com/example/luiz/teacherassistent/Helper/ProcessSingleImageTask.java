package com.example.luiz.teacherassistent.Helper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Helper.api.SingleProcessRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ProcessSingleImageTask extends AsyncTask<File, Object, DetectionResult> {

    private static final String TAG = ProcessSingleImageTask.class.getSimpleName();
    @Override
    protected DetectionResult doInBackground(File... params) {
        if (params.length > 0) {
            File imageFile = params[0];
            byte[] arraysBytes;
            if (imageFile != null) {
                try {
                    if (imageFile.exists()) {
                        FileInputStream fis = new FileInputStream(imageFile);
                        arraysBytes = new byte[(int) imageFile.length()];
                        fis.read(arraysBytes);
                    } else {
                        arraysBytes = null;
                    }

                    if (arraysBytes != null) {
                        OkHttpClient client = new OkHttpClient();
                        MediaType mediaType = MediaType.parse("application/json");
                        SingleProcessRequest singleProcessRequest = new SingleProcessRequest(arraysBytes);
                        MediaType JSON = MediaType.parse("application/json");
                        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(singleProcessRequest));
                        Request request = new Request.Builder()
                                .url("https://api.mathpix.com/v3/latex")
                                .addHeader("content-type", "application/json")
                                .addHeader("app_id", "luizfrancisco2000_gmail_com")
                                .addHeader("app_key", "1ad72749ebbc4a8041d2")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        DetectionResult detectionResult = new Gson().fromJson(response.body().string(),DetectionResult.class);
                        //String teste = new  Gson().fromJson(response.body().string(),String.class);

                        if (!response.isSuccessful()) {
                            Log.d(TAG, "Deu errado");
                            Log.d(TAG,response.message());
                        } else {
                            Log.d(TAG, "Deu certo");
                        }
                        if (response == null) {
                            return null;
                        } else {
                            ResponseBody responseBody = response.body();
                            if (responseBody == null) {
                                return null;
                            }
                            return detectionResult;
                        }
                    }else{
                        return null;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(DetectionResult s) {
        super.onPostExecute(s);
        //Log response string
        Log.e(TAG, s.latex);
    }
}
