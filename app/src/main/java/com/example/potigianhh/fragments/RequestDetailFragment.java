package com.example.potigianhh.fragments;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.potigianhh.R;
import com.example.potigianhh.fragments.adapters.RequestDetailsAdapter;
import com.example.potigianhh.fragments.decorators.RequestMarginDecorator;
import com.example.potigianhh.model.RequestDetails;
import com.example.potigianhh.model.RequestHeader;
import com.example.potigianhh.utils.Constants;
import com.google.common.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RequestDetailFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_detail, container, false);
    }

    @Override
    public void onPause() {
        final RecyclerView recyclerView = getView().findViewById(R.id.requestdetail_product_list);
        RequestDetailsAdapter adapter = (RequestDetailsAdapter) recyclerView.getAdapter();
        RequestHeader requestHeader = getMainActivity().get(
                Constants.CURRENT_REQUEST_KEY,
                RequestHeader.class,
                new TypeToken<RequestHeader>(){}.getType());

        SparseArray<String> requestActuals = new SparseArray<>();
        if (adapter != null && adapter.getItemCount() > 0) {

            for (int i = 0; i < adapter.getItemCount(); i++) {
                RequestDetails details = adapter.getItemAt(i);
                String value = adapter.getValueAt(i);
                if ("".equals(value))
                    value = "0";
                requestActuals.put(details.getArticleCode(), value);
            }

            getMainActivity().set(
                    Constants.COUNT_REQUEST_KEY
                            .replace("{prefixDoc}", requestHeader.getDocumentPrefix().toString())
                            .replace("{document}", requestHeader.getDocumentCode().toString())
                            .replace("{suffixDoc}", requestHeader.getDocumentSuffix().toString()),
                    requestActuals);
        }

        super.onPause();
    }

    @Override
    public void onBarcode(String content) {
        final RecyclerView recyclerView = getView().findViewById(R.id.requestdetail_product_list);
        RequestDetailsAdapter adapter = (RequestDetailsAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                RequestDetails request = adapter.getItemAt(i);
                // To make sure it matches, we simply validate, not only the value, but also the
                // value removing one character
                if (request.getBarcodeCodes()
                        .stream()
                        .map(String::trim)
                        .filter(s -> s.length() > 0)
                        .anyMatch(s -> s.equals(content) || s.substring(0, s.length() - 1).equals(content)))
                {
                    String currentValue = adapter.getValueAt(i);
                    if ("".equals(currentValue))
                        currentValue = "0";
                    String newValue = Integer.toString(Integer.parseInt(currentValue) + 1);
                    adapter.setValueAt(i, newValue);

                    RequestDetailsAdapter.ViewHolder holder =
                            (RequestDetailsAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        new Handler(Looper.getMainLooper()).post(() -> holder.updateActualValue(newValue));
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        final RecyclerView recyclerView = getView().findViewById(R.id.requestdetail_product_list);
        RequestDetailsAdapter adapter = (RequestDetailsAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            adapter.clearMainActivityReference();
        }

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RequestHeader request = getMainActivity().get(
                Constants.CURRENT_REQUEST_KEY,
                RequestHeader.class,
                new TypeToken<RequestHeader>(){}.getType());

        Button backButton = view.findViewById(R.id.requestdetail_back_button);
        Button closeRequestButton = view.findViewById(R.id.requestdetail_close_request_button);
        final RecyclerView recyclerView = view.findViewById(R.id.requestdetail_product_list);

        String url = Constants.REQUEST_DETAILS_URL
                .replace("{prefixDoc}", request.getDocumentPrefix().toString())
                .replace("{document}", request.getDocumentCode().toString())
                .replace("{suffixDoc}", request.getDocumentSuffix().toString());
        doListRequest(Request.Method.GET, url, RequestDetails.class, null,
                RequestDetailFragment.this::onDataReceived, null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new RequestMarginDecorator( 5));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        addContentToTextViews(view, request);
        verifyRecyclerViewState();

        backButton.setOnClickListener(v -> getMainActivity().replaceFragment(RequestHeadFragment.class));
        closeRequestButton.setOnClickListener(v -> onCloseRequest());
    }

    private void onCloseRequest() {
        final RecyclerView recyclerView = getView().findViewById(R.id.requestdetail_product_list);
        RequestDetailsAdapter adapter = (RequestDetailsAdapter) recyclerView.getAdapter();

        switch (adapter.getRequestsStatus()) {
            case FINE:
                getMainActivity().replaceFragment(ValidateRequestFragment.class);
                break;
            case LESS:
                new AlertDialog.Builder(this.getContext())
                        .setIcon(R.drawable.ic_warn_icon)
                        .setTitle("Alerta del pedido")
                        .setMessage("Hay productos cuya cantidad es inferior a la esperada." +
                                " ¿Desea continuar? No podrá finalizar por completo el pedido")
                        .setPositiveButton("Continuar", (dialog, which) -> {
                            getMainActivity().replaceFragment(ValidateRequestFragment.class);
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {})
                        .show();
                break;
            case MORE:
                displayErrorDialog("Pedido inválido",
                        "Hay productos cuya cantidad es superior a la esperada.");
                break;
        }
    }

    private void onDataReceived(List<RequestDetails> requestDetails) {
        RequestHeader request = getMainActivity().get(
                Constants.CURRENT_REQUEST_KEY,
                RequestHeader.class,
                new TypeToken<RequestHeader>(){}.getType());

        SparseArray<String> storedValues = getMainActivity().get(
                Constants.COUNT_REQUEST_KEY
                        .replace("{prefixDoc}", request.getDocumentPrefix().toString())
                        .replace("{document}", request.getDocumentCode().toString())
                        .replace("{suffixDoc}", request.getDocumentSuffix().toString()),
                SparseArray.class,
                new TypeToken<SparseArray<String>>(){}.getType());

        RecyclerView recyclerView = getView().findViewById(R.id.requestdetail_product_list);
        recyclerView.setAdapter(new RequestDetailsAdapter(getMainActivity(), requestDetails, storedValues));

        verifyRecyclerViewState();
    }

    private void addContentToTextViews(View view, RequestHeader request) {
        String requestTextContent = "Pedido " +
                request.getDocumentPrefix() + "-" +
                request.getDocumentCode() + "-" +
                request.getDocumentSuffix() + " del " +
                new SimpleDateFormat("dd/MM/yyyy").format(request.getInsertDate());
        String clientTextContent = request.getClientCode() + " - " +
                request.getClientName().trim();

        TextView requestText = view.findViewById(R.id.requestdetail_request_text);
        TextView clientText = view.findViewById(R.id.requestdetail_request_client_text);
        TextView addressText = view.findViewById(R.id.requestdetail_request_address_text);
        TextView townText = view.findViewById(R.id.requestdetail_request_town_text);

        requestText.setText(requestTextContent);
        clientText.setText(clientTextContent);
        addressText.setText(request.getClientAddress().trim());
        townText.setText(request.getClientTown().trim());
    }

    private void verifyRecyclerViewState() {
        final TextView emptyView = getView().findViewById(R.id.requestdetail_product_list_empty_text);
        final RecyclerView recyclerView = getView().findViewById(R.id.requestdetail_product_list);

        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
