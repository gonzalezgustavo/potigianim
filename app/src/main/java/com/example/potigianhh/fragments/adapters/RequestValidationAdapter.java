package com.example.potigianhh.fragments.adapters;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.potigianhh.MainActivity;
import com.example.potigianhh.R;
import com.example.potigianhh.model.RequestDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestValidationAdapter extends RecyclerView.Adapter<RequestValidationAdapter.ViewHolder> {
    private List<RequestDetails> requests;
    private String[] values;
    private MainActivity mainActivity;

    public RequestValidationAdapter(MainActivity mainActivity, List<RequestDetails> requests,
                                 SparseArray<String> currentValues) {
        if (requests == null)
            this.requests = new ArrayList<>();
        else
            this.requests = requests;

        this.mainActivity = mainActivity;
        this.values = new String[requests.size()];

        if (currentValues == null) {
            for (int i = 0; i < requests.size(); i++) {
                values[i] = "0";
            }
        } else {
            for (int i = 0; i < requests.size(); i++) {
                values[i] = currentValues.get(requests.get(i).getArticleCode());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.request_validation_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RequestDetails request = requests.get(position);
        String articleInfo = "  " + request.getArticleCode() + " - " + request.getArticleName().trim();
        holder.productView.setText(articleInfo);
        holder.expectedCountView.setText(Integer.toString(request.getPackagesGrams()));
        holder.actualCountView.setText(values[position]);

        boolean areCorrect = request.getPackagesGrams() == Integer.valueOf(values[position]);
        String color = areCorrect ? "#008000" : "#AA0000";
        holder.actualCountView.setTextColor(Color.parseColor(color));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public RequestDetails getItemAt(int position) {
        return requests.get(position);
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
