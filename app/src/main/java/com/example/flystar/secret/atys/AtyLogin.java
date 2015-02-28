package com.example.flystar.secret.atys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.R;
import com.example.flystar.secret.net.GetCode;
import com.example.flystar.secret.net.Login;
import com.example.flystar.secret.tools.LogMessage;
import com.example.flystar.secret.tools.MD5Tool;

public class AtyLogin extends Activity
{
    private EditText etPhone = null;
    private EditText etCode;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = (EditText) findViewById(R.id.etPhoneNum);
        etCode = (EditText) findViewById(R.id.etCode);

        findViewById(R.id.btnGetCode).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(etPhone.getText()))
                {
                    Toast.makeText(AtyLogin.this,"电话号码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i("FLY","正在连接");


                final ProgressDialog pd = ProgressDialog.show(AtyLogin.this,"正在连接","正在连接服务器");
                Log.i("FLY","正在连接");

                new GetCode(etPhone.getText().toString(),new GetCode.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        Log.i("FLY",etPhone.getText().toString());
                        Toast.makeText(AtyLogin.this,"获取验证码成功",Toast.LENGTH_SHORT).show();


                    }
                },new GetCode.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();
                        Toast.makeText(AtyLogin.this,"获取验证码失败",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });


        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPhone.getText()))
                {
                    Toast.makeText(AtyLogin.this,"电话号码不能为空",Toast.LENGTH_SHORT).show();

                    return;
                }


                if(TextUtils.isEmpty(etCode.getText()))
                {
                    Toast.makeText(AtyLogin.this,"验证码不能为空",Toast.LENGTH_SHORT).show();

                    return;
                }
                final ProgressDialog pd = ProgressDialog.show(AtyLogin.this,"正在连接","正在连接服务器");

                new Login(MD5Tool.md5(etPhone.getText().toString()),etCode.getText().toString(), new Login.LoginSuccessCallback() {
                    @Override
                    public void onLoginSuccess(String token) {
                        pd.dismiss();
                        //登录成功，缓存token
                        Config.cacheToken(AtyLogin.this,token);
                        //缓存phone
                        LogMessage.LogPrint("etponenum******"+etPhone.getText().toString());
                        Config.cachePhoneNum(AtyLogin.this,etPhone.getText().toString());


                        Intent intent = new Intent(AtyLogin.this,AtyTimeline.class);
                        intent.putExtra(Config.KEY_TOKEN,token);
                        intent.putExtra(Config.KEY_PHONE_NUM,etPhone.getText().toString());
                        startActivity(intent);

                        finish();

                    }
                }, new Login.LoginFailCallback() {

                    @Override
                    public void onLoginFail() {
                        pd.dismiss();
                        Toast.makeText(AtyLogin.this,"登录失败，请稍候再试",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }
}
