package com.example.goldendreamsbowling.JavaFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goldendreamsbowling.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CartAdapter extends FirebaseRecyclerAdapter<cartModel ,CartAdapter.myviewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CartAdapter(@NonNull FirebaseRecyclerOptions<cartModel> options) {
        super(options);
    }

    FirebaseAuth ID;
    Context context;
    String UID;
    String converter;
    double total =0,totalPrice=0,itemPrice,delItemPrice=0;
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull cartModel model) {
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        holder.productName.setText(model.getName());
        holder.productPrice.setText("RM "+model.getPrice());
        itemPrice = Double.parseDouble(model.getPrice());
        add add = new add();
        total += itemPrice;
        add.setAdda(total);
        Glide.with(holder.imageView.getContext()).load(model.getImage()).into(holder.imageView);
        holder.mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.imageView.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("AddToCart").child(UID)
                                .child(getRef(position).getKey()).removeValue();
                       delItemPrice += Double.parseDouble(model.getPrice());
                       add.setDelete(delItemPrice);
                       notifyItemRemoved(position);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });

       // totalPrice = total - delItemPrice;
        //converter = String.valueOf(totalPrice);
       // PayCart();

    }





    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartcontainer,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productName,productPrice,mDeleteImage;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cartImage);
            productName = itemView.findViewById(R.id.pName);
            productPrice = itemView.findViewById(R.id.pPrice);
            mDeleteImage =itemView.findViewById(R.id.txt_option);
        }

    }
}
