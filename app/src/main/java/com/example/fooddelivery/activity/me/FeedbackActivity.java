package com.example.fooddelivery.activity.me;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.model.Feedback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class FeedbackActivity extends AppCompatActivity {
    ImageButton bt_back;
    Button bt_send;
    EditText et_details;
    ProgressBar bar;
    String[] topics;
    ArrayAdapter<String> languageAdapter;
    AutoCompleteTextView temp;
    String choosenTopic;
    FirebaseFirestore root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Init();
    }

    private void Init() {
        bt_back = findViewById(R.id.me_feeback_ib_back);
        bt_send = findViewById(R.id.fb_bt_send);
        et_details = findViewById(R.id.fb_et_details);
        bar = findViewById(R.id.fb_wating_bar);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        temp = findViewById(R.id.auto_topics);
        topics = getResources().getStringArray(R.array.feedback_title);
        languageAdapter = new ArrayAdapter<>(FeedbackActivity.this, R.layout.dropdown_item, topics);
        temp.setAdapter(languageAdapter);

        temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosenTopic = parent.getItemAtPosition(position).toString();
            }
        });

        choosenTopic = getResources().getString(R.string.feedback);
        root = FirebaseFirestore.getInstance();
    }

    private void sendFeedback() {
        bar.setVisibility(View.VISIBLE);
        Feedback feedback = new Feedback();
        feedback.setUserId(LoginActivity.userID);
        feedback.setSubject(choosenTopic);
        feedback.setDetails(et_details.getText().toString());
        feedback.setDate(new Date().toString());

        root.collection("Feedback/")
                .add(feedback)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FeedbackActivity.this, R.string.send_feedback_success, Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FeedbackActivity.this, R.string.send_feedback_fail, Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}