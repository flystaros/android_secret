package com.example.flystar.secret.net;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.bean.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flystar on 2015/2/27.
 */
public class TimeLine
{
    public TimeLine(String phone_md5,String token,int page,int perpage, final TimeLineSucess timeLineSucess, final TimeLineFail timeLineFail)
    {
        new NetConnection(Config.SERVER_URL,HttpMethod.POST,new NetConnection.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    switch (jsonObject.getInt(Config.KEY_STATUS))
                    {
                        case Config.RESULT_STATUS_SUCCESS:
                            if(timeLineSucess != null)
                            {
                                List<Message> msgs = new ArrayList<Message>();
                                JSONArray msgJsonArray = jsonObject.getJSONArray(Config.KEY_TIMELINE);
                                JSONObject msgObj;
                                for(int i=0;i<msgJsonArray.length();i++)
                                {
                                    msgObj = msgJsonArray.getJSONObject(i);
                                    msgs.add(new Message(
                                            msgObj.getString(Config.KEY_MSG_ID),
                                            msgObj.getString(Config.KEY_MSG),
                                            msgObj.getString(Config.KEY_PHONE_MD5)
                                            ));
                                }

                                timeLineSucess.onTimeLineSuccess(jsonObject.getInt(Config.KEY_PAGE),jsonObject.getInt(Config.KEY_PERPAGE),msgs);
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if(timeLineFail != null)
                            {
                                timeLineFail.onTimeLineFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if(timeLineFail != null)
                            {
                                timeLineFail.onTimeLineFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(timeLineFail != null)
                    {
                        timeLineFail.onTimeLineFail(Config.RESULT_STATUS_FAIL);
                    }
                }


            }
        },new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if(timeLineFail != null)
                {
                    timeLineFail.onTimeLineFail(Config.RESULT_STATUS_FAIL);
                }
            }
        },Config.KEY_ACTION,Config.ACTION_TIMELINE,
                Config.KEY_PHONE_MD5,phone_md5,
                Config.KEY_TOKEN,token,
                Config.KEY_PAGE,page+"",
                Config.KEY_PERPAGE,perpage+""
        );
    }


    public static interface TimeLineSucess
    {
        void onTimeLineSuccess(int page,int perpage,List<Message> timeline);
    }

    public static interface TimeLineFail
    {
        void onTimeLineFail(int errorcode);
    }
}
