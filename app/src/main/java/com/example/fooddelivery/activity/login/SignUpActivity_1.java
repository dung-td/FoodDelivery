package com.example.fooddelivery.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.me.LanguageSetting;
import com.example.fooddelivery.model.Regex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignUpActivity_1 extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    EditText et_email, et_lastname, et_firstname,
            et_phone, et_pass1, et_pass2;
    TextInputLayout ti_email;
    ImageView bt_next, bt_back;
    TextView tv_haveaccont, tv_address, tv_choosseAddress;
    ProgressDialog progressDialog;
    Regex regex;
    FirebaseFirestore root;
    String uid;
    Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocal();
        setContentView(R.layout.activity_sign_up_1);

        Init();
    }

    private void Init() {
        root = FirebaseFirestore.getInstance();
        regex = new Regex();
        et_email = findViewById(R.id.su1_et_email);
        et_lastname = findViewById(R.id.su1_et_lastname);
        et_firstname = findViewById(R.id.su1_et_firstname);
        et_phone = findViewById(R.id.su1_et_phone);
        tv_address = findViewById(R.id.su1_tv_address);
        et_pass1 = findViewById(R.id.su1_et_pass);
        et_pass2 = findViewById(R.id.su1_et_repass);
        ti_email = findViewById(R.id.su1_ti_email);
        bt_next = findViewById(R.id.su1_bt_next);
        bt_back = findViewById(R.id.su1_bt_back);
        tv_haveaccont = findViewById(R.id.su1_tv_haveaccount);
        tv_choosseAddress = findViewById(R.id.su1_tv_addresspicker);

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

        tv_choosseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(SignUpActivity_1.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);

            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            address = addresses.get(0);
            tv_address.setText(address.getAddressLine(0));
        }
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
                                checkPhone();
                            }
                        }
                    }
                });
    }

    private void checkPhone() {
        String phone = et_phone.getText().toString();
        if (phone.length() == 9)
            phone = "0" + et_phone.getText().toString();
        root.collection("User")
                .whereEqualTo("phone_Number", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                progressDialog.dismiss();
                                et_phone.setError(getString(R.string.phone_been_used));
                                et_phone.requestFocus();
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

    private void SendInformation(Intent nextSU) {
        nextSU.putExtra("firstname", et_firstname.getText().toString());
        nextSU.putExtra("lastname", et_lastname.getText().toString());
        nextSU.putExtra("phone", et_phone.getText().toString());
        nextSU.putExtra("email", et_email.getText().toString());
        nextSU.putExtra("address", address.getAddressLine(0));
        nextSU.putExtra("city", address.getLocality());
        nextSU.putExtra("state", address.getAdminArea());
        nextSU.putExtra("country", address.getCountryName());
        nextSU.putExtra("latitude", address.getLatitude());
        nextSU.putExtra("longitude", address.getLongitude());
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
        } else if (et_phone.getText().toString().length() < 9 ||
                et_phone.getText().toString().length() > 10) {
            et_phone.setError(getString(R.string.wrong_phone_format));
            et_phone.requestFocus();
            return false;
        } else if (et_pass1.getText().toString().isEmpty()) {
            et_pass1.setError(getString(R.string.please_enter_pass));
            et_pass1.requestFocus();
            return false;
        } else if (et_pass2.getText().toString().isEmpty()) {
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
        } else if (tv_address.getText().toString().equals(getString(R.string.no_data))) {
            tv_address.setError(getString(R.string.pls_enter_address));
            tv_address.requestFocus();
            return false;
        }
        return true;
    }

    public void setLocal() {
        String langCode;

        if (WelcomeActivity.language.equals("vi"))
            langCode = "vi";
        else {
            langCode = "en";
        }

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}