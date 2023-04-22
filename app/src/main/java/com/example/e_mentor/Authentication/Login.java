package com.example.e_mentor.Authentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.App.AppClass;
import com.example.e_mentor.Helpers.User;
import com.example.e_mentor.HomePage;
import com.example.e_mentor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


// Add necessary imports

public class Login extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton;
    private TextView signupButton,forgotPw;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference firebaseDatabase;

    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("user");
        email = findViewById(R.id.log_input_email);
        password = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.button);
        signupButton = findViewById(R.id.sign_up_btn);
        forgotPw = findViewById(R.id.textView9);
        loadingSpinner = findViewById(R.id.loading_spinner);

        int LOADING_SPINNER_DISPLAY_LENGTH = 3000; // 3000 milliseconds = 3 seconds




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
            }
        };

        forgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingSpinner.setVisibility(View.VISIBLE); // Show the loading spinner

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingSpinner.setVisibility(View.GONE); // Hide the loading spinner after 2 seconds
                    }
                }, LOADING_SPINNER_DISPLAY_LENGTH);

                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (userEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();

                } else {
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("user").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dss : snapshot.getChildren()) {
                                            if (dss.getKey().compareTo(mAuth.getUid()) == 0) {
                                                AppClass.Session.user = dss.getValue(User.class);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                try {
                                    Thread.sleep(3000);

                                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, HomePage.class));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                password.setError("Incorrect password");
                                password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.pwerror, 0);
                                password.setTextColor(Color.RED);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        password.setError(null);
                                        password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.confirm, 0);
                                        password.setTextColor(Color.BLACK);
                                    }
                                }, 3000);
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // Checks if the user is already signed in, and if so, updates the UI and finishes the activity
    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
        if (currentUser != null) {
            updateUI(currentUser);
            finish(); // Add this line to close the Login activity after navigating to Profile
        }
    }

    // Navigates to the HomePage activity with the current user's email
    public void updateUI(FirebaseUser currentUser) {
        Intent homePageIntent = new Intent(getApplicationContext(), HomePage.class);
        homePageIntent.putExtra("email", currentUser.getEmail());
        Log.v("DATA", currentUser.getUid());
        startActivity(homePageIntent);
    }

}
