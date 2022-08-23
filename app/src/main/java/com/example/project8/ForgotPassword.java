package com.example.project8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText et1;
    Button bt1,btnback;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnback = (Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et1 = (EditText)findViewById(R.id.email_ID);
        bt1 = (Button) findViewById(R.id.resetpassword);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }
    public void onBackPressed() {

        finish();
    }
    private void resetPassword(){
        String email_ID = et1.getText().toString().trim();
        if(email_ID.isEmpty()){
            et1.setError("Email is required!");
            et1.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_ID).matches()){
            et1.setError("Please provide valid email");
            et1.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email_ID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check your email to reset your password",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(ForgotPassword.this,"Try again! Something wrong happened",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}