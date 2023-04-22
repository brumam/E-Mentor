package com.example.e_mentor.Module;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddNewModule extends AppCompatActivity {
    // Declare impostors
    private ImageButton mModuleImage;
    private EditText mModuleName;
    private EditText mModuleDesc;
    private Button mSubmitButton;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_module);

        // Bind impostors
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference("MODULE_PICS");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Modules");
        mModuleImage = findViewById(R.id.imageButton);
        mModuleName = findViewById(R.id.postTitleEt);
        mModuleDesc = findViewById(R.id.descriptionEt);
        mSubmitButton = findViewById(R.id.submitPost);

        // Set on click - get Content images
        mModuleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        // Calling method startPosting
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Posting to our database
                startPosting();
            }
        });
    }

    // Check request code - add data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mModuleImage.setImageURI(mImageUri);
            Picasso.get().load(mImageUri).fit().into(mModuleImage);
        }
    }

    // Start Posting new Module
    private void startPosting() {
        mProgress.setMessage("Adding new module...");
        mProgress.show();

        final String nameVal = mModuleName.getText().toString().trim();
        final String descVal = mModuleDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(nameVal) && !TextUtils.isEmpty(descVal) && mImageUri != null) {
            //start the uploading...

            String name = mDatabase.push().getKey();
            StorageReference reference = mStorage.child(name + "." + getExt(mImageUri));
            reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();

                            DatabaseReference newPost = mDatabase.push();

                            Map<String, String> dataToSave = new HashMap<>();
                            dataToSave.put("name", nameVal);
                            dataToSave.put("description", descVal);
                            dataToSave.put("imageURL", downloadUrl);
                            dataToSave.put("rating", "0"); // Set initial rating to 0

                            newPost.setValue(dataToSave);

                            mProgress.dismiss();


                            startActivity(new Intent(AddNewModule.this, Recycler.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            reference.delete();
                            Toast.makeText(AddNewModule.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                    });
                }
            });

        } else {
            Toast.makeText(AddNewModule.this, "Please try to complete all fields.", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }
    }

    // Content extension resolver
    private String getExt(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

}

