package com.example.flystar.secret.net;

import android.util.Log;

import com.example.flystar.secret.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flystar on 2015/2/27.
 * 获取验证码
 */
public class GetCode {
    public GetCode(String phone, final SuccessCallback successCallback, final FailCallback failCallback)
    {
        new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    //把返回的json字符串，包装成json对象
                    JSONObject jsonObject = new JSONObject(result);
                    switch (jsonObject.getInt(Config.KEY_STATUS))
                    {
                        case Config.RESULT_STATUS_SUCCESS:
                            if(successCallback != null)
                            {
                                successCallback.onSuccess();
                                Log.i("FLY",result);
                                Log.i("FLY",String.valueOf(jsonObject.getInt(Config.KEY_STATUS)));
                            }
                            break;
                        default:
                            if (failCallback != null)
                            {
                                failCallback.onFail();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(failCallback != null)
                    {
                        failCallback.onFail();
                    }
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        },Config.KEY_ACTION,Config.ACTION_GET_CODE,Config.KEY_PHONE_NUM,phone);
    }


    public static interface SuccessCallback
    {
        void onSuccess();
    }

    public static interface  FailCallback
    {
        void onFail();
    }
}
