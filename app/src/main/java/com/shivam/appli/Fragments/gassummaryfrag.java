package com.shivam.appli.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shivam.appli.ADMIN.gasoverwrite;
import com.shivam.appli.Java_objects.gas_object;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.appli.R;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class gassummaryfrag extends Fragment {

String strdate="";
Context context;
double from,to;

    public gassummaryfrag() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public gassummaryfrag(String date, Context context) {
        this.strdate=date;
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_gassummaryfrag, container, false);


        int year=Integer.valueOf(strdate.substring(0,4));
        int month=Integer.valueOf(strdate.substring(4,6));
        int date=Integer.valueOf(strdate.substring(6));
        TextView datetime=rootview.findViewById(R.id.datetime);
        datetime.setText(date+"/"+month+"/"+year);

        final TextView input=rootview.findViewById(R.id.input);
        final TextView difference=rootview.findViewById(R.id.difference);
        final TextView scm=rootview.findViewById(R.id.scm);
        final TextView mmbto=rootview.findViewById(R.id.mmbto);
        final TextView ride=rootview.findViewById(R.id.ride);
        final TextView bill=rootview.findViewById(R.id.bill);
        final TextView time=rootview.findViewById(R.id.time);


        final Button overwrite=rootview.findViewById(R.id.overwrite);
        overwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, gasoverwrite.class);
                i.putExtra("DATE",strdate);
                startActivity(i);


            }
        });



        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference("GASMUKTA").child("RANGE");
        myRef11.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                from =dataSnapshot.child("FROM").getValue(Double.class);
                to =dataSnapshot.child("TO").getValue(Double.class);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });




        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("GASMUKTA").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(date));

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    gas_object obj=dataSnapshot.getValue(gas_object.class);
                    input.setText((obj.getAinput()) + "");
                    difference.setText((obj.getBdifference()) + "");
                    scm.setText((obj.getCscm()) + "");
                    mmbto.setText((obj.getDmmbto()) + "");
                    ride.setText((obj.getEride()) + "");

                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                    String moneyString = formatter.format(Double.valueOf(obj.getFbill()));
                    bill.setText(moneyString);

                    time.setText(obj.getTime());
                    if(Double.valueOf(obj.getFbill())>=from&&Double.valueOf(obj.getFbill())<=to){
                        ride.setTextColor(Color.rgb(14,131,19));
                        bill.setTextColor(Color.rgb(14,131,19));
                    }else{
                        ride.setTextColor(Color.RED);
                        bill.setTextColor(Color.RED);
                    }
                }else{
                    input.setText("NULL");
                    difference.setText("NULL");
                    scm.setText("NULL");
                    mmbto.setText("NULL");
                    ride.setText("NULL");
                    bill.setText("NULL");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


        return  rootview;
    }

}
