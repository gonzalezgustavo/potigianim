package com.example.potigianhh.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.potigianhh.MainActivity;
import com.example.potigianhh.R;
import com.example.potigianhh.fragments.RequestDetailFragment;
import com.example.potigianhh.model.RequestHeader;
import com.example.potigianhh.utils.Constants;
import com.example.potigianhh.utils.ProvinceUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestHeadersAdapter extends RecyclerView.Adapter<RequestHeadersAdapter.ViewHolder> {
    private List<RequestHeader> requests;
    private MainActivity mainActivity;

    public RequestHeadersAdapter(MainActivity mainActivity, List<RequestHeader> requests) {
        if (requests == null)
            this.requests = new ArrayList<>();
        else
            this.requests = requests;

        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.request_header_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RequestHeader request = requests.get(position);

        String itemsText = "Items: " +
                request.getDocumentItems() +
                "    Bultos: " +
                request.getTotalPackages() +
                "    Peso: " +
                request.getTotalWeight() +
                "kg";
        String clientText = "Cliente: " +
                request.getClientCode() +
                " - " +
                request.getClientName().trim();
        String cuitCpText = "CUIT: " +
                request.getClientCUIT().trim() +
                "    CP: " +
                request.getClientPostalCode().trim();
        String priceText = "Reparto: " + request.getDistributionNumber() +
                "    Precio: $ " + new DecimalFormat("#.00").format(request.getTotalPrice());

        holder.documentView.setText(request.getComposedDoc());
        holder.itemsView.setText(itemsText);
        holder.clientView.setText(clientText);
        holder.cuitCpView.setText(cuitCpText);
        holder.priceView.setText(priceText);
        holder.addressView.setText(request.getClientAddress().trim());
        holder.cityView.setText(request.getClientTown().trim());
        holder.provinceView.setText(ProvinceUtils.getById(request.getClientProvinceCode()));

        holder.wrapper.setOnClickListener(v -> {
            mainActivity.set(Constants.CURRENT_REQUEST_KEY, request);
            mainActivity.replaceFragment(RequestDetailFragment.class);
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void clearMainActivityReference() {
        this.mainActivity = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView documentView;
        private TextView itemsView;
        private TextView clientView;
        private TextView cuitCpView;
        private TextView priceView;
        private TextView addressView;
        private TextView cityView;
        private TextView provinceView;
        private LinearLayout wrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            this.documentView = itemView.findViewById(R.id.requestheaditem_doc_text);
            this.itemsView = itemView.findViewById(R.id.requestheaditem_items_text);
            this.clientView = itemView.findViewById(R.id.requestheaditem_client_text);
            this.cuitCpView = itemView.findViewById(R.id.requestheaditem_cuit_text);
            this.priceView = itemView.findViewById(R.id.requestheaditem_pricedeliver_text);
            this.addressView = itemView.findViewById(R.id.requestheaditem_address_text);
            this.cityView = itemView.findViewById(R.id.requestheaditem_city_text);
            this.provinceView = itemView.findViewById(R.id.requestheaditem_province_text);
            this.wrapper = itemView.findViewById(R.id.requestheaditem_wrapper);
        }
    }
}
