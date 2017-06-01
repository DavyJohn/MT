package com.zzh.mt.http.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;
import com.zzh.mt.mode.UserData;

import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/6/1.
 */

public abstract class InfoCallback extends Callback<UserData> {
    @Override
    public UserData parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        UserData user = new Gson().fromJson(string, UserData.class);
        return user;
    }
}
