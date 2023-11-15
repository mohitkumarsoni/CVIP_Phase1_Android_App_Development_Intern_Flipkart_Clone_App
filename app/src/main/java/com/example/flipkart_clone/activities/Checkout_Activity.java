package com.example.flipkart_clone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flipkart_clone.R;
import com.example.flipkart_clone.adapters.Cart_Adapter;
import com.example.flipkart_clone.constants.Constants;
import com.example.flipkart_clone.databinding.ActivityCheckoutBinding;
import com.example.flipkart_clone.models.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Checkout_Activity extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    private ArrayList<Product> products;
    private Cart_Adapter adapter;
    private Cart cart;
    double finalAmtWithTax;
    final int tax = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        products = new ArrayList<>();
        cart = TinyCartHelper.getCart();

        //here Item = "all product detail" & Integer = "No. of product Quantity (of Item)"
        for (Map.Entry<Item, Integer> item :  cart.getAllItemsWithQty().entrySet()){
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        //setting adapter to RecyclerView
        adapter = new Cart_Adapter(Checkout_Activity.this, products, new Cart_Adapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                //everytime Quantity will change , view will be updated.
                binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));
            }
        });
        binding.cartList.setLayoutManager(new LinearLayoutManager(this));
        binding.cartList.setAdapter(adapter);

        //displaying subTotal amount of products in cart
        binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));
        //setting final amount with TAX
        finalAmtWithTax = (cart.getTotalPrice().doubleValue() *tax/100) + cart.getTotalPrice().doubleValue();
        //displaying final amount with TAX
        binding.totalWithOtherChargesTv.setText(String.format("INR %.2f",finalAmtWithTax));


        //action when "Checkout btn" will be clicked
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOrder();
            }
        });

    }

    //action when "Checkout btn" will be clicked
    private void processOrder() {
        binding.progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        //preparing JsonObject to send
        JSONObject product_order = new JSONObject();
        JSONObject loadData = new JSONObject();
        try {
            product_order.put("buyer", binding.nameEt.getText().toString().trim());
            product_order.put("address", binding.addressEt.getText().toString().trim());
            product_order.put("email", binding.emailEt.getText().toString().trim());
            product_order.put("shipping", "Economy");
            product_order.put("shipping_rate", "40");
            product_order.put("shipping_location", "");
            product_order.put("date_ship", Calendar.getInstance().getTimeInMillis());
            product_order.put("phone", "123456");
            product_order.put("comment", binding.additionalCommentsEt.getText().toString().trim());
            product_order.put("status", "");
            product_order.put("total_fees", 123);
            product_order.put("tax", tax);
            product_order.put("serial", "123456");
            product_order.put("created_at", Calendar.getInstance().getTimeInMillis());
            product_order.put("updated_at", Calendar.getInstance().getTimeInMillis());

            JSONArray product_order_detail = new JSONArray();
            for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()){
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                JSONObject productObject = new JSONObject();
                productObject.put("product_id",product.getId());
                productObject.put("product_name",product.getName());
                productObject.put("amount",product.getFinal_discount_price());
                productObject.put("price_item",product.getPrice());
                productObject.put("created_at",Calendar.getInstance().getTimeInMillis());
                productObject.put("updated_at",Calendar.getInstance().getTimeInMillis());

                product_order_detail.put(productObject);
            }

            loadData.put("product_order", product_order);
            loadData.put("product_order_detail", product_order_detail);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        //JsonObjectRequest when using POST method to send "Json Object"
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, loadData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.progressBar.setVisibility(View.GONE);

                try {
                    if (response.getString("status").equals("success")){
                        //if response is successful, show alert dialog to make order payment
                        Toast.makeText(Checkout_Activity.this, "order success", Toast.LENGTH_SHORT).show();

                        Log.e("res", response.toString());
                        String orderNumber = response.getJSONObject("data").getString("code");

                        new AlertDialog.Builder(Checkout_Activity.this)
                                .setTitle("Order successful")
                                .setMessage("your order number is "+orderNumber)
                                .setCancelable(false)
                                .setPositiveButton("Pay now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Checkout_Activity.this, Payment_Activity.class);
                                        intent.putExtra("orderCode", orderNumber);
                                        startActivity(intent);
                                    }
                                }).show();
                    }else {
                        //if response is failed, show alert dialog to inform user
                        Toast.makeText(Checkout_Activity.this, "order failed, try again", Toast.LENGTH_SHORT).show();
                        Log.e("res", response.toString());

                        new AlertDialog.Builder(Checkout_Activity.this)
                                .setTitle("Order failed")
                                .setMessage(response.getString("msg"))
                                .setCancelable(false)
                                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(Checkout_Activity.this, "order failed", Toast.LENGTH_SHORT).show();

            }
        }){
            //header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String > headers = new HashMap<>();
                headers.put("Security","secure_code");
                return headers;
            }
        };
        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}