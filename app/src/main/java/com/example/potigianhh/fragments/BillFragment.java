package com.example.potigianhh.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.potigianhh.R;
import com.example.potigianhh.fragments.adapters.BillAdapter;
import com.example.potigianhh.model.RequestDetails;
import com.example.potigianhh.model.RequestHeader;
import com.example.potigianhh.utils.Constants;
import com.example.potigianhh.utils.ProvinceUtils;
import com.google.common.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BillFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    @Override
    public void onDestroyView() {
        final RecyclerView recyclerView = getView().findViewById(R.id.bill_scrollview)
                .findViewById(R.id.bill_scrollview_inner)
                .findViewById(R.id.bill_scrollview_inner_content)
                .findViewById(R.id.bill_product_list);
        BillAdapter adapter = (BillAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            adapter.clearMainActivityReference();
        }

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button backButton = view.findViewById(R.id.bill_back_button);
        RecyclerView recyclerView = getView().findViewById(R.id.bill_scrollview)
                .findViewById(R.id.bill_scrollview_inner)
                .findViewById(R.id.bill_scrollview_inner_content)
                .findViewById(R.id.bill_product_list);

        RequestHeader request = getMainActivity().get(
                Constants.CURRENT_REQUEST_KEY,
                RequestHeader.class,
                new TypeToken<RequestHeader>() {}.getType());

        populateText(view, request);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        String url = Constants.REQUEST_DETAILS_URL
                .replace("{prefixDoc}", request.getDocumentPrefix().toString())
                .replace("{document}", request.getDocumentCode().toString())
                .replace("{suffixDoc}", request.getDocumentSuffix().toString());
        doListRequest(Request.Method.GET, url, RequestDetails.class, null,
                BillFragment.this::onDataReceived, null);

        backButton.setOnClickListener(v -> new AlertDialog.Builder(BillFragment.this.getContext())
                .setIcon(R.drawable.ic_info_icon)
                .setTitle("Alerta del pedido")
                .setMessage("¿Está seguro que desea volver? No podrá volver a " +
                        "visualizar esta factura desde la aplicación.")
                .setPositiveButton("Continuar", (dialog, which) -> {
                    getMainActivity().remove(Constants.CURRENT_REQUEST_KEY);
                    getMainActivity().remove(Constants.COUNT_REQUEST_KEY
                            .replace("{prefixDoc}", request.getDocumentPrefix().toString())
                            .replace("{document}", request.getDocumentCode().toString())
                            .replace("{suffixDoc}", request.getDocumentSuffix().toString()));

                    List<RequestHeader> requests = new ArrayList<>(getMainActivity().get(Constants.ASSIGNED_KEY.replace(
                            "{preparerId}",
                            Integer.toString(getPreparer().getId())),
                            List.class,
                            new TypeToken<List<RequestHeader>>(){}.getType()));

                    for (int i = 0; i < requests.size(); i++) {
                        RequestHeader currentRequest = requests.get(i);
                        if (currentRequest.getDocumentPrefix().equals(request.getDocumentPrefix()) &&
                                currentRequest.getDocumentCode().equals(request.getDocumentCode()) &&
                                currentRequest.getDocumentSuffix().equals(request.getDocumentSuffix())) {
                            requests.remove(i);
                            break;
                        }
                    }
                    getMainActivity().set(Constants.ASSIGNED_KEY
                            .replace("{preparerId}",
                                    Integer.toString(getPreparer().getId())), requests);
                    getMainActivity().replaceFragment(RequestHeadFragment.class);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {})
                .show());

    }

    private void onDataReceived(List<RequestDetails> requestDetails) {
        final RecyclerView recyclerView = getView().findViewById(R.id.bill_scrollview)
                .findViewById(R.id.bill_scrollview_inner)
                .findViewById(R.id.bill_scrollview_inner_content)
                .findViewById(R.id.bill_product_list);
        final TextView totalView = getView().findViewById(R.id.bill_total_text);
        String totalText = "Total: $ " + new DecimalFormat("#.00").format((Double) requestDetails.stream()
                .map(RequestDetails::getArticleTotal).mapToDouble(Double::doubleValue).sum());
        totalView.setText(totalText);
        recyclerView.setAdapter(new BillAdapter(getMainActivity(), requestDetails));
    }

    private void populateText(View view, RequestHeader request) {
        TextView requestView = view.findViewById(R.id.bill_request_text);
        LinearLayout billLayout = view.findViewById(R.id.bill_scrollview)
                .findViewById(R.id.bill_scrollview_inner)
                .findViewById(R.id.bill_scrollview_inner_content);
        TextView clientNameView = billLayout.findViewById(R.id.bill_client_name_text);
        TextView clientCuitView = billLayout.findViewById(R.id.bill_client_cuit_text);
        TextView clientAddressView = billLayout.findViewById(R.id.bill_client_address_text);
        TextView postalCodeTownView = billLayout.findViewById(R.id.bill_client_town_pc_text);
        TextView provinceView = billLayout.findViewById(R.id.bill_province_text);

        String requestText = "Pedido " +
                request.getDocumentPrefix() + "-" +
                request.getDocumentCode() + "-" +
                request.getDocumentSuffix();
        String cuitText = "CUIT " + request.getClientCUIT();
        String townPcText = request.getClientPostalCode().trim() + " " + request.getClientTown().trim();

        requestView.setText(requestText);
        clientNameView.setText(request.getClientName().trim());
        clientCuitView.setText(cuitText);
        clientAddressView.setText(request.getClientAddress().trim());
        postalCodeTownView.setText(townPcText);
        provinceView.setText(ProvinceUtils.getById(request.getClientProvinceCode()));
    }

}
