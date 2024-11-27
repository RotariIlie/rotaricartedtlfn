package com.example.rotaricartedtlfn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class AddEditContactActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 2;

    private EditText nameEditText, phoneEditText, emailEditText;
    private Button saveButton, uploadImageButton;
    private ImageView contactImageView;
    private byte[] contactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        saveButton = findViewById(R.id.saveButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        contactImageView = findViewById(R.id.contactImageView);

        // Eveniment pentru încărcarea imaginii din galerie
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
            }
        });

        // Salvarea contactului în baza de date
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(AddEditContactActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    // Salvare contact în baza de date
                    DatabaseHelper db = new DatabaseHelper(AddEditContactActivity.this);
                    boolean isInserted = db.insertContact(name, phone, email, contactImage);
                    if (isInserted) {
                        Toast.makeText(AddEditContactActivity.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Închide activitatea
                    } else {
                        Toast.makeText(AddEditContactActivity.this, "Error adding contact", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Tratarea rezultatului din galeria de imagini
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            try {
                // Obține imaginea din galeria dispozitivului
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                contactImageView.setImageBitmap(selectedImage);

                // Convertirea imaginii într-un byte[] pentru a fi stocată în baza de date
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                contactImage = stream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show();
        }
    }
}