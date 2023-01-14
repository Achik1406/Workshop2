package com.example.goldendreamsbowling.JavaFile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goldendreamsbowling.PaymentAfterCart;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.Totebag_page;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PaymentCartAdapter extends FirebaseRecyclerAdapter<PayCartModel , PaymentCartAdapter.myviewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PaymentCartAdapter(@NonNull FirebaseRecyclerOptions<PayCartModel> options) {
        super(options);
    }

    FirebaseAuth ID;
    String UID;
    String converter;
    double total =0,itemPrice;

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull PayCartModel model) {


        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        holder.productName.setText(model.getName());
        holder.productPrice.setText(model.getPrice());
        itemPrice = Double.parseDouble(model.getPrice());
        Glide.with(holder.imageView.getContext()).load(model.getImage()).into(holder.imageView);
        total = total + itemPrice;
        converter = String.valueOf(total);
        //PayCart();

    }

    private void PayCart() {

        FirebaseDatabase.getInstance().getReference().child("AddToCart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabase.getInstance().getReference().child("AddTotal").child(UID).child("Total").setValue(converter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentcartcontainer,parent,false);
        return new myviewholder(view);
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productName,productPrice;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cartImage);
            productName = itemView.findViewById(R.id.pName);
            productPrice = itemView.findViewById(R.id.pPrice);
        }

    }
}
