package com.example.e_mentor.Authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.BuildConfig;
import com.example.e_mentor.Profile.ResetPassword;
import com.example.e_mentor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OTP extends AppCompatActivity {
    private String TAG = OTP.class.getSimpleName();

    private TextView textView,resendCodeButton;
    private EditText editText1, editText2, editText3, editText4;
    private Button verifyButton;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private String email, otp;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        otp = intent.getStringExtra("OTP");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        textView = findViewById(R.id.check_email);
        editText1 = findViewById(R.id.otp_char_1);
        editText2 = findViewById(R.id.otp_char_2);
        editText3 = findViewById(R.id.otp_char_3);
        editText4 = findViewById(R.id.otp_char_4);


        setupEditText(editText1, editText2);
        setupEditText(editText2, editText3);
        setupEditText(editText3, editText4);
        setupLastEditText(editText4);



        verifyButton = findViewById(R.id.continue_button);
        resendCodeButton = findViewById(R.id.resend);

        resendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel the current CountDownTimer
                countDownTimer.cancel();

                // Start a new CountDownTimer
                countDownTimer.start();

                // Generate new OTP code
                otp = generateOtp();

                // Send new OTP code to user's email
                new SendPasswordResetEmailTask().execute(email);
            }
        });


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeEntered = editText1.getText().toString() + editText2.getText().toString()
                        + editText3.getText().toString() + editText4.getText().toString();

                if (codeEntered.equals(otp)) {
                    // Code is correct, allow user to reset password
                    Intent intent = new Intent(OTP.this, ResetPassword.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                    Toast.makeText(OTP.this, "Thank you for confirming OTP code.", Toast.LENGTH_SHORT).show();
                } else {
                    // Code is incorrect, show error message
                    Toast.makeText(OTP.this, "Incorrect code. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                resendCodeButton.setText("Resend OTP in " + secondsRemaining + " seconds");
            }

            public void onFinish() {
                resendCodeButton.setText("Click here to resend OTP");
            }
        };

        countDownTimer.start();



    }
    private void setupEditText(EditText current, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    next.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        current.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (current.getText().toString().isEmpty()) {
                    current.requestFocus();
                    return true;
                }
            }
            return false;
        });
    }

    private void setupLastEditText(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    editText.clearFocus();
                    editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editText.getText().toString().isEmpty()) {
                    editText3.requestFocus();
                    return true;
                }
            }
            return false;
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

            // SendGrid code to send email with new OTP code
            Email from = new Email("weloveselfiestore@gmail.com");
            String subject = "Reset Password OTP code for MyApp";
            Email to = new Email(email);
            Content content = new Content("text/plain", "Your new OTP code is: " + otp);
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
}