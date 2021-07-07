package com.example.fooddelivery.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.binaryfork.spanny.Spanny;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class VerifyPhoneActivity extends AppCompatActivity {
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;

    EditText et_code_1, et_code_2, et_code_3, et_code_4, et_code_5, et_code_6;
    Button bt_finish, bt_resend;
    TextView tv_sms;

    String verificationId;
    String phoneNumber;
    String codeByUser;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    ProgressDialog progressDialog = new ProgressDialog(this);
    FirebaseAuth mAuth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            progressDialog.dismiss();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCodeAndUpdate(code);
                progressDialog.dismiss();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toasty.error(VerifyPhoneActivity.this, getString(R.string.error_happend_try_again)).show();
            Log.e("SMS", e.getMessage());
            progressDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        startSmartUserConsent();

        Init();

        sendVerificationCode(phoneNumber);

        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeByUser = et_code_1.getText().toString() +
                        et_code_2.getText().toString() +
                        et_code_3.getText().toString() +
                        et_code_4.getText().toString() +
                        et_code_5.getText().toString() +
                        et_code_6.getText().toString();
                if (codeByUser.isEmpty() || codeByUser.length() < 6) {
                    et_code_6.setError("Sai mật mã OTP");
                    et_code_1.requestFocus();
                    return;
                }

                verifyCodeAndUpdate(codeByUser);
            }
        });

        bt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phoneNumber);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        progressDialog.setMessage(getString(R.string.data_loading));
        progressDialog.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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

    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT){

            if ((resultCode == RESULT_OK) && (data != null)){

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);


            }
        }
    }

    private void getOtpFromMessage(String message) {
        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){
            Toasty.success(this, "Thanh Cong").show();
        }
    }

    private void registerBroadcastReceiver(){

        smsBroadcastReceiver = new SmsBroadcastReceiver();

        smsBroadcastReceiver.listener = new SmsBroadcastReceiver.SmsReceiverListener() {
            @Override
            public void OnSuccess(Intent intent) {
                startActivityForResult(intent,REQ_USER_CONSENT);
            }

            @Override
            public void OnFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
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

    void updatePhone() {
        FirebaseFirestore root = FirebaseFirestore.getInstance();

        Log.e("Update phone", phoneNumber.toString());
        root.collection("User").document(userID)
                .update("phone_Number", phoneNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), getString(R.string.update_infodata_done), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void verifyCodeAndUpdate(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        updateAuthPhone(credential);
    }

    private void updateAuthPhone(PhoneAuthCredential credential) {
        user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    et_code_1.setText("");
                    et_code_2.setText("");
                    et_code_3.setText("");
                    et_code_4.setText("");
                    et_code_5.setText("");
                    et_code_6.setText("");
                    updatePhone();
                    onBackPressed();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.verify_failed), Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Send email failed", e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), getString(R.string.verify_failed), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void Init() {
        bt_resend = findViewById(R.id.phone_bt_send);
        bt_finish = findViewById(R.id.phone_bt_finish);

        et_code_1 = findViewById(R.id.phone_et_code_1);
        et_code_2 = findViewById(R.id.phone_et_code_2);
        et_code_3 = findViewById(R.id.phone_et_code_3);
        et_code_4 = findViewById(R.id.phone_et_code_4);
        et_code_5 = findViewById(R.id.phone_et_code_5);
        et_code_6 = findViewById(R.id.phone_et_code_6);
        tv_sms = findViewById(R.id.phone_tv_smshelper);
        setTextChangedListener();

        phoneNumber = getPhoneExtra();


        Spanny spanny = new Spanny(getString(R.string.verfiedcode) + "\n" + getString(R.string.is_sent))
                .append(phoneNumber, new StyleSpan(Typeface.BOLD_ITALIC));
        tv_sms.setText(spanny);

        mAuth = FirebaseAuth.getInstance();
    }

    private String getPhoneExtra() {
        return getIntent().getStringExtra("phone").toString();
    }

}