package com.example.fooddelivery.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity_2 extends AppCompatActivity {
    Button bt_back, bt_mail, bt_phone;
    TextView tv_phone, tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_2);

        Init();
    }

    private void Init() {
        bt_mail = findViewById(R.id.fp2_bt_email);
        bt_phone = findViewById(R.id.fp2_bt_phone);
        bt_back = findViewById(R.id.fp2_bt_back);
        tv_email = findViewById(R.id.fp2_tv_email);
        tv_phone = findViewById(R.id.fp2_tv_mobile);

        tv_phone.setText(getIntent().getStringExtra("phone"));
        tv_email.setText(getIntent().getStringExtra("email"));

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassActivity1 = new Intent(ForgotPassActivity_2.this, ForgotPassActivity_3.class);
                startActivity(forgotPassActivity1);
            }
        });

        bt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassActivity3 = new Intent(ForgotPassActivity_2.this, ForgotPassActivity_3.class);
                forgotPassActivity3.putExtra("phone", tv_phone.getText().toString());
                startActivity(forgotPassActivity3);
            }
        });

        bt_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetByEmail(tv_email.getText().toString());
            }
        });
    }

    private void sendPasswordResetByEmail(String email) {
        ProgressDialog progressDialog = new ProgressDialog(ForgotPassActivity_2.this);
        progressDialog.setMessage("Đang gửi email...");
        progressDialog.show();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Email verify", "success");
                        progressDialog.dismiss();
                        Intent forgotPassActivity5 = new Intent(ForgotPassActivity_2.this, ForgotPassActitivy_5.class);
                        forgotPassActivity5.putExtra("email", "true");
                        startActivity(forgotPassActivity5);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e("Email verify", e.getMessage());
                    }
                });
    }
}