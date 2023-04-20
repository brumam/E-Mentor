package com.example.e_mentor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.Profile.Profile;
import com.example.e_mentor.design.CircleTransformation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;




    public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

        private BottomNavigationView bottomNavigationView;
        private ImageView mProfilePictureImageView;
        private TextView mUserNameTextView;

        private FirebaseAuth mAuth;
        private DatabaseReference mDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);

            bottomNavigationView = findViewById(R.id.bottom_nav_bar);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            // Set the default selected menu item
            bottomNavigationView.setSelectedItemId(R.id.home);


            mProfilePictureImageView = findViewById(R.id.profile_picture);
            mUserNameTextView = findViewById(R.id.user_name);

            mAuth = FirebaseAuth.getInstance();
            String userId = mAuth.getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("user").child(userId);


            // Example hardcoded text
            // Retrieve user's name from Firebase Database and set it in the corresponding TextView with capitalized words





            // Retrieve user's name and profile picture from Firebase Database and set them in the corresponding Views
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue().toString();
                        mUserNameTextView.setText(name);

                        String capitalizedText = "";
                        String[] words = name.split("\\s+");
                        for (String word : words) {
                            if (word.length() > 0) {
                                capitalizedText += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
                            }
                        }
                        mUserNameTextView.setText(capitalizedText.trim());



                        // Load user's profile picture into ImageView using Picasso
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePictures/" + userId);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).transform(new CircleTransformation()).into(mProfilePictureImageView);

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("HomePage", "Error retrieving user data: " + error.getMessage());
                }
            });
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    // Handle the Home menu item click
                    return true;
                case R.id.explore:
                    // Handle the Explore menu item click
                    return true;
                case R.id.e_mentor:
                    // Handle the E-Mentor menu item click
                    return true;
                case R.id.learning_hub:
                    // Handle the Learning Hub menu item click
                    return true;
                case R.id.profile:
                    // Handle the Profile menu item click
                    Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(profileIntent);
                    return true;
                default:
                    return false;
            }
        }
    }
