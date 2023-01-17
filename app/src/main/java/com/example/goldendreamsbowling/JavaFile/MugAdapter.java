package com.example.goldendreamsbowling.JavaFile;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goldendreamsbowling.CircleAnimationUtil;
import com.example.goldendreamsbowling.Mug_page;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.Totebag_page;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class MugAdapter extends RecyclerView.Adapter<MugAdapter.MyViewHolder> {

    Context context;
    List<productModel> productModels;
    FloatingActionButton floatingActionButton;
    FirebaseAuth ID;
    String UID;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");

    public MugAdapter(Context context, List<productModel> productModels, FloatingActionButton floatingActionButton) {
        this.context = context;
        this.productModels = productModels;
        this.floatingActionButton = floatingActionButton;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.container,parent,false);
        return new MugAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String name =productModels.get(position).getName();
        String price = productModels.get(position).getPrice();
        String image  = productModels.get(position).getImage();
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();

        holder.productName.setText(name);
        holder.productPrice.setText("RM "+price);
        try {
            Picasso.get().load(image).into(holder.imageView);
        }catch (Exception e){

        }
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(name,price,image);

                new CircleAnimationUtil().attachActivity((Activity) context).setTargetView(holder.imageView).setMoveDuration(1000).setDestView(floatingActionButton).setAnimationListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ((Mug_page)context).recreate();
                        //((Tshirt_page)context).getcartCount();

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).startAnimation();

            }

        });
    }


    private void addToCart(String name, String price, String image) {

        Random random = new Random();
        String ItemId = String.valueOf(random.nextInt(200000));
        databaseReference.child("AddToCart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("AddToCart").child(UID).child(ItemId).child("Name").setValue(name);
                databaseReference.child("AddToCart").child(UID).child(ItemId).child("Price").setValue(price);
                databaseReference.child("AddToCart").child(UID).child(ItemId).child("Image").setValue(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView productName,productPrice;
        Button addToCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.pName);
            productPrice = itemView.findViewById(R.id.pPrice);
            addToCart = itemView.findViewById(R.id.addCartBtn);
        }
    }
}
