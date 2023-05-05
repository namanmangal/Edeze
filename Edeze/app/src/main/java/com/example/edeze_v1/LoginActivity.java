package com.example.edeze_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

// db
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    EditText et_login_email,et_login_pw;
    Button buttonLoginEmail, buttonLoginFingerprint;
    String pw_string,email_string;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On Create");
        setContentView(R.layout.activity_login);

        // set UI elements
        buttonLoginEmail = findViewById(R.id.loginEmailButton);
        buttonLoginFingerprint = findViewById(R.id.loginFingerprintButton);
        et_login_email = findViewById(R.id.loginEmailEditText);
        et_login_pw = findViewById(R.id.loginPasswordEditText);

        // init FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        executor = ContextCompat.getMainExecutor(this);

        buttonLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_string = et_login_email.getText().toString();
                pw_string = et_login_pw.getText().toString();
                logIn(email_string,pw_string);
            }
        });

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        boolean isLogin=sharedPreferences.getBoolean("isLogin", false);
        if(isLogin){
            buttonLoginFingerprint.setVisibility(View.VISIBLE);
        }

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                String email=sharedPreferences.getString("email", "");
                String password=sharedPreferences.getString("password", "");
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                logIn(email,password);
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        buttonLoginFingerprint.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"On Start");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"On Resume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG,"On Pause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"On Stop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"On Destroy");
    }

    public void loginEmailClick (View view) {
        email_string = et_login_email.getText().toString();
        pw_string = et_login_pw.getText().toString();
        logIn(email_string,pw_string);
    }

    void logIn(String email,String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putBoolean("isLogin", true);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Authenticated.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,QAActivity.class);
                    startActivity(intent);
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}