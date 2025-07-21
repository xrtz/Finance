package com.example.finance.sampledata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.R;

import java.util.List;
import java.util.Objects;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinanceViewHolder> {

    private List<FinanceModel> mData;

    public FinanceAdapter(List<FinanceModel> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public FinanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.finance_item, parent, false);
        return new FinanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceViewHolder holder, int position) {
        if (!Objects.equals(mData.get(position).getNameOfTransaction(), "name1")){
        holder.tvNameType.setText(mData.get(position).getNameOfTransaction());}
        else{
            holder.tvNameType.setText(mData.get(position).getCategory());
        }
        holder.tvDate.setText(mData.get(position).getDate());
        if (Objects.equals(mData.get(position).getType(), "expenses"))
            holder.tvCost.setText("-" + mData.get(position).getAmount() + "");
        else
            holder.tvCost.setText("+" + mData.get(position).getAmount() + "");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<FinanceModel> getData() {
        return mData;
    }

    public static class FinanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameType;
        TextView tvCost;
        TextView tvDate;

        public FinanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameType = itemView.findViewById(R.id.tv_nameType);
            tvCost = itemView.findViewById(R.id.tv_cost);
            tvDate = itemView.findViewById(R.id.tv_date_item);
        }
    }

    public void updateData(List<FinanceModel> newData) {
        mData = newData;
        notifyDataSetChanged();
    }
}