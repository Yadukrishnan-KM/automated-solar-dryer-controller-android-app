package com.example.one;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SpalshActivity extends AppCompatActivity {

    Animation topAnim,bottomAnim,handAnim;
    ImageView logo,hand;
    TextView name,text;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalsh);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        handAnim = AnimationUtils.loadAnimation(this,R.anim.backgroundhand_animation);

        logo = findViewById(R.id.spalsh_logo);
        name = findViewById(R.id.spalsh_name);
        hand = findViewById(R.id.spalsh_hand);
        text = findViewById(R.id.spalsh_bottom_text);

        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);
        hand.setAnimation(handAnim);
        text.setAnimation(bottomAnim);

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SpalshActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        },2000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
