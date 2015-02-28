package com.example.flystar.secret.net;

import android.util.Log;

import com.example.flystar.secret.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flystar on 2015/2/27.
 * 登录实现
 */
public class Login
{


    public Login(String phone_md5, String code, final LoginSuccessCallback loginSuccessCallback, final LoginFailCallback loginFailCallback)
    {
        new NetConnection(Config.SERVER_URL,HttpMethod.POST, new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result)
            {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS))
                    {
                        case Config.RESULT_STATUS_SUCCESS:
                            if(loginSuccessCallback != null)
                            {
                                Log.i("FLY","返回的json"+result);
                                Log.i("FFFF","request---------+++++-"+result.toString());
                                Log.i("FLY","obj.getString(Config.KEY_TOKEN)"+obj.getString(Config.KEY_TOKEN));
                                loginSuccessCallback.onLoginSuccess(obj.getString(Config.KEY_TOKEN));

                            }
                            break;
                        default:
                            if(loginFailCallback != null)
                            {
                                loginFailCallback.onLoginFail();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(loginFailCallback != null)
                    {
                        loginFailCallback.onLoginFail();
                    }
                }
            }
        },new NetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        },Config.KEY_ACTION,"login",Config.KEY_PHONE_MD5,phone_md5,Config.KEY_CODE,code);
    }

    public static interface LoginSuccessCallback
    {
        void onLoginSuccess(String token);
    }

    public static interface LoginFailCallback
    {
        void onLoginFail();
    }

}
