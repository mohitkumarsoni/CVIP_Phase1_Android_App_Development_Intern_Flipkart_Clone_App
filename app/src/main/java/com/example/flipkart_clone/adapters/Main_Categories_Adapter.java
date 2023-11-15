package com.example.flipkart_clone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flipkart_clone.R;
import com.example.flipkart_clone.activities.Category_Activity;
import com.example.flipkart_clone.databinding.MainItemCategoriesBinding;
import com.example.flipkart_clone.models.Category;

import java.util.ArrayList;

public class Main_Categories_Adapter extends RecyclerView.Adapter<Main_Categories_Adapter.CategoriesViewHolder> {
    private Context context;
    private ArrayList<Category> categoryArrayList;

    public Main_Categories_Adapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item_categories, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Category category = categoryArrayList.get(position);

        //populating views
        Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.mainItemCategoriesImageIv);
        holder.binding.mainItemCategoriesNameTv.setText(category.getName());

        //onClick of Category, Category activity will launch, which will show products related to particular category
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Category_Activity.class);
                intent.putExtra("catId", category.getId());
                intent.putExtra("catName", category.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        MainItemCategoriesBinding binding;
        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MainItemCategoriesBinding.bind(itemView);
        }
    }
}
