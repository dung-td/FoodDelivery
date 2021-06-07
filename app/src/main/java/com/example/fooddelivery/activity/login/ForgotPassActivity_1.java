package com.example.fooddelivery.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Regex;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPassActivity_1 extends AppCompatActivity {
    Regex regex;
    EditText et_credentials;
    Button bt_continue, bt_back;
    TextView tv_change;
    boolean StatusEmail = true;

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
        bt_continue = findViewById(R.id.fp1_bt_continue);
        tv_change = findViewById(R.id.fp1_tv_change);
        bt_back = findViewById(R.id.fp1_bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(ForgotPassActivity_1.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String credentials = et_credentials.getText().toString();
                if (StatusEmail) {
                    if (regex.validateEmail(credentials))
                        checkEmail(credentials);
                } else {
                    if (regex.validatePassword(credentials))
                        continueFP2(credentials);
                }
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusEmail = !StatusEmail;
                if (StatusEmail) {
                    et_credentials.setHint(getString(R.string.phoneNumber));
                    et_credentials.setInputType(InputType.TYPE_CLASS_PHONE);
                } else {
                    et_credentials.setHint(getString(R.string.login_email_hint));
                    et_credentials.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
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
        root.collection("User")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            et_credentials.setError(getResources().getString(R.string.user_not_exist));
                        }
                        else {
                            continueFP2(queryDocumentSnapshots.getDocuments().get(0).get("phone_number").toString());
                        }
                    }
                });
    }

    public void checkPhone(String phone) {
        root.collection("User")
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            et_credentials.setError(getResources().getString(R.string.user_not_exist));
                        }
                        else {

                            continueFP2(queryDocumentSnapshots.getDocuments().get(0).get("email").toString());
                        }
                    }
                });
    }
}