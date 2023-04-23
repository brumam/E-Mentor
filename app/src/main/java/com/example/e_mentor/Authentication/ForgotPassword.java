package com.example.e_mentor.Authentication;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";
    private EditText mEmailField;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.email_field);
        Button sendMailButton = findViewById(R.id.send_mail_button);
        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmailField.setError("Email is required.");
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Password reset email sent.");
                                    Toast.makeText(ForgotPassword.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                    // Navigate user back to login page
                                    // Create an Intent to go to Login activity
                                    Intent intent = new Intent(ForgotPassword.this, Login.class);
                                    // Add a flag to clear the activity stack and start a new task
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    // Start the Login activity
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.e(TAG, "Error sending password reset email.", task.getException());
                                    Toast.makeText(ForgotPassword.this, "Error sending password reset email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


}


