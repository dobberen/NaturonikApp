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
    private String receiverProductID;
    private String img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("products");

        receiverProductID = getIntent().getExtras().get("itemPosition").toString();

        tv_product_country = findViewById(R.id.product_country);
        tv_product_sort = findViewById(R.id.product_sort);
        tv_product_description = findViewById(R.id.product_description);
        tv_price = findViewById(R.id.price);
        tv_about = findViewById(R.id.about);
        tv_energy = findViewById(R.id.energy_value);
        tv_nutritional = findViewById(R.id.nutritional_value);
        img_id = findViewById(R.id.product_thumbnail);

        retriveProductInfo();

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

    private void retriveProductInfo() {

        mDatabase.child(receiverProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("img"))){

                    String price = dataSnapshot.child("price").getValue().toString();
                    String country_id = dataSnapshot.child("countrys_name").getValue().toString();
                    String sort_id = dataSnapshot.child("sorts_name").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    String about = dataSnapshot.child("about").getValue().toString();
                    String energy_value = dataSnapshot.child("energy_value").getValue().toString();
                    String nutritional_value = dataSnapshot.child("nutritional_value").getValue().toString();
                    String img = dataSnapshot.child("img").getValue().toString();

                    tv_product_country.setText(country_id);
                    tv_product_sort.setText(sort_id);
                    tv_product_description.setText(description);
                    tv_price.setText(price);
                    tv_about.setText(about);
                    tv_energy.setText(energy_value);
                    tv_nutritional.setText(nutritional_value);
                    Glide.with(getApplication()).load(img).into(img_id);

                    img1 = img;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void handleSaveData(View view) {

        Favourite newFavoutire = new Favourite(
                tv_product_country.getText().toString(),
                tv_product_sort.getText().toString(),
                img1,
                tv_product_description.getText().toString(),
                tv_price.getText().toString(),
                tv_about.getText().toString(),
                tv_energy.getText().toString(),
                tv_nutritional.getText().toString());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String productId = mDatabase.push().getKey();

        mDatabase.child("favourites").child("users").child(user.getUid()).child(productId.toString()).setValue(newFavoutire);
    }


}
