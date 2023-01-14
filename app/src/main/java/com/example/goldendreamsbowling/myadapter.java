package com.example.goldendreamsbowling;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goldendreamsbowling.JavaFile.model;
import com.example.goldendreamsbowling.JavaFile.productModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final model model) {

      holder.pCode.setText(model.getPromoCode());

              holder.pCode.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      AppCompatActivity activity=(AppCompatActivity)view.getContext();
                      activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new promoDetail(model.getPromoCode(),model.getPromoValue())).addToBackStack(null).commit();
                  }
                  //
              });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);

        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        TextView pCode;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            pCode=itemView.findViewById(R.id.pCode);
        }
    }

}
