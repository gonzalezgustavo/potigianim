package com.example.potigianim.fragments.adapters;

import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.potigianim.MainActivity;
import com.example.potigianim.R;
import com.example.potigianim.model.PurchaseOrderDetail;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderValidationAdapter extends RecyclerView.Adapter<OrderValidationAdapter.ViewHolder> {
    private List<PurchaseOrderDetail> orders;
    private String[] values;
    private MainActivity mainActivity;

    public OrderValidationAdapter(MainActivity mainActivity, List<PurchaseOrderDetail> orders,
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
        View listItem = layoutInflater.inflate(R.layout.order_validation_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PurchaseOrderDetail request = orders.get(position);
        String articleInfo = "  " + (int) request.getArticleCode() + " - " + request.getArticleName().trim();
        holder.productView.setText(articleInfo);
        holder.expectedCountView.setText(Integer.toString((int) request.getRequestedPackages()));
        holder.actualCountView.setText(values[position]);

        boolean areCorrect = request.getRequestedPackages() == Integer.valueOf(values[position]);
        String color = areCorrect ? "#008000" : "#AA0000";
        holder.actualCountView.setTextColor(Color.parseColor(color));
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

    public void setValueAt(int position, String value) {
        values[position] = value;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productView;
        private TextView expectedCountView;
        private TextView actualCountView;
        private LinearLayout wrapper;
        ViewHolder(View itemView) {
            super(itemView);
            this.productView = itemView.findViewById(R.id.requestvalidationitem_product_text);
            this.expectedCountView = itemView.findViewById(R.id.requestvalidationitem_count_expected_text);
            this.actualCountView = itemView.findViewById(R.id.requestvalidationitem_count_actual_text);
            this.wrapper = itemView.findViewById(R.id.requestvalidationitem_wrapper);
        }
    }
}
