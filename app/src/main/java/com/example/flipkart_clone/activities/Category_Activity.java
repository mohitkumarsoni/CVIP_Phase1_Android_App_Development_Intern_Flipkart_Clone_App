package com.example.flipkart_clone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flipkart_clone.R;
import com.example.flipkart_clone.adapters.Main_Product_Adapter;
import com.example.flipkart_clone.constants.Constants;
import com.example.flipkart_clone.databinding.ActivityCategoryBinding;
import com.example.flipkart_clone.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Category_Activity extends AppCompatActivity {
    ActivityCategoryBinding binding ;
    Main_Product_Adapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int catId = getIntent().getIntExtra("catId",0);
        String catName = getIntent().getStringExtra("catName");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(catName);

        getProducts(catId);

        products = new ArrayList<>();
        adapter = new Main_Product_Adapter(Category_Activity.this, products);
        binding.categoryProductsRv.setLayoutManager(new GridLayoutManager(this,2));
        binding.categoryProductsRv.setAdapter(adapter);



    }

    private void getProducts(int catId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL+"?category_id"+catId;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject childObj = jsonArray.getJSONObject(i);

                            Product product = new Product(
                                    childObj.getString("name"),
                                    Constants.PRODUCTS_IMAGE_URL+childObj.getString("image"),
                                    childObj.getString("status"),
                                    childObj.getDouble("price"),
                                    childObj.getDouble("price_discount"),
                                    childObj.getInt("stock"),
                                    childObj.getInt("id")
                            );
                            products.add(product);
                        }
                        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}