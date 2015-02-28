package com.example.flystar.secret;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.flystar.secret.atys.AtyLogin;
import com.example.flystar.secret.atys.AtyTimeline;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String token = Config.getCachedToken(this);  //token的值
        String phoneNum = Config.getCachedPhoneNum(this);
        if(token != null && phoneNum != null)
        {
            Intent intent = new Intent(this, AtyTimeline.class);
            intent.putExtra(Config.KEY_TOKEN,token);
            intent.putExtra(Config.KEY_PHONE_NUM,phoneNum);
            startActivity(intent);
        }
        else
        {
            startActivity(new Intent(this, AtyLogin.class));
        }

        //
        finish();
    }





}
