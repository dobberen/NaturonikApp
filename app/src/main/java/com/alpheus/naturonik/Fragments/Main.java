package com.alpheus.naturonik.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpheus.naturonik.Adapters.ProductsAdapter;
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

public class Main extends Fragment {

    private static final String URL = "https://www.naturonik.ru/API/public/getImages";

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList;
    private GridLayoutManager gridLayoutManager;

    private int spanCount;

    public Main() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        if (width <= 1080) {
            spanCount = 3;
        }

        if (width > 1080) {
            spanCount = 4;

        }

        Log.i("Width", "Width: " + width);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();

        adapter = new ProductsAdapter(getActivity(),productList);
        recyclerView.setAdapter(adapter);

        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response == null) {
                            Toast.makeText(getActivity().getApplicationContext(), "Невозможно получить данные", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Product> items = new Gson().fromJson(response.toString(), new TypeToken<List<Product>>() {
                        }.getType());

                        productList.clear();
                        productList.addAll(items);

                        adapter.addData(productList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

}
