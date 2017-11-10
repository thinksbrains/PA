package com.devilucky.placeapp.utils;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Hakos on 20/07/2017.
 */

public class Petitions {
    Context context;
    HashMap<String,String> params;
    String petition,method;
    public Petitions(Context context, HashMap<String,String> params, String petition,String method){
        this.context=context;
        this.params=params;
        this.petition=petition;
        this.method=method;
    }

    public void requestPetition(){
        RequestQueue requestQueue=Volley.newRequestQueue(context);
        JsonObjectRequest json=null;
        switch (method) {
            case "post":
                json = new JsonObjectRequest(
                        Request.Method.POST,
                        context.getResources().getString(R.string.enlace) + petition,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                break;
            case "get":
                json = new JsonObjectRequest(
                        Request.Method.GET,
                        context.getResources().getString(R.string.enlace) + petition,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                break;
            case "put":
                json = new JsonObjectRequest(
                        Request.Method.PUT,
                        context.getResources().getString(R.string.enlace) + petition,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                break;
            case "delete":
                json = new JsonObjectRequest(
                        Request.Method.DELETE,
                        context.getResources().getString(R.string.enlace) + petition,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                break;
        }
        if (json!=null)
            requestQueue.add(json);
    }
}
