package com.example.goldendreamsbowling;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldendreamsbowling.Booking.CheckOutBook;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link promoDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class promoDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth ID;
    String UID;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    String PromoCode,PromoValue;

    public promoDetail(String promoCode, String promoValue) {
        PromoCode = promoCode;
        PromoValue = promoValue;
    }

    public promoDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment promoDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static promoDetail newInstance(String param1, String param2) {
        promoDetail fragment = new promoDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_promo_detail, container, false);
        TextView promocode = view.findViewById(R.id.PromoCode);
        TextView promovalue = view.findViewById(R.id.PromoValue);
        Button button = view.findViewById(R.id.Claimpromo);

        promocode.setText("Promo Code : "+PromoCode);
        promovalue.setText("Promo Code : "+PromoValue);
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("ClaimedPromo").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            Toast.makeText(getContext(), "Cannot claim multiple coupon", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            database.child("ClaimedPromo").child(UID).child("PromoCode").setValue(PromoCode);
                            database.child("ClaimedPromo").child(UID).child("PromoValue").setValue(PromoValue);
                            database.child("Promotion").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        String datacode = dataSnapshot.child("PromoCode").getValue(String.class);
                                        String datavalue = dataSnapshot.child("PromoValue").getValue(String.class);
                                        if (PromoCode.equals(datacode)&&PromoValue.equals(datavalue)) {
                                            dataSnapshot.child("PromoValue").getRef().removeValue();
                                            dataSnapshot.child("Email").getRef().removeValue();
                                            dataSnapshot.child("PromoCode").getRef().removeValue();
                                        }
                                    }
                                    Toast.makeText(getContext(), "successful", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        Intent intent = new Intent(getContext(), HomePage.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }
}