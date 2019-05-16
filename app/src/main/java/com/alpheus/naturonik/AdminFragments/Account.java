package com.alpheus.naturonik.AdminFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alpheus.naturonik.Activities.AuthActivity;
import com.alpheus.naturonik.Activities.RegistrationActivity;
import com.alpheus.naturonik.R;
import com.google.firebase.auth.FirebaseAuth;


public class Account extends Fragment {

    Button btn_sign_out;

    public Account() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_account, container, false);

        setHasOptionsMenu(true);

        btn_sign_out = (Button) view.findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_add);
        MenuItem item1 = menu.findItem(R.id.action_add_product);
        if (item != null || item1 != null)
            item.setVisible(false);
            item1.setVisible(false);
    }
}
