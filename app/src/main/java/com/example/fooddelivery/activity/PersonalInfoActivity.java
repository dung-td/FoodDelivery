package com.example.fooddelivery.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.SignUpActivity_1;
import com.example.fooddelivery.activity.login.SignUpActivity_2;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.fragment.MeFragment;
import com.example.fooddelivery.model.CallBackData;
import com.example.fooddelivery.model.Comment;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.Regex;
import com.example.fooddelivery.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PersonalInfoActivity extends AppCompatActivity {

    EditText et_Email, et_Phone, et_Address;
    TextView tv_ChangeAvatar, tv_ChangeEmail, tv_ChangePhone, tv_ChangeAddress;
    TextView tv_Email, tv_Phone, tv_Address, tv_Name, tv_Username;
    ImageView iv_Avatar;
    ImageButton bt_back;
    ProgressBar progressBar;
    Uri imageUri;
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    Regex regex = new Regex();
    String oldEmail, oldPhone, oldAddress;
    User currentUser = new User();
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        loadLanguage();
        initView();

        loadInformation();
        loadAvatar();

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        tv_ChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(PersonalInfoActivity.this);
            }
        });

    }

    void initView() {
        bt_back = (ImageButton)findViewById(R.id.bt_perinfo_back);
        et_Email = (EditText) findViewById(R.id.et_perinfo_email);
        et_Phone = (EditText) findViewById(R.id.et_perinfo_phone);
        et_Address = (EditText) findViewById(R.id.et_perinfo_address);

        tv_ChangeAvatar = (TextView) findViewById(R.id.tv_perinfo_changeavatar);
        tv_ChangeEmail = (TextView) findViewById(R.id.tv_perinfo_changeemail);
        tv_ChangePhone = (TextView) findViewById(R.id.tv_perinfo_changephone);
        tv_ChangeAddress = (TextView) findViewById(R.id.tv_perinfo_changeaddress);

        tv_Address = (TextView) findViewById(R.id.tv_perinfo_address);
        tv_Phone = (TextView) findViewById(R.id.tv_perinfo_phone);
        tv_Email = (TextView) findViewById(R.id.tv_perinfo_email);
        tv_Name = (TextView)findViewById(R.id.tv_perinfo_name);

        iv_Avatar = (ImageView) findViewById(R.id.im_perinfo);

        progressBar = (ProgressBar)findViewById(R.id.perinfo_wating);
        progressBar.setVisibility(View.INVISIBLE);

        et_Address.setVisibility(View.GONE);
        et_Phone.setVisibility(View.GONE);
        et_Email.setVisibility(View.GONE);

        currentUser = LoginActivity.firebase.getUser();
        userID = LoginActivity.firebase.getUserId();
    }

    void beginChangeInfomation(EditText et_Info, TextView tv_Info, TextView confirm) {
        et_Info.setVisibility(View.VISIBLE);
        et_Info.setText(tv_Info.getText());

        tv_Info.setVisibility(View.GONE);

        confirm.setText(getString(R.string.done));
    }

    void finishChangeInfomation(EditText et_Info, TextView tv_Info, TextView confirm) {
        if (et_Info.getText().toString().isEmpty())
        {
            et_Info.setError(getString(R.string.data_empty));
            et_Info.requestFocus();
        } else {
            tv_Info.setVisibility(View.VISIBLE);
            tv_Info.setText(et_Info.getText());

            et_Info.setVisibility(View.GONE);

            confirm.setText(getString(R.string.change));
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_perinfo_changeemail:
                if (tv_ChangeEmail.getText().equals(getString(R.string.change))){
                    beginChangeInfomation(et_Email, tv_Email, tv_ChangeEmail);

                }
                else {
                    if (validEmail())
                    {
                        if (!oldEmail.equals(et_Email.getText().toString()))
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setIndeterminate(true);
                            changeEmail(et_Email.getText().toString());
                        }
                        else
                            progressBar.setVisibility(View.INVISIBLE);

                        if (newEmail || oldEmail.equals(et_Email.getText().toString())) {
                            finishChangeInfomation(et_Email, tv_Email, tv_ChangeEmail);
                        }
                    }

                }
                break;

            case R.id.tv_perinfo_changephone:

                if (tv_ChangePhone.getText().equals(getString(R.string.change))){
                    beginChangeInfomation(et_Phone, tv_Phone, tv_ChangePhone);
                }
                else {
                    if (validPhone())
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(true);
                        Log.e("Old phone", oldPhone);
                        Log.e("newPhone", et_Phone.getText().toString());
                        if (!oldPhone.equals(et_Phone.getText().toString()))
                        {
                            Log.e("Change phone", "go to change phone" + et_Phone.getText().toString());
                            changePhone(et_Phone.getText().toString());
                        }
                        else
                            progressBar.setVisibility(View.INVISIBLE);
                        finishChangeInfomation(et_Phone, tv_Phone, tv_ChangePhone);
                    }

                }
                break;

            case R.id.tv_perinfo_changeaddress:
                if (tv_ChangeAddress.getText().equals(getString(R.string.change))){
                    beginChangeInfomation(et_Address, tv_Address, tv_ChangeAddress);
                }
                else
                {
                    if (!oldAddress.equals(et_Address.getText().toString()) ) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(true);
                        updateAddress();
                    }
                    else
                        progressBar.setVisibility(View.INVISIBLE);

                    finishChangeInfomation(et_Address, tv_Address, tv_ChangeAddress);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            updateAvatar();
            iv_Avatar.setImageURI(imageUri);
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    void updateAvatar() {
        StorageReference fileRef = reference.child("UserImage/"+ userID);

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        root.collection("User/")
                                .document(userID)
                                .update("profileImageLink", uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.update_ava_done), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
            }
        });
    }
    void updateAddress(){
        String address = et_Address.getText().toString();

        root.collection("User").document(userID)
                .update("address", address)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        currentUser.setAddress(address);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), getString(R.string.update_infodata_done), Toast.LENGTH_LONG).show();
                    }
                });
    }

    void loadInformation()
    {
        tv_Address.setText(currentUser.getAddress());
        tv_Phone.setText(currentUser.getPhone_Number());
        tv_Email.setText(currentUser.getEmail());
        tv_Name.setText(String.format("%s %s", currentUser.getLast_Name(), currentUser.getFirst_Name()));
        Glide.with(PersonalInfoActivity.this).load(currentUser.getProfileImage());

        oldAddress = tv_Address.getText().toString();
        oldPhone = tv_Phone.getText().toString();
        oldEmail = tv_Email.getText().toString();
    }

    void loadAvatar() {
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setIndeterminate(true);
//        StorageReference fileRef = reference.child("UserImage/"+userID);
//        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.e("Avatar", uri.toString());
//                Picasso.get().load(uri).into(iv_Avatar);
//            }
//        });
//        progressBar.setVisibility(View.INVISIBLE);

        Uri uri = LoginActivity.firebase.getUser().getProfileImage();
        if (uri !=null)
        {
            Picasso.get().load(uri).into(iv_Avatar);
        }
    }

    void loadLanguage() {
        String langPref = "lang_code";
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");

        Log.e("language", language);

        Locale locale = new Locale(language);
        locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    boolean validEmail() {
        Log.e("Email", et_Email.getText().toString());
        if (!regex.validateEmail(et_Email.getText().toString())) {
            Log.e("Email", "wrong email");
            et_Email.setError(getString(R.string.wrong_email_format));
            et_Email.requestFocus();
            return false;
        }

        return true;
    }
    boolean validPhone(){
        if (et_Phone.getText().toString().length() != 10 ||
                !et_Phone.getText().toString().startsWith("0")) {
            et_Phone.setError(getString(R.string.wrong_phone_format));
            et_Phone.requestFocus();
            return false;
        }
        return true;
    }

    boolean newEmail = false;
    void changeEmail(String email) {
//        root.collection("User")
//                .whereEqualTo("email", email)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if (!task.getResult().isEmpty()) {
//                                et_Email.setError(getString(R.string.email_has_been_used));
//                                et_Email.requestFocus();
//                                Log.e(getString(R.string.email_has_been_used), getString(R.string.email_has_been_used));
//                                progressBar.setVisibility(View.INVISIBLE);
//                            }
//                            else {
//                                Log.e("sendVerifyEmail", "sendVerifyEmail");
//                                sendVerifyEmail(email);
//                            }
//
//                        }
//                    }
//                });

        checkExistedEmail(email, new CallBackData() {
            @Override
            public void firebaseResponseCallback(boolean result) {
                if (result) {
                    et_Email.setError(getString(R.string.email_has_been_used));
                    et_Email.requestFocus();
                    Log.e(getString(R.string.email_has_been_used), getString(R.string.email_has_been_used));
                    progressBar.setVisibility(View.INVISIBLE);
                }else{
                    sendVerifyEmail(email);
                }

            }

            @Override
            public void callbackComment(Comment result) {

            }

            @Override
            public void callbackListOrder() {

            }
        });

    }

    public void checkExistedEmail(String email, CallBackData callback){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean isExist = false;

                if (!task.getResult().getSignInMethods().isEmpty())
                    isExist = true;

                callback.firebaseResponseCallback(isExist);
            }
        });
    }

    void changePhone(String phone) {
        root.collection("User")
                .whereEqualTo("phone_Number", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("Change phone", phone);
                            if (!task.getResult().isEmpty()) {

                                et_Phone.setError(getString(R.string.phone_been_used));
                                et_Phone.requestFocus();
                            }
                            else {
                                Intent verifyPhone = new Intent(PersonalInfoActivity.this, VerifyPhoneActivity.class);
                                verifyPhone.putExtra("phone", phone);
                                startActivity(verifyPhone);
                            }
                        }
                    }
                });

    }


    private void sendVerifyEmail(String email) {
        Toast.makeText(this, getString(R.string.verified_email_sent)+ " " +email, Toast.LENGTH_LONG).show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.verifyBeforeUpdateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("Send email", "SEND");
                        user.reload();
                        if (task.isSuccessful() && user.isEmailVerified()) {
                            Log.e("verifyBeforeUpdateEmail", "verifyBeforeUpdateEmail");
                           // updateEmail(email);
                            newEmail = true;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Send email failed", e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), getString(R.string.verify_failed), Toast.LENGTH_LONG).show();
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);
    }
//    void updateEmail(String email) {
//        FirebaseFirestore root = FirebaseFirestore.getInstance();
//        root.collection("User").document(userID)
//                .update("email", email)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getApplicationContext(), getString(R.string.update_infodata_done), Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
}