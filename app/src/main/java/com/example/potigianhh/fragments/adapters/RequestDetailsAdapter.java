package com.example.potigianhh.fragments.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.potigianhh.MainActivity;
import com.example.potigianhh.R;
import com.example.potigianhh.model.RequestDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestDetailsAdapter extends RecyclerView.Adapter<RequestDetailsAdapter.ViewHolder> {
    private List<RequestDetails> requests;
    private String[] values;
    private MainActivity mainActivity;

    public RequestDetailsAdapter(MainActivity mainActivity, List<RequestDetails> requests,
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
        View listItem = layoutInflater.inflate(R.layout.request_detail_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RequestDetails request = requests.get(position);
        String articleInfo = request.getArticleCode() + " - " + request.getArticleName().trim();
        holder.values = values;
        holder.position = position;
        holder.productView.setText(articleInfo);
        holder.expectedCountView.setText(Integer.toString(request.getPackagesGrams()));
        holder.actualCountView.setText(values[position]);
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

    public RequestDetailsStatus getRequestsStatus() {
        for (int i = 0; i < getItemCount(); i++) {
            final RequestDetails request = requests.get(i);
            int value = "".equals(values[i]) ? 0 : Integer.valueOf(values[i]);

            if (request.getPackagesGrams() < value) {
                return RequestDetailsStatus.MORE;
            }
        }

        for (int i = 0; i < getItemCount(); i++) {
            final RequestDetails request = requests.get(i);
            int value = "".equals(values[i]) ? 0 : Integer.valueOf(values[i]);

            if (request.getPackagesGrams() > value) {
                return RequestDetailsStatus.LESS;
            }
        }

        return RequestDetailsStatus.FINE;
    }

    public void setValueAt(int position, String value) {
        values[position] = value;
    }

    public enum RequestDetailsStatus {
        FINE,
        LESS,
        MORE
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
