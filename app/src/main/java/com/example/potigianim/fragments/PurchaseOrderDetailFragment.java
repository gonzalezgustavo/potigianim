package com.example.potigianim.fragments;

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
import com.example.potigianim.R;
import com.example.potigianim.fragments.adapters.OrderDetailsAdapter;
import com.example.potigianim.fragments.decorators.RequestMarginDecorator;
import com.example.potigianim.model.OrderAdditionalInfo;
import com.example.potigianim.model.PurchaseOrderDetail;
import com.example.potigianim.model.PurchaseOrderHeader;
import com.example.potigianim.utils.Constants;
import com.google.common.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PurchaseOrderDetailFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase_order_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PurchaseOrderHeader order = getOrderHeader();
        setTextFields(order, view);

        final Button backButton = view.findViewById(R.id.orderdetail_back_button);
        final Button finalizeButton = view.findViewById(R.id.orderdetail_finalize_button);

        final RecyclerView recyclerView = view.findViewById(R.id.orderdetail_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new RequestMarginDecorator(5));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        String url = Constants.OC_DETAILS_URL
                .replace("{prefixOc}", Integer.toString(order.getOrderPrefix()))
                .replace("{oc}", Integer.toString(order.getOrderCode()))
                .replace("{suffixOc}", Integer.toString(order.getOrderSuffix()));
        doListRequest(Request.Method.GET, url, PurchaseOrderDetail.class, null,
                PurchaseOrderDetailFragment.this::onDataReceived, null);

        backButton.setOnClickListener(v -> getMainActivity().replaceFragment(PurchaseOrderSearchFragment.class));
        finalizeButton.setOnClickListener(v -> {
            OrderDetailsAdapter adapter = (OrderDetailsAdapter) recyclerView.getAdapter();
            OrderDetailsAdapter.OrderDetailsStatus status = adapter.getRequestsStatus();

            switch (status) {
                case MOREANDLESS:
                    displayValidationDialog("Hay algunos detalles con mayor cantidad a lo esperado" +
                            " y otros con menor cantidad. ¿Desea continuar?");
                break;
                case MORE:
                    displayValidationDialog("Hay detalles con mayor cantidad a lo esperado. " +
                            "¿Desea continuar?");
                break;
                case LESS:
                    displayValidationDialog("Hay detalles con menor cantidad a lo esperado. " +
                            "¿Desea continuar?");
                break;
                case FINE:
                    getMainActivity().replaceFragment(ValidateOrderFragment.class);
            }
        });

        verifyRecyclerViewState();
    }

    private void displayValidationDialog(String message) {
        new AlertDialog.Builder(PurchaseOrderDetailFragment.this.getContext())
                .setIcon(R.drawable.ic_warn_icon)
                .setTitle("Alerta del pedido")
                .setMessage(message)
                .setPositiveButton("Continuar", (dialog, which) -> {
                    getMainActivity().replaceFragment(ValidateOrderFragment.class);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> { })
                .show();
    }

    @Override
    public void onBarcode(String content) {
        final RecyclerView recyclerView = getView().findViewById(R.id.orderdetail_list);
        OrderDetailsAdapter adapter = (OrderDetailsAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                PurchaseOrderDetail request = adapter.getItemAt(i);
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
                    String newValue = Integer.toString(Integer.parseInt(currentValue) + (int) request.getProviderFactor());
                    adapter.setValueAt(i, newValue);

                    OrderDetailsAdapter.ViewHolder holder =
                            (OrderDetailsAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        new Handler(Looper.getMainLooper()).post(() -> holder.updateActualValue(newValue));
                    }

                    adapter.notifyItemChanged(i);

                    break;
                }
            }
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

        RecyclerView recyclerView = getView().findViewById(R.id.orderdetail_list);
        recyclerView.setAdapter(new OrderDetailsAdapter(getMainActivity(), orderDetails, storedValues));

        verifyRecyclerViewState();
    }

    private void setTextFields(PurchaseOrderHeader order, View view) {
        OrderAdditionalInfo info = getMainActivity().get(Constants.CURRENT_ORDER_ADDITIONAL_KEY,
                OrderAdditionalInfo.class, new TypeToken<OrderAdditionalInfo>(){}.getType());

        String billText = "Factura: " + (int)info.getPrefixOc() + "-" + (int)info.getSuffixOc();
        String userText = "Usuario: " + getUser().getCode();
        String ocText = "OC: " + order.getOrderPrefix() + "-" + order.getOrderCode() + "-" + order.getOrderSuffix();
        String dateText = "Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(order.getEmissionDate());
        String buyerText = "Comprador: " + order.getBuyerCode();
        String providerText = "Proveedor: " + order.getProviderCode();

        final TextView billView = view.findViewById(R.id.orderdetail_bill_text);
        final TextView userView = view.findViewById(R.id.orderdetail_user_text);
        final TextView ocView = view.findViewById(R.id.orderdetail_oc_text);
        final TextView dateView = view.findViewById(R.id.orderdetail_date_text);
        final TextView buyerView = view.findViewById(R.id.orderdetail_buyer_text);
        final TextView providerView = view.findViewById(R.id.orderdetail_provider_text);

        billView.setText(billText);
        userView.setText(userText);
        ocView.setText(ocText);
        dateView.setText(dateText);
        buyerView.setText(buyerText);
        providerView.setText(providerText);
    }

    @Override
    public void onDestroyView() {
        final RecyclerView recyclerView = getView().findViewById(R.id.orderdetail_list);
        OrderDetailsAdapter adapter = (OrderDetailsAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            adapter.clearMainActivityReference();
        }

        super.onDestroyView();
    }

    @Override
    public void onPause() {
        final RecyclerView recyclerView = getView().findViewById(R.id.orderdetail_list);
        OrderDetailsAdapter adapter = (OrderDetailsAdapter) recyclerView.getAdapter();
        PurchaseOrderHeader order = getOrderHeader();

        SparseArray<String> requestActuals = new SparseArray<>();
        if (adapter != null && adapter.getItemCount() > 0) {

            for (int i = 0; i < adapter.getItemCount(); i++) {
                PurchaseOrderDetail details = adapter.getItemAt(i);
                String value = adapter.getValueAt(i);
                if ("".equals(value))
                    value = "0";
                requestActuals.put((int) details.getArticleCode(), value);
            }

            getMainActivity().set(
                    Constants.COUNT_ORDER_KEY
                            .replace("{prefixOc}", Integer.toString(order.getOrderPrefix()))
                            .replace("{oc}", Integer.toString(order.getOrderCode()))
                            .replace("{suffixOc}", Integer.toString(order.getOrderSuffix())),
                    requestActuals);
        }

        super.onPause();
    }

    private void verifyRecyclerViewState() {
        final TextView emptyView = getView().findViewById(R.id.orderdetail_list_empty);
        final RecyclerView recyclerView = getView().findViewById(R.id.orderdetail_list);

        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
