package com.example.fooddelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.binaryfork.spanny.Spanny;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.ForgotPassActitivy_5;
import com.example.fooddelivery.activity.login.ForgotPassActivity_2;
import com.example.fooddelivery.activity.login.ForgotPassActivity_3;
import com.example.fooddelivery.activity.login.SignUpActivity_2;
import com.example.fooddelivery.model.User;
import com.example.fooddelivery.model.modifiedFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    EditText et_code_1, et_code_2, et_code_3, et_code_4, et_code_5, et_code_6;
    Button bt_finish, bt_resend;
    TextView tv_sms;

    String verificationId;
    String phoneNumber;
    String codeByUser;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    FirebaseAuth mAuth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCodeAndUpdate(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

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
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
                        .setTimeout(1L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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