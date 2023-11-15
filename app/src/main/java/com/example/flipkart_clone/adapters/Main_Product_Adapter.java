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
import com.example.flipkart_clone.activities.Product_Detail_Activity;
import com.example.flipkart_clone.databinding.MainItemProductsBinding;
import com.example.flipkart_clone.models.Product;

import java.util.ArrayList;

public class Main_Product_Adapter extends RecyclerView.Adapter<Main_Product_Adapter.Product_View_holder> {

    private Context context;
    private ArrayList<Product> productArrayList;

    public Main_Product_Adapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public Product_View_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item_products, parent, false);
        return new Product_View_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_View_holder holder, int position) {
        Product product = productArrayList.get(position);

        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.mainItemProductsImageIv);
        holder.binding.mainItemProductsNameTv.setText(product.getName());
        holder.binding.mainItemProductsPriceTv.setText((product.getPrice()-product.getDiscount()) +" INR ");


        //task to perform when clicked on product
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Product_Detail_Activity.class);
                intent.putExtra("name",product.getName());
                intent.putExtra("image", product.getImage());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("id", product.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class Product_View_holder extends RecyclerView.ViewHolder {
        MainItemProductsBinding binding;
        public Product_View_holder(@NonNull View itemView) {
            super(itemView);
            binding = MainItemProductsBinding.bind(itemView);
        }
    }
}

