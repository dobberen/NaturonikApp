package com.alpheus.naturonik.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;

    private EditText ETemail, ETpassword, ETpasswordRepeat;
    private Button btn_registration;
    private TextView linkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        ETemail = (EditText) findViewById(R.id.et_email);
        ETpassword = (EditText) findViewById(R.id.et_password);
        ETpasswordRepeat = (EditText) findViewById(R.id.et_password_repeat);

        linkLogin = findViewById(R.id.link_login);
        btn_registration = findViewById(R.id.btn_registration);


        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ETpassword.getText().toString().trim().equals(ETpasswordRepeat.getText().toString().trim()) ){
                    if (ETemail.getText().toString().trim().isEmpty()){

                        Toast.makeText(RegistrationActivity.this, "Введите Е-мэйл", Toast.LENGTH_LONG).show();
                    }
                    else {
                        registration(ETemail.getText().toString(), ETpassword.getText().toString());
                    }

                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Пароли не совпадают(меньше 6 символов)", Toast.LENGTH_LONG).show();
                }


            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void registration(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();

                    FirebaseUser user = mAuth.getCurrentUser();

                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(RegistrationActivity.this, "Регистрация провалена/Е-мэйл введен неверно", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
