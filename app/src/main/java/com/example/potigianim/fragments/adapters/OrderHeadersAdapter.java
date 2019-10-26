package com.example.potigianim.fragments.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.potigianim.MainActivity;
import com.example.potigianim.R;
import com.example.potigianim.fragments.PurchaseOrderDetailFragment;
import com.example.potigianim.model.OrderAdditionalInfo;
import com.example.potigianim.model.PurchaseOrderHeader;
import com.example.potigianim.model.RequestDetails;
import com.example.potigianim.utils.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHeadersAdapter extends RecyclerView.Adapter<OrderHeadersAdapter.ViewHolder> {
    private List<PurchaseOrderHeader> orders;
    private MainActivity mainActivity;

    public OrderHeadersAdapter(MainActivity mainActivity, List<PurchaseOrderHeader> requests) {
        if (requests == null)
            this.orders = new ArrayList<>();
        else
            this.orders = requests;

        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PurchaseOrderHeader order = orders.get(position);
        holder.dateView.setText(new SimpleDateFormat("dd/MM/yyyy").format(order.getEmissionDate()));
        holder.providerView.setText(Integer.toString(order.getProviderCode()));
        holder.ocView.setText(Integer.toString(order.getOrderSuffix()));
        holder.buyerView.setText(Integer.toString(order.getBuyerCode()));
        holder.itemsView.setText(Integer.toString(order.getItems()));

        holder.wrapper.setOnClickListener(v -> {
            displayContentDialog(order);
        });
    }
        @Override
    public int getItemCount() {
        return orders.size();
    }

    public PurchaseOrderHeader getItemAt(int position) {
        return orders.get(position);
    }

    public void clearMainActivityReference() {
        this.mainActivity = null;
    }

    private void displayContentDialog(PurchaseOrderHeader order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_billinfo, null);

        final Spinner spinner = dialogView.findViewById(R.id.billinfo_type);
        final EditText prefixEdit = dialogView.findViewById(R.id.billinfo_prefix_edit);
        final EditText suffixEdit = dialogView.findViewById(R.id.billinfo_suffix_edit);
        final EditText dateEdit = dialogView.findViewById(R.id.billinfo_date_text);
        final EditText observationEdit = dialogView.findViewById(R.id.billinfo_observations_text);

        List<String> options = new ArrayList<>();
        options.add("Factura"); //1
        options.add("Nota de débito"); //4
        options.add("Remito"); //9

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.mainActivity, android.R.layout.simple_spinner_item,
                options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        AlertDialog.Builder badDateDialog = new AlertDialog.Builder(this.mainActivity)
                .setIcon(R.drawable.ic_error_icon)
                .setTitle("Fecha inválida")
                .setMessage("La fecha ingresada no es válida")
                .setPositiveButton("Ok", (dialog, which) -> {});

        AlertDialog.Builder emptyFieldsDialog = new AlertDialog.Builder(this.mainActivity)
                .setIcon(R.drawable.ic_error_icon)
                .setTitle("Datos vacíos")
                .setMessage("Hay campos con datos vacíos")
                .setPositiveButton("Ok", (dialog, which) -> {});

        AlertDialog theDialog = builder.setView(dialogView)
                .setPositiveButton("Continuar", (dialog, which) -> {
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {})
                .create();

        theDialog.show();
        theDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            boolean close = true;
            String dateAsText = dateEdit.getText().toString();
            String prefixAsText = prefixEdit.getText().toString();
            String suffixAsText = suffixEdit.getText().toString();

            if (dateAsText.isEmpty() || prefixAsText.isEmpty() || suffixAsText.isEmpty()) {
                emptyFieldsDialog.show();
                close = false;
            } else {
                if (!isValidDate(dateAsText)) {
                    badDateDialog.show();
                    close = false;
                } else {
                    int billType = getMappedOption(spinner.getSelectedItemPosition());
                    double prefix = Double.parseDouble(prefixEdit.getText().toString());
                    double suffix = Double.parseDouble(suffixEdit.getText().toString());
                    String observations = observationEdit.getText().toString();
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd/MM/yyyy").parse(dateAsText);
                    } catch (ParseException e) {} // wont happen. previously handled

                    OrderAdditionalInfo info = new OrderAdditionalInfo();
                    info.setBillType(billType);
                    info.setPrefixOc(prefix);
                    info.setSuffixOc(suffix);
                    info.setObservations(observations);
                    info.setDate(date);
                    info.setUser(this.mainActivity.getCurrentFragment().getUser().getCode());

                    mainActivity.set(Constants.CURRENT_ORDER_ADDITIONAL_KEY, info);
                    mainActivity.set(Constants.CURRENT_ORDER_KEY, order);
                    mainActivity.replaceFragment(PurchaseOrderDetailFragment.class);
                }
            }
            if (close)
                theDialog.dismiss();
        });
    }

    private boolean isValidDate(String date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        try {
            Date dateObj = formatter.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private int getMappedOption(int position) {
        switch (position) {
            case 0: return 1;
            case 1: return 4;
            case 2: return 9;
        }

        return -1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateView;
        private TextView ocView;
        private TextView providerView;
        private TextView buyerView;
        private TextView itemsView;
        private LinearLayout wrapper;

        ViewHolder(View itemView) {
            super(itemView);
            this.dateView = itemView.findViewById(R.id.orderitem_date);
            this.ocView = itemView.findViewById(R.id.orderitem_oc);
            this.providerView = itemView.findViewById(R.id.orderitem_provider);
            this.buyerView = itemView.findViewById(R.id.orderitem_buyer);
            this.itemsView = itemView.findViewById(R.id.orderitem_items);
            this.wrapper = itemView.findViewById(R.id.orderitem_wrapper);
        }
    }
}
