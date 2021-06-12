package com.example.fooddelivery.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    Button bt_SaveChanges;
    TextView tv_Email, tv_Phone, tv_Address, tv_Name, tv_Username;
    ImageView iv_Avatar;
    ImageButton bt_back;

    Uri imageUri;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    boolean uploadAvatar = false;
    String userID = "Hp9LlgLygEstFV4sIpxc";


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

        bt_SaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadAvatar) {
                    updateAvatar();
                }
                updateData();
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
        tv_Username = (TextView)findViewById(R.id.tv_perinfo_username);
        tv_Name = (TextView)findViewById(R.id.tv_perinfo_name);

        bt_SaveChanges = (Button) findViewById(R.id.bt_perinfo_save);

        iv_Avatar = (ImageView) findViewById(R.id.im_perinfo);

        et_Address.setVisibility(View.GONE);
        et_Phone.setVisibility(View.GONE);
        et_Email.setVisibility(View.GONE);
    }

    void beginChangeInfomation(EditText et_Info, TextView tv_Info, TextView confirm) {
        et_Info.setVisibility(View.VISIBLE);
        et_Info.setText(tv_Info.getText());

        tv_Info.setVisibility(View.GONE);

        confirm.setText(getString(R.string.done));
    }

    void finishChangeInfomation(EditText et_Info, TextView tv_Info, TextView confirm) {
        tv_Info.setVisibility(View.VISIBLE);
        tv_Info.setText(et_Info.getText());

        et_Info.setVisibility(View.GONE);

        confirm.setText(getString(R.string.change));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_perinfo_changeemail:
                if (tv_ChangeEmail.getText().equals(getString(R.string.change)))
                    beginChangeInfomation(et_Email, tv_Email, tv_ChangeEmail);
                else
                    finishChangeInfomation(et_Email, tv_Email, tv_ChangeEmail);
                break;

            case R.id.tv_perinfo_changephone:
                if (tv_ChangePhone.getText().equals(getString(R.string.change)))
                    beginChangeInfomation(et_Phone, tv_Phone, tv_ChangePhone);
                else
                    finishChangeInfomation(et_Phone, tv_Phone, tv_ChangePhone);
                break;

            case R.id.tv_perinfo_changeaddress:
                if (tv_ChangeAddress.getText().equals(getString(R.string.change)))
                    beginChangeInfomation(et_Address, tv_Address, tv_ChangeAddress);
                else
                    finishChangeInfomation(et_Address, tv_Address, tv_ChangeAddress);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            iv_Avatar.setImageURI(imageUri);
            uploadAvatar = true;
        }
    }

    void updateAvatar() {
//        StorageReference fileRef = reference.child(
//                "User/" + user.getUid() +"."+
//                imageUri.toString().substring(imageUri.toString().lastIndexOf(".")));

//        StorageReference fileRef = reference.child("UserImage/"+userID+
//                        imageUri.toString().substring(imageUri.toString().lastIndexOf(".")));

        StorageReference fileRef = reference.child("UserImage/"+userID);

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, String> image = new HashMap<>();
                        image.put("imageLink", uri.toString());
                        image.put("timeUpload", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                        root.collection("User").add(image);
                    }
                });
            }
        });
    }

    void updateData() {
        String phone = tv_Phone.getText().toString();
        String email = tv_Email.getText().toString();
        String address = tv_Address.getText().toString();

        Map<String, String> map = new HashMap<>();

        map.put("address", address);
        map.put("phone_number", phone);
        map.put("email", email);

        root.collection("User").document(userID)
                .update("address", address,
                        "phone_number", phone,
                        "email", email);
    }

    void loadInformation() {
        DocumentReference docRef = root.collection("User").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //User userInfo = document.toObject(User.class);
                        tv_Username.setText(document.get("username").toString());
                        tv_Address.setText(document.get("address").toString());
                        tv_Phone.setText(document.get("phone_number").toString());
                        tv_Email.setText(document.get("email").toString());
                        tv_Name.setText(document.get("last_name").toString() +" "+ document.get("middle_name").toString() +" "+ document.get("first_name").toString());
                    }
                }
            }
        });
    }

    void loadAvatar() {
        StorageReference fileRef = reference.child("UserImage/"+userID);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Avatar", uri.toString());
                Picasso.get().load(uri).into(iv_Avatar);
            }
        });
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
}