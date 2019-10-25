package com.nalexander240.hungry101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button signInBtn, signUpBtn;
    TextView sloganTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = (Button) findViewById(R.id.signInBtn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        sloganTxt = (TextView) findViewById(R.id.sloganTxt);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        sloganTxt.setTypeface(face);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });
    }
}
