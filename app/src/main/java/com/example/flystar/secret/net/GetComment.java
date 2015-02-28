package com.example.flystar.secret.net;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.bean.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flystar on 2015/2/28.
 */
public class GetComment
{
    public GetComment(String phone_md5,String token, final String msgId, final int page, final int perpage, final GetCommentSuccess getCommentSuccess, final GetCommnetFail getCommnetFail)
    {
        new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    switch (jsonObject.getInt(Config.KEY_STATUS))
                    {
                        case Config.RESULT_STATUS_SUCCESS:

                            if(getCommentSuccess != null)
                            {
                                List<Comment> comments = new ArrayList<Comment>();
                                JSONArray commentsJsonArray = jsonObject.getJSONArray(Config.KEY_COMMENTS);
                                JSONObject commentObj;

                                for(int i = 0; i < commentsJsonArray.length(); i++)
                                {
                                    commentObj = commentsJsonArray.getJSONObject(i);
                                    comments.add(new Comment(commentObj.getString(Config.KEY_CONTENT),commentObj.getString(Config.KEY_PHONE_MD5)));
                                }

                                getCommentSuccess.onGetCommentSuccess(jsonObject.getInt(Config.KEY_PAGE),jsonObject.getInt(Config.KEY_PERPAGE),jsonObject.getString(Config.KEY_MSG_ID),comments);


                            }

                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if(getCommnetFail != null)
                            {
                                getCommnetFail.onGetCommentFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if(getCommnetFail != null)
                            {
                                getCommnetFail.onGetCommentFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(getCommnetFail != null)
                    {
                        getCommnetFail.onGetCommentFail(Config.RESULT_STATUS_FAIL);
                    }
                }

            }
        },new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if(getCommnetFail != null)
                {
                    getCommnetFail.onGetCommentFail(Config.RESULT_STATUS_FAIL);
                }

            }
        },Config.KEY_ACTION,Config.ACTION_GET_COMMENT,
                Config.KEY_TOKEN,token,
                Config.KEY_PHONE_MD5,phone_md5,
                Config.KEY_PAGE,page+"",
                Config.KEY_PERPAGE,perpage+"",
                Config.KEY_MSG_ID,msgId);
    }

    public static interface GetCommentSuccess
    {
        void onGetCommentSuccess(int page,int perpage,String msgid,List<Comment> comments);
    }

    public static interface GetCommnetFail
    {
        void onGetCommentFail(int errorCode);
    }
}
