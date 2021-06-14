package com.example.fooddelivery.activity.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.example.fooddelivery.activity.main.MainActivity;
import com.example.fooddelivery.R;
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

import java.util.concurrent.TimeUnit;

public class SignUpActivity_2 extends AppCompatActivity {

    private static final String TAG = "PHONE VERIFY";

    EditText et_code_1, et_code_2, et_code_3, et_code_4, et_code_5, et_code_6;
    Button bt_finish, bt_resend;
    ImageView bt_back;
    TextView tv_sms, tv_mail;

    String verificationId;
    User userInfo;
    String uID;
    String codeByUser;

    FirebaseAuth mAuth;
    modifiedFirebase firebase;

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
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_2);

        Init();

        sendVerificationCode(userInfo.getPhone_Number());

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previousSU = new Intent(SignUpActivity_2.this, SignUpActivity_1.class);
                SendInformation(previousSU, userInfo);
                startActivity(previousSU);
            }
        });

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
                verifyCode(codeByUser);
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
        tv_mail = findViewById(R.id.su2_tv_emailhelper);
        setTextChangedListener();

        userInfo = new User();
        userInfo = GetExtras();

        Spanny spanny = new Spanny("Nhập mã xác được được \n gửi đến số ")
                .append(userInfo.getPhone_Number(), new StyleSpan(Typeface.BOLD_ITALIC));
        tv_sms.setText(spanny);

        spanny = new Spanny("Hoặc xác thực bằng mail \n được gửi đến ")
                .append(userInfo.getEmail(), new StyleSpan(Typeface.BOLD_ITALIC));
        tv_mail.setText(spanny);

        firebase = new modifiedFirebase();
        firebase.setObject(userInfo);
        firebase.setCollectionPath("User");

        mAuth = FirebaseAuth.getInstance();
    }

    private User GetExtras() {
        Intent i = getIntent();
        return new User(
                i.getStringExtra("firstname"),
                i.getStringExtra("lastname"),
                i.getStringExtra("phone"),
                i.getStringExtra("email"),
                i.getStringExtra("address"),
                i.getStringExtra("password"));
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpActivity_2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            uID = user.getUid();
                            if (!firebase.checkUID(uID))
                                addNewUser(user);
                            loginComplete(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUpActivity_2.this, "Mã đã hết hạn", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void addNewUser(FirebaseUser user) {
        user.updatePassword(userInfo.getPassword());
        user.updateEmail(userInfo.getEmail());
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        firebase.setObject(userInfo);
        firebase.setCollectionPath("User");
        firebase.insertDataFirestore(user.getUid());
    }

    private void loginComplete(FirebaseUser user) {
        Intent mainActivity = new Intent(SignUpActivity_2.this, MainActivity.class);
        startActivity(mainActivity);
    }

    private void SendInformation(Intent previousSU, User userInfo) {
        previousSU.putExtra("firstname", userInfo.getFirst_Name());
        previousSU.putExtra("lastname", userInfo.getLast_Name());
        previousSU.putExtra("phone", userInfo.getPhone_Number());
        previousSU.putExtra("email", userInfo.getEmail());
        previousSU.putExtra("address", userInfo.getAddress());
        previousSU.putExtra("password", userInfo.getPassword());
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