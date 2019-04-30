package com.alpheus.naturonik.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Activities.ProductActivity;
import com.alpheus.naturonik.Models.Product;
import com.alpheus.naturonik.R;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Main extends Fragment {


    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    public static final String MY_REQUEST_CODE  = "";


    public Main() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("products");

        recyclerView = view.findViewById(R.id.recycler_view);

        gridLayoutManager = new GridLayoutManager(getActivity(), calculateNoOfColumns(getActivity()));
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mDatabase, Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductsViewHolder holder, final int position, @NonNull final Product model) {

                        try{
                            holder.description.setText(model.getDescription());
                            Glide.with(getActivity())
                                    .load("https://naturonik.ru/img/" + model.getImage())
                                    .into(holder.thumbnail);
                        }
                        catch (Exception e){
                            Toast.makeText(getActivity(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        }


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String itemPosition = getRef(position).getKey();

                                Intent intent = new Intent(getActivity(), ProductActivity.class);

                                intent.putExtra("itemPosition", itemPosition);
                                intent.putExtra("description", model.getDescription());

                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.product_card_view, viewGroup, false);
                        ProductsViewHolder viewHolder = new ProductsViewHolder(view);

                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);

        adapter.startListening();

        return view;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }


    //Поиск

    private SearchView searchView;

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                adapter.getFilter().filter(query);
                return false;
            }
        });
    }*/

    public static class ProductsViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        ImageView thumbnail;
        CardView cardView;

        public ProductsViewHolder(@NonNull View itemView) {

            super(itemView);

            description = itemView.findViewById(R.id.tv_description);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
