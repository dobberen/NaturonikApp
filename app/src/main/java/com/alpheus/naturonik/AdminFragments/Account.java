package com.alpheus.naturonik.AdminFragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Activities.AuthActivity;
import com.alpheus.naturonik.Activities.RegistrationActivity;
import com.alpheus.naturonik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Account extends Fragment {

    final Calendar mCalendar = Calendar.getInstance();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mData;

    private EditText etEmail, etPhone, etVK, etBirthday;
    private LinearLayout changeEmailBtn, changePassBtn, signOutBtn;
    private ImageView changeUsernameBtn;
    private TextView tvUsername;

    private Button saveButton;

    public Account() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_account, container, false);

        setHasOptionsMenu(true);

        mData = FirebaseDatabase.getInstance().getReference();

        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_telephone);
        etVK = view.findViewById(R.id.et_vk);
        etBirthday = view.findViewById(R.id.et_birthday);

        tvUsername = view.findViewById(R.id.tv_username);

        changeUsernameBtn = view.findViewById(R.id.change_username);

        changeEmailBtn = view.findViewById(R.id.change_email_btn);
        changePassBtn = view.findViewById(R.id.change_pass_btn);
        signOutBtn = view.findViewById(R.id.sign_out_btn);

        retriveUserInfo();

        //--------------------Смена юзернейма-------------------------------------------------------

        changeUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeUsernameDialog();
            }
        });

        //--------------------Смена почты-----------------------------------------------------------

        changeEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeEmailDialog();
            }
        });

        //--------------------Смена пароля----------------------------------------------------------

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });

        //--------------------Выход-----------------------------------------------------------------

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
            }
        });

        //--------------------Дата рождения---------------------------------------------------------

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(getActivity(), date,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //--------------------Сохранение данных-----------------------------------------------------

        saveButton = view.findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveAccountInfo();

                Toast.makeText(getActivity(), "Данные успешно сохранены", Toast.LENGTH_LONG).show();
            }
        });

        //--------------------E-mail----------------------------------------------------------------

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            etEmail.setText(email.toString());
        }


        return view;
    }
//--------------------Сохранить информацию об аккаунте------------------------------------------

    private void saveAccountInfo() {

        if(etPhone.length() > 0){

            etPhone.getText().toString();
            etPhone.setText(etPhone.getText().toString());

            mData.child("users").child(user.getUid()).child("telephone").setValue(etPhone.getText().toString());
        }

        if (etVK.length() > 0){

            etVK.getText().toString();
            etVK.setText(etVK.getText().toString());

            mData.child("users").child(user.getUid()).child("vk_link").setValue(etVK.getText().toString());
        }



    }

    //--------------------Смена почты---------------------------------------------------------------

    private void showChangeEmailDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.et_change);
        final EditText edtPass = dialogView.findViewById(R.id.et_change_for_pass);

        dialogBuilder.setTitle("Изменение электронной почты");
        dialogBuilder.setMessage("Введите адрес новой почты и свой текущий пароль:");

        dialogBuilder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null && edt.length() > 0 && edtPass.length() > 0) {

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), edtPass.getText().toString());

                    user.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    user.updateEmail(edt.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    etEmail.setText(edt.getText().toString());
                                                    Toast.makeText(getActivity(), "Адрес успешно изменен", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Введите корректные данные", Toast.LENGTH_SHORT).show();
                }


            }
        });

        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    //--------------------Смена пароля--------------------------------------------------------------

    private void showChangePasswordDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_change_pass, null);
        dialogBuilder.setView(dialogView);

        final EditText edtPass = dialogView.findViewById(R.id.et_pass);
        final EditText edtNewPass = dialogView.findViewById(R.id.et_new_pass);

        dialogBuilder.setTitle("Изменение пароля");
        dialogBuilder.setMessage("Введите свой текущий и новый пароли:");

        dialogBuilder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null && edtNewPass.length() > 0 && edtPass.length() > 0) {

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), edtPass.getText().toString());

                    user.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    user.updatePassword(edtNewPass.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getActivity(), "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Введите корректные данные", Toast.LENGTH_SHORT).show();
                }


            }
        });

        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    //--------------------Смена юзернейма-----------------------------------------------------------

    private void showChangeUsernameDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_change_username, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.et_change_username);

        dialogBuilder.setTitle("Изменение имени профиля");
        dialogBuilder.setMessage("Введите новое имя:");

        dialogBuilder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (edt.length() > 0) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    tvUsername.setText(edt.getText().toString());

                    mData.child("users").child(user.getUid()).child("username").setValue(edt.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Введите корректные данные", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    //--------------------Обновление календаря------------------------------------------------------

    private void updateLabel() {
        String mFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(mFormat, Locale.ENGLISH);

        etBirthday.setText(sdf.format(mCalendar.getTime()));

        if (etBirthday.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            mData.child("users").child(user.getUid()).child("birthday").setValue(etBirthday.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Введите корректные данные", Toast.LENGTH_SHORT).show();
        }
    }

    //--------------------Получение данных о пользователе-------------------------------------------

    private void retriveUserInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mData.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("username").exists()) {
                        String username = dataSnapshot.child("username").getValue().toString();

                        if (username.length() > 0) {
                            tvUsername.setText(username);
                        }
                    }

                    if (dataSnapshot.child("telephone").exists()) {
                        String birthday = dataSnapshot.child("telephone").getValue().toString();

                        if (birthday.length() > 0) {
                            etPhone.setText(birthday);
                        }
                    }

                    if (dataSnapshot.child("vk_link").exists()) {
                        String birthday = dataSnapshot.child("vk_link").getValue().toString();

                        if (birthday.length() > 0) {
                            etVK.setText(birthday);
                        }
                    }
                    if (dataSnapshot.child("birthday").exists()) {
                        String birthday = dataSnapshot.child("birthday").getValue().toString();

                        if (birthday.length() > 0) {
                            etBirthday.setText(birthday);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
