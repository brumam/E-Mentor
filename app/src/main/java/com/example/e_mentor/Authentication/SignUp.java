package com.example.e_mentor.Authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.HomePage;
import com.example.e_mentor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SignUp extends AppCompatActivity {

    private EditText mNameInput, mEmailInput, mPasswordInput, mConfirmPasswordInput;
    private Button mSignUpButton;
    private TextView mLoginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView profilePicture;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // BIND / INITIATE VARIABLES
        mNameInput = findViewById(R.id.name_input);
        mEmailInput = findViewById(R.id.email_input);
        mPasswordInput = findViewById(R.id.password_input);
        mConfirmPasswordInput = findViewById(R.id.confirm_password_input);
        mSignUpButton = findViewById(R.id.sign_up_button);
        mLoginButton = findViewById(R.id.login_button);
        profilePicture = findViewById(R.id.reg_userimg);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("user");

        // ...

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(profilePicture);

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Login activity
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            profilePicture.setImageURI(mImageUri);
        }
    }

    private void registerUser(ImageView profilePicture) {
        String name = mNameInput.getText().toString().trim();
        String email = mEmailInput.getText().toString().trim();
        String password = mPasswordInput.getText().toString().trim();
        String confirmPassword = mConfirmPasswordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = mDatabase.child(userId);

                            currentUserDb.child("name").setValue(name);
                            currentUserDb.child("email").setValue(email);
                            currentUserDb.child("userType").setValue("student");

                            // Upload profile picture to Firebase Storage
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePictures/" + userId);
                            profilePicture.setDrawingCacheEnabled(true);
                            profilePicture.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = storageRef.putBytes(data);
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(intent);
                            finish(); // Call finish to prevent user from going back to the Signup activity with the back button
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(SignUp.this, "Failed to upload profile picture.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> downloadUrlTask = storageRef.getDownloadUrl();
                                    downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            currentUserDb.child("profilePictureUrl").setValue(imageUrl);
                                            Toast.makeText(SignUp.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            storageRef.delete();
                                        }
                                    });

                                }
                            });

                        } else {
                            // User registration failed
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // Check if the failure was due to email already being registered
                                Toast.makeText(SignUp.this, "Your email is already registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Some other type of registration failure occurred
                                Toast.makeText(SignUp.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }}
