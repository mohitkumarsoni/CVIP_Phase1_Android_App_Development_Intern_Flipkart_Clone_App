package com.example.flipkart_clone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.flipkart_clone.R;
import com.example.flipkart_clone.constants.Constants;
import com.example.flipkart_clone.databinding.ActivityPaymentBinding;

public class Payment_Activity extends AppCompatActivity {
    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String orderCode = getIntent().getStringExtra("orderCode");

        binding.webview.setMixedContentAllowed(true);
        binding.webview.loadUrl(Constants.PAYMENT_URL+orderCode);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}