package com.example.flystar.secret.atys;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.R;
import com.example.flystar.secret.net.Publish;

public class AtyPubMessage extends Activity
{
    private EditText etMsg;
    private Button btPublishMsg;

    private String phone_md5;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_message);

        phone_md5 = getIntent().getStringExtra(Config.KEY_PHONE_MD5);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);

        etMsg = (EditText) findViewById(R.id.etMsg);

        btPublishMsg = (Button) findViewById(R.id.btPublishMsg);

        btPublishMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Publish(phone_md5,token, etMsg.getText().toString(),new Publish.PublishSuccess()
                {
                    @Override
                    public void onPubSuccess() {

                        setResult(Config.ACTIVIY_RESULT_NEED_REFRESH);

                        Toast.makeText(AtyPubMessage.this,"发布秘密成功",Toast.LENGTH_LONG).show();
                        finish();


                    }
                },new Publish.PublishFail() {
                    @Override
                    public void onPubFail(int errorCode)
                    {
                        if(errorCode == Config.RESULT_STATUS_INVALID_TOKEN)
                        {
                            startActivity(new Intent(AtyPubMessage.this,AtyLogin.class));
                        }
                        else
                        {
                            Toast.makeText(AtyPubMessage.this,"发布秘密失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


}
