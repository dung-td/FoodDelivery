package com.example.fooddelivery.activity.login;

import android.app.ProgressDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.binaryfork.spanny.Spanny;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class SignUpActivity_2 extends AppCompatActivity {
    EditText et_code_1, et_code_2, et_code_3, et_code_4, et_code_5, et_code_6;
    Button bt_finish, bt_resend;
    ImageView bt_back;
    TextView tv_sms;
    ProgressDialog progressDialog;

    User userInfo;
    String uid;
    String codeByUser;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocal();
        setContentView(R.layout.activity_sign_up_2);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("SMS", "complete");
                progressDialog.dismiss();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("SMS", e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressDialog.dismiss();
                super.onCodeSent(s, forceResendingToken);
                Log.e("SMS", "code sent");
                verificationId = s;
                token = forceResendingToken;
            }
        };


        Init();

        sendVerificationCode(userInfo.getPhone_Number());

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity_2.super.onBackPressed();
            }
        });

        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage(getString(R.string.data_loading));
                progressDialog.show();
                codeByUser = et_code_1.getText().toString() +
                        et_code_2.getText().toString() +
                        et_code_3.getText().toString() +
                        et_code_4.getText().toString() +
                        et_code_5.getText().toString() +
                        et_code_6.getText().toString();
                if (codeByUser.isEmpty() || codeByUser.length() < 6) {
                    et_code_6.setError(getString(R.string.wrong_otp));
                    et_code_1.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                verifyCode(codeByUser);
            }
        });

        bt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVeritificationCode(userInfo.getPhone_Number());
            }
        });
    }

    private void Init() {
        bt_resend = findViewById(R.id.su2_bt_send);
        bt_finish = findViewById(R.id.su2_bt_finish);
        bt_back = findViewById(R.id.su2_bt_back);
        et_code_1 = findViewById(R.id.su2_et_code_1);
        et_code_2 = findViewById(R.id.su2_et_code_2);
        et_code_3 = findViewById(R.id.su2_et_code_3);
        et_code_4 = findViewById(R.id.su2_et_code_4);
        et_code_5 = findViewById(R.id.su2_et_code_5);
        et_code_6 = findViewById(R.id.su2_et_code_6);
        tv_sms = findViewById(R.id.su2_tv_smshelper);
        setTextChangedListener();

        userInfo = new User();
        getExtras();
      
        progressDialog = new ProgressDialog(this);

        if (WelcomeActivity.language.equals("vi")) {
            Spanny spanny = new Spanny("Nhập mã xác được được \n gửi đến số ")
                    .append(userInfo.getPhone_Number(), new StyleSpan(Typeface.BOLD_ITALIC));
            tv_sms.setText(spanny);
        } else {
            Spanny spanny = new Spanny("Please enter the verify code \n sent to ")
                    .append(userInfo.getPhone_Number(), new StyleSpan(Typeface.BOLD_ITALIC));
            tv_sms.setText(spanny);
        }

        mAuth = FirebaseAuth.getInstance();
    }

    private void getExtras() {
        Intent i = getIntent();
        userInfo.setFirst_Name(i.getStringExtra("firstname"));
        userInfo.setLast_Name(i.getStringExtra("lastname"));
        userInfo.setPhone_Number(i.getStringExtra("phone"));
        userInfo.setEmail(i.getStringExtra("email"));
        userInfo.setPassword(i.getStringExtra("password"));
        userInfo.getAddress().setAddressLine(0, i.getStringExtra("address"));
        userInfo.getAddress().setAdminArea(i.getStringExtra("state"));
        userInfo.getAddress().setLocality(i.getStringExtra("city"));
        userInfo.getAddress().setCountryName(i.getStringExtra("country"));
        userInfo.getAddress().setLatitude(i.getDoubleExtra("latitude", 0.0));
        userInfo.getAddress().setLongitude(i.getDoubleExtra("longitude", 0.0));
    }

    private void sendVerificationCode(String phoneNumber) {
        timeLeftInMillis = 60000;
        startStop();
        progressDialog.setMessage(getString(R.string.data_loading));
        progressDialog.show();
        Log.e("Create", "options");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
                        .setTimeout(50L, TimeUnit.SECONDS)// Timeout and unit
                        .setActivity(SignUpActivity_2.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)// OnVerificationStateChangedCallbacks
                        .build();
        try {
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Log.e("SMS", e.getMessage());
        }
    }

    private void resendVeritificationCode(String phoneNumber) {
        timeLeftInMillis = 60000;
        startStop();
        progressDialog.setMessage(getString(R.string.data_loading));
        progressDialog.show();
        Log.e("Create", "options");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
                        .setTimeout(1L, TimeUnit.SECONDS)// Timeout and unit
                        .setActivity(SignUpActivity_2.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
                        .build();
        try {
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Log.e("SMS", e.getMessage());
        }
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addNewUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e("SMS", "signInWithCredential:failure" + e.getMessage());
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toasty.error(SignUpActivity_2.this, getString(R.string.expired_code), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addNewUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.updatePassword(userInfo.getPassword())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updateEmail(userInfo.getEmail())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        user.sendEmailVerification()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        uid = user.getUid();
                                                        WelcomeActivity.firebase.addNewUser(userInfo, uid, new OnGetDataListener() {
                                                            @Override
                                                            public void onStart() {

                                                            }

                                                            @Override
                                                            public void onSuccess() {
                                                                progressDialog.dismiss();
                                                                loginComplete();
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    private void loginComplete() {
        Toasty.success(this, getString(R.string.signup_success)).show();
        WelcomeActivity.userID = mAuth.getCurrentUser().getUid();
        Intent welcome = new Intent(SignUpActivity_2.this, WelcomeActivity.class);
        startActivity(welcome);
        finish();
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
                bt_resend.setEnabled(true);
                bt_resend.setText(getString(R.string.resend));
                bt_resend.setBackground(getResources().getDrawable(R.drawable.button_background_blue));
            }
        }.start();

        timerRunning = true;
    }

    private void updateTimer() {
        int seconds = (int) timeLeftInMillis / 1000;
        bt_resend.setText(String.format("%s (%d)", getString(R.string.resend), seconds));
        if (timeLeftInMillis > 0) {
            bt_resend.setEnabled(false);
            bt_resend.setBackground(getResources().getDrawable(R.drawable.button_background_grey));
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

    public void setLocal() {
        String langCode;

        if (WelcomeActivity.language.equals("vi"))
            langCode = "vi";
        else {
            langCode = "en";
        }

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}