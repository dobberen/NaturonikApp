package com.alpheus.naturonik.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpheus.naturonik.Fragments.Main;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;

public class ProductActivity extends AppCompatActivity {

    private TextView tv_product_country, tv_product_sort, tv_product_description, tv_price;
    private ImageView img_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tv_product_country = findViewById(R.id.product_country);
        tv_product_sort = findViewById(R.id.product_sort);
        tv_product_description = findViewById(R.id.product_description);
        tv_price = findViewById(R.id.price);
        img_id = findViewById(R.id.product_thumbnail);

        Intent intent = getIntent();
        String price = intent.getExtras().getString("price");
        String country_id = intent.getExtras().getString("countrys_name");
        String sort_id = intent.getExtras().getString("sorts_name");
        String description = intent.getExtras().getString("description");
        String img = intent.getExtras().getString("img");

        tv_product_country.setText(country_id);
        tv_product_sort.setText(sort_id);
        tv_product_description.setText(description);
        tv_price.setText(price);
        Glide.with(this).load("https://naturonik.ru/img/" + img).into(img_id);

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(ProductActivity.this, MainActivity.class);
                startActivity(backIntent);
            }
        });

    }
}
