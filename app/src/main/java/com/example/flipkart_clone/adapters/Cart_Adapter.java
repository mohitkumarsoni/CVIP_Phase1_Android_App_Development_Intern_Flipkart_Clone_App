package com.example.flipkart_clone.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flipkart_clone.R;
import com.example.flipkart_clone.databinding.ItemCartBinding;
import com.example.flipkart_clone.databinding.QuantityDialogBinding;
import com.example.flipkart_clone.models.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.logging.Handler;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.CartAdapter_ViewHolder> {
    private Context context;
    private ArrayList<Product> productArrayList;
    Cart cart;
    CartListener cartListener;

    public interface CartListener{
        public void onQuantityChanged();
    }

    public Cart_Adapter(Context context, ArrayList<Product> productArrayList, CartListener cartListener) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.cartListener = cartListener;
        //init cart as adapter called
        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter_ViewHolder holder, int position) {
        Product product = productArrayList.get(position);

        //populating views
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.cartItemIv);
        holder.binding.cartItemProductNameTv.setText(product.getName());
        holder.binding.cartItemProductAmountTv.setText((product.getPrice() - product.getDiscount())+" INR");
        holder.binding.cartItemProductCountTv.setText(product.getQuantity()+" item(s)");

        // action to perform when clicked on view.  (Alert dialog will popup to set Quantity of product)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //inflating & binding xml layout
                QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));

                //getRoot() to access its views
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityDialogBinding.getRoot())
                        .create();

                //making background transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80ffffff")));

                //populating dialog views
                quantityDialogBinding.quantityDialogProductNameTv.setText(product.getName());
                quantityDialogBinding.quantityDialogProductStockTv.setText("Stock "+product.getStock());
                quantityDialogBinding.quantityDialogCountTv.setText(String.valueOf(product.getQuantity()));
                int stock = product.getStock();

                //logic when "+" is clicked
                quantityDialogBinding.quantityDialogButtonPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = product.getQuantity();
                        quantity++;
                        if (quantity > product.getStock()){
                            Toast.makeText(context, "max stock limit reached !", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantityDialogCountTv.setText(String.valueOf(quantity));

                            cart.updateItem(product, product.getQuantity());
                            notifyDataSetChanged();
                            cartListener.onQuantityChanged();
                        }
                    }
                });
                //logic when "-" is clicked
                quantityDialogBinding.quantityDialogButtonMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = product.getQuantity();
                        quantity--;
                        if (quantity == 0){
                            Toast.makeText(context, "MOQ is 1 ", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantityDialogCountTv.setText(String.valueOf(quantity));

                            cart.updateItem(product, product.getQuantity());
                            notifyDataSetChanged();
                            cartListener.onQuantityChanged();
                        }
                    }
                });
                //logic when "save" is clicked
                quantityDialogBinding.quantityDialogSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class CartAdapter_ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public CartAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }

}
