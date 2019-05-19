package com.alpheus.naturonik.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private EditText ETemail, ETpassword, ETpasswordRepeat;
    private Button btn_login;
    private TextView linkRegistration, linkForgetPass;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (user.getUid().equals("4GCDV8FyQfbnMLngj5jnYGxf8Md2")) {
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

        linkRegistration = findViewById(R.id.link_registration);
        linkForgetPass = findViewById(R.id.link_forget_pass);
        btn_login = findViewById(R.id.btn_login);

        final LinearLayout sv = findViewById(R.id.ll_auth);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar = new ProgressBar(AuthActivity.this, null, android.R.attr.progressBarStyleLarge);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);

                sv.setGravity(Gravity.CENTER);
                sv.addView(progressBar, params);
                progressBar.setVisibility(View.VISIBLE);

                signin(ETemail.getText().toString(), ETpassword.getText().toString());
            }
        });

        linkRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        linkForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ETemail.getText().toString().isEmpty()) {
                    Toast.makeText(AuthActivity.this, "Введите Е-мэйл", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseAuth.getInstance().sendPasswordResetEmail(ETemail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AuthActivity.this, "Отправлено на почту", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AuthActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String user = mAuth.getUid();

                        if (mAuth.getUid() == null) {
                            user = "4GCDV8FyQfbnMLngj5jnYGxf8Md2";
                        }

                        if (user.equals("4GCDV8FyQfbnMLngj5jnYGxf8Md2") && task.isSuccessful()) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(AuthActivity.this, "Админ, здарова", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AuthActivity.this, AdminActivity.class);
                            startActivity(intent);

                        } else if (task.isSuccessful()) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(AuthActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(AuthActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
