package com.example.potigianhh.utils.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.potigianhh.exceptions.ServerException;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonListRequest<T> extends Request<List<T>> {
    private final Gson gson;
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<List<T>> listener;
    private final Object body;

    public GsonListRequest(int method, String url, Class<T> clazz, Object body,
                           Map<String, String> headers, Response.Listener<List<T>> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.body = body;
        this.headers = headers;
        this.listener = listener;

        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonDateDeserializer())
                .create();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (body != null) {
            return gson.toJson(body).getBytes();
        }

        return super.getBody();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>(this.headers != null ? this.headers : super.getHeaders());
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    protected void deliverResponse(List<T> response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<List<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Type responseType = new TypeToken<ResponseObject<List<T>>>() {}
                    .where(new TypeParameter<T>() {}, clazz).getType();
            ResponseObject<List<T>> responseObject = gson.fromJson(json, responseType);

            if (responseObject.isSuccess()) {
                return Response.success(responseObject.getObject(), HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.error(new ParseError(new ServerException(responseObject.getErrorInfo())));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}