package com.example.flystar.secret.net;

import com.example.flystar.secret.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flystar on 2015/2/28.
 * publish message
 */
public class Publish
{

    public Publish(String phone_md5,String token,String msg, final PublishSuccess publishSuccess, final PublishFail publishFail)
    {
        new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    switch (jsonObject.getInt(Config.KEY_STATUS))
                    {
                        case Config.RESULT_STATUS_SUCCESS:
                            if(publishSuccess != null)
                            {
                                publishSuccess.onPubSuccess();
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if(publishFail != null)
                            {
                                publishFail.onPubFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if(publishFail != null)
                            {
                                publishFail.onPubFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(publishFail != null)
                    {
                        publishFail.onPubFail(Config.RESULT_STATUS_FAIL);
                    }
                }

            }
        },new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if(publishFail != null)
                {
                    publishFail.onPubFail(Config.RESULT_STATUS_FAIL);
                }

            }
        },Config.KEY_ACTION,Config.KEY_PUBLISH,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_TOKEN,token,Config.KEY_MSG,msg);
    }

    public static interface PublishSuccess
    {
        void onPubSuccess();
    }
    public static interface PublishFail
    {
        void onPubFail(int errorCode);
    }
}
