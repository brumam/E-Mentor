package com.example.e_mentor.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.Authentication.Login;
import com.example.e_mentor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



public class Profile extends AppCompatActivity {
    private TextView nameTxtView, lastTxtView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        nameTxtView = findViewById(R.id.name_textview);
        lastTxtView = findViewById(R.id.last_name_textview);
        emailTxtView = findViewById(R.id.email_imageview);
        passwordTxtView = findViewById(R.id.password_textview);
        userImageView = findViewById(R.id.user_imageview);
        onfoff = findViewById(R.id.onoff);
        ed_pass = findViewById(R.id.ed_password);
        signOutButton = findViewById(R.id.ed_sign_out);

        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("user");



        mDatabase.addValueEventListener(new ValueEventListener() {
            String fname, lname, password, imageUrl;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        fname = keyId.child("firstName").getValue(String.class);
                        lname = keyId.child("lastName").getValue(String.class);
                        password = keyId.child("password").getValue(String.class);
                        imageUrl = keyId.child("imageUrl").getValue(String.class);

                        break;
                    }
                }
                nameTxtView.setText(fname);
                lastTxtView.setText(lname);
                emailTxtView.setText(email);
                passwordTxtView.setText(password);
                Picasso.get().load(imageUrl).fit().into(userImageView);
                onfoff.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        ed_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
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

}

