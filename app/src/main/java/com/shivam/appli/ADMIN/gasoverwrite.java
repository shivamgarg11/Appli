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
import com.shivam.appli.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.shivam.appli.Java_objects.gas_object;
import com.shivam.appli.Java_objects.gasconstants;
import com.shivam.appli.Java_objects.gaslastvalue;



public class gasoverwrite extends AppCompatActivity {


    ArrayList<gas_object> arr=new ArrayList<>();
    ArrayList<String> arr1=new ArrayList<>();
    String date="";
    gasconstants[] constant = new gasconstants[1];
    final String[] lastdate = {""};
    final String[] over = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasoverwrite);





        date=getIntent().getStringExtra("DATE");

        Log.d("DATES", "onCreate: "+date);




        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference("GASMUKTA").child(Integer.valueOf(date.substring(0,4))+"").child(Integer.valueOf(date.substring(4,6))+"").child(Integer.valueOf(date.substring(6))+"");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gas_object obj=dataSnapshot.getValue(gas_object.class);
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
                FancyToast.makeText(gasoverwrite.this,"PERMISSION DENIED (older than 7 days)  ", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                onBackPressed();
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }






        TextView datetime=findViewById(R.id.datetime);
        final EditText oilinput1=findViewById(R.id.gasinput);

        Button submit=findViewById(R.id.done);
        Button close=findViewById(R.id.close);


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

                Double reading=Double.valueOf(strreading);


                gas_object mainobj=arr.remove(1);
                gas_object old=arr.remove(0);

                String startdate=arr1.remove(0)+" "+old.getTime();
                String enddate=arr1.remove(0)+" "+mainobj.getTime();
                long diff=1;

                try {
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
                    Date date1 = myFormat.parse(startdate);
                    Date date2 = myFormat.parse(enddate);
                    diff = TimeUnit.MILLISECONDS.toHours(date2.getTime() - date1.getTime());
                    if(diff==0)
                        diff=1;




                } catch (ParseException e) {
                    e.printStackTrace();
                }



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("GASMUKTA").child(enddate.substring(0,4)).child(Integer.valueOf(enddate.substring(4,6))+"").child(Integer.valueOf(enddate.substring(6,8))+"");

                gas_object obj = new gas_object();
                obj.setAinput(String.format("%.2f",reading));
                obj.setBdifference(String.format("%.2f",reading-Double.valueOf(old.getAinput())));
                obj.setCscm(String.format("%.2f",Double.valueOf(obj.getBdifference())*constant[0].getC1()));
                obj.setDmmbto(String.format("%.2f",(Double.valueOf(obj.getCscm())*constant[0].getC2()*constant[0].getC3())/constant[0].getC5()));
                obj.setEride(String.format("%.2f",(Double.valueOf(obj.getDmmbto())*constant[0].getC4()*24)/diff));
                obj.setTime(mainobj.getTime());
                obj.setFbill(String.format("%.2f",(Double.valueOf(obj.getEride())*15*24)/diff));
                obj.setGlastval(lastdate[0]);



               myRef.setValue(obj);




                if(arr.size()>0) {
                    gas_object temp=arr.remove(0);
                    startdate=enddate;
                    enddate=arr1.remove(0)+" "+temp.getTime();



                    diff=1;

                    try {
                        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
                        Date date1 = myFormat.parse(startdate);
                        Date date2 = myFormat.parse(enddate);
                        diff = TimeUnit.MILLISECONDS.toHours(date2.getTime() - date1.getTime());
                        if(diff==0)
                            diff=1;




                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                    gas_object obj1 = new gas_object();
                    obj1.setAinput(temp.getAinput());
                    obj1.setBdifference(String.format("%.2f",Double.valueOf(temp.getAinput())- Double.valueOf(obj.getAinput())));
                    obj1.setCscm(String.format("%.2f",Double.valueOf(obj1.getBdifference())*constant[0].getC1()));
                    obj1.setDmmbto(String.format("%.2f",(Double.valueOf(obj1.getCscm())*constant[0].getC2()*constant[0].getC3())/constant[0].getC5()));
                    obj1.setEride(String.format("%.2f",Double.valueOf(obj1.getDmmbto())*constant[0].getC4()));
                    obj1.setTime(temp.getTime());
                    obj1.setFbill(String.format("%.2f",(Double.valueOf(obj1.getEride())*15*24)/diff));
                    obj1.setGlastval(temp.getGlastval());



                    myRef = database.getReference("GASMUKTA").child(enddate.substring(0,4)).child(Integer.valueOf(enddate.substring(4,6))+"").child(Integer.valueOf(enddate.substring(6,8))+"");

                    myRef.setValue(obj1);


                    Log.d("DATES", "onClick: "+obj.getTime()+" "+obj.getAinput()+" "+obj.getBdifference()+" "+obj.getCscm()+" "+obj.getDmmbto()+" "+obj.getEride()+" "+obj.getFbill()+" ");

                    Log.d("DATES", "onClick: "+obj1.getTime()+" "+obj1.getAinput()+" "+obj1.getBdifference()+" "+obj1.getCscm()+" "+obj1.getDmmbto()+" "+obj1.getEride()+" "+obj1.getFbill()+" ");





                }else{

                    gaslastvalue last=new gaslastvalue(enddate.substring(6,8)+" "+enddate.substring(4,6)+" "+enddate.substring(0,4)+mainobj.getTime(),obj.getAinput());
                    myRef = database.getReference("GASMUKTA").child("LASTVALUE");
                    myRef.setValue(last);



                }

                FancyToast.makeText(gasoverwrite.this,"SUCCESSFUL OVERWRITING", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

                onBackPressed();

            }
        });




    }







    public void getprevoiusdata() {

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("GASMUKTA").child("CONSTANTS");


        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                constant[0] = dataSnapshot.getValue(gasconstants.class);
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
            DatabaseReference myRef = database.getReference("GASMUKTA");



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

                                    gas_object obj=dataSnapshot.getValue(gas_object.class);
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
        startActivity(new Intent(gasoverwrite.this, admin.class));
        finish();
    }

}
