package com.example.project8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Homes_In_Delhi extends AppCompatActivity {

    RecyclerView recyclerView;
    myadapter adapter;
    Button btnback,homee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homes__in__delhi);

        homee = (Button) findViewById(R.id.homee);
        homee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });

        btnback = (Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.homelist2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<house> options
                = new FirebaseRecyclerOptions.Builder<house>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Houses/All_Houses").orderByChild("place").startAt("Delhi").endAt("Delhi"), house.class)
                .build();

        adapter = new myadapter(options,getApplicationContext());
        recyclerView.setAdapter(adapter);


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
}