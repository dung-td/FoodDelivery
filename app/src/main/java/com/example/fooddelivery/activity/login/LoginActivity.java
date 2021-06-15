package com.example.fooddelivery.activity.login;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.MainActivity;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.Regex;
import com.example.fooddelivery.model.User;
import com.example.fooddelivery.model.modifiedFirebase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity {

    String uID;
    private static final String TAG = "GOOGLE SIGN IN";
    EditText et_email, et_pass;
    Button bt_login;
    TextView tv_forgotPass, tv_register;
    ProgressBar pb_wating;
    ImageView iv_register, iv_google_signin;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    public static ModifyFirebase firebase;
    public static String userID;

    public LoginActivity() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loginComplete();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();
        createGoogleSignInRequest();
    }

    private void Init() {
        mAuth = FirebaseAuth.getInstance();
        firebase = new ModifyFirebase();
        et_email = findViewById(R.id.lg_et_email);
        et_pass = findViewById(R.id.lg_et_password);
        bt_login = findViewById(R.id.lg_bt_login);
        tv_forgotPass = findViewById(R.id.lg_tv_forgetPass);
        tv_register = findViewById(R.id.lg_tv_register);
        pb_wating = findViewById(R.id.lg_pb_wating);
        iv_register = findViewById(R.id.lg_iv_register);
        iv_google_signin = findViewById(R.id.lg_bt_google);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_wating.setVisibility(View.VISIBLE);
                pb_wating.setIndeterminate(true);
                loginWithEmailAndPassWord();
            }
        });

        iv_google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass();
            }
        });

        iv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void createGoogleSignInRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            uID = user.getUid();
                            if (!firebase.checkUID(uID))
                                addNewUser(user);
                            loginComplete();

                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            loginComplete();
                        } else {
                            pb_wating.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.email_pass_not_correct),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addNewUser(FirebaseUser user) {
        User temp = new User();

        firebase.setObject(temp);
        firebase.setCollectionPath("User");
        firebase.insertDataFirestore(user.getUid());
    }

    public void loginWithEmailAndPassWord() {
        if (checkRequirements())
            signInWithEmailAndPassword(et_email.getText().toString(), et_pass.getText().toString());
        else {
            pb_wating.setVisibility(View.INVISIBLE);
        }
    }

    private void loginComplete() {
        userID = mAuth.getUid();
        firebase.setUserId(userID);
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        firebase.getData(new OnGetDataListener() {
            @Override
            public void onStart() {
                progressDialog.setMessage("Đang tải");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }

            @Override
            public void onSuccess() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    navigateToMainActivity();
                }
            }
        });
    }

    public void register() {
        Intent signUp = new Intent(LoginActivity.this, SignUpActivity_1.class);
        startActivity(signUp);
    }

    public void forgotPass() {
        Intent forgotPass = new Intent(LoginActivity.this, ForgotPassActivity_1.class);
        startActivity(forgotPass);
    }

    private boolean checkRequirements() {
        Regex regex = new Regex();
        if (et_email.getText().toString().isEmpty()) {
            et_email.setError(getString(R.string.please_enter_email));
            et_email.requestFocus();
            return false;
        } else if (!regex.validateEmail(et_email.getText().toString())) {
            et_email.setError(getString(R.string.wrong_email_format));
            et_email.requestFocus();
            return false;
        } else if (et_pass.getText().toString().isEmpty()) {
            et_pass.setError(getString(R.string.please_enter_pass));
            et_pass.requestFocus();
            return false;
        }
        return true;
    }

    void loadLanguage() {
        String langPref = "lang_code";
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");

        Log.e("language", language);

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void navigateToMainActivity () {
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}