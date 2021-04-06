package com.jennyni.fallproject.net;

import com.jennyni.fallproject.utils.JsonParse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 969 on 2019/3/14.
 */

public class NetWorkBuilder {
    private static NetWorkBuilder instance;
    private static OkHttpClient okHttpClient;
    private NetWorkBuilder() {
    }

    public static NetWorkBuilder getInstance() {
        synchronized (NetWorkBuilder.class) {
            if (instance == null) {
                instance = new NetWorkBuilder();
            }
        }
        return instance;
    }
    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

    public static void getOkHttp(String url, Callback callback) {
        OkHttpClient okHttpClient = getOkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

}
