package com.example.potigianhh.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.potigianhh.MainActivity;
import com.example.potigianhh.R;
import com.example.potigianhh.model.Preparer;
import com.example.potigianhh.utils.Constants;
import com.example.potigianhh.utils.requests.GsonListRequest;
import com.example.potigianhh.utils.requests.GsonRequest;
import com.example.potigianhh.utils.requests.RequestService;
import com.google.common.reflect.TypeToken;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public String getFragmentName() {
        return this.getClass().getSimpleName();
    }

    private void informVolleyError(VolleyError error) {
        String errInfo = error.getMessage();
        int status = error.networkResponse != null ? error.networkResponse.statusCode : -1;
        String data = error.networkResponse != null ? new String(error.networkResponse.data) : "None";

        displayErrorDialog("Error en solicitud",
                "Mensaje: " + errInfo + "\n" +
                        "Status: " + status + "\n" +
                        "Body: " + data);
    }

    void displayErrorDialog(String title, String message) {
        new AlertDialog.Builder(this.getContext())
                .setIcon(R.drawable.ic_error_icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialog, which) -> {})
                .show();
    }

    MainActivity getMainActivity() {
        return (MainActivity) this.getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.clearFocus();
    }

    public void onBarcode(String content) {}

    <T> void doRequest(int method, String url, Class<T> clazz,
                       Object body, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<>(method, url, clazz,
                body, null, listener, error -> {
                    if (errorListener != null)
                        errorListener.onErrorResponse(error);
                    informVolleyError(error);
                });
        RequestService.getInstance(this.getContext()).executeAsync(request);
    }

    <T> void doListRequest(int method, String url, Class<T> clazz,
                           Object body, Response.Listener<List<T>> listener,
                           Response.ErrorListener errorListener) {
        GsonListRequest<T> request = new GsonListRequest<>(method, url, clazz,
                body, null, listener, error -> {
                    if (errorListener != null)
                        errorListener.onErrorResponse(error);
                    informVolleyError(error);
                });
        RequestService.getInstance(this.getContext()).executeAsync(request);
    }

    Preparer getPreparer() {
        return getMainActivity().get(Constants.PREPARER_KEY,
                Preparer.class,
                new TypeToken<Preparer>(){}.getType());
    }

    void displayToast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
