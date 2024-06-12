package com.example.svhtcmobile.Api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    final static int REQUEST_CODE = 1232;
    private static String BASE_URL = "http://10.252.2.141:8080/api/";

    private static Gson gson = new GsonBuilder().create();
    public static Retrofit getClient(String authToken) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + authToken)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public static String getBaseUrl(){
        return BASE_URL;
    }

    public static void askPermissions(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    public static void convertXMLToPDF( @NonNull Activity activity,View view, String fileName) {


        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.getDisplay().getRealMetrics(displayMetrics);
        } else
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        PdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
        }


        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();


        PdfDocument.Page page = document.startPage(pageInfo);


        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.WHITE);


        view.draw(canvas);


        document.finishPage(page);


        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File filePath = new File(downloadsDir, fileName);

        try {

            FileOutputStream fos = new FileOutputStream(filePath);
            document.writeTo(fos);
            document.close();
            fos.close();

            Toast.makeText(activity, "XML to PDF Conversion Successful", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
