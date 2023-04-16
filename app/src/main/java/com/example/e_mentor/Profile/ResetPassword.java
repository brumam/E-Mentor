package com.example.e_mentor.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.Authentication.ChangePasswordSuccessfulActivity;
import com.example.e_mentor.R;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText newPasswordEditText, confirmPasswordEditText;
    private Button resetPasswordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        newPasswordEditText = findViewById(R.id.new_pw);
        confirmPasswordEditText = findViewById(R.id.confirm_pw);
        resetPasswordButton = findViewById(R.id.sign_up_button);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(ResetPassword.this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ResetPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Password has been updated", Toast.LENGTH_SHORT).show();

                             startActivity(new Intent(ResetPassword.this, ChangePasswordSuccessfulActivity.class));
                             finish();
                        } else {
                            Toast.makeText(ResetPassword.this, "Error updating password", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(ResetPassword.this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }


}
