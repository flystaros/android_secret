package com.example.flystar.secret.net;

import com.example.flystar.secret.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flystar on 2015/2/27.
 */
public class UploadContacts
{
    public UploadContacts(String phone_md5,String token,String contacts, final UploadContactSuccessCallback uploadContactSuccessCallback,
                          final UploadContactFailCallback uploadContactFailCallback)
    {

        new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    switch (jsonObject.getInt(Config.KEY_STATUS))
                    {
                        case Config.RESULT_STATUS_SUCCESS:
                            if(uploadContactSuccessCallback != null)
                            {
                                uploadContactSuccessCallback.onUploadContactSuccess();
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if(uploadContactFailCallback != null)
                            {
                                uploadContactFailCallback.onUploadContactFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if(uploadContactFailCallback != null)
                            {
                                uploadContactFailCallback.onUploadContactFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    if(uploadContactFailCallback != null)
                    {
                        uploadContactFailCallback.onUploadContactFail(Config.RESULT_STATUS_FAIL);
                    }
                }


            }
        },new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if(uploadContactFailCallback != null) {
                    uploadContactFailCallback.onUploadContactFail(Config.RESULT_STATUS_FAIL);
                }

            }
        },Config.KEY_ACTION,Config.ACTION_UPLOAD_CONTACTS,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_TOKEN,token,Config.KEY_CONTACTS,contacts);
    }

    public static interface UploadContactSuccessCallback
    {
        void onUploadContactSuccess();
    }

    public  static interface UploadContactFailCallback
    {
        void onUploadContactFail(int errorCode);
    }


}
