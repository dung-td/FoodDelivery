package com.example.fooddelivery.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignUpActivity_1 extends AppCompatActivity {

    EditText et_email, et_lastname, et_firstname,
            et_phone, et_address, et_pass1, et_pass2;
    TextInputLayout ti_email;
    ImageView bt_next, bt_back;
    TextView tv_haveaccont;
    ProgressDialog progressDialog;
    Regex regex;
    FirebaseFirestore root;
    String uid;


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
        ti_email = findViewById(R.id.su1_ti_email);
        bt_next =findViewById(R.id.su1_bt_next);
        bt_back = findViewById(R.id.su1_bt_back);
        tv_haveaccont = findViewById(R.id.su1_tv_haveaccount);

        progressDialog = new ProgressDialog(SignUpActivity_1.this);

        String email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");

        if (!email.equals("")) {
            et_email.setText(email);
            et_email.setEnabled(false);
            ti_email.setEndIconMode(TextInputLayout.END_ICON_NONE);
        }

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRequirements()) {
                    progressDialog.setMessage(getString(R.string.checking_info));
                    checkEmail();
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity_1.super.onBackPressed();
            }
        });

        tv_haveaccont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity_1.super.onBackPressed();
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
                                progressDialog.dismiss();
                                et_email.setError(getString(R.string.email_has_been_used));
                                et_email.requestFocus();
                            } else {
                                progressDialog.dismiss();
                                Intent signUp2 = new Intent(SignUpActivity_1.this, SignUpActivity_2.class);
                                SendInformation(signUp2);
                                startActivity(signUp2);
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
        nextSU.putExtra("uid", uid);
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