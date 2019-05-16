package com.alpheus.naturonik.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alpheus.naturonik.Fragments.Main;
import com.alpheus.naturonik.Models.Product;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etDescription, etAbout, etCountry, etSort, etPrice,
            etEnergy1, etEnergy2,
            etNutritional1, etNutritional2, etNutritional3;
    private Button buttonChooseImage, buttonUpload;
    private TextView tvShowUploads;
    private ImageView imageView;

    private Uri imageUri;

    private StorageReference mStorage;
    private DatabaseReference mData;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

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

        buttonChooseImage = findViewById(R.id.button_choose_image);
        buttonUpload = findViewById(R.id.button_upload);

        tvShowUploads = findViewById(R.id.text_view_show_uploads);
        imageView = findViewById(R.id.image_view);

        mStorage = FirebaseStorage.getInstance().getReference();
        mData = FirebaseDatabase.getInstance().getReference("products");

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(AddProductActivity.this, "Загрузка...", Toast.LENGTH_SHORT).show();
                } else{
                    uploadFile();
                }
            }
        });

        tvShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddProductActivity.this, AdminActivity.class);
                startActivity(intent);
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

            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    private void uploadFile() {

        final String productId = mData.push().getKey();

        final StorageReference imageRef = mStorage.child("images/" + productId);

        if (imageUri != null && etDescription.getText().length() != 0
                && etAbout.getText().length() != 0 && etCountry.getText().length() != 0
                && etSort.getText().length() != 0 && etPrice.getText().length() != 0
                && etEnergy1.getText().length() != 0 && etEnergy2.getText().length() != 0
                && etNutritional1.getText().length() != 0 && etNutritional2.getText().length() != 0
                && etNutritional3.getText().length() != 0)
        {
            imageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
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

                        mData.child(productId).setValue(product);

                        Toast.makeText(AddProductActivity.this, "Загрузка успешна", Toast.LENGTH_LONG).show();
                    } else
                    {
                        Toast.makeText(AddProductActivity.this, "Ошибка загрузки: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Файл не выбран/не все поля заполнены", Toast.LENGTH_SHORT).show();
        }

    }

}
