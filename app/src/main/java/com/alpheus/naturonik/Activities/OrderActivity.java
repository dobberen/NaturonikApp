package com.alpheus.naturonik.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Models.Cart;
import com.alpheus.naturonik.R;
import com.alpheus.naturonik.Service.EmailService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private LinearLayout btnOrderDetail;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;

    private RecyclerView recyclerView;

    private TextView tvOrderPrice;
    private EditText etName, etEmail, etPhone, etAddress;
    private Button orderBtn;
    private int totalPrice;

    String totalPrice1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Детали заказа");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_telephone);
        etAddress = findViewById(R.id.et_address);
        orderBtn = findViewById(R.id.order_btn);

        btnOrderDetail = findViewById(R.id.order_detail_ll);
        tvOrderPrice = findViewById(R.id.order_price_tv);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        retriveUserInfo();

        btnOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cart")
                .child("users").child(user.getUid());

        recyclerView = findViewById(R.id.rv_order);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<com.alpheus.naturonik.Models.Cart> options =
                new FirebaseRecyclerOptions.Builder<com.alpheus.naturonik.Models.Cart>()
                        .setQuery(mDatabase, com.alpheus.naturonik.Models.Cart.class)
                        .build();

        FirebaseRecyclerAdapter<com.alpheus.naturonik.Models.Cart, OrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<com.alpheus.naturonik.Models.Cart, OrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, final int position, @NonNull final com.alpheus.naturonik.Models.Cart model) {

                        try {
                            holder.description_tv.setText(model.getDescription() + " - " + model.getAmount() + " грамм");

                        } catch (Exception e) {
                            Toast.makeText(OrderActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @NonNull
                    @Override
                    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View mView = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.order_view, viewGroup, false);

                        return new OrdersViewHolder(mView);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView description_tv;


        public OrdersViewHolder(@NonNull View itemView) {

            super(itemView);

            description_tv = itemView.findViewById(R.id.order_description_tv);

        }
    }

    private void retriveUserInfo() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        mData.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("username").exists()) {
                        String username = dataSnapshot.child("username").getValue().toString();

                        if (username.length() > 0) {
                            etName.setText(username);
                        }
                    }

                    if (dataSnapshot.child("telephone").exists()) {
                        String birthday = dataSnapshot.child("telephone").getValue().toString();

                        if (birthday.length() > 0) {
                            etPhone.setText(birthday);
                        }
                    }


                    if (dataSnapshot.child("address").exists()) {
                        String birthday = dataSnapshot.child("address").getValue().toString();

                        if (birthday.length() > 0) {
                            etAddress.setText(birthday);
                        }
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String email = user.getEmail();
                        etEmail.setText(email.toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cart")
                .child("users").child(user.getUid());

        mData.child("cart").child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                totalPrice = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("total_price");
                    int pValue = Integer.parseInt(String.valueOf(price));
                    totalPrice += pValue;

                    tvOrderPrice.setText(String.valueOf(totalPrice + "\u20BD"));

                    totalPrice1 = String.valueOf(totalPrice);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Введите имя", Toast.LENGTH_LONG).show();
                } else if (etEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Введите адрес электронной почты", Toast.LENGTH_LONG).show();
                } else if (etPhone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Введите номер телефона", Toast.LENGTH_LONG).show();
                } else if (etAddress.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Введите адрес доставки", Toast.LENGTH_LONG).show();
                } else {
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            final List<String> products = new ArrayList<String>();

                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                String productDesc = childDataSnapshot.child("description").getValue(String.class);
                                String productAmount = childDataSnapshot.child("amount").getValue(String.class);
                                products.add(productDesc + " " + productAmount + " грамм");
                            }


                            String listString = "";
                            for (String s : products) {
                                listString += s + "\n";

                            }


                            try {

                                String to = "naturonik.ru@gmail.com";
                                String toClient = user.getEmail();
                                String subject = "Заказ";
                                String message = user.getEmail().toString() + "\n"
                                        + etName.getText().toString().trim() + "\n"
                                        + etPhone.getText().toString().trim() + "\n"
                                        + etAddress.getText().toString().trim() + "\n"
                                        + listString + "\n"
                                        + totalPrice1 + "\u20BD";

                                if (to.isEmpty()) {
                                    Toast.makeText(OrderActivity.this, "You must enter a recipient email", Toast.LENGTH_LONG).show();
                                } else if (subject.isEmpty()) {
                                    Toast.makeText(OrderActivity.this, "You must enter a Subject", Toast.LENGTH_LONG).show();
                                } else if (message.isEmpty()) {
                                    Toast.makeText(OrderActivity.this, "You must enter a message", Toast.LENGTH_LONG).show();
                                } else {
                                    //everything is filled out
                                    //send email
                                    new EmailService().sendEmail(to, subject, message);
                                    new EmailService().sendEmail(toClient, subject, message);
                                    Toast.makeText(getApplication(), "Заявка отправлена", Toast.LENGTH_SHORT).show();


                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
        });
    }
}
