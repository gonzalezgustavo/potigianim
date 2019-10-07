package com.example.potigianhh.utils.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestService {
    private static RequestService instance;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestService(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestService getInstance(Context context) {
        if (instance == null) {
            instance = new RequestService(context);
        }

        return instance;
    }

    public <T> void executeAsync(Request<T> request) {
        getRequestQueue().add(request);
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }
}
