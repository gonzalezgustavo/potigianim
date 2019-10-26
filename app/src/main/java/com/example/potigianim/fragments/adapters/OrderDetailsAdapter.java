package com.example.potigianim.fragments.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.potigianim.MainActivity;
import com.example.potigianim.R;
import com.example.potigianim.model.PurchaseOrderDetail;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private List<PurchaseOrderDetail> orders;
    private String[] values;
    private MainActivity mainActivity;

    public OrderDetailsAdapter(MainActivity mainActivity, List<PurchaseOrderDetail> orders,
                               SparseArray<String> currentValues) {
        if (orders == null)
            this.orders = new ArrayList<>();
        else
            this.orders = orders;

        this.mainActivity = mainActivity;
        this.values = new String[orders.size()];

        if (currentValues == null) {
            for (int i = 0; i < orders.size(); i++) {
                values[i] = "0";
            }
        } else {
            for (int i = 0; i < orders.size(); i++) {
                values[i] = currentValues.get((int) orders.get(i).getArticleCode());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_detail_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PurchaseOrderDetail order = orders.get(position);
        String articleInfo = (int) order.getArticleCode() + " - " + order.getArticleName().trim();
        holder.values = values;
        holder.position = position;
        holder.productView.setText(articleInfo);
        holder.expectedCountView.setText(Integer.toString((int) order.getRequestedPackages()));
        holder.actualCountView.setText(values[position]);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public PurchaseOrderDetail getItemAt(int position) {
        return orders.get(position);
    }

    public void clearMainActivityReference() {
        this.mainActivity = null;
    }

    public String getValueAt(int position) {
        return values[position];
    }

    public OrderDetailsStatus getRequestsStatus() {
        boolean anyWithMore = false;
        boolean anyWithLess = false;

        for (int i = 0; i < getItemCount(); i++) {
            final PurchaseOrderDetail order = orders.get(i);
            int value = "".equals(values[i]) ? 0 : Integer.valueOf(values[i]);

            if (order.getRequestedPackages() < value) {
                anyWithMore = true;
            }
        }

        for (int i = 0; i < getItemCount(); i++) {
            final PurchaseOrderDetail order = orders.get(i);
            int value = "".equals(values[i]) ? 0 : Integer.valueOf(values[i]);

            if (order.getRequestedPackages() > value) {
                anyWithLess = true;
            }
        }

        if (anyWithMore && anyWithLess)
            return OrderDetailsStatus.MOREANDLESS;
        if (anyWithMore)
            return OrderDetailsStatus.MORE;
        if (anyWithLess)
            return OrderDetailsStatus.LESS;
        return OrderDetailsStatus.FINE;
    }

    public void setValueAt(int position, String value) {
        values[position] = value;
    }

    public enum OrderDetailsStatus {
        FINE,
        LESS,
        MORE,
        MOREANDLESS
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productView;
        private TextView expectedCountView;
        private EditText actualCountView;
        private LinearLayout wrapper;
        private int position;
        private String[] values;

        ViewHolder(View itemView) {
            super(itemView);
            this.productView = itemView.findViewById(R.id.requestdetailitem_product_text);
            this.expectedCountView = itemView.findViewById(R.id.requestdetailitem_count_expected_text);
            this.actualCountView = itemView.findViewById(R.id.requestdetailitem_count_edit);
            this.wrapper = itemView.findViewById(R.id.requestdetailitem_wrapper);

            this.actualCountView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (actualCountView.hasFocus())
                        values[position] = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        public void updateActualValue(String newValue) {
            actualCountView.setText(newValue);
        }
    }
}
