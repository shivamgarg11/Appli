package com.shivam.appli.ADMIN;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.Java_objects.Tunneltank_object;
import com.shivam.appli.Java_objects.Tunneltankconstant;
import com.shivam.appli.Java_objects.Tunneltanklastvalue;
import com.shivam.appli.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Tunneloverwrite extends AppCompatActivity {

    ArrayList<Tunneltank_object> arr=new ArrayList<>();
    ArrayList<String> arr1=new ArrayList<>();
    String date="";
    String path="";
    Tunneltankconstant[] constant = new Tunneltankconstant[1];
    final String[] lastdate = {""};
    final String[] over = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunneloverwrite);


        date=getIntent().getStringExtra("DATE");
        path=getIntent().getStringExtra("PATHWAY");



        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference("OIL"+path).child(Integer.valueOf(date.substring(0,4))+"").child(Integer.valueOf(date.substring(4,6))+"").child(Integer.valueOf(date.substring(6))+"");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tunneltank_object obj=dataSnapshot.getValue(Tunneltank_object.class);
                over[0] =obj.getGlastval();
                lastdate[0]=over[0];
                Log.d("TAG", "onDataChange: "+over[0]);
                getallobjects();
                getprevoiusdata();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });







        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);

        try {
            Date overwritedate =myFormat.parse(date);
            Date currentDate = c.getTime();
            long diff = TimeUnit.MILLISECONDS.toDays(currentDate.getTime() - overwritedate.getTime());

            if(diff>=7) {
                FancyToast.makeText(Tunneloverwrite.this,"PERMISSION DENIED (older than 7 days)  ", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                onBackPressed();
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }









        TextView headpath=findViewById(R.id.path);
        TextView datetime=findViewById(R.id.datetime);
        final EditText oilinput1=findViewById(R.id.oilinput1);
        final EditText oilinput2=findViewById(R.id.oilinput2);

        Button submit=findViewById(R.id.done);
        Button close=findViewById(R.id.close);


        headpath.setText("ADMIN/OIL/"+path+"/Change");
        datetime.setText(date.substring(6)+"/"+date.substring(4,6)+"/"+date.substring(0,4));


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        String strreading=oilinput1.getText().toString();
        String strtrolly=oilinput2.getText().toString();

        Double reading=Double.valueOf(strreading);
        Double trolly=Double.valueOf(strtrolly);


        Tunneltank_object mainobj=arr.remove(1);
        Tunneltank_object old=arr.remove(0);

        String startdate=arr1.remove(0)+" "+old.getAtime();
        String enddate=arr1.remove(0)+" "+mainobj.getAtime();
                long diff=1;

            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
                Date date1 = myFormat.parse(startdate);
                Date date2 = myFormat.parse(enddate);
                 diff = TimeUnit.MILLISECONDS.toMinutes(date2.getTime() - date1.getTime());
                if(diff==0)
                    diff=1;



            } catch (ParseException e) {
                e.printStackTrace();
            }



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("OIL"+path).child(enddate.substring(0,4)).child(Integer.valueOf(enddate.substring(4,6))+"").child(Integer.valueOf(enddate.substring(6,8))+"");


            Tunneltank_object obj = new Tunneltank_object(mainobj.getAtime(), String.format("%.2f",reading), String.format("%.2f",trolly), String.format("%.2f",(reading - Double.valueOf(old.getBreading()))), String.format("%.2f",((reading - Double.valueOf(old.getBreading())) * constant[0].getA())), String.format("%.2f",((((reading - Double.valueOf(old.getBreading())) * constant[0].getA()) * 24 * 60)/diff)),lastdate[0]);

               // Log.d("DATES", "onClick: "+mainobj.getAtime()+" "+ reading+" "+ trolly+" "+ (reading - old.getBreading())+" "+ (reading - old.getBreading()) * constant[0].getA()+" "+ (((reading - old.getBreading()) * constant[0].getA()) * 24 * 60)/diff);

                myRef.setValue(obj);




       if(arr.size()>0) {
           Tunneltank_object temp=arr.remove(0);
           startdate=enddate;
           enddate=arr1.remove(0)+" "+temp.getAtime();



           diff=1;

           try {
               SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
               Date date1 = myFormat.parse(startdate);
               Date date2 = myFormat.parse(enddate);
               diff = TimeUnit.MILLISECONDS.toMinutes(date2.getTime() - date1.getTime());
               if(diff==0)
                   diff=1;



           } catch (ParseException e) {
               e.printStackTrace();
           }

           Tunneltank_object obj1 = new Tunneltank_object(temp.getAtime(),temp.getBreading(),temp.getCtrolly(),String.format("%.2f",(Double.valueOf(temp.getBreading())- Double.valueOf(obj.getBreading()))),String.format("%.2f",((Double.valueOf(temp.getBreading()) - Double.valueOf(obj.getBreading())) * constant[0].getA())), String.format("%.2f",((((Double.valueOf(temp.getBreading()) - Double.valueOf(obj.getBreading())) * constant[0].getA()) * 24 * 60)/diff)),temp.getGlastval());

           Log.d("DATES", "onClick: "+obj1.getAtime()+" "+obj1.getBreading()+" "+obj1.getCtrolly()+" "+obj1.getDdiff()+" "+obj1.getEoutput1()+" "+obj1.getFoutput2());


            myRef = database.getReference("OIL"+path).child(enddate.substring(0,4)).child(Integer.valueOf(enddate.substring(4,6))+"").child(Integer.valueOf(enddate.substring(6,8))+"");

           myRef.setValue(obj1);




       }else{

           Tunneltanklastvalue last=new Tunneltanklastvalue(enddate.substring(6,8)+"/"+enddate.substring(4,6)+"/"+enddate.substring(0,4)+mainobj.getAtime(),obj.getBreading());
           myRef = database.getReference("OIL"+path).child("LASTVALUE");
           myRef.setValue(last);



       }


                FancyToast.makeText(Tunneloverwrite.this,"SUCCESSFUL OVERWRITING", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

                onBackPressed();

        }
            });




    }







    public void getprevoiusdata() {

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("OIL" + path).child("CONSTANTS");


        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                constant[0] = dataSnapshot.getValue(Tunneltankconstant.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }



    public void getallobjects(){


        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        String today = myFormat.format(c.getTime());


        try {


            over[0]=over[0].substring(6)+over[0].substring(3,5)+over[0].substring(0,2);
            Date overwritedate =myFormat.parse(over[0]);

            c.setTime(overwritedate);
            c.add(Calendar.DATE, 0);

        Date newDate = c.getTime();
        String getdate=myFormat.format(newDate);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("OIL"+path);

            Log.d("DATES", "getallobjects: "+path);


            while (getdate.compareTo(today)!=0){


                final String getstrdate=getdate;
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d("DATES", "getallobjects: 1 : ");

                        if(dataSnapshot.child(getstrdate.substring(0,4)).exists()) {
                            dataSnapshot = dataSnapshot.child(getstrdate.substring(0, 4));

                            Log.d("DATES", "getallobjects: 2 : ");


                            if (dataSnapshot.child(Integer.valueOf(getstrdate.substring(4, 6))+"").exists()) {
                                dataSnapshot = dataSnapshot.child(Integer.valueOf(getstrdate.substring(4, 6))+"");

                                Log.d("DATES", "getallobjects: 3 : ");

                                if (dataSnapshot.child(Integer.valueOf(getstrdate.substring(6))+"").exists()) {
                                    dataSnapshot = dataSnapshot.child(Integer.valueOf(getstrdate.substring(6))+"");

                                    Tunneltank_object obj=dataSnapshot.getValue(Tunneltank_object.class);
                                    arr.add(obj);
                                    arr1.add(getstrdate);

                                    Log.d("DATES", "getallobjects: array : "+arr);
                                    Log.d("DATES", "getallobjects: array1 : "+arr1);

                                }


                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("DATES", "Failed to read value.", error.toException());
                    }
                });








            Log.d("DATES", "getallobjects: "+getdate);

            c.add(Calendar.DATE, 1);
            newDate = c.getTime();
            getdate=myFormat.format(newDate);


        }


        } catch (ParseException e) {
            e.printStackTrace();
        }





    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(Tunneloverwrite.this, admin.class));
        finish();
    }

}
