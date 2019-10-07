package com.example.potigianhh.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.potigianhh.MainActivity;
import com.example.potigianhh.R;
import com.example.potigianhh.model.RequestDetails;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<RequestDetails> requests;
    private MainActivity mainActivity;

    public BillAdapter(MainActivity mainActivity, List<RequestDetails> requests) {
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
        View listItem = layoutInflater.inflate(R.layout.bill_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RequestDetails request = requests.get(position);
        DecimalFormat format = new DecimalFormat("#.00");
        String article = request.getArticleCode() + " - " + request.getArticleName().trim();
        String price = "$ " + format.format(request.getFinalArticleUnitaryPrice());
        String total = "$ " + format.format(request.getArticleTotal());
        holder.articleView.setText(article);
        holder.countView.setText(Integer.toString(request.getPackagesGrams()));
        holder.priceView.setText(price);
        holder.totalPriceView.setText(total);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void clearMainActivityReference() {
        this.mainActivity = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView articleView;
        private TextView countView;
        private TextView priceView;
        private TextView totalPriceView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.articleView = itemView.findViewById(R.id.billview_description);
            this.countView = itemView.findViewById(R.id.billview_units);
            this.priceView = itemView.findViewById(R.id.billview_unit_price);
            this.totalPriceView = itemView.findViewById(R.id.billview_total_price);
        }
    }
}
