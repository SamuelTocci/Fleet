package com.example.fleet;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpHandler{
    private JSONArray responseData;

    public HttpHandler(){
    }

    public JSONArray getResponse(){
        return responseData;
    }

    public void httpRequest(String url, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                responseData = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(submitRequest);
    }
}
