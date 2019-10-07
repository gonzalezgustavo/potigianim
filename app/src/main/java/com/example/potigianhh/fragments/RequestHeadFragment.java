package com.example.potigianhh.fragments;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.potigianhh.R;
import com.example.potigianhh.fragments.adapters.RequestHeadersAdapter;
import com.example.potigianhh.fragments.decorators.RequestMarginDecorator;
import com.example.potigianhh.model.RequestHeader;
import com.example.potigianhh.utils.Constants;
import com.google.common.reflect.TypeToken;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RequestHeadFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_base, container, false);
    }

    @Override
    public void onDestroyView() {
        final RecyclerView recyclerView = getView().findViewById(R.id.requesthead_list);

        if (recyclerView.getAdapter() != null) {
            // We cleanup the activity reference
            ((RequestHeadersAdapter) recyclerView.getAdapter()).clearMainActivityReference();
        }

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView userText = view.findViewById(R.id.requesthead_user_text);

        final Button assignCigaretteRequestButton = view.findViewById(R.id.requesthead_button_cigarettes_equests);
        final Button assignAnyRequestButton = view.findViewById(R.id.requesthead_button_others_requests);

        final RecyclerView recyclerView = view.findViewById(R.id.requesthead_list);

        getMainActivity().remove(Constants.CURRENT_REQUEST_KEY);
        List<RequestHeader> requests = getMainActivity().get(Constants.ASSIGNED_KEY.replace(
                    "{preparerId}",
                    Integer.toString(getPreparer().getId())),
                List.class,
                new TypeToken<List<RequestHeader>>(){}.getType());

        recyclerView.setAdapter(new RequestHeadersAdapter(getMainActivity(), requests));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new RequestMarginDecorator( 5));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        assignCigaretteRequestButton.setOnClickListener(v -> {
            String url = Constants.REQUESTS_HEADERS_ASSIGNED_URL
                    .replace("{preparerId}", Integer.toString(getPreparer().getId()))
                    .replace("{onlyCigarettes}", "true");
            doListRequest(Request.Method.POST, url, RequestHeader.class, null,
                    RequestHeadFragment.this::onDataReceived, null);
        });

        assignAnyRequestButton.setOnClickListener(v -> {
            String url = Constants.REQUESTS_HEADERS_ASSIGNED_URL
                    .replace("{preparerId}", Integer.toString(getPreparer().getId()))
                    .replace("{onlyCigarettes}", "false");
            doListRequest(Request.Method.POST, url, RequestHeader.class, null,
                    RequestHeadFragment.this::onDataReceived, null);
        });

        verifyRecyclerViewState();

        userText.setText("Trabajando como " + getPreparer().getName());
    }

    private void onDataReceived(List<RequestHeader> requestHeaders) {
        if (requestHeaders.size() == 0) {
            new Handler(Looper.getMainLooper()).post(() -> {
                displayErrorDialog("Error en solicitud", "No se encontraron pedidos disponibles");
            });
            return;
        }

        final RecyclerView recyclerView = getView().findViewById(R.id.requesthead_list);
        recyclerView.setAdapter(new RequestHeadersAdapter(getMainActivity(), requestHeaders));

        verifyRecyclerViewState();

        getMainActivity().set(
                Constants.ASSIGNED_KEY
                        .replace("{preparerId}", Integer.toString(getPreparer().getId())),
                requestHeaders);
    }

    private void verifyRecyclerViewState() {
        final TextView emptyView = getView().findViewById(R.id.requesthead_empty_list);
        final RecyclerView recyclerView = getView().findViewById(R.id.requesthead_list);

        if (recyclerView.getAdapter().getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
