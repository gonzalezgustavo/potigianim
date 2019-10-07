package com.example.potigianhh.fragments;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.potigianhh.R;
import com.example.potigianhh.model.Preparer;
import com.example.potigianhh.utils.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class LoginFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button loginButton = view.findViewById(R.id.login_button);
        final EditText loginEditText = view.findViewById(R.id.login_id_field);

        loginButton.setOnClickListener(v -> {
            String url = Constants.LOGIN_URL
                    .replace("{preparerId}", loginEditText.getText());
            loginButton.setEnabled(false);
            doRequest(Request.Method.GET, url, Preparer.class, null,
                    LoginFragment.this::displayPreparerFragment,
                    LoginFragment.this::onLoginRequestFailed);
        });

        loginEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.loginDisabled));
                } else {
                    loginButton.setEnabled(true);
                    loginButton.setBackgroundTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.loginEnabled));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void logout() {
        Preparer preparer = getPreparer();

        if (preparer != null) {
            new AlertDialog.Builder(this.getContext())
                    .setIcon(R.drawable.ic_info_icon)
                    .setTitle("Cierre de Sesión")
                    .setMessage("¿Está seguro que desea salir?")
                    .setPositiveButton("Si", (dialog, which) -> {
                        getMainActivity().remove(Constants.PREPARER_KEY);
                        getMainActivity().replaceFragment(LoginFragment.class);

                        displayToast("Sesión cerrada");
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                    })
                    .show();
        }
    }

    private void onLoginSuccessful(Preparer preparer) {
        setLoginButtonEnabled(true);
        getMainActivity().set(Constants.PREPARER_KEY, preparer);
        getMainActivity().replaceFragment(RequestHeadFragment.class);
        displayToast("Bienvenido " + preparer.getName());
    }

    private void displayPreparerFragment(final Preparer preparer) {


        if (preparer.getId() == -1) {
            displayErrorDialog("Error", "No se encontró un preparador con ese identificador.");
            setLoginButtonEnabled(true);
        }
        else {
            String alertMessage = "¿Usted es " + preparer.getName() + "?";

            new AlertDialog.Builder(this.getContext())
                    .setIcon(R.drawable.ic_info_icon)
                    .setTitle("Confirmación")
                    .setMessage(alertMessage)
                    .setPositiveButton("Si", (dialog, which) -> onLoginSuccessful(preparer))
                    .setNegativeButton("No", (dialog, which) -> {})
                    .show();
        }
    }

    private void setLoginButtonEnabled(boolean value) {
        Button loginButton = getView().findViewById(R.id.login_button);
        loginButton.setEnabled(value);
    }

    private void onLoginRequestFailed(VolleyError error) {
        setLoginButtonEnabled(true);
    }
}
