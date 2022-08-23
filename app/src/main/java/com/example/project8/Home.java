package com.example.project8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton mumbai , pune, delhi, bangalore, kolkata, chennai;
    RecyclerView recyclerView;
    myadapter adapter;
    Button button;
    private CircleImageView profileImageView;


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        button = (Button) findViewById(R.id.et_search);
        button.setBackgroundColor(Color.rgb(235, 235, 235));
        button.setText("Search your Dream");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WishList.class);
                startActivity(intent);
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.best_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<house> options
                = new FirebaseRecyclerOptions.Builder<house>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Best_Home"), house.class)
                .build();

        adapter = new myadapter(options,getApplicationContext());
        recyclerView.setAdapter(adapter);


        mumbai = findViewById(R.id.circle_mumbai);
        mumbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_In_Mumbai.class);
                startActivity(intent);
            }
        });
        pune = findViewById(R.id.circle_pune);
        pune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeInPune.class);
                startActivity(intent);
            }
        });
        delhi = findViewById(R.id.circle_delhi);
        delhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Homes_In_Delhi.class);
                startActivity(intent);
            }
        });
        bangalore = findViewById(R.id.circle_bangalore);
        bangalore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Homes_In_Bangalore.class);
                startActivity(intent);
            }
        });
        kolkata = findViewById(R.id.circle_kolkata);
        kolkata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Homes_In_Kolkata.class);
                startActivity(intent);
            }
        });
        chennai = findViewById(R.id.circle_chennai);
        chennai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Homes_In_Chennai.class);
                startActivity(intent);
            }
        });




        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Users/images");
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        final TextView textViewname = (TextView) header.findViewById(R.id.fullname);
        profileImageView =(CircleImageView) header.findViewById(R.id.dp);


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullname = userProfile.fullname;


                    textViewname.setText(fullname);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this,"Something happened wrong!",Toast.LENGTH_LONG).show();


            }
        });
        Userinfo();


    }



    private void Userinfo() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("Users/images/*"+userID);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profileImageView.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapter.startListening();
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Intent intent = new Intent(this,Profile.class);
                startActivity(intent);
                finish();
                break;
            case R.id.Customer_Services:
                Intent intent1 = new Intent(this,CustomerService.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.setting:
                Intent intent2 = new Intent(this,Setting.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(this,Login.class);
                startActivity(intent3);
                break;
        }
        return true;
    }

}


