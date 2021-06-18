package com.example.fooddelivery.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Regex;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import es.dmoral.toasty.Toasty;

public class ForgotPassActivity_1 extends AppCompatActivity {
    Regex regex;
    EditText et_credentials;
    TextInputLayout ti_credentials;
    Button bt_continue, bt_back;
    TextView tv_change, tv_info;
    boolean StatusEmail = true;
    ProgressDialog progressDialog;
    private FirebaseFirestore root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_1);

        Init();
    }

    private void Init() {
        regex = new Regex();
        root = FirebaseFirestore.getInstance();
        et_credentials = findViewById(R.id.fp1_et_emailphone);
        ti_credentials = findViewById(R.id.fp1_ti_emailphone);
        bt_continue = findViewById(R.id.fp1_bt_continue);
        tv_change = findViewById(R.id.fp1_tv_change);
        tv_info = findViewById(R.id.fp1_tv_info);
        bt_back = findViewById(R.id.fp1_bt_back);

        progressDialog = new ProgressDialog(ForgotPassActivity_1.this);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPassActivity_1.super.onBackPressed();
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage(getString(R.string.checking_info));
                progressDialog.show();
                String credentials = et_credentials.getText().toString();
                if (StatusEmail) {
                    if (regex.validateEmail(credentials))
                        checkEmail(credentials);
                    else {
                        progressDialog.dismiss();
                        Toasty.error(ForgotPassActivity_1.this, getResources().getString(R.string.wrong_email_format)).show();
                    }

                } else {
                    if (credentials.length() == 10)
                        checkPhone(credentials);
                    else {
                        progressDialog.dismiss();
                        Toasty.error(ForgotPassActivity_1.this, getResources().getString(R.string.wrong_phone_format)).show();
                    }
                }
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StatusEmail) {
                    ti_credentials.setHint(getString(R.string.phoneNumber));
                    ti_credentials.setStartIconDrawable(getDrawable(R.drawable.ic_baseline_phone_24));
                    et_credentials.setInputType(InputType.TYPE_CLASS_PHONE);
                    tv_change.setText(R.string.use_email);
                    tv_info.setText(R.string.provide_phone_help);
                } else {
                    ti_credentials.setHint(getString(R.string.login_email_hint));
                    ti_credentials.setStartIconDrawable(getDrawable(R.drawable.ic_baseline_alternate_email_24));
                    et_credentials.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    tv_change.setText(R.string.use_phoneNum);
                    tv_info.setText(R.string.provide_email_help);
                }
                StatusEmail = !StatusEmail;
            }
        });
    }

    private void continueFP2(String credentials_2) {
        Intent forgotPass2 = new Intent(ForgotPassActivity_1.this, ForgotPassActivity_2.class);
        if (StatusEmail) {
            forgotPass2.putExtra("email", et_credentials.getText().toString());
            forgotPass2.putExtra("phone", credentials_2);
        } else {
            forgotPass2.putExtra("phone", et_credentials.getText().toString());
            forgotPass2.putExtra("email", credentials_2);
        }
        startActivity(forgotPass2);
    }

    public void checkEmail(String email) {
        root.collection("User/")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("get info", String.valueOf(queryDocumentSnapshots.size()));
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toasty.error(ForgotPassActivity_1.this, getResources().getString(R.string.user_not_exist)).show();
                            progressDialog.dismiss();
                        } else {
                            continueFP2(queryDocumentSnapshots.getDocuments().get(0).get("phone_Number").toString());
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void checkPhone(String phone) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        root.collection("User/")
                .whereEqualTo("phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toasty.error(ForgotPassActivity_1.this, getResources().getString(R.string.user_not_exist)).show();
                            progressDialog.dismiss();
                        } else {
                            continueFP2(queryDocumentSnapshots.getDocuments().get(0).get("email").toString());
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}