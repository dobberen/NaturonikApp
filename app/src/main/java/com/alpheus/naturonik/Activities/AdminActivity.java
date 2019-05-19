package com.alpheus.naturonik.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alpheus.naturonik.AdminFragments.Account;
import com.alpheus.naturonik.AdminFragments.Main;
import com.alpheus.naturonik.AdminFragments.Search;
import com.alpheus.naturonik.R;

public class AdminActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.admin_toolbar_main);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_main);
    }

    Account accountFragment = new Account();
    Main mainFragment = new Main();
    Search searchFragment = new Search();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.navigation_account:
                getSupportActionBar().setTitle("Профиль");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, accountFragment).commit();
                return true;


            case R.id.navigation_main:
                getSupportActionBar().setTitle("Главная");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment).commit();

                return true;

            case R.id.navigation_search:
                getSupportActionBar().setTitle("Поиск");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, searchFragment).commit();
                return true;

        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
