package com.example.project8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Buy_Home extends AppCompatActivity {

    ImageView Image;
    TextView textplace,textaddress,textprice;
    Button buy,btnback,homee;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy__home);

        homee = (Button) findViewById(R.id.homee);
        homee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });


        Image = findViewById(R.id.varun);
        textplace = findViewById(R.id.textplace);
        textaddress = findViewById(R.id.textaddress);
        textprice = findViewById(R.id.textprice);
        btnback = findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buy = findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        Glide.with(Image.getContext()).load(getIntent().getStringExtra("imageUrl")).apply(new RequestOptions().error(R.drawable.network_check_24).fitCenter()).into(Image);
        textplace.setText(getIntent().getStringExtra("place"));
        textaddress.setText(getIntent().getStringExtra("address"));
        textprice.setText(getIntent().getStringExtra("price"));
    }

    private void sendMail() {

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String mobile_no = userProfile.mobile_no;
                    String fullname = userProfile.fullname;


                    String subject = "I want to Buy this Home";
                    String message0 = getIntent().getStringExtra("place");
                    String message1 = getIntent().getStringExtra("address");
                    String message2 = getIntent().getStringExtra("price");
                    String message3 = mobile_no;
                    String message4 = fullname;
                    String message = "\n"+"Address:    "+message0+"\n"+"\n"+"Place:    "+message1+"\n"+"\n"+"Price:    "+message2+"\n"+"\n"+"My Mobile:    "+message3+"\n"+"\n"+"Full Name:    "+message4;


                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "vrnbhosle@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, message);


                    intent.setType("message/rfc822");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "Choose an email client"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}