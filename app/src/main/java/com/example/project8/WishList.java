package com.example.project8;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.FirebaseDatabase;

public class WishList extends AppCompatActivity {

    RecyclerView recyclerView;
    myadapter adapter;
    Button btnback;
    SearchView searchView;
    TextView textView;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        searchView = (SearchView) findViewById(R.id.et_search1);
        searchView.setQueryHint("Search for City....");
        textView=(TextView)findViewById(R.id.search_result);
        textView.setVisibility(View.GONE);
        imageView = (ImageView) findViewById(R.id.nothing);
        imageView.setVisibility(View.VISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                recyclerView = (RecyclerView) findViewById(R.id.searchbar);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                FirebaseRecyclerOptions<house> options
                        = new FirebaseRecyclerOptions.Builder<house>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Houses/All_Houses").orderByChild("place").startAfter(s).endAt(s + "\uf8ff"), house.class)
                        .build();

                adapter = new myadapter(options, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.startListening();

                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {

                recyclerView = (RecyclerView) findViewById(R.id.searchbar);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                FirebaseRecyclerOptions<house> options
                        = new FirebaseRecyclerOptions.Builder<house>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Houses/All_Houses").orderByChild("place").startAfter(s).endAt(s + "\uf8ff"), house.class)
                        .build();

                adapter = new myadapter(options, getApplicationContext());
                recyclerView.setAdapter(adapter);
                if (s.isEmpty()){
                    adapter.stopListening();
                    textView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                }else {
                    adapter.startListening();
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }


                return false;
            }
        });




        btnback = (Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
        finish();
    }
}