package com.example.project8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btn_reg,btn_sign,forgot;
    EditText editTextemail_ID,editTextpassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_sign =(Button)findViewById(R.id.btn_sign);
        editTextemail_ID = (EditText)findViewById(R.id.et2);
        editTextpassword = (EditText)findViewById(R.id.et4);
        btn_reg = (Button) findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(this);
        btn_sign.setOnClickListener(this);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        forgot = (Button)findViewById(R.id.forgot);
        forgot.setOnClickListener(this);


    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reg:
            startActivity(new Intent(this,Resistration.class));
            break;
            case R.id.btn_sign:
                userLogin();
                break;
            case R.id.forgot:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
        }

    }
    public void userLogin(){
        String email_ID = editTextemail_ID.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();

        if(email_ID.isEmpty()){
            editTextemail_ID.setError("Email is required!");
            editTextemail_ID.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextpassword.setError("Password is required!");
            editTextpassword.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            editTextpassword.setError("Password must be at least 6 character");
            editTextpassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email_ID,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                startActivity(new Intent(Login.this,Home.class));
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }else{
                                user.sendEmailVerification();
                                Toast.makeText(Login.this,"Check your mail for Verification",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }else {
                            Toast.makeText(Login.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}