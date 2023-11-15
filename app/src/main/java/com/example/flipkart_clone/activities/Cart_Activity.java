package com.example.flipkart_clone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.flipkart_clone.R;
import com.example.flipkart_clone.adapters.Cart_Adapter;
import com.example.flipkart_clone.databinding.ActivityCartBinding;
import com.example.flipkart_clone.models.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class Cart_Activity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private Cart_Adapter cartAdapter;
    private ArrayList<Product> productArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productArrayList = new ArrayList<>();
        Cart cart = TinyCartHelper.getCart();

        //here Item = "all product detail" & Integer = "No. of product Quantity (of Item)"
        for (Map.Entry< Item,Integer> item : cart.getAllItemsWithQty().entrySet()){
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            productArrayList.add(product);
        }

        //setting adapter to RecyclerView
        cartAdapter = new Cart_Adapter(Cart_Activity.this , productArrayList, new Cart_Adapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                //everytime Quantity will change , view will be updated.
                binding.cartTotalTv.setText(String.format("INR %.2f",cart.getTotalPrice()));
            }
        });
        binding.cartRv.setLayoutManager(new LinearLayoutManager(this));
        binding.cartRv.setAdapter(cartAdapter);

        //displaying total amount of products in cart
        binding.cartTotalTv.setText(String.format("INR %.2f",cart.getTotalPrice()));

        //Button to go to next "Checkout activity"
        binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cart_Activity.this, Checkout_Activity.class));
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}