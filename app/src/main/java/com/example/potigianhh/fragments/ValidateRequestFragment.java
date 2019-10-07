package com.example.potigianhh.fragments;

import android.os.Bundle;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.potigianhh.R;
import com.example.potigianhh.fragments.adapters.RequestValidationAdapter;
import com.example.potigianhh.fragments.decorators.RequestMarginDecorator;
import com.example.potigianhh.model.RequestDetails;
import com.example.potigianhh.model.RequestHeader;
import com.example.potigianhh.utils.Constants;
import com.google.common.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ValidateRequestFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validate_request, container, false);
    }

    @Override
    public void onDestroyView() {
        final RecyclerView recyclerView = getView().findViewById(R.id.validaterequest_product_list);
        RequestValidationAdapter adapter = (RequestValidationAdapter) recyclerView.getAdapter();

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

        Button backButton = view.findViewById(R.id.validaterequest_back_button);
        Button closeRequestButton = view.findViewById(R.id.validaterequest_close_request_button);
        final RecyclerView recyclerView = view.findViewById(R.id.validaterequest_product_list);

        String url = Constants.REQUEST_DETAILS_URL
                .replace("{prefixDoc}", request.getDocumentPrefix().toString())
                .replace("{document}", request.getDocumentCode().toString())
                .replace("{suffixDoc}", request.getDocumentSuffix().toString());
        doListRequest(Request.Method.GET, url, RequestDetails.class, null,
                ValidateRequestFragment.this::onDataReceived, null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new RequestMarginDecorator( 5));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        addContentToTextViews(view, request);
        verifyRecyclerViewState();

        backButton.setOnClickListener(v -> getMainActivity().replaceFragment(RequestDetailFragment.class));
        closeRequestButton.setOnClickListener(v -> onCloseRequested());
    }

    private void onCloseRequested() {
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

        Map<Integer, Integer> values = new HashMap<>();

        for (int i = 0; i < storedValues.size(); i++) {
            values.put(storedValues.keyAt(i), Integer.valueOf(storedValues.valueAt(i)));
        }

        String url = Constants.REQUEST_CLOSE_URL
                .replace("{prefixDoc}", request.getDocumentPrefix().toString())
                .replace("{document}", request.getDocumentCode().toString())
                .replace("{suffixDoc}", request.getDocumentSuffix().toString());

        doRequest(Request.Method.POST, url, Boolean.class, values, this::onCloseResponse, null);
    }

    private void onCloseResponse(boolean closed) {
        RequestHeader request = getMainActivity().get(
                Constants.CURRENT_REQUEST_KEY,
                RequestHeader.class,
                new TypeToken<RequestHeader>(){}.getType());

        if (closed) {
            new AlertDialog.Builder(this.getContext())
                    .setIcon(R.drawable.ic_info_icon)
                    .setTitle("Pedido cerrado")
                    .setMessage("El pedido se ha cerrado")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        getMainActivity().replaceFragment(BillFragment.class);
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this.getContext())
                    .setIcon(R.drawable.ic_warn_icon)
                    .setTitle("Pedido incompleto")
                    .setMessage("El pedido esta incompleto. Se actualizaron los faltantes")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        getMainActivity().remove(Constants.CURRENT_REQUEST_KEY);
                        getMainActivity().remove(Constants.COUNT_REQUEST_KEY
                                .replace("{prefixDoc}", request.getDocumentPrefix().toString())
                                .replace("{document}", request.getDocumentCode().toString())
                                .replace("{suffixDoc}", request.getDocumentSuffix().toString()));
                        getMainActivity().replaceFragment(RequestHeadFragment.class);
                    })
                    .show();
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

        setTotalPrice(requestDetails, storedValues);
        RecyclerView recyclerView = getView().findViewById(R.id.validaterequest_product_list);
        recyclerView.setAdapter(new RequestValidationAdapter(getMainActivity(), requestDetails, storedValues));

        verifyRecyclerViewState();
    }

    private void setTotalPrice(List<RequestDetails> details, SparseArray<String> actualValues) {
        final TextView totalPrice = getView().findViewById(R.id.validaterequest_total_text);

        double price = 0;
        for (int i = 0; i < details.size(); i++) {
            RequestDetails detail = details.get(i);
            int count = Integer.valueOf(actualValues.get(detail.getArticleCode(), "0"));
            price += (detail.getSalePrice() * count);
        }

        String text = "Valor actual del pedido: $ " + new DecimalFormat("#.00").format(price);
        totalPrice.setText(text);
    }

    private void addContentToTextViews(View view, RequestHeader request) {
        String requestTextContent = "Pedido " +
                request.getDocumentPrefix() + "-" +
                request.getDocumentCode() + "-" +
                request.getDocumentSuffix() + " del " +
                new SimpleDateFormat("dd/MM/yyyy").format(request.getInsertDate());

        TextView requestText = view.findViewById(R.id.validaterequest_request_text);

        requestText.setText(requestTextContent);
    }

    private void verifyRecyclerViewState() {
        final TextView emptyView = getView().findViewById(R.id.validaterequest_product_list_empty_text);
        final RecyclerView recyclerView = getView().findViewById(R.id.validaterequest_product_list);

        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
