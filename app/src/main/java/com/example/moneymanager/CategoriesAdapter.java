package com.example.moneymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter {
    private final Categories categories;
    private final List<Integer> datas;
    private final CategoryCallback callback;

    public CategoriesAdapter(Categories categories, List<Integer> images, CategoryCallback callback) {
        this.categories = categories;
        this.datas = images;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview,parent,false);
        return (new CategoryHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int data = datas.get(position);
        img.setImageResource(data);
        txt.setVisibility(View.GONE);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onIconClicked(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    private ImageView img;
    private TextView txt;
    private class CategoryHolder extends RecyclerView.ViewHolder {
        public CategoryHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageview);
            txt = view.findViewById(R.id.txt);
        }
    }

    public interface CategoryCallback{
        void onIconClicked(int id);
    }
}
