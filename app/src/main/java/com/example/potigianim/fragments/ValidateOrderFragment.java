package com.example.potigianim.fragments;

import android.os.Bundle;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.potigianim.R;
import com.example.potigianim.fragments.adapters.OrderValidationAdapter;
import com.example.potigianim.fragments.decorators.RequestMarginDecorator;
import com.example.potigianim.model.OrderAdditionalInfo;
import com.example.potigianim.model.PurchaseOrderDetail;
import com.example.potigianim.model.PurchaseOrderHeader;
import com.example.potigianim.model.SynchronizeOrderPayload;
import com.example.potigianim.model.SynchronizeOrderResponse;
import com.example.potigianim.utils.Constants;
import com.google.common.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ValidateOrderFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validate_order, container, false);
    }

    @Override
    public void onDestroyView() {
        final RecyclerView recyclerView = getView().findViewById(R.id.validateorder_product_list);
        OrderValidationAdapter adapter = (OrderValidationAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            adapter.clearMainActivityReference();
        }

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PurchaseOrderHeader order = getOrderHeader();

        Button backButton = view.findViewById(R.id.validateorder_back_button);
        Button closeRequestButton = view.findViewById(R.id.validateorder_close_request_button);
        final RecyclerView recyclerView = view.findViewById(R.id.validateorder_product_list);

        String url = Constants.OC_DETAILS_URL
                .replace("{prefixOc}", Integer.toString(order.getOrderPrefix()))
                .replace("{oc}", Integer.toString(order.getOrderCode()))
                .replace("{suffixOc}", Integer.toString(order.getOrderSuffix()));
        doListRequest(Request.Method.GET, url, PurchaseOrderDetail.class, null,
                ValidateOrderFragment.this::onDataReceived, null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new RequestMarginDecorator( 5));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        addContentToTextViews(order, view);
        verifyRecyclerViewState();

        backButton.setOnClickListener(v -> getMainActivity().replaceFragment(PurchaseOrderDetailFragment.class));
        closeRequestButton.setOnClickListener(v -> onCloseRequested());
    }

    private void onCloseRequested() {
        PurchaseOrderHeader order = getOrderHeader();
        SynchronizeOrderPayload payload = new SynchronizeOrderPayload();

        SparseArray<String> storedValues = getMainActivity().get(
                Constants.COUNT_ORDER_KEY
                        .replace("{prefixOc}", Integer.toString(order.getOrderPrefix()))
                        .replace("{oc}", Integer.toString(order.getOrderCode()))
                        .replace("{suffixOc}", Integer.toString(order.getOrderSuffix())),
                SparseArray.class,
                new TypeToken<SparseArray<String>>(){}.getType());

        Map<Integer, Integer> values = new HashMap<>();

        for (int i = 0; i < storedValues.size(); i++) {
            values.put(storedValues.keyAt(i), Integer.valueOf(storedValues.valueAt(i)));
        }

        payload.setOrderAdditionalInfo(getMainActivity().get(Constants.CURRENT_ORDER_ADDITIONAL_KEY,
                OrderAdditionalInfo.class, new TypeToken<OrderAdditionalInfo>(){}.getType()));
        payload.setArticleCount(values);

        String url = Constants.OC_DETAILS_URL
                .replace("{prefixOc}", Integer.toString(order.getOrderPrefix()))
                .replace("{oc}", Integer.toString(order.getOrderCode()))
                .replace("{suffixOc}", Integer.toString(order.getOrderSuffix()));
        doRequest(Request.Method.POST, url, SynchronizeOrderResponse.class, payload, this::onCloseResponse, null);
    }

    private void onCloseResponse(SynchronizeOrderResponse response) {
        boolean displayError = response.getHeaderReturnCode() != 0
                || response.getDetailInfo().stream().anyMatch(i -> i.getDetailReturnCode() != 0);

        StringBuilder messageInfo = new StringBuilder("La sincronización falló.\nInfo de la cabecera: \n")
                .append("Código resp: ").append(response.getHeaderReturnCode()).append("\n")
                .append("Mensaje: ").append(response.getHeaderMessage()).append("\n\n");

        response.getDetailInfo().stream()
                .filter(i -> i.getDetailReturnCode() != 0)
                .forEach(i -> messageInfo.append("  Art: ").append(i.getArticleCode().intValue())
                        .append(" - Código rta: ").append(i.getDetailReturnCode()).append("\n")
                        .append("  Mensaje: ").append(i.getDetailMessage()).append("\n\n"));

        if (!displayError) {
            PurchaseOrderHeader order = getOrderHeader();

            new AlertDialog.Builder(this.getContext())
                    .setIcon(R.drawable.ic_info_icon)
                    .setTitle("Pedido sincronizado")
                    .setMessage("La orden se ha sincronizado")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        getMainActivity().remove(Constants.CURRENT_ORDER_KEY);
                        getMainActivity().remove(Constants.CURRENT_ORDER_ADDITIONAL_KEY);
                        getMainActivity().remove(Constants.COUNT_ORDER_KEY
                                        .replace("{prefixOc}", Integer.toString(order.getOrderPrefix()))
                                        .replace("{oc}", Integer.toString(order.getOrderCode()))
                                        .replace("{suffixOc}", Integer.toString(order.getOrderSuffix())));
                        getMainActivity().replaceFragment(PurchaseOrderSearchFragment.class);
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this.getContext())
                    .setIcon(R.drawable.ic_error_icon)
                    .setTitle("Error al sincronizar")
                    .setMessage(messageInfo.toString())
                    .setPositiveButton("Ok", (dialog, which) -> {})
                    .show();
        }
    }

    private void onDataReceived(List<PurchaseOrderDetail> orderDetails) {
        PurchaseOrderHeader order = getOrderHeader();

        SparseArray<String> storedValues = getMainActivity().get(
                Constants.COUNT_ORDER_KEY
                        .replace("{prefixOc}", Integer.toString(order.getOrderPrefix()))
                        .replace("{oc}", Integer.toString(order.getOrderCode()))
                        .replace("{suffixOc}", Integer.toString(order.getOrderSuffix())),
                SparseArray.class,
                new TypeToken<SparseArray<String>>(){}.getType());

        RecyclerView recyclerView = getView().findViewById(R.id.validateorder_product_list);
        recyclerView.setAdapter(new OrderValidationAdapter(getMainActivity(), orderDetails, storedValues));

        verifyRecyclerViewState();
    }

    private void addContentToTextViews(PurchaseOrderHeader order, View view) {
        OrderAdditionalInfo info = getMainActivity().get(Constants.CURRENT_ORDER_ADDITIONAL_KEY,
                OrderAdditionalInfo.class, new TypeToken<OrderAdditionalInfo>(){}.getType());

        String billText = "Factura: " + (int)info.getPrefixOc() + "-" + (int)info.getSuffixOc();
        String userText = "Usuario: " + getUser().getCode();
        String ocText = "OC: " + order.getOrderPrefix() + "-" + order.getOrderCode() + "-" + order.getOrderSuffix();
        String dateText = "Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(order.getEmissionDate());
        String buyerText = "Comprador: " + order.getBuyerCode();
        String providerText = "Proveedor: " + order.getProviderCode();

        final TextView billView = view.findViewById(R.id.validateorder_bill_text);
        final TextView userView = view.findViewById(R.id.validateorder_user_text);
        final TextView ocView = view.findViewById(R.id.validateorder_oc_text);
        final TextView dateView = view.findViewById(R.id.validateorder_date_text);
        final TextView buyerView = view.findViewById(R.id.validateorder_buyer_text);
        final TextView providerView = view.findViewById(R.id.validateorder_provider_text);

        billView.setText(billText);
        userView.setText(userText);
        ocView.setText(ocText);
        dateView.setText(dateText);
        buyerView.setText(buyerText);
        providerView.setText(providerText);
    }

    private void verifyRecyclerViewState() {
        final TextView emptyView = getView().findViewById(R.id.validateorder_product_list_empty_text);
        final RecyclerView recyclerView = getView().findViewById(R.id.validateorder_product_list);

        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
