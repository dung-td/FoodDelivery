package com.example.fooddelivery.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Regex;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity_4 extends AppCompatActivity {
    Button bt_back, bt_finish;
    EditText et_pass, et_repass;

    Regex regex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_4);

        Init();
    }

    private void Init() {
        bt_back = findViewById(R.id.fp4_bt_back);
        bt_finish = findViewById(R.id.fp4_bt_continue);
        et_pass = findViewById(R.id.fp4_et_pass);
        et_repass = findViewById(R.id.fp4_et_repass);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPass3 = new Intent(ForgotPassActivity_4.this, ForgotPassActivity_3.class);
                startActivity(forgotPass3);
            }
        });

        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRequirement()) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.getCurrentUser().updatePassword(et_pass.getText().toString());
                    Intent forgotPass5 = new Intent(ForgotPassActivity_4.this, ForgotPassActitivy_5.class);
                    startActivity(forgotPass5);
                    finish();
                }
            }
        });
    }

    private boolean checkRequirement() {
        if (!et_pass.getText().toString().equals(
                et_repass.getText().toString())) {
            et_repass.setError("Mật khẩu phải trùng khớp");
            et_repass.requestFocus();
            return false;
        } else if (!regex.validatePassword(et_pass.getText().toString())) {
            et_pass.setError("Mật khẩu phải có ít nhất 8 ký tự, tồn tại chữ thường, chữ hoa, số và kí tự đặc biệt");
            et_pass.requestFocus();
            return false;
        }
        return true;
    }
}