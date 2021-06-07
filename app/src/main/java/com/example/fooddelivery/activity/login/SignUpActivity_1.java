package com.example.fooddelivery.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignUpActivity_1 extends AppCompatActivity {

    EditText et_email, et_lastname, et_firstname,
            et_phone, et_address, et_pass1, et_pass2;
    ImageView bt_next, bt_back;
    ProgressBar progressBar;
    ImageView bt_signin_gg;
    TextView tv_haveaccont;

    Regex regex;
    FirebaseFirestore root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_1);

        Init();
    }

    private void Init() {
        root = FirebaseFirestore.getInstance();
        regex = new Regex();
        et_email = findViewById(R.id.su1_et_email);
        et_lastname =findViewById(R.id.su1_et_lastname);
        et_firstname =  findViewById(R.id.su1_et_firstname);
        et_phone = findViewById(R.id.su1_et_phone);
        et_address =  findViewById(R.id.su1_et_address);
        et_pass1 = findViewById(R.id.su1_et_pass);
        et_pass2 = findViewById(R.id.su1_et_repass);
        bt_next =findViewById(R.id.su1_bt_next);
        bt_back = findViewById(R.id.su1_bt_back);
        tv_haveaccont = findViewById(R.id.su1_tv_haveaccount);
        bt_signin_gg = findViewById(R.id.su1_iv_gg);
        progressBar = findViewById(R.id.su1_pb_wating);
        progressBar.setVisibility(View.INVISIBLE);

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRequirements()) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    checkEmail();
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignUpActivity_1.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        tv_haveaccont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bt_signin_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void checkEmail() {
        root.collection("User")
                .whereEqualTo("email", et_email.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                et_email.setError(getString(R.string.email_has_been_used));
                                et_email.requestFocus();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent nextSU = new Intent(SignUpActivity_1.this, SignUpActivity_2.class);
                                SendInformation(nextSU);
                                startActivity(nextSU);
                            }
                        }
                    }
                });
    }

    private void SendInformation(Intent nextSU)
    {
        nextSU.putExtra("firstname", et_firstname.getText().toString());
        nextSU.putExtra("lastname",et_lastname.getText().toString());
        nextSU.putExtra("phone", et_phone.getText().toString());
        nextSU.putExtra("email", et_email.getText().toString());
        nextSU.putExtra("address", et_address.getText().toString());
        nextSU.putExtra("password", et_pass1.getText().toString());
    }

    private boolean checkRequirements() {
        if (et_firstname.getText().toString().isEmpty()) {
            et_firstname.setError(getString(R.string.pls_enter_firstname));
            et_firstname.requestFocus();
            return false;
        } else if (et_lastname.getText().toString().isEmpty()) {
            et_lastname.setError(getString(R.string.pls_enter_lastename));
            et_lastname.requestFocus();
            return false;
        } else if (et_email.getText().toString().isEmpty()) {
            et_email.setError(getString(R.string.please_enter_email));
            et_email.requestFocus();
            return false;
        } else if (!regex.validateEmail(et_email.getText().toString())) {
            et_email.setError(getString(R.string.wrong_email_format));
            et_email.requestFocus();
            return false;
        } else if (et_phone.getText().toString().isEmpty()) {
            et_phone.setError(getString(R.string.pls_enter_phone));
            et_phone.requestFocus();
            return false;
        } else if (et_phone.getText().toString().length() != 10 ||
                !et_phone.getText().toString().startsWith("0")) {
            et_phone.setError(getString(R.string.wrong_phone_format));
            et_phone.requestFocus();
            return false;
        } else if (et_pass1.getText().toString().isEmpty())  {
            et_pass1.setError(getString(R.string.please_enter_pass));
            et_pass1.requestFocus();
            return false;
        } else if (et_pass2.getText().toString().isEmpty())  {
            et_pass2.setError(getString(R.string.please_enter_pass));
            et_pass2.requestFocus();
            return false;
        } else if (!et_pass1.getText().toString().equals(
                et_pass2.getText().toString())) {
            et_pass2.setError(getString(R.string.pass_must_match));
            et_pass2.requestFocus();
            return false;
        } else if (!regex.validatePassword(et_pass1.getText().toString())) {
            et_pass1.setError(getString(R.string.pass_regex));
            et_pass1.requestFocus();
            return false;
        } else if (et_address.getText().toString().isEmpty())  {
            et_address.setError(getString(R.string.pls_enter_address));
            et_address.requestFocus();
            return false;
        }
        return true;
    }
}