package com.example.fooddelivery.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.inline.InlineContentView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.PersonalInfoActivity;
import com.example.fooddelivery.activity.me.FeedbackActivity;
import com.example.fooddelivery.activity.me.SettingActivity;
import com.example.fooddelivery.activity.me.VoucherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MeFragment extends Fragment {

    ImageButton bt_setting, bt_voucher, bt_logout, bt_feedback, bt_info, bt_payment;
    ImageView imageUser;
    TextView tv_userName;

    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
   // String userID = "KrSKPkEqkMP5KuzR60QBiBcWsoE2";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    boolean shouldRefreshOnResume;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Init();

        loadAvatar();
        loadName();

        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingActivity = new Intent(MeFragment.super.getContext(), SettingActivity.class);
                startActivity(settingActivity);
            }
        });

        bt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackActivity = new Intent(MeFragment.super.getContext(), FeedbackActivity.class);
                startActivity(feedbackActivity);
            }
        });

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bt_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucherActivity = new Intent(MeFragment.super.getContext(), VoucherActivity.class);
                startActivity(voucherActivity);
            }
        });

        bt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent personalInfoActivity = new Intent(MeFragment.super.getContext(), PersonalInfoActivity.class);
                startActivity(personalInfoActivity);
            }
        });

    }


    private void Init() {
        bt_setting = getView().findViewById(R.id.me_ib_setting);
        bt_voucher = getView().findViewById(R.id.me_ib_voucher);
        bt_logout = getView().findViewById(R.id.me_ib_logout);
        bt_feedback = getView().findViewById(R.id.me_ib_feedback);
        bt_info = getView().findViewById(R.id.me_ib_info);
        bt_payment = getView().findViewById(R.id.me_ib_payment);

        imageUser = getView().findViewById(R.id.img_user);

        tv_userName = getView().findViewById(R.id.me_fl_name);
    }

    public void loadAvatar() {
        StorageReference fileRef = reference.child("UserImage/"+userID);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Avatar", uri.toString());
                Picasso.get().load(uri).into(imageUser);
            }
        });
    }

    void loadName()
    {
        DocumentReference docRef = root.collection("User").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //User userInfo = document.toObject(User.class);
                        tv_userName.setText(document.get("last_Name").toString() +  " " + document.get("first_Name").toString());
                    }
                }
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        shouldRefreshOnResume = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldRefreshOnResume) loadAvatar();
    }
}
