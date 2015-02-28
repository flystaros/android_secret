package com.example.flystar.secret.net;

import android.os.AsyncTask;
import android.util.Log;

import com.example.flystar.secret.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by flystar on 2015/2/26.
 */
public class NetConnection
{
    //网络连接 请求地址， 请求方式，请求参数
    public NetConnection(final String url, final HttpMethod method, final SuccessCallBack successCallBack, final FailCallback failCallback, final String ... kvs)
    {
        new AsyncTask<Void,Void,String>()
        {
            @Override
            protected String doInBackground(Void... params) {

                StringBuffer stringBuffer = new StringBuffer();
                for (int i=0; i < kvs.length; i+=2)
                {
                    stringBuffer.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
                }

                try {
                    URLConnection urlConnection;
                    switch (method)
                    {
                        case POST:
                            urlConnection = new URL(url).openConnection();
                            urlConnection.setDoOutput(true); //如果是post方式，则需要设置setDoOutput为true
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), Config.CHARSET));
                            bw.write(stringBuffer.toString());// 把参数post到服务器
                            bw.flush();
                            Log.i("FLY","request url:-----"+urlConnection.getURL());
                            Log.i("FLY","request data:-----"+stringBuffer.toString());
                            break;
                        default:
                            urlConnection = new URL(url+"?"+stringBuffer.toString()).openConnection();  //get方式提交的数据
                            break;
                    }
                    System.out.print("request url: "+urlConnection.getURL());
                    System.out.print("request data: "+stringBuffer.toString());
                    //处理返回的结果
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),Config.CHARSET));
                    String line = null;
                    StringBuffer result = new StringBuffer();

                    while((line = br.readLine()) != null)
                    {
                        result.append(line);

                    }
                    System.out.println("Result:"+result);
                    return result.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result)
            {
                if(result != null)
                {
                    if(successCallBack != null)
                    {
                        Log.i("ffllyy","request----------"+result.toString());
                        successCallBack.onSuccess(result);
                    }
                }
                else
                {
                    if(failCallback != null)
                    {
                        failCallback.onFail();
                    }
                }
                super.onPostExecute(result);
            }
        }.execute();
    }

    public static interface SuccessCallBack
    {
        void onSuccess(String result);
    }

    public static interface  FailCallback
    {
        void onFail();
    }
}
