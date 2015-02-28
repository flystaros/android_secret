package com.example.flystar.secret.ld;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.tools.LogMessage;
import com.example.flystar.secret.tools.MD5Tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flystar on 2015/2/26.
 */
public class MyContacts
{
    public static String getContactsJSONString(Context context)
    {
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        String phoneNum;
        JSONArray jsonArray = new JSONArray();
        while(cursor.moveToNext())
        {
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(phoneNum.charAt(0)=='+' && phoneNum.charAt(1)=='8' && phoneNum.charAt(2)=='6')
            {
                phoneNum = phoneNum.substring(3);
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Config.KEY_PHONE_MD5, MD5Tool.md5(phoneNum));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            LogMessage.LogPrint(phoneNum);
            System.out.println(phoneNum);
        }

        return jsonArray.toString();
    }
}
