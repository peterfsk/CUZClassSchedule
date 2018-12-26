package com.asus.zhechuankebiao.data.http;

import com.google.gson.Gson;
import com.asus.zhechuankebiao.utils.LogUtil;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by lalala on 18-12-24.
 */

public abstract class JsonCallback<T> extends Callback<T> {

    private Class<T> clazz;
    private Gson gson;

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
        gson = new Gson();
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        try {
            String jsonString = response.body().string();
            LogUtil.e(this,jsonString);
            return gson.fromJson(jsonString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}