package com.example.flystar.secret.net;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.tools.LogMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flystar on 2015/2/28.
 */
public class PubComment
{
    public PubComment(String phone_md5,String token,String content,String msgId, final PubCommentSucess pubCommentSucess, final PubCommentFail pubCommentFail)
    {
       new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallBack() {
           @Override
           public void onSuccess(String result)
           {
               try {

                   LogMessage.LogPrint("result**********"+result.toString());
                   JSONObject jsonObject = new JSONObject(result);

                   switch (jsonObject.getInt(Config.KEY_STATUS))
                   {
                       case Config.RESULT_STATUS_SUCCESS:
                            if(pubCommentSucess != null)
                            {
                                pubCommentSucess.onPubCommentSucess();
                            }
                         break;
                       case Config.RESULT_STATUS_INVALID_TOKEN:
                           if(pubCommentFail != null)
                           {
                               pubCommentFail.onPubCommentFail(Config.RESULT_STATUS_INVALID_TOKEN);
                           }
                           break;
                       default:
                           if(pubCommentFail != null)
                           {
                               pubCommentFail.onPubCommentFail(Config.RESULT_STATUS_FAIL);
                           }
                           break;
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
                   if(pubCommentFail != null)
                   {
                       pubCommentFail.onPubCommentFail(Config.RESULT_STATUS_FAIL);
                   }
               }


           }
       },new NetConnection.FailCallback() {
           @Override
           public void onFail()
           {
                if(pubCommentFail != null)
                {
                    pubCommentFail.onPubCommentFail(Config.RESULT_STATUS_FAIL);
                }
           }
       },Config.KEY_ACTION,Config.KEY_PUB_COMMENT,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_TOKEN,token,Config.KEY_MSG,content,Config.KEY_MSG_ID,msgId);

    }

    public static interface PubCommentSucess
    {
        void onPubCommentSucess();
    }
    public static interface  PubCommentFail
    {
        void onPubCommentFail(int errorCode);
    }
}
