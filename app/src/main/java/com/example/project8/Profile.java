package com.example.project8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView profileImageView;
    Button b1,btnback;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;
    ProgressBar progressBar;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImageView =findViewById(R.id.dp);
        profileImageView.setOnClickListener(this);


        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        b1 = (Button)findViewById(R.id.resetpass);
        b1.setOnClickListener(this);
        btnback = (Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(this);



        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Users/images");
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();



        final TextView textViewname = (TextView) findViewById(R.id.fullname);
        final TextView textViewemail = (TextView) findViewById(R.id.email_ID);
        final TextView textViewmobile =(TextView)findViewById(R.id.mobile_no);


        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullname = userProfile.fullname;
                    String email_ID = userProfile.email_ID;
                    String mobile_no = userProfile.mobile_no;



                    textViewname.setText(fullname);
                    textViewemail.setText(email_ID);
                    textViewmobile.setText(mobile_no);





                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this,"Something happened wrong!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
        Userinfo();
    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
        finish();
    }

    private void Userinfo() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("Users/images/*"+userID);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                progressBar.setVisibility(View.GONE);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profileImageView.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetpass:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
            case R.id.btnback:
                startActivity(new Intent(this,Home.class));
                finish();
                break;
            case R.id.dp:
                startActivity(new Intent(this,EditProfile.class));
                break;
        }
    }
}