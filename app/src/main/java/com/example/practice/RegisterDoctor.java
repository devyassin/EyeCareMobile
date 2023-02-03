package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practice.Model.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class RegisterDoctor extends Activity implements View.OnClickListener {
    int SELECT_PHOTO = 1;

    private TextView signIn;
    private EditText editTextName, editTextEmail, editTextMobile, editTextVille,editTextHopitale,editTextSpecial, editTextPassword, editTextConPassword;
    private Button signUp;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private Button addImage;

    private Uri imageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
        mAuth = FirebaseAuth.getInstance();



        signUp = (Button) findViewById(R.id.signUpBtn);
        signUp.setOnClickListener(this);

        addImage = (Button) findViewById(R.id.addImage);
        addImage.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("Users");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        editTextName = (EditText) findViewById(R.id.nameDoctor);
        editTextEmail = (EditText) findViewById(R.id.emailDoctor);
        editTextMobile = (EditText) findViewById(R.id.mobileDoctor);
        editTextVille = (EditText) findViewById(R.id.villeDoctor);
        editTextHopitale = (EditText) findViewById(R.id.HopitalDoctor);
        editTextSpecial = (EditText) findViewById(R.id.DomainDoctor);
        editTextPassword = (EditText) findViewById(R.id.passwordDoctor);
        editTextConPassword = (EditText) findViewById(R.id.conPasswordDoctor);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.signUpBtn:
                uploadFile();
                break;
            case R.id.addImage:
                pickupImage();
                break;
        }
    }

    private void pickupImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();

            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Toast.makeText(RegisterDoctor.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    String name = editTextName.getText().toString().trim();
                                    String email = editTextEmail.getText().toString().trim();
                                    String mobile = editTextMobile.getText().toString().trim();
                                    String ville = editTextVille.getText().toString().trim();
                                    String hopitale = editTextHopitale.getText().toString().trim();
                                    String specialite = editTextSpecial.getText().toString().trim();
                                    String password = editTextPassword.getText().toString().trim();
                                    String conPassword = editTextConPassword.getText().toString().trim();

                                    if (name.isEmpty()) {
                                        editTextName.setError("Name is required!");
                                        editTextName.requestFocus();
                                        return;
                                    }

                                    if (ville.isEmpty()) {
                                        editTextVille.setError("Ville is required!");
                                        editTextVille.requestFocus();
                                        return;
                                    }

                                    if (hopitale.isEmpty()) {
                                        editTextHopitale.setError("Hopitale is required!");
                                        editTextHopitale.requestFocus();
                                        return;
                                    }

                                    if (specialite.isEmpty()) {
                                        editTextSpecial.setError("Specialite is required!");
                                        editTextSpecial.requestFocus();
                                        return;
                                    }
                                    if (email.isEmpty()) {
                                        editTextEmail.setError("Email is required!");
                                        editTextEmail.requestFocus();
                                        return;
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        editTextEmail.setError("Please provide valid email!");
                                        editTextEmail.requestFocus();
                                        return;
                                    }
                                    if (mobile.isEmpty()) {
                                        editTextMobile.setError("Mobile is required!");
                                        editTextMobile.requestFocus();
                                        return;
                                    }

                                    if (password.isEmpty()) {
                                        editTextPassword.setError("Password is required!");
                                        editTextPassword.requestFocus();
                                        return;
                                    }
                                    if (conPassword.isEmpty()) {
                                        editTextConPassword.setError("Confirm password is required!");
                                        editTextConPassword.requestFocus();
                                        return;
                                    }

                                    if (password.length() < 6) {
                                        editTextPassword.setError("Min password length is 6 characters!");
                                        editTextPassword.requestFocus();
                                        return;
                                    }

                                    if (!password.equals(conPassword)) {
                                        Toast.makeText(RegisterDoctor.this, "Password are not matching", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Doctor doctor = new Doctor(name, email, mobile,ville,hopitale,specialite,password, url);

                                                            FirebaseDatabase.getInstance().getReference("Users")
                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .setValue(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(RegisterDoctor.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                                                                progressBar.setVisibility(View.GONE);
                                                                                 /*startActivity(new Intent(RegisterUser.this, Login.class));
                                                                                finish(); */

                                                                                //redirect to login layout!
                                                                            } else {
                                                                                Toast.makeText(RegisterDoctor.this, "Failed to register! try again!", Toast.LENGTH_SHORT).show();
                                                                                progressBar.setVisibility(View.GONE);
                                                                            }
                                                                        }
                                                                    });
                                                        } else {
                                                            Toast.makeText(RegisterDoctor.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterDoctor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}