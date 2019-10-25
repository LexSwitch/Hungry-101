package com.nalexander240.hungry101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nalexander240.hungry101.Model.User;

public class SignUp extends AppCompatActivity {

    TextInputLayout phone, password, name;
    TextInputEditText phoneTxt, passwordTxt, nameTxt;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        phone = (TextInputLayout) findViewById(R.id.phone);
        password = (TextInputLayout) findViewById(R.id.password);
        name = (TextInputLayout) findViewById(R.id.name);

        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        phoneTxt = (TextInputEditText) findViewById(R.id.phoneTxt);
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt);
        nameTxt = (TextInputEditText) findViewById(R.id.nameTxt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please wait...");
                mDialog.setCancelable(false);
                mDialog.show();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                //check for internet connection
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                    mDialog.dismiss();
                    Toast.makeText(SignUp.this, "Check internet connection.", Toast.LENGTH_SHORT).show();
                }

                if (phoneTxt.equals("")) {
                    Toast.makeText(SignUp.this, "Enter phone number.", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else if (passwordTxt.equals("")) {
                    Toast.makeText(SignUp.this, "Enter password.", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else if (nameTxt.equals("")) {
                    Toast.makeText(SignUp.this, "Enter name.", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else {
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(phoneTxt.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone number already registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(nameTxt.getText().toString(), passwordTxt.getText().toString());
                                table_user.child(phoneTxt.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Account created successfully.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this, SignIn.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
