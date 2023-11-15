package com.example.flipkart_clone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flipkart_clone.R;
import com.example.flipkart_clone.adapters.Main_Product_Adapter;
import com.example.flipkart_clone.constants.Constants;
import com.example.flipkart_clone.databinding.ActivitySearchBinding;
import com.example.flipkart_clone.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity {
    ActivitySearchBinding binding;
    Main_Product_Adapter adapter;
    ArrayList<Product>products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        products = new ArrayList<>();
        adapter = new Main_Product_Adapter(Search_Activity.this, products);

        String query = getIntent().getStringExtra("query");
        getSupportActionBar().setTitle(query);
        getProducts(query);

        binding.searchRv.setLayoutManager(new GridLayoutManager(this, 2));
        binding.searchRv.setAdapter(adapter);

    }

    private void getProducts(String query){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL+ "?q"+query;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object =new JSONObject(response);

                    if (object.getString("status").equals("success")){
                        JSONArray productsArray = object.getJSONArray("products");
                        for (int i=0 ; i<productsArray.length() ; i++){
                            JSONObject childObj = productsArray.getJSONObject(i);
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