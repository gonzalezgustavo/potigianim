package com.example.potigianim.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.potigianim.R;
import com.example.potigianim.fragments.adapters.OrderHeadersAdapter;
import com.example.potigianim.fragments.decorators.RequestMarginDecorator;
import com.example.potigianim.model.PurchaseOrderHeader;
import com.example.potigianim.utils.Constants;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PurchaseOrderSearchFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase_order_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestOCs(null, null);

        final Button filterBtn = view.findViewById(R.id.search_apply_filter_button);
        final EditText providerEdit = view.findViewById(R.id.search_provider_edit);
        final EditText ocEdit = view.findViewById(R.id.search_oc_edit);
        final RecyclerView recyclerView = view.findViewById(R.id.search_oc_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new RequestMarginDecorator( 5));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        filterBtn.setOnClickListener(v -> requestOCs(providerEdit.getText().toString(), ocEdit.getText().toString()));
    }

    @Override
    public void onDestroyView() {
        final RecyclerView recyclerView = getView().findViewById(R.id.search_oc_list);

        if (recyclerView.getAdapter() != null) {
            // We cleanup the activity reference
            ((OrderHeadersAdapter) recyclerView.getAdapter()).clearMainActivityReference();
        }

        super.onDestroyView();
    }

    private void requestOCs(String providerCode, String suffixCode) {
        if (providerCode == null || providerCode.isEmpty())
            providerCode = "none";
        if (suffixCode == null || suffixCode.isEmpty())
            suffixCode = "none";

        String url = Constants.SEARCH_OC_URL
                .replace("{providerCode}", providerCode)
                .replace("{suffixCode}", suffixCode);
        doListRequest(Request.Method.GET, url, PurchaseOrderHeader.class, null,
                PurchaseOrderSearchFragment.this::onDataReceived, null);
    }

    private void onDataReceived(List<PurchaseOrderHeader> orders) {
        final TextView initialText = getView().findViewById(R.id.search_oc_list_loading);
        initialText.setVisibility(View.INVISIBLE);

        final RecyclerView recyclerView = getView().findViewById(R.id.search_oc_list);
        recyclerView.setAdapter(new OrderHeadersAdapter(getMainActivity(), orders));
        verifyRecyclerViewState();
    }

    private void verifyRecyclerViewState() {
        final TextView emptyView = getView().findViewById(R.id.search_oc_list_empty);
        final RecyclerView recyclerView = getView().findViewById(R.id.search_oc_list);

        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
