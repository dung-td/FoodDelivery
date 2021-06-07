package com.example.fooddelivery.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity_2 extends AppCompatActivity {
    RelativeLayout sms_box, email_box;
    Button bt_back;
    TextView tv_phone, tv_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_2);

        Init();
    }

    private void Init() {
        sms_box = findViewById(R.id.fp3_fl_sms_box);
        email_box = findViewById(R.id.fp2_fl_email_box);
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

        sms_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassActivity3 = new Intent(ForgotPassActivity_2.this, ForgotPassActivity_3.class);
                forgotPassActivity3.putExtra("phone", tv_phone.getText().toString());
                startActivity(forgotPassActivity3);
            }
        });

        email_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetByEmail(tv_email.getText().toString());
            }
        });
    }

    private void sendPasswordResetByEmail(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent forgotPassActivity5 = new Intent(ForgotPassActivity_2.this, ForgotPassActitivy_5.class);
                        startActivity(forgotPassActivity5);
                    }
                });
    }
}