package com.alpheus.naturonik.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Activities.OrderActivity;
import com.alpheus.naturonik.Activities.ProductActivity;
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

import java.util.Map;

public class Cart extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private LinearLayout placeholder;

    private ScrollView scrollView;
    private Button buyBtn, btnToShop;

    private TextView amountResultTv, priceResultTv;

    private int totalAmount, totalPrice;

    public Cart() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_cart, container, false);
        setHasOptionsMenu(true);

        amountResultTv = (TextView) view.findViewById(R.id.amount_cart_tv);
        priceResultTv = (TextView) view.findViewById(R.id.price_cart_tv);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        placeholder = (LinearLayout) view.findViewById(R.id.placeholder_ll_cart);

        scrollView = (ScrollView) view.findViewById(R.id.cart_scrollview);
        buyBtn = (Button) view.findViewById(R.id.buy_btn);

        btnToShop = (Button) view.findViewById(R.id.btn_to_shop);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cart")
                .child("users").child(user.getUid());

        recyclerView = view.findViewById(R.id.recycler_view_cart);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<com.alpheus.naturonik.Models.Cart> options =
                new FirebaseRecyclerOptions.Builder<com.alpheus.naturonik.Models.Cart>()
                        .setQuery(mDatabase, com.alpheus.naturonik.Models.Cart.class)
                        .build();

        FirebaseRecyclerAdapter<com.alpheus.naturonik.Models.Cart, ProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<com.alpheus.naturonik.Models.Cart, ProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductsViewHolder holder, final int position, @NonNull final com.alpheus.naturonik.Models.Cart model) {

                        try {
                            holder.description_tv.setText(model.getDescription());
                            holder.energy_tv.setText(model.getEnergy_value());
                            holder.nutrional_tv.setText(model.getNutritional_value());
                            holder.amount_tv.setText("Вес: " + model.getAmount() + " грамм");
                            holder.price_tv.setText("Цена за кг: " + model.getPrice() + " рублей");
                            holder.total_price_tv.setText("Итого: " + model.getTotal_price() + " рублей");



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

                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View mView = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.cart_view, viewGroup, false);

                        return new ProductsViewHolder(mView);
                    }
                };

        recyclerView.setAdapter(adapter);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                if (!dataSnapshot.exists()) {

                    scrollView.setVisibility(View.GONE);
                    buyBtn.setVisibility(View.GONE);
                    placeholder.setVisibility(View.VISIBLE);
                }

                if (dataSnapshot.exists()) {

                    scrollView.setVisibility(View.VISIBLE);
                    buyBtn.setVisibility(View.VISIBLE);
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
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Поиск");
                BottomNavigationView botNavView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation_view);
                botNavView.getMenu().getItem(3).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });

    //--------------------Подсчет числа позиций и итоговой суммы------------------------------------
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                totalPrice = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("total_price");
                    int pValue = Integer.parseInt(String.valueOf(price));
                    totalPrice += pValue;

                    //Log.d("Fiiire", "total= " + totalPrice);
                    //Log.d("Fiiire", "count= " + dataSnapshot.getChildrenCount());

                    priceResultTv.setText("Итого: " + String.valueOf(totalPrice));
                }


                amountResultTv.setText("Число позиций: " + String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    public static class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView description_tv, energy_tv, nutrional_tv, amount_tv, price_tv, total_price_tv;
        ImageView thumbnail;

        public ProductsViewHolder(@NonNull View itemView) {

            super(itemView);

            description_tv = itemView.findViewById(R.id.description_cart_tv);
            energy_tv = itemView.findViewById(R.id.energy_cart_tv);
            nutrional_tv = itemView.findViewById(R.id.nutritional_cart_tv);
            amount_tv = itemView.findViewById(R.id.amount_cart_tv);
            price_tv = itemView.findViewById(R.id.price_cart_tv);
            total_price_tv = itemView.findViewById(R.id.total_price_cart_tv);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }


}
