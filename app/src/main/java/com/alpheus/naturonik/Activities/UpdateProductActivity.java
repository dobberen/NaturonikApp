package com.alpheus.naturonik.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Models.Product;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UpdateProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView tvDescription, tvAbout, tvCountry, tvSort, tvPrice, tvEnergy, tvNutritional;
    private ImageView imageViewFrom, imageViewTo;
    private EditText etDescription, etAbout, etCountry, etSort, etPrice,
            etEnergy1, etEnergy2,
            etNutritional1, etNutritional2, etNutritional3;
    private Button buttonChooseImage, buttonUpload, buttonDelete;
    private TextView tvShowUploads;

    private String receiverProductID;

    private Uri imageUri;
    private StorageTask mUploadTask;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        receiverProductID = getIntent().getExtras().get("itemPosition").toString();

        tvDescription = findViewById(R.id.tv_description);
        tvAbout = findViewById(R.id.tv_about);
        tvCountry = findViewById(R.id.tv_country);
        tvSort = findViewById(R.id.tv_sort);
        tvPrice = findViewById(R.id.tv_price);
        tvEnergy = findViewById(R.id.tv_energy);
        tvNutritional = findViewById(R.id.tv_nutritional);
        imageViewFrom = findViewById(R.id.image_view_from);

        etDescription = findViewById(R.id.et_description);
        etAbout = findViewById(R.id.et_about);
        etCountry = findViewById(R.id.et_country);
        etSort = findViewById(R.id.et_sort);
        etPrice = findViewById(R.id.et_price);
        etEnergy1 = findViewById(R.id.et_energy1);
        etEnergy2 = findViewById(R.id.et_energy2);
        etNutritional1 = findViewById(R.id.et_nutritional1);
        etNutritional2 = findViewById(R.id.et_nutritional2);
        etNutritional3 = findViewById(R.id.et_nutritional3);
        imageViewTo = findViewById(R.id.image_view_to);

        buttonChooseImage = findViewById(R.id.button_choose_image);
        buttonUpload = findViewById(R.id.button_upload);
        buttonDelete = findViewById(R.id.button_delete);
        tvShowUploads = findViewById(R.id.text_view_show_uploads);

        retriveProductInfo();


        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UpdateProductActivity.this, "Загрузка...", Toast.LENGTH_SHORT).show();
                } else {
                    updateProductInfo();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StorageReference photoRef = mStorage.child("images/" + receiverProductID);

                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateProductActivity.this, "Файл успешно удален", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UpdateProductActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                mDatabase.child("products").child(receiverProductID).setValue(null);
            }
        });

        tvShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UpdateProductActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateProductInfo() {

        final StorageReference imageRef = mStorage.child("images/"+receiverProductID);

        if (imageUri != null && etDescription.getText().length() != 0
                && etAbout.getText().length() != 0 && etCountry.getText().length() != 0
                && etSort.getText().length() != 0 && etPrice.getText().length() != 0
                && etEnergy1.getText().length() != 0 && etEnergy2.getText().length() != 0
                && etNutritional1.getText().length() != 0 && etNutritional2.getText().length() != 0
                && etNutritional3.getText().length() != 0) {
            imageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        String energy = etEnergy1.getText().toString()
                                + " Ккал/" + etEnergy2.getText().toString() + "кДж";

                        String nutritional = "Белки - " + etNutritional1.getText().toString()
                                + "г., Углеводы - " + etNutritional2.getText().toString()
                                + "г., Жиры - " + etNutritional3.getText().toString() + "г.";

                        Uri downloadUri = task.getResult();

                        Product product = new Product(
                                etCountry.getText().toString().trim(),
                                etSort.getText().toString().trim(),
                                downloadUri.toString(),
                                etDescription.getText().toString().trim(),
                                etPrice.getText().toString().trim(),
                                etAbout.getText().toString().trim(),
                                energy,
                                nutritional);

                        mDatabase.child("products").child(receiverProductID).setValue(product);

                        Toast.makeText(UpdateProductActivity.this, "Загрузка успешна", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UpdateProductActivity.this, "Ошибка загрузки: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Файл не выбран/не все поля заполнены", Toast.LENGTH_SHORT).show();
        }

    }

    private void retriveProductInfo() {

        mDatabase.child("products").child(receiverProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists())) {

                    String price = dataSnapshot.child("price").getValue().toString();
                    String country_id = dataSnapshot.child("countrys_name").getValue().toString();
                    String sort_id = dataSnapshot.child("sorts_name").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    String about = dataSnapshot.child("about").getValue().toString();
                    String energy_value = dataSnapshot.child("energy_value").getValue().toString();
                    String nutritional_value = dataSnapshot.child("nutritional_value").getValue().toString();
                    String img = dataSnapshot.child("img").getValue().toString();


                    tvCountry.setText(country_id);
                    tvSort.setText("Сорт: " + sort_id);
                    tvDescription.setText(description);
                    tvPrice.setText(price);
                    tvAbout.setText(about);
                    tvEnergy.setText(energy_value);
                    tvNutritional.setText(nutritional_value);
                    Glide.with(getApplication()).load(img).centerCrop().into(imageViewFrom);

                    etCountry.setText(country_id);
                    etSort.setText(sort_id);
                    etDescription.setText(description);
                    etPrice.setText(price);
                    etAbout.setText(about);
                    etEnergy1.setText("0");
                    etEnergy2.setText("0");
                    etNutritional1.setText("0");
                    etNutritional2.setText("0");
                    etNutritional3.setText("0");
                    Glide.with(getApplication()).load(img).centerCrop().into(imageViewFrom);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            Glide.with(this).load(imageUri).into(imageViewTo);
        }
    }

    public void deleteImage(){

    }

}
