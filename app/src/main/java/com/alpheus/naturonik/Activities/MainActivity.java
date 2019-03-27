package com.alpheus.naturonik.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alpheus.naturonik.Adapters.ProductsAdapter;
import com.alpheus.naturonik.Fragments.Account;
import com.alpheus.naturonik.Fragments.Cart;
import com.alpheus.naturonik.Fragments.Favourites;
import com.alpheus.naturonik.Fragments.Main;
import com.alpheus.naturonik.Fragments.Search;
import com.alpheus.naturonik.Models.Product;
import com.alpheus.naturonik.MyApplication;
import com.alpheus.naturonik.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductsAdapter mAdapter;

    private SearchView searchView;

    private int spanCount;

    private static final String URL = "https://www.naturonik.ru/API/public/getImages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_main);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        if (width <= 1080) {
            spanCount = 3;
        }
        if (width > 1080) {
            spanCount = 5;
        }

        Log.i("Width", "Width: " + width);

        recyclerView = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        mAdapter = new ProductsAdapter(this, productList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchContacts();
    }

    private void fetchContacts() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Невозможно получить данные", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Product> items = new Gson().fromJson(response.toString(), new TypeToken<List<Product>>() {
                        }.getType());

                        productList.clear();
                        productList.addAll(items);

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Ошибка: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    // Bottom Navigation Bar

    Account accountFragment = new Account();
    Favourites favouritesFragment = new Favourites();
    Main mainFragment = new Main();
    Search searchFragment = new Search();
    Cart cartFragment = new Cart();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.navigation_account:
                getSupportActionBar().setTitle("Профиль");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, accountFragment).commit();
                return true;

            case R.id.navigation_favourite:
                getSupportActionBar().setTitle("Избранное");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, favouritesFragment).commit();
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

            case R.id.navigation_cart:
                getSupportActionBar().setTitle("Корзина");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cartFragment).commit();
                return true;
        }

        return false;
    }


}

