package com.alpheus.naturonik.Activities;

import android.graphics.Color;
import android.support.annotation.NonNull;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.alpheus.naturonik.Models.Cart;
import com.alpheus.naturonik.Models.Favourite;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.rhexgomez.typer.roboto.TyperRoboto;


public class ProductActivity extends AppCompatActivity {

    private TextView tv_product_country, tv_product_sort, tv_product_description, tv_price,
            tv_about, tv_energy, tv_nutritional;
    private ImageView img_id;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String receiverProductID;
    private String img1, text;
    Button toCartButton;
    FloatingActionButton fav_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product1);


        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black));

            collapsingToolbarLayout.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());
            collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());

            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        //---------------------------------------Листенер "Закладки"---------------------------------------

        FloatingActionButton favouriteButton = findViewById(R.id.button_favourite);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("favourites").child("users").child(user.getUid()).child(receiverProductID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                fav_button = findViewById(R.id.button_favourite);

                                if (!dataSnapshot.exists()) {

                                    handleSaveDataToFavourites(view);
                                    Toast.makeText(getApplicationContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();

                                } else {

                                    mDatabase.child("favourites").child("users").child(user.getUid()).child(receiverProductID).setValue(null);
                                    Toast.makeText(getApplicationContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        });

        //---------------------------------------Листенер "Корзина"---------------------------------------

        toCartButton = findViewById(R.id.to_cart_btn);
        toCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("cart").child("users").child(user.getUid()).child(receiverProductID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.exists()) {

                                    handleSaveDataToCart(view);
                                    Toast.makeText(getApplicationContext(), "Добавлено в корзину", Toast.LENGTH_SHORT).show();
                                } else {

                                    mDatabase.child("cart").child("users").child(user.getUid()).child(receiverProductID).setValue(null);
                                    Toast.makeText(getApplicationContext(), "Удалено из корзины", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

        });
    }

    //---------------------------------------Получение данных из таблицы "Продукты"---------------------------------------

    private void retriveProductInfo() {

        mDatabase.child("products").child(receiverProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists())) {

                    String price = dataSnapshot.child("price").getValue().toString();
                    String country_id = dataSnapshot.child("countrys_name").getValue().toString();
                    String sort_id = dataSnapshot.child("sorts_name").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    String about = dataSnapshot.child("about").getValue().toString();
                    String energy_value = dataSnapshot.child("energy_value").getValue().toString();
                    String nutritional_value = dataSnapshot.child("nutritional_value").getValue().toString();
                    String img = dataSnapshot.child("img").getValue().toString();


                    tv_product_country.setText(country_id);
                    tv_product_sort.setText("Сорт: " + sort_id);
                    tv_product_description.setText(description);
                    tv_price.setText(price);
                    tv_about.setText(about);
                    tv_energy.setText(energy_value);
                    tv_nutritional.setText(nutritional_value);
                    Glide.with(getApplication()).load("https://naturonik.ru/img/" + img).into(img_id);

                    img1 = img;

                    getSupportActionBar().setTitle(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("favourites").child("users").child(user.getUid()).child(receiverProductID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        fav_button = findViewById(R.id.button_favourite);

                        if (dataSnapshot.exists()) {
                            fav_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_product_on, getApplicationContext().getTheme()));
                        } else {
                            fav_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite, getApplicationContext().getTheme()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        mDatabase.child("cart").child("users").child(user.getUid()).child(receiverProductID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            toCartButton.setText("Удалить из корзины");
                        } else {
                            toCartButton.setText("Добавить в корзину");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //---------------------------------------Добавление данных в таблицу "Закладки"---------------------------------------
    public void handleSaveDataToFavourites(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Favourite newFavoutire = new Favourite(
                tv_product_country.getText().toString(),
                tv_product_sort.getText().toString(),
                img1,
                tv_product_description.getText().toString(),
                tv_price.getText().toString(),
                tv_about.getText().toString(),
                tv_energy.getText().toString(),
                tv_nutritional.getText().toString());

        mDatabase.child("favourites").child("users").child(user.getUid()).child(receiverProductID).setValue(newFavoutire);


    }


    //---------------------------------------Добавление данных в таблицу "Корзина"---------------------------------------

    public void handleSaveDataToCart(View view) {

        Cart cart = new Cart(
                img1,
                tv_product_description.getText().toString(),
                tv_price.getText().toString(),
                "1",
                tv_energy.getText().toString(),
                tv_nutritional.getText().toString());

        mDatabase.child("cart").child("users").child(user.getUid()).child(receiverProductID).setValue(cart);
    }

}


