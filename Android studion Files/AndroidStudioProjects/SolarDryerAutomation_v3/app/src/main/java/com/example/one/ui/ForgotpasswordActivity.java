package com.example.one.ui;

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

import java.util.Objects;

public class ForgotpasswordActivity extends AppCompatActivity{

    private EditText editTextEmail;
    private String email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate (Bundle savedInstanceState){
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        isOnline();
        offlineDialogBox();

        editTextEmail = findViewById(R.id.forgotpasswordLinearLayout1Card1Linearlayout1Text1);
        Button buttonSubmit = findViewById(R.id.forgotpasswordLinearLayout1Card1Linearlayout1Text2);
        TextView textToSignup = findViewById(R.id.textView10);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        buttonSubmit.setOnClickListener(v -> {
            email = editTextEmail.getText().toString();
            if (!email.matches(emailPattern)){
                editTextEmail.setError("Enter correct email address");
            }
            else {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotpasswordActivity.this, "Email send successfully, Check email.", Toast.LENGTH_LONG).show();
                                Intent intent = new  Intent(ForgotpasswordActivity.this, LoginActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(ForgotpasswordActivity.this, "Failed: "+task.getException(), Toast.LENGTH_LONG).show();
                            }
                        });
            }

        });

        textToSignup.setOnClickListener(v -> {
            Intent intent = new  Intent(ForgotpasswordActivity.this, SignupActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }


    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void offlineDialogBox(){
        if(!isOnline()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ForgotpasswordActivity.this);
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