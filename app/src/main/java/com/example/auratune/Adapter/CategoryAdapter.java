package com.example.auratune.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auratune.Domain.CategoryModel;
import com.example.auratune.R;
import com.example.auratune.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryModel> items;
    private Context context;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    public CategoryAdapter(ArrayList<CategoryModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding=ViewholderCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.binding.TitleCat.setText(items.get(position).getTitle());

        int adapterPosition = holder.getBindingAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        holder.binding.TitleCat.setText(items.get(adapterPosition).getTitle());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPosition = holder.getBindingAdapterPosition();
                if (newPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        if(selectedPosition==adapterPosition){
            holder.binding.TitleCat.setBackgroundResource(R.drawable.purple_category_bg);
            holder.binding.TitleCat.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.binding.TitleCat.setBackgroundResource(R.drawable.purple_full_corner_bg);
            holder.binding.TitleCat.setTextColor(context.getResources().getColor(R.color.white));
        }
    }


    @Override
    public int getItemCount() {
        // Return actual item count; guard against null list to avoid crashes
        return items == null ? 0 : items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;

        public ViewHolder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
