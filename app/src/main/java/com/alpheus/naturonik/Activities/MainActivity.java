package com.alpheus.naturonik.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.alpheus.naturonik.Fragments.Account;
import com.alpheus.naturonik.Fragments.Cart;
import com.alpheus.naturonik.Fragments.Favourites;
import com.alpheus.naturonik.Fragments.Main;
import com.alpheus.naturonik.Fragments.Search;
import com.alpheus.naturonik.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_main);


    }

    // Bottom Navigation Bar

    Account accountFragment = new Account();
    Favourites favouritesFragment = new Favourites();
    Main mainFragment = new Main();
    Search searchFragment = new Search();
    Cart cartFragment = new Cart();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.navigation_account:
                getSupportActionBar().setTitle("Профиль");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, accountFragment).addToBackStack(null).commit();
                return true;

            case R.id.navigation_favourite:
                getSupportActionBar().setTitle("Избранное");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, favouritesFragment).addToBackStack(null).commit();
                return true;

            case R.id.navigation_main:
                getSupportActionBar().setTitle("Главная");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment).addToBackStack(null).commit();
                return true;

            case R.id.navigation_search:
                getSupportActionBar().setTitle("Поиск");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, searchFragment).addToBackStack(null).commit();
                return true;

            case R.id.navigation_cart:
                getSupportActionBar().setTitle("Корзина");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cartFragment).addToBackStack(null).commit();
                return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddProductActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            backToast.cancel();
            super.onBackPressed();
            finish();
            moveTaskToBack(true);
            return;
        } else {

            backToast = Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }


}

