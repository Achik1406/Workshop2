package com.example.goldendreamsbowling.JavaFile;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goldendreamsbowling.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;


public class ViewBookAdapter extends FirebaseRecyclerAdapter<ViewBookModel , ViewBookAdapter.myviewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ViewBookAdapter(@NonNull FirebaseRecyclerOptions<ViewBookModel> options) {
        super(options);
    }

    FirebaseAuth ID;
    String UID;
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull ViewBookModel model) {
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        holder.date.setText("Date : "+model.getDate());
        holder.email.setText("Email : "+model.getEmail());
        holder.fullname.setText("Fullname : "+model.getFullName());
        holder.lane.setText("Lane : "+model.getLane());
        holder.Numgame.setText("Number Of game : "+model.getNumberGame());
        holder.numplayer.setText("Number of Player : "+model.getNumberPlayer());
        holder.numshoes.setText("Number of Shoes : "+model.getNumberShoes());
        holder.paymentid.setText("Payment ID : "+model.getPaymentID());
        holder.time.setText("Time : "+model.getTime());
        holder.totalprice.setText("Total Price : RM"+model.getTotalPrice());
    }





    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewcontainer,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView date,email,fullname,lane,Numgame,numplayer,numshoes,paymentid,time,totalprice;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            email = itemView.findViewById(R.id.email);
            fullname = itemView.findViewById(R.id.fullname);
            lane = itemView.findViewById(R.id.Lane);
            Numgame = itemView.findViewById(R.id.Numbergame);
            numplayer = itemView.findViewById(R.id.NumberPlayer);
            numshoes = itemView.findViewById(R.id.Numbershoes);
            paymentid = itemView.findViewById(R.id.PaymentID);
            time = itemView.findViewById(R.id.Time);
            totalprice = itemView.findViewById(R.id.totalprice);

        }

    }
}
