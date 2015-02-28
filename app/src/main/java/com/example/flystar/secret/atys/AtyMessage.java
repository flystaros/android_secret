package com.example.flystar.secret.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flystar.secret.Config;
import com.example.flystar.secret.R;
import com.example.flystar.secret.bean.Comment;
import com.example.flystar.secret.net.GetComment;
import com.example.flystar.secret.net.PubComment;
import com.example.flystar.secret.tools.LogMessage;
import com.example.flystar.secret.tools.MD5Tool;

import java.util.List;

public class AtyMessage extends ListActivity
{
    private String phone_md5;
    private String msg;
    private String msgId;
    private String token;

    private TextView tvMessage;
    private AtyMessageCommentListAdapter adapter;
    private EditText etComment;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        etComment = (EditText) findViewById(R.id.etComment);

        Intent data = getIntent();

        phone_md5 = data.getStringExtra(Config.KEY_PHONE_MD5);
        msg = data.getStringExtra(Config.KEY_MSG);
        msgId = data.getStringExtra(Config.KEY_MSG_ID);
        token = data.getStringExtra(Config.KEY_TOKEN);

        tvMessage.setText(msg);

        adapter = new AtyMessageCommentListAdapter(this);
        setListAdapter(adapter);

        LogMessage.LogPrint("get comment ++++++++++++++");
        getComments();

        findViewById(R.id.btnSendComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etComment.getText()))
                {
                    Toast.makeText(AtyMessage.this,"comment can not be empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final ProgressDialog pd = ProgressDialog.show(AtyMessage.this,"正在连接","正在连接。。。。。。");
                    new PubComment(MD5Tool.md5(Config.getCachedPhoneNum(AtyMessage.this)),token,etComment.getText().toString(),msgId,new PubComment.PubCommentSucess() {
                        @Override
                        public void onPubCommentSucess()
                        {
                            pd.dismiss();
                            etComment.setText("");
                            getComments();
                        }
                    },new PubComment.PubCommentFail() {
                        @Override
                        public void onPubCommentFail(int errorCode) {
                            pd.dismiss();
                            if(errorCode == Config.RESULT_STATUS_INVALID_TOKEN)
                            {
                                startActivity(new Intent(AtyMessage.this,AtyLogin.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(AtyMessage.this,"fail to pub comment",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



            }
        });
    }

    private void getComments() {
        final ProgressDialog pd = ProgressDialog.show(AtyMessage.this,"正在连接","正在连接。。。。。。");
        new GetComment(phone_md5,token,msgId,1,20,new GetComment.GetCommentSuccess() {
            @Override
            public void onGetCommentSuccess(int page, int perpage, String msgid, List<Comment> comments)
            {
                pd.dismiss();
                LogMessage.LogPrint("---------------" + comments);
                adapter.clear();
                adapter.addAll(comments);

            }
        },new GetComment.GetCommnetFail() {
            @Override
            public void onGetCommentFail(int errorCode)
            {
                pd.dismiss();
                if(errorCode == Config.RESULT_STATUS_INVALID_TOKEN)
                {
                    startActivity(new Intent(AtyMessage.this,AtyLogin.class));
                    finish();
                }
                else
                {
                    Toast.makeText(AtyMessage.this, "fail to get comments", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
