package com.shivam.appli.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.appli.Java_objects.Tunneltank_object;
import com.shivam.appli.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tunnelsummaryfrag extends Fragment {

    String strdate="";
    Context context;
    double from,to;
    String pathway="";

    public Tunnelsummaryfrag() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Tunnelsummaryfrag(String date, Context context,String pathway) {
        this.strdate=date;
        this.context=context;
        this.pathway=pathway;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =inflater.inflate(R.layout.fragment_tunnelsummaryfrag, container, false);

        int year=Integer.valueOf(strdate.substring(0,4));
        int month=Integer.valueOf(strdate.substring(4,6));
        int date=Integer.valueOf(strdate.substring(6));
        TextView datetime=rootview.findViewById(R.id.datetime);
        datetime.setText(date+"/"+month+"/"+year);

        final TextView input=rootview.findViewById(R.id.input);
        final TextView difference=rootview.findViewById(R.id.difference);
        final TextView trolly=rootview.findViewById(R.id.trolly);
        final TextView output1=rootview.findViewById(R.id.output1);
        final TextView output2=rootview.findViewById(R.id.output2);
        final TextView time=rootview.findViewById(R.id.time);




        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference("OIL"+pathway).child("RANGE");
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
        final DatabaseReference myRef1 = database1.getReference("OIL"+pathway).child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(date));

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    Tunneltank_object obj=dataSnapshot.getValue(Tunneltank_object.class);
                    input.setText(String.format("%.2f",(float)obj.getBreading()) + "");
                    difference.setText(String.format("%.2f",(float)obj.getDdiff()) + "");
                    trolly.setText(String.format("%.2f",(float)obj.getCtrolly()) + "");
                    output1.setText(String.format("%.2f",(float)obj.getEoutput1()) + "");
                    output2.setText(String.format("%.2f",(float)obj.getFoutput2()) + "");
                    time.setText(obj.getAtime());

                    if((float)obj.getFoutput2()>=from&&(float)obj.getFoutput2()<=to){
                        output2.setTextColor(Color.rgb(14,131,19));
                        input.setTextColor(Color.rgb(14,131,19));
                    }else {
                        output2.setTextColor(Color.RED);
                        input.setTextColor(Color.RED);
                    }
                }else{
                    input.setText("NULL");
                    difference.setText("NULL");
                    output2.setText("NULL");
                    output1.setText("NULL");
                    trolly.setText("NULL");
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