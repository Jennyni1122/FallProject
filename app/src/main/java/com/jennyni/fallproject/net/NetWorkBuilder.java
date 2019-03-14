package com.jennyni.fallproject.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 969 on 2019/3/14.
 */

public class NetWorkBuilder {


    public static void getOkHttp(String url, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

}
