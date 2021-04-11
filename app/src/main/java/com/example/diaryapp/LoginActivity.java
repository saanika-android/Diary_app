package com.example.diaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;
    EditText phoneNumber, codeEnter;
    Button nextbtn;
    ProgressBar progressBar;
    TextView state;

    String verification;
    PhoneAuthProvider.ForceResendingToken Token;
    Boolean verificationInProgress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        phoneNumber = findViewById(R.id.phone);
        codeEnter = findViewById(R.id.codeEnter);
        progressBar = findViewById(R.id.progressBar);
        nextbtn = findViewById(R.id.nextBtn);
        state = findViewById(R.id.state);
        //codePicker= findViewById(R.id.ccp);*/
        try {

            nextbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!verificationInProgress) {
                        if (!phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() == 10) {
                            String phoneNum = "+91" + phoneNumber.getText().toString();
                            Log.d(TAG, "OnClick: Phone NO ->" + phoneNum);
                            progressBar.setVisibility(View.VISIBLE);
                            state.setText("Sending OTP");
                            state.setVisibility(View.VISIBLE);
                            requestOTP(phoneNum);

                        } else {
                            phoneNumber.setError("no valid");
                        }
                    } else {
                        String userOtp = codeEnter.getText().toString();
                        if (!userOtp.isEmpty() && userOtp.length() == 6) {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, userOtp);
                            VerifyAuth(credential);


                        } else {
                            codeEnter.setError("valid otp is required");
                        }
                    }
                }
            });

        }
        catch (NullPointerException ignored){

        }
    }

  /*  @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null){
            progressBar.setVisibility(View.VISIBLE);
            state.setText("Checking..");
            state.setVisibility(View.VISIBLE);
        }
    }*/

    private void VerifyAuth(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed Authentication", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);

                state.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);

                verification = s;
                Token = forceResendingToken;
                nextbtn.setText("vERIFY");

                verificationInProgress = true;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

               VerifyAuth(phoneAuthCredential);
               nextbtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent i = new Intent(LoginActivity.this, MainActivity.class);
                       startActivity(i);
                   }
               });




            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, "Cannot create account" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}



