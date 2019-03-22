package com.shivam.appli.ADMIN;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.appli.Java_objects.Tunneltank_object;
import com.shivam.appli.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Tunneloverwrite extends AppCompatActivity {

    ArrayList<Tunneltank_object> arr=new ArrayList<>();
    String date="";
    String path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunneloverwrite);


        date=getIntent().getStringExtra("DATE");
        path=getIntent().getStringExtra("PATHWAY");

        getallobjects();




    }


    public void getallobjects(){


//        try {
//            SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
//
//            Calendar c = Calendar.getInstance();
//            String today = myFormat.format(c.getTime());
//
//
//            Date todaydate=myFormat.parse(date);
//            Date newDate = new Date(todaydate.getTime() - 86400);
//
//             String getdate=myFormat.format(newDate);
//
//
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("OIL"+path);
//
//            Log.d("OBJECTS", "getallobjects: "+" "+getdate+" "+today);
//
//            int i=1;
//            long t=86400;
//            while (i<7){
//
//                Log.d("OBJECTS", "getallobjects: "+getdate);
//
//                final String finalGetdate = getdate;
//
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.child(finalGetdate.substring(0,4)).child(finalGetdate.substring(4,6)).child(finalGetdate.substring(6)).exists()){
//
//                            Tunneltank_object obj=dataSnapshot.child(finalGetdate.substring(0,4)).child(finalGetdate.substring(4,6)).child(finalGetdate.substring(6)).getValue(Tunneltank_object.class);
//                            arr.add(obj);
//
//                            Log.d("OBJECTS", "getallobjects: "+arr);
//
//
//                        }else{
//                            Log.d("OBJECTS", "NOTgetallobjects: "+arr);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//
//
//                    }
//                });
//
//
//                 Date xtodaydate=myFormat.parse(date);
//
//                 Log.d("OBJECTS", "getallobjects: olddate "+xtodaydate);
//                 Date  xnewDate = new Date(xtodaydate.getTime() + t);
//
//                 t=2*t;
//
//                 Log.d("OBJECTS", "getallobjects: newdate "+xnewDate);
//                 getdate=myFormat.format(xnewDate);
//
//                 i++;
//
//
//
//            }

//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }







    }




}
