package com.example.potigianim.fragments;

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
import com.example.potigianim.R;
import com.example.potigianim.model.User;
import com.example.potigianim.utils.Constants;

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
        final EditText userEditText = view.findViewById(R.id.login_usr_field);
        final EditText passEditText = view.findViewById(R.id.login_pwd_field);

        loginButton.setOnClickListener(v -> {
            String url = Constants.LOGIN_URL
                    .replace("{user}", userEditText.getText())
                    .replace("{accessCode}", passEditText.getText());
            loginButton.setEnabled(false);
            doRequest(Request.Method.GET, url, User.class, null,
                    LoginFragment.this::displaySearchFragment,
                    LoginFragment.this::onLoginRequestFailed);
        });

        userEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.loginDisabled));
                } else {
                    if (passEditText.length() > 0) {
                        loginButton.setEnabled(true);
                        loginButton.setBackgroundTintList(ContextCompat
                                .getColorStateList(getContext(), R.color.loginEnabled));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        passEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.loginDisabled));
                } else {
                    if (userEditText.length() > 0) {
                        loginButton.setEnabled(true);
                        loginButton.setBackgroundTintList(ContextCompat
                                .getColorStateList(getContext(), R.color.loginEnabled));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void logout() {
        User user = getUser();

        if (user != null) {
            new AlertDialog.Builder(this.getContext())
                    .setIcon(R.drawable.ic_info_icon)
                    .setTitle("Cierre de Sesión")
                    .setMessage("¿Está seguro que desea salir?")
                    .setPositiveButton("Si", (dialog, which) -> {
                        getMainActivity().remove(Constants.USER_KEY);
                        getMainActivity().replaceFragment(LoginFragment.class);

                        displayToast("Sesión cerrada");
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                    })
                    .show();
        }
    }

    private void onLoginSuccessful(User user) {
        setLoginButtonEnabled(true);
        getMainActivity().set(Constants.USER_KEY, user);
        getMainActivity().replaceFragment(PurchaseOrderSearchFragment.class);
        displayToast("Bienvenido " + user.getName());
    }

    private void displaySearchFragment(final User user) {
        if (user.getCode().equals("-1")) {
            displayErrorDialog("Error", "Usuario/código de acceso inválidos.");
            setLoginButtonEnabled(true);
        }
        else {
            onLoginSuccessful(user);
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
