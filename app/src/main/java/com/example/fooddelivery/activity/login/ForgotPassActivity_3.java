package com.example.fooddelivery.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.binaryfork.spanny.Spanny;
import com.example.fooddelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class ForgotPassActivity_3 extends AppCompatActivity {
    private static final String TAG = "SMS";

    Button bt_back, bt_continue;
    EditText et_code_1, et_code_2, et_code_3, et_code_4, et_code_5, et_code_6;
    TextView tv_resend, tv_status;

    String phoneNumber;
    String codeByUser;
    String verificationId;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken token;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            token = forceResendingToken;
            Log.d(TAG, "CODE SEND");
            progressDialog.dismiss();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "complete");
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toasty.error(ForgotPassActivity_3.this, getString(R.string.error_happend_try_again)).show();
            Log.e("SMS", e.getMessage());
            progressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_3);

        Init();

        progressDialog = new ProgressDialog(this);
        sendVerificationCode(phoneNumber);
    }

    private void Init() {
        et_code_1 = findViewById(R.id.et_code_1);
        et_code_2 = findViewById(R.id.et_code_2);
        et_code_3 = findViewById(R.id.et_code_3);
        et_code_4 = findViewById(R.id.et_code_4);
        et_code_5 = findViewById(R.id.et_code_5);
        et_code_6 = findViewById(R.id.et_code_6);
        bt_continue = findViewById(R.id.fp3_bt_continue);
        bt_back = findViewById(R.id.fp3_bt_back);
        tv_resend = findViewById(R.id.fp3_tv_resend);
        tv_status = findViewById(R.id.fp3_provide_otp_help);
        setTextChangedListener();

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phone");

        Log.e(TAG, "CREATE OPTIONS");

        Spanny spanny = new Spanny("Nhập mã xác thực được \n gửi đến số ")
                .append(phoneNumber, new StyleSpan(Typeface.BOLD_ITALIC));
        tv_status.setText(spanny);

        mAuth = FirebaseAuth.getInstance();

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeByUser = et_code_1.getText().toString() +
                        et_code_2.getText().toString() +
                        et_code_3.getText().toString() +
                        et_code_4.getText().toString() +
                        et_code_5.getText().toString() +
                        et_code_6.getText().toString();
                if (codeByUser.isEmpty() || codeByUser.length() < 6) {
                    et_code_1.setError("Sai mật mã OTP");
                    et_code_1.requestFocus();
                    return;
                }

                verifyCode(codeByUser);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPassActivity_3.super.onBackPressed();
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phoneNumber);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        progressDialog.setMessage(getString(R.string.data_loading));
        progressDialog.show();
        timeLeftInMillis = 60000;
        startStop();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
                        .setTimeout(50L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        Log.d(TAG, "CREATE OPTIONS");
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ForgotPassActivity_3.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            et_code_1.setText("");
                            et_code_2.setText("");
                            et_code_3.setText("");
                            et_code_4.setText("");
                            et_code_5.setText("");
                            et_code_6.setText("");
                            continueFP4();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toasty.error(ForgotPassActivity_3.this, getString(R.string.expired_code), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void continueFP4() {
        Intent forgotPass4 = new Intent(ForgotPassActivity_3.this, ForgotPassActivity_4.class);
        startActivity(forgotPass4);
    }

    long timeLeftInMillis = 60000;
    boolean timerRunning = false;
    CountDownTimer countDownTimer;

    private void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                tv_resend.setEnabled(true);
                tv_resend.setText(getString(R.string.resend));
                tv_resend.setTextColor(getResources().getColor(R.color.primaryColor));
            }
        }.start();

        timerRunning = true;
    }

    private void updateTimer() {
        int seconds = (int) timeLeftInMillis / 1000;
        tv_resend.setText(String.format("%s (%d)", getString(R.string.resend), seconds));
        if (timeLeftInMillis > 0) {
            tv_resend.setEnabled(false);
            tv_resend.setTextColor(getResources().getColor(R.color.light_grey));
        }
        if (timeLeftInMillis <= 0)
            countDownTimer.onFinish();
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    private void setTextChangedListener() {
        et_code_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() == 1)
                    et_code_2.requestFocus();
            }
        });

        et_code_2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() == 1)
                    et_code_3.requestFocus();
                else
                    et_code_1.requestFocus();
            }
        });

        et_code_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() == 1)
                    et_code_4.requestFocus();
                else
                    et_code_2.requestFocus();
            }
        });

        et_code_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() == 1)
                    et_code_5.requestFocus();
                else
                    et_code_3.requestFocus();
            }
        });

        et_code_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() == 1)
                    et_code_6.requestFocus();
                else
                    et_code_4.requestFocus();
            }
        });

        et_code_6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() == 1)
                    ;
                else
                    et_code_5.requestFocus();
            }
        });

    }

}