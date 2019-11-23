package com.shivam.appli.Fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.ADMIN.Tunneloverwrite;
import com.shivam.appli.Java_objects.Tunneltank_object;
import com.shivam.appli.Java_objects.Tunneltankconstant;
import com.shivam.appli.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tunnelsummaryfrag extends Fragment {

    String strdate="";
    Context context;
    double from,to;
    String pathway="";
    Tunneltankconstant[] constant = new Tunneltankconstant[1];

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


        final Button overwrite=rootview.findViewById(R.id.overwrite);
        overwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, Tunneloverwrite.class);
                i.putExtra("DATE",strdate);
                i.putExtra("PATHWAY",pathway);
                startActivity(i);


            }
        });



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
                    input.setText((obj.getBreading()) + "");
                    difference.setText((obj.getDdiff()) + "");
                    trolly.setText((obj.getCtrolly()) + "");
                    output1.setText((obj.getEoutput1()) + " ");

                    NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "IN"));
                    String moneyString = formatter.format(Double.valueOf(obj.getFoutput2()));
                    output2.setText(moneyString+" ");

                    time.setText(obj.getAtime());

                    if(Double.valueOf(obj.getFoutput2())>=from&&Double.valueOf(obj.getFoutput2())<=to){
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
