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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Resistration extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextfullname, editTextemail_Id, editTextmobile_no,editTextpassword;
    Button Register, Sign_in;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resistration);
        mAuth = FirebaseAuth.getInstance();

        Sign_in = (Button)findViewById(R.id.btn_signin);
        Sign_in.setOnClickListener(this);
        Register = (Button)findViewById(R.id.btn2);
        Register.setOnClickListener(this);

        editTextfullname = (EditText) findViewById(R.id.et1);
        editTextemail_Id = (EditText) findViewById(R.id.et2);
        editTextmobile_no = (EditText) findViewById(R.id.et3);
        editTextpassword = (EditText) findViewById(R.id.et4);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signin:
                startActivity(new Intent(this,Login.class));
                finish();
                break;
            case R.id.btn2:
                registeruser();
                break;

        }
    }
    private void registeruser(){
        String fullname = editTextfullname.getText().toString().trim();
        String email_ID = editTextemail_Id.getText().toString().trim();
        String mobile_no = editTextmobile_no.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();


        if(fullname.isEmpty()){
            editTextfullname.setError("Full name is required");
            editTextfullname.requestFocus();
            return;
        }
        if(email_ID.isEmpty()){
            editTextemail_Id.setError("Email is required");
            editTextemail_Id.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_ID).matches()){
            editTextemail_Id.setError("Please provide valid email");
            editTextemail_Id.requestFocus();
            return;
        }
        if(mobile_no.isEmpty()){
            editTextmobile_no.setError("Mobile number is required");
            editTextmobile_no.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }
        if(password.length() < 6) {
            editTextpassword.setError("Password must be 6 character long");
            editTextpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_ID,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname,email_ID,mobile_no,password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Resistration.this,"User has been registered",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(),Login.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(Resistration.this,"Registration Failed",Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        }else {
                            Toast.makeText(Resistration.this,"Registration Failed,try again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}