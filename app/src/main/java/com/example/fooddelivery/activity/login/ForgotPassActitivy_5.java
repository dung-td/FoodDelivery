package com.example.fooddelivery.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActitivy_5 extends AppCompatActivity {

    Button bt_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_5);
        bt_finish = findViewById(R.id.fp5_bt_login);

        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(ForgotPassActitivy_5.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }
}