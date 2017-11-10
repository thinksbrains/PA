package com.devilucky.placeapp.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Hakos on 01/08/2016.
 */
public class JSONParser {
    static InputStream is=null;
    static JSONObject jobj=null;
    static String json="";
    public JSONParser(){

    }
    @SuppressLint("LongLogTag")
    public static JSONObject makeHttpRequest(String url, String method, ArrayList<NameValuePair> params){
        try{
            if(method=="POST"){
                DefaultHttpClient httpClient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpresponse=httpClient.execute(httppost);
                HttpEntity httpentity=httpresponse.getEntity();
                is=httpentity.getContent();
            }else if(method=="GET"){
                DefaultHttpClient httpclient=new DefaultHttpClient();
                String paramString= URLEncodedUtils.format(params, "utf-8");
                url+="?"+paramString;
                HttpGet httpGet=new HttpGet(url);
                HttpResponse httpResponse=httpclient.execute(httpGet);
                HttpEntity httpEntity=httpResponse.getEntity();
                is=httpEntity.getContent();
            }
        }catch(UnsupportedEncodingException e){
            Log.e("UnsupportedEncodingException", "Unsoported:" + e.toString());
            e.printStackTrace();
        }catch(ClientProtocolException e){
            Log.e("ClientProtocolException", "clientException::" + e.toString());
            e.printStackTrace();
        }catch(IOException e){
            Log.e("IOException", "ioException:::" + e.toString());
            e.printStackTrace();
        }

        try{
            BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=reader.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            json=sb.toString();
        }catch(Exception e){
            Log.e("buffer Error", "error conversion" + e.toString());
        }

        try{
            jobj=new JSONObject(json);
            Log.e("json-return",json.toString());
        }catch(JSONException e){
            Log.e("json-return-error",json.toString());
            Log.e("Error Parser", "error Parse data"+e.toString());
        }
        return jobj;
    }
}
