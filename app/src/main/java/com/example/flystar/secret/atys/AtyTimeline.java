package com.example.flystar.secret.atys;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.R;
import com.example.flystar.secret.bean.Message;
import com.example.flystar.secret.ld.MyContacts;
import com.example.flystar.secret.net.TimeLine;
import com.example.flystar.secret.net.UploadContacts;
import com.example.flystar.secret.tools.MD5Tool;

import org.json.JSONArray;

import java.util.List;

public class AtyTimeline extends ListActivity
{
    private String phone_num;
    private String token;
    private String phone_md5;
    private Button btPublish;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        phone_num = getIntent().getStringExtra(Config.KEY_PHONE_NUM);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);
        phone_md5 = MD5Tool.md5(phone_num);

        btPublish = (Button) findViewById(R.id.btPublish);

        btPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AtyTimeline.this,AtyPubMessage.class);
                intent.putExtra(Config.KEY_PHONE_MD5,phone_md5);
                intent.putExtra(Config.KEY_TOKEN, token);
                startActivityForResult(intent,0);
            }
        });


        adapter = new AtyTimelineMessageListAdapter(this);
        setListAdapter(adapter);

        final ProgressDialog pd = ProgressDialog.show(AtyTimeline.this,"正在连接","正在连接服务器");

        new UploadContacts(phone_md5,token, MyContacts.getContactsJSONString(AtyTimeline.this),
                new UploadContacts.UploadContactSuccessCallback() {
            @Override
            public void onUploadContactSuccess() {
                pd.dismiss();
                loadMessage();
            }
        },new UploadContacts.UploadContactFailCallback() {
            @Override
            public void onUploadContactFail(int errorCode) {

                pd.dismiss();

                if(errorCode == Config.RESULT_STATUS_INVALID_TOKEN)
                {
                    startActivity(new Intent(AtyTimeline.this,AtyLogin.class));
                    finish();
                }
                else
                {
                    loadMessage();

                }

            }
        });
    }

    private AtyTimelineMessageListAdapter adapter = null;


    private void loadMessage()
    {
        Log.i("FLY","loadMessage>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        final ProgressDialog pd = ProgressDialog.show(AtyTimeline.this,"正在连接","正在连接服务器");
        //TimeLine(String phone_md5,String token,int page,int perpage, final TimeLineSucess timeLineSucess, final TimeLineFail timeLineFail)

        new TimeLine(phone_md5,token,1,20,new TimeLine.TimeLineSucess() {
            @Override
            public void onTimeLineSuccess(int page, int perpage, List<Message> timeline)
            {
                pd.dismiss();
                adapter.clear();
                adapter.addAll(timeline);
                Toast.makeText(AtyTimeline.this,"success to timeline",Toast.LENGTH_SHORT).show();

            }
        },new TimeLine.TimeLineFail()
        {
            @Override
            public void onTimeLineFail(int errorcode)
            {
                pd.dismiss();
                if (errorcode ==Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(AtyTimeline.this, AtyLogin.class));
                    finish();
                }else{
                    Toast.makeText(AtyTimeline.this,"fail to get timeline ",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        Message msg = adapter.getItem(position);

        Intent intent = new Intent(AtyTimeline.this,AtyMessage.class);
        intent.putExtra(Config.KEY_MSG,msg.getMsg());
        intent.putExtra(Config.KEY_MSG_ID,msg.getMsgId());
        intent.putExtra(Config.KEY_PHONE_MD5,msg.getPhone_md5());
        intent.putExtra(Config.KEY_TOKEN,token);
        startActivity(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Config.ACTIVIY_RESULT_NEED_REFRESH:
                loadMessage();
                break;
            default:
                break;

        }
    }
}
