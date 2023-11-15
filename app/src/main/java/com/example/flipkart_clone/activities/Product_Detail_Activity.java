package com.example.flipkart_clone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.flipkart_clone.R;
import com.example.flipkart_clone.constants.Constants;
import com.example.flipkart_clone.databinding.ActivityProductDetailBinding;
import com.example.flipkart_clone.models.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class Product_Detail_Activity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private String name, image;
    private double original_price, discount_amount, finalPrice;
    private int id;
    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //receive intent details from previous activity
        getDetailsFromIntent();

        //init cart
        Cart cart = TinyCartHelper.getCart();

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        get_show_ProductsDetail(id);

        //load product into into views
        Glide.with(getApplicationContext())
                .load(image)
                .into(binding.productDetailIv);
        binding.productDetailName.setText(name);
        binding.productDetailOriginalPrice.setText(String.valueOf(original_price));
        binding.productDetailOriginalPrice.setPaintFlags(binding.productDetailOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //adding product to cart
        binding.productDetailAddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct, 1);
                Toast.makeText(Product_Detail_Activity.this, "1 item added to cart !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //receive product details via Volley to display on activity
    private void get_show_ProductsDetail(int id) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.GET_PRODUCT_DETAILS_URL+id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")){
                        JSONObject productObject = jsonObject.getJSONObject("product");
                        String description = productObject.getString("description");
                        discount_amount = productObject.getDouble("price_discount");
                        finalPrice = original_price - discount_amount;

                        binding.productDetailDescription.setText(HtmlCompat.fromHtml(description,0));
                        binding.productDetailDiscountAmount.setText(discount_amount+" Off");
                        binding.productDetailFinalPrice.setText(finalPrice+" INR");

                        //==================================

                        currentProduct = new Product(
                                productObject.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL+productObject.getString("image"),
                                productObject.getString("status"),
                                productObject.getDouble("price"),
                                productObject.getDouble("price_discount"),
                                productObject.getInt("stock"),
                                productObject.getInt("id")
                        );
                        currentProduct.setFinal_discount_price(finalPrice);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

    }

    //goto cart menu icon will be displayed
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //action perform when menu option selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_cart){
            startActivity(new Intent(getApplicationContext(), Cart_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //receive intent details from previous activity
    private void getDetailsFromIntent() {
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        original_price = getIntent().getDoubleExtra("price",0);
        id = getIntent().getIntExtra("id",0);
//        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
    }

    //tas to perform on back pressed
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}