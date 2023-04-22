package com.example.e_mentor.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.Authentication.Login;
import com.example.e_mentor.Authentication.OTP;
import com.example.e_mentor.BuildConfig;
import com.example.e_mentor.HomePage;
import com.example.e_mentor.Module.Recycler;
import com.example.e_mentor.design.CircleTransformation;
import com.example.e_mentor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Profile extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private TextView nameTxtView;
    private TextView emailTxtView, passwordTxtView;
    private ImageView userImageView;
    private final String TAG = this.getClass().getName().toUpperCase();
    private String email;
    private static final String USERS = "user";
    private FirebaseAuth mAuth;
    private FirebaseUser fuser;
    private Uri url;
    private DatabaseReference mDatabase;
    private ImageView onfoff;
    private Button ed_pass;

    private Button signOutButton;


    private TextView mNameTextView, mEmailTextView;
    private ImageView mProfilePictureImageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        mNameTextView = findViewById(R.id.name_textview);
        mEmailTextView = findViewById(R.id.email_imageview);
        mProfilePictureImageView = findViewById(R.id.user_imageview);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        passwordTxtView = findViewById(R.id.password_textview);
        onfoff = findViewById(R.id.onoff);
        ed_pass = findViewById(R.id.ed_password);
        signOutButton = findViewById(R.id.ed_sign_out);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Get current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get reference to current user's data in Firebase Database
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("user").child(userId);



        // Retrieve user's name and email from Firebase Database and set them in the corresponding TextViews
        currentUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();

                    mNameTextView.setText(name);
                    mEmailTextView.setText(email);


                    String capitalizedText = "";
                    String[] words = name.split("\\s+");
                    for (String word : words) {
                        if (word.length() > 0) {
                            capitalizedText += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
                        }
                    }
                    mNameTextView.setText(capitalizedText.trim());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Profile", "Error retrieving user data: " + error.getMessage());
            }
        });

        // Load user's profile picture into ImageView using Picasso
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePictures/" + userId);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).transform(new CircleTransformation()).into(mProfilePictureImageView);
                onfoff.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Profile", "Error loading profile picture: " + e.getMessage());
            }
        });

        ed_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mEmailTextView.getText().toString().trim();
                new SendPasswordResetEmailTask().execute(userEmail);
                intent.putExtra("email", userEmail);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(Profile.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    private String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return Integer.toString(otp);
    }
    private class SendPasswordResetEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String email = strings[0];

            // Generate OTP code
            String otp = generateOtp();
            Intent intent = new Intent(Profile.this, OTP.class);
            intent.putExtra("OTP", otp);
            intent.putExtra("email", email);
            startActivity(intent);

            // SendGrid code (same as in your original ForgotPassword.java)
            Email from = new Email("weloveselfiestore@gmail.com");
            String subject = "Password reset for MyApp";
            Email to = new Email(email);
            Content content = new Content("text/plain", "Your OTP code is: " + otp);
            Mail mail = new Mail(from, subject, to, content);

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            try {
                RequestBody body = RequestBody.create(mediaType, mail.build());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://api.sendgrid.com/v3/mail/send")
                        .addHeader("Authorization", "Bearer " + BuildConfig.SENDGRID_API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                Log.d("SendGrid", "Email sent with status code: " + response.code());
            } catch (IOException ex) {
                Log.e("SendGrid", "Error sending email: " + ex.getMessage());
            }

            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                // Handle the Home menu item click
                Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homeIntent);
                return true;
            case R.id.explore:
                // Handle the Explore menu item click
                Intent profileIntent = new Intent(getApplicationContext(), Recycler.class);
                startActivity(profileIntent);
                return true;
            case R.id.e_mentor:
                // Handle the E-Mentor menu item click
                return true;
            case R.id.learning_hub:
                // Handle the Learning Hub menu item click
                return true;
            case R.id.profile:
                // Handle the Profile menu item click

                return true;
            default:
                return false;
        }
    }
}




