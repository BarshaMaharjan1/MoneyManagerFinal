package com.example.moneymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.pojos.Records;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter {
    private List<Records> data;
    private final RecordsCallback callback;

    public RecordsAdapter(List<Records> records, RecordsCallback callback) {
        this.data = records;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_records_data_item,parent,false);
        return (new RecordsHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Records records = data.get(position);
        amount.setText("NPR "+records.getAmount());
        date.setText(records.getDate());
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onMenuSelected(records,holder);
            }
        });
        callback.updateItemData(title,icon,records,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    AppCompatTextView title,date,amount;
    AppCompatImageView menu,icon;

    public void setFilteredValue(List<Records> records) {
        this.data = records;
        notifyDataSetChanged();
    }

    private class RecordsHolder extends RecyclerView.ViewHolder {
        public RecordsHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            amount = view.findViewById(R.id.amount);
            menu = view.findViewById(R.id.menu);
            icon = view.findViewById(R.id.icon);
        }
    }

    public interface RecordsCallback{
        void updateItemData(AppCompatTextView title, AppCompatImageView icon, Records records,int position);

        void onMenuSelected(Records records, RecyclerView.ViewHolder menu);
    }
}
