package com.alpheus.naturonik.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Fragments.Main;
import com.alpheus.naturonik.Models.Favourite;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private TextView tv_product_country, tv_product_sort, tv_product_description, tv_price,
            tv_about, tv_energy, tv_nutritional;
    private ImageView img_id;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tv_product_country = findViewById(R.id.product_country);
        tv_product_sort = findViewById(R.id.product_sort);
        tv_product_description = findViewById(R.id.product_description);
        tv_price = findViewById(R.id.price);
        tv_about = findViewById(R.id.about);
        tv_energy = findViewById(R.id.energy_value);
        tv_nutritional = findViewById(R.id.nutritional_value);
        img_id = findViewById(R.id.product_thumbnail);

        Intent intent = getIntent();
        String price = intent.getExtras().getString("price");
        String country_id = intent.getExtras().getString("countrys_name");
        String sort_id = intent.getExtras().getString("sorts_name");
        String description = intent.getExtras().getString("description");
        String about = intent.getExtras().getString("about");
        String energy_value = intent.getExtras().getString("energy_value");
        String nutritional_value = intent.getExtras().getString("nutritional_value");
        String img = intent.getExtras().getString("img");

        tv_product_country.setText(country_id);
        tv_product_sort.setText(sort_id);
        tv_product_description.setText(description);
        tv_price.setText(price);
        tv_about.setText(about);
        tv_energy.setText(energy_value);
        tv_nutritional.setText(nutritional_value);

        Glide.with(getApplication()).load(img).into(img_id);
        
        //Toast.makeText(getApplication(),  dataSnapshot.toString(), Toast.LENGTH_LONG).show();

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(ProductActivity.this, MainActivity.class);
                startActivity(backIntent);
            }
        });

        Button favouriteButton = findViewById(R.id.button_favourite);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();

                handleSaveData(view);
            }
        });
    }

    public void handleSaveData(View view) {

        Favourite newFavoutire = new Favourite("test1", "ffff1.jpg");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String productId = mDatabase.push().getKey();

        mDatabase.child("favourites").child("users").child(user.getUid()).child(productId.toString()).setValue(newFavoutire);
    }


}
