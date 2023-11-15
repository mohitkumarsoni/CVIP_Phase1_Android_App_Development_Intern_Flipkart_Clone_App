package com.example.flipkart_clone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flipkart_clone.R;
import com.example.flipkart_clone.adapters.Main_Categories_Adapter;
import com.example.flipkart_clone.adapters.Main_Product_Adapter;
import com.example.flipkart_clone.constants.Constants;
import com.example.flipkart_clone.databinding.ActivityMainBinding;
import com.example.flipkart_clone.models.Category;
import com.example.flipkart_clone.models.Product;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Category> categories;
    private ArrayList<Product> products;
    private Main_Categories_Adapter categoriesAdapter;
    private Main_Product_Adapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, Search_Activity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        getRecentOffers();
        initProduct();
        initCategories();

    }

    private void initCategories() {
        categories = new ArrayList<>();
        categoriesAdapter = new Main_Categories_Adapter(getApplicationContext(), categories);

        getCategories();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        binding.categoryRv.setLayoutManager(layoutManager);
        binding.categoryRv.setAdapter(categoriesAdapter);
    }

    private void getCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("categories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject childObj = jsonArray.getJSONObject(i);
                            Category category = new Category(
                                    childObj.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + childObj.getString("icon"),
                                    childObj.getString("color"),
                                    childObj.getString("brief"),
                                    childObj.getInt("id")
                            );
                            categories.add(category);
                        }
                        categoriesAdapter.notifyDataSetChanged();
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

    private void initProduct() {
        products = new ArrayList<>();
        productAdapter = new Main_Product_Adapter(this, products);

        getProduct();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productRv.setLayoutManager(layoutManager);
        binding.productRv.setAdapter(productAdapter);

    }

    private void getProduct() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_PRODUCTS_URL, new Response.Listener<String>() {
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
                        productAdapter.notifyDataSetChanged();
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


    //========= will populate offer slider bar on top =======   using volley library    ==============
    private void getRecentOffers() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("news_infos");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject childObj = jsonArray.getJSONObject(i);
                            binding.carousel.addData(new CarouselItem(
                                    Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                    childObj.getString("title")
                            ));
                        }
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

    //========== cart menu ==========
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_cart){
            startActivity(new Intent(MainActivity.this, Cart_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}








