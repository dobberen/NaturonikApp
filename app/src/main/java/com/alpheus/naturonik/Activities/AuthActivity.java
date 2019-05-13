package com.alpheus.naturonik.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alpheus.naturonik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail, ETpassword;
    private Button btn_sign_in, btn_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (user.getUid().equals("4GCDV8FyQfbnMLngj5jnYGxf8Md2") ) {
                        Toast.makeText(AuthActivity.this, "Админ, здарова", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AuthActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);
                    }


                } else {
                    // User is signed out
                }


            }
        };

        ETemail = (EditText) findViewById(R.id.et_email);
        ETpassword = (EditText) findViewById(R.id.et_password);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_registration = findViewById(R.id.btn_registration);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_registration).setOnClickListener(this);

        btn_sign_in.setEnabled(false);
        btn_registration.setEnabled(false);


        ETemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsForEmptyValues();
            }
        });

        ETpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsForEmptyValues();
            }
        });


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            if (user.getUid().equals("4GCDV8FyQfbnMLngj5jnYGxf8Md2") ) {
                Toast.makeText(AuthActivity.this, "Админ, здарова", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AuthActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sign_in) {
            signin(ETemail.getText().toString(), ETpassword.getText().toString());
        } else if (view.getId() == R.id.btn_registration) {
            registration(ETemail.getText().toString(), ETpassword.getText().toString());
        }

    }

    public void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String user = mAuth.getUid();

                        if (mAuth.getUid() == null){
                            user = "4GCDV8FyQfbnMLngj5jnYGxf8Md2";
                        }

                        if(user.equals("4GCDV8FyQfbnMLngj5jnYGxf8Md2") && task.isSuccessful() ){

                            Toast.makeText(AuthActivity.this, "Админ, здарова", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AuthActivity.this, AdminActivity.class);
                            startActivity(intent);

                        }

                        else if(task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(AuthActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void registration(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(AuthActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFieldsForEmptyValues() {

        String s1 = ETemail.getText().toString();
        String s2 = ETpassword.getText().toString();

        if (s1.length() > 0 && s2.length() > 0) {
            btn_sign_in.setEnabled(true);
            btn_registration.setEnabled(true);
        } else {
            btn_sign_in.setEnabled(false);
            btn_registration.setEnabled(false);
        }

    }
}
