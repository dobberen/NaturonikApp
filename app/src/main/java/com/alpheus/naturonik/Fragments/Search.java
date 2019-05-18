package com.alpheus.naturonik.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Activities.ProductActivity;
import com.alpheus.naturonik.Activities.UpdateProductActivity;
import com.alpheus.naturonik.Models.Product;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Search extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseSearch;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    private EditText searchET;
    private ImageButton searchIB;

    private ProgressBar progressBar;

    public Search() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        setHasOptionsMenu(true);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("products");
        mDatabaseSearch = FirebaseDatabase.getInstance().getReference("products");

        searchET = view.findViewById(R.id.search_et);
        searchIB = view.findViewById(R.id.search_ib);

        recyclerView = view.findViewById(R.id.recycler_view);

        gridLayoutManager = new GridLayoutManager(getActivity(), calculateNoOfColumns(getActivity()));
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);


        Query firebaseSearchQuery = mDatabaseSearch.orderByChild("description");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(firebaseSearchQuery, Product.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductsViewHolder holder, final int position, @NonNull final Product model) {

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
                    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.product_card_view, viewGroup, false);
                        ProductsViewHolder viewHolder = new ProductsViewHolder(view);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter.startListening();

        searchIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = searchET.getText().toString();

                productSearch(searchText);
            }
        });

        return view;
    }

    //-------------------------Поиск-------------------------

    private void productSearch(String searchText) {

        Toast.makeText(getActivity(), "Поиск начался", Toast.LENGTH_SHORT).show();

        Query firebaseSearchQuery = mDatabaseSearch.orderByChild("description").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(firebaseSearchQuery, Product.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Product, ProductsViewHolder> searchRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, final int position, final @NonNull Product model) {

                holder.setDescription(model.getDescription());
                holder.setThumbnail(getContext(), "https://naturonik.ru/img/" +  model.getImg());

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

                View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_card_view, viewGroup, false);
                return new ProductsViewHolder(mView);
            }
        };

        recyclerView.setAdapter(searchRecyclerAdapter);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchRecyclerAdapter.startListening();
    }

    //-------------------------Подсчет строк для отображения-------------------------

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
        View mView;

        public ProductsViewHolder(@NonNull View itemView) {

            super(itemView);

            description = itemView.findViewById(R.id.tv_description);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            cardView = itemView.findViewById(R.id.cardview_id);

            mView = itemView;
        }

        public void setDescription(String product_description) {
            description.setText(product_description);
        }

        public void setThumbnail(Context context, String product_thumbnail) {

            Glide.with(context).load("https://naturonik.ru/img/" + product_thumbnail).into(thumbnail);
        }

    }
}
