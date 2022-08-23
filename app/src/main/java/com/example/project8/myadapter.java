package com.example.project8;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<house,myadapter.myviewholder> {

    Context context;

    public myadapter(@NonNull FirebaseRecyclerOptions<house> options,Context context) {
        super(options);

        this.context = context;


    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull house model) {





        holder.place.setText(model.getPlace());
        holder.address.setText(model.getAddress());
        holder.price.setText(model.getPrice());
        Glide.with(holder.image.getContext()).load(model.getImageUrl()).apply(new RequestOptions().error(R.drawable.network_check_24).fitCenter()).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Buy_Home.class);
                intent.putExtra("place",model.getPlace());
                intent.putExtra("address",model.getAddress());
                intent.putExtra("price",model.getPrice());
                intent.putExtra("imageUrl",model.getImageUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new myviewholder(view);
    }

    class  myviewholder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView place,address,price;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            place = (TextView)itemView.findViewById(R.id.textplace);
            address = (TextView)itemView.findViewById(R.id.textaddress);
            price = (TextView)itemView.findViewById(R.id.textprice);

        }
    }
}
