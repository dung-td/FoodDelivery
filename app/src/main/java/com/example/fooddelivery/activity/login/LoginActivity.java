package com.example.fooddelivery.activity.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.main.MainActivity;
import com.example.fooddelivery.activity.me.LanguageSetting;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Regex;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {
    String uID;
    private static final String TAG = "GOOGLE SIGN IN";
    EditText et_email, et_pass;
    Button bt_login;
    TextView tv_forgotPass, tv_register;
    ImageView iv_register, iv_google_signin;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    FirebaseFirestore root;

    String[] languages;
    ArrayAdapter<String> languageAdapter;
    AutoCompleteTextView autoLanguage;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocal();
        setContentView(R.layout.activity_login);
        Init();
        createGoogleSignInRequest();
    }

    private void Init() {
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.lg_et_email);
        et_pass = findViewById(R.id.lg_et_password);
        bt_login = findViewById(R.id.lg_bt_login);
        tv_forgotPass = findViewById(R.id.lg_tv_forgetPass);
        tv_register = findViewById(R.id.lg_tv_register);
        iv_register = findViewById(R.id.lg_iv_register);
        iv_google_signin = findViewById(R.id.lg_bt_google);

        root = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);

        autoLanguage = findViewById(R.id.lg_atcp_language);
        languages = getResources().getStringArray(R.array.languages);
        languageAdapter = new ArrayAdapter<>(LoginActivity.this, R.layout.dropdown_item, languages);


        autoLanguage.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (WelcomeActivity.language.equals("vi"))
                    autoLanguage.setText(languages[1]);
                else
                    autoLanguage.setText(languages[0]);

                autoLanguage.setAdapter(languageAdapter);
            }
        }, 10);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage(getString(R.string.loging));
                progressDialog.show();
                loginWithEmailAndPassWord();
            }
        });

        iv_google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage(getString(R.string.loging));
                progressDialog.show();
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

        autoLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (languages[position].equals("Tiếng Việt")) {
                    setLocal(LoginActivity.this, "vi");
                    WelcomeActivity.language = "vi";
                } else{
                    setLocal(LoginActivity.this, "en");
                    WelcomeActivity.language = "en";
                }

                recreate();
//                finish();
//                startActivity(getIntent());
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
                progressDialog.dismiss();
                Toasty.error(LoginActivity.this, getString(R.string.login_fail)).show();
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
                            Log.d("GG", uID);
                            root.collection("User/")
                                    .document(uID)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.get("email") != null)
                                                loginComplete();
                                            else
                                                newUser(mAuth.getCurrentUser());
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toasty.error(LoginActivity.this, getString(R.string.login_fail)).show();
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
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toasty.error(LoginActivity.this, getResources().getString(R.string.email_pass_not_correct),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void newUser(FirebaseUser user) {
        Intent signUp1 = new Intent(LoginActivity.this, SignUpActivity_1.class);
        signUp1.putExtra("email", user.getEmail());
        signUp1.putExtra("uid", user.getUid());
        startActivity(signUp1);
    }

    public void loginWithEmailAndPassWord() {
        if (checkRequirements())
            signInWithEmailAndPassword(et_email.getText().toString(), et_pass.getText().toString());
        else {
            progressDialog.dismiss();
        }
    }

    private void loginComplete() {
        WelcomeActivity.userID = mAuth.getUid();
        WelcomeActivity.firebase.setUserId(WelcomeActivity.userID);
        WelcomeActivity.firebase.getData(new OnGetDataListener() {
            @Override
            public void onStart() {
                progressDialog.setMessage(getString(R.string.data_loading));
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
        signUp.putExtra("email", "");
        signUp.putExtra("uid", "");
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

    private void navigateToMainActivity() {
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
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

    public void setLocal(Activity activity, String langCode)
    {
        Locale locale = new Locale(langCode);
        createShareReferences(langCode);
        locale.setDefault(locale);

        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public void createShareReferences(String langCode)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("lang_code", langCode);  // Saving string

        editor.apply();

        Log.e("Save ", langCode);
    }
}