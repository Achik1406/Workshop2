package com.example.goldendreamsbowling.JavaFile;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goldendreamsbowling.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;


public class viewmerchpayment extends FirebaseRecyclerAdapter<ViewModel , viewmerchpayment.myviewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public viewmerchpayment(@NonNull FirebaseRecyclerOptions<ViewModel> options) {
        super(options);
    }

    FirebaseAuth ID;
    String UID;
    String converter;
    double total =0,itemPrice;

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull ViewModel model) {


        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        holder.productName.setText(model.getName());
        holder.productPrice.setText("RM "+model.getPrice());
        itemPrice = Double.parseDouble(model.getPrice());
        Glide.with(holder.imageView.getContext()).load(model.getImage()).into(holder.imageView);
        total = total + itemPrice;
        converter = String.valueOf(total);
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
