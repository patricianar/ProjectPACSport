package com.example.projectpacsport;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyService {

    Context context;
    RequestQueue requestQueue;

    VolleyService(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void executeRequest(String url, final VolleyCallback callback) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (callback != null)
                        callback.getResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley: ", error.getMessage());
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    String credentials = "5d63830e-ad82-459a-a7d4-6d1298:MYSPORTSFEEDS";
                    String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    params.put("Authorization", auth);
                    return params;
                }
            };
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            Log.e("Volley: ", e.getMessage());
        }
    }

    public interface VolleyCallback {
        void getResponse(String response);
    }
}
