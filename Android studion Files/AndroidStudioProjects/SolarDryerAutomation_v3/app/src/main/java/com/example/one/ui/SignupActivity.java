package com.example.one.ui;

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

import com.example.one.LoginActivity;

import com.example.one.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity{
    private EditText editTextName, editTextEmail, editTextPassword, editTextReenterPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        isOnline();
        offlineDialogBox();

        editTextName = findViewById(R.id.signupLinearlayout1Card1Linearlayout1EditText1);
        editTextEmail = findViewById(R.id.signupLinearlayout1Card1Linearlayout1Edittext2);
        editTextPassword = findViewById(R.id.signupLinearlayout1Card1Linearlayout1Edittext3);
        editTextReenterPassword = findViewById(R.id.signupLinearlayout1Card1Linearlayout1Edittext4);
        Button bottonSignup = findViewById(R.id.signupLinearlayout1Card1Linearlayout1Button1);
        TextView textToLogin = findViewById(R.id.textView9);

        progressDialog = new ProgressDialog(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        bottonSignup.setOnClickListener(v -> Registration());
        textToLogin.setOnClickListener(v -> {
            Intent intent = new  Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });


    }

    private void Registration() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String reenterpassword = editTextReenterPassword.getText().toString();

        if (!email.matches(emailPattern)){
            editTextEmail.setError("Enter correct email address");
        }
        else if (password.isEmpty() || password.length()<6){
            editTextPassword.setError("Invalid password, minimum length: 8");
        }
        else if (!password.equals(reenterpassword)){
            editTextReenterPassword.setError("passwords must be same");
        }
        else{
            progressDialog.setMessage("Registering...");
            progressDialog.setTitle("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(SignupActivity.this,"Registration Successful, Login to Continue",Toast.LENGTH_LONG).show();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,"Failed: "+task.getException(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new  Intent(SignupActivity.this, LoginActivity.class);
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("Internet not available, Check your connection and Try again ...");
            builder.setTitle("No Internet");
            builder.setIcon(R.mipmap.ic_nonetwok_foreground);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.create();
            builder.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}