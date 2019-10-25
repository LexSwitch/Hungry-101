package com.nalexander240.hungry101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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

public class SignIn extends AppCompatActivity {

    TextInputLayout phone, password;
    TextInputEditText phoneTxt, passwordTxt;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phone = (TextInputLayout) findViewById(R.id.phone);
        password = (TextInputLayout) findViewById(R.id.password);
        signInBtn = (Button) findViewById(R.id.signInBtn);

        phoneTxt = (TextInputEditText) findViewById(R.id.phoneTxt);
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
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
                    Toast.makeText(SignIn.this, "Check internet connection.", Toast.LENGTH_SHORT).show();
                }

                if (phoneTxt.equals("")) {
                    Toast.makeText(SignIn.this, "Enter phone number.", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else if (passwordTxt.equals("")) {
                    Toast.makeText(SignIn.this, "Enter password.", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            //check if user exists
                            if (dataSnapshot.child(phoneTxt.getText().toString()).exists()) {
                                //get user info

                                mDialog.dismiss();

                                User user = dataSnapshot.child(phoneTxt.getText().toString()).getValue(User.class);
                                if (user.getPassword().equals(passwordTxt.getText().toString())) {
                                    Toast.makeText(SignIn.this, "Sign in successful.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignIn.this, "Sign in failed.Wrong password.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "User does not exist.", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            }
        });
    }
}
