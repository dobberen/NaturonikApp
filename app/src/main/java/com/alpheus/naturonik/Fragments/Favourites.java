package com.alpheus.naturonik.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Activities.ProductActivity;
import com.alpheus.naturonik.Models.Product;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Favourites extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    private ProgressBar progressBar;
    private LinearLayout placeholder;
    private Button btnToShop;

    public Favourites() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        setHasOptionsMenu(true);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        placeholder = (LinearLayout) view.findViewById(R.id.placeholder_ll_fav);
        btnToShop = (Button) view.findViewById(R.id.btn_to_shop);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("favourites")
                .child("users").child(user.getUid());

        recyclerView = view.findViewById(R.id.recycler_view_favourites);

        gridLayoutManager = new GridLayoutManager(getActivity(), calculateNoOfColumns(getActivity()));
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mDatabase, Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, Search.ProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, Search.ProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Search.ProductsViewHolder holder, final int position, @NonNull final Product model) {


                        try {
                            holder.description.setText(model.getDescription());
                            Glide.with(getActivity())
                                    .load("https://naturonik.ru/img/" + model.getImg())
                                    .into(holder.thumbnail);
                        } catch (Exception e) {
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
                    public Search.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.product_card_view, viewGroup, false);
                        Search.ProductsViewHolder viewHolder = new Search.ProductsViewHolder(view);

                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);


                }

                if(!dataSnapshot.exists()){
                    placeholder.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.exists()){
                    placeholder.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter.startListening();

        btnToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new Search();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            }
        });

        return view;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {

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
