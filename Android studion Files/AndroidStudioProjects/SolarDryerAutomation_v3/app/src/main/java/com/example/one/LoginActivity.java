package com.example.one;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.one.ui.ForgotpasswordActivity;
import com.example.one.ui.SignupActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity{

    private EditText editTextEmail, editTextPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        Objects.requireNonNull(getSupportActionBar()).hide();
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        isOnline();
        offlineDialogBox();

        editTextEmail = findViewById(R.id.loginCard1Linearlayout1Edittext1);
        editTextPassword = findViewById(R.id.loginCard1Linearlayout1Edittext2);
        Button buttonLogin = findViewById(R.id.loginCard1Linearlayout1Button1);
        TextView textForgotPassword = findViewById(R.id.loginCard1Linearlayout1Text4);
        TextView textGoogle = findViewById(R.id.loginCard1Linearlayout1Text5);
        Button buttonSignup = findViewById(R.id.loginCard1Linearlayout1Button2);

        progressDialog = new ProgressDialog(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



        buttonLogin.setOnClickListener(v -> Login());

        textForgotPassword.setOnClickListener(v -> {
            Intent intent = new  Intent(LoginActivity.this, ForgotpasswordActivity.class);
            startActivity(intent);
        });
        buttonSignup.setOnClickListener(v -> {
            Intent intent = new  Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        textGoogle.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, GoogleSigninActivity.class);
            startActivity(intent);
        });

    }

    private void Login() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (!email.matches(emailPattern)){
            editTextEmail.setError("Enter correct email address");
        }
        else if (password.isEmpty() || password.length()<6){
            editTextPassword.setError("Invalid password, minimum length: 8");
        }
        else{
            progressDialog.setMessage("LogIn...");
            progressDialog.setTitle("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(LoginActivity.this,"LogIn Success",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"LogIn Failed: "+task.getException(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new  Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void offlineDialogBox(){
        if(!isOnline()){
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Internet not available, Check your connection and Try again ...");
            builder.setTitle("No Internet");
            builder.setIcon(R.mipmap.ic_nonetwok_foreground);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.create();
            builder.show();
        }

    }

    @Override
    public void onDestroy() { super.onDestroy(); }


}
