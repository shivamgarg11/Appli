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
import com.shivam.appli.Java_objects.electricity_object;
import com.shivam.appli.Java_objects.electricityconstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class electricityoverwrite extends AppCompatActivity {

    ArrayList<electricity_object> arr=new ArrayList<>();
    ArrayList<String> arr1=new ArrayList<>();
    String date="";
    String path="";
    electricityconstants[] constant = new electricityconstants[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricityoverwrite);






        date=getIntent().getStringExtra("DATE");
        path=getIntent().getStringExtra("PATHWAY");

        getallobjects();
        getprevoiusdata();

        TextView headpath=findViewById(R.id.path);
        TextView datetime=findViewById(R.id.datetime);
        final EditText oilinput1=findViewById(R.id.oilinput1);
        final EditText oilinput2=findViewById(R.id.oilinput2);

        Button submit=findViewById(R.id.done);
        Button close=findViewById(R.id.close);


        headpath.setText("ADMIN/ELECTRICITY/"+path+"/Change");
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


                EditText electricityinput1 = findViewById(R.id.electricityinput1);
                String datastr1=electricityinput1.getText().toString()+"";


                EditText electricityinput2 = findViewById(R.id.electricityinput2);
                String datastr2=electricityinput2.getText().toString()+"";


                EditText electricityinput3 = findViewById(R.id.electricityinput3);
                String datastr3=electricityinput3.getText().toString()+"";


                EditText electricityinput4 = findViewById(R.id.electricityinput4);
                String datastr4=electricityinput4.getText().toString()+"";


                final double data1 = Double.valueOf(datastr1);
                final double data2 = Double.valueOf(datastr2);
                final double data3 = Double.valueOf(datastr3);
                final double data4 = Double.valueOf(datastr4);


                electricity_object mainobj=arr.remove(1);
                electricity_object old=arr.remove(0);

                String startdate=arr1.remove(0)+" "+old.getTime();
                String enddate=arr1.remove(0)+" "+mainobj.getTime();
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
                DatabaseReference myRef = database.getReference("ELECTRICITY"+path).child(enddate.substring(0,4)).child(Integer.valueOf(enddate.substring(4,6))+"").child(Integer.valueOf(enddate.substring(6,8))+"");

                electricity_object obj = new electricity_object();
                obj.setAkwh(data1);
                obj.setCkvah(data2);
                obj.setEmpf(data3);
                obj.setFppf(data4);
                obj.setGcal_pf( (data1-old.getAkwh())/(data2-old.getCkvah()));
                obj.setBdiffkwh( (data1-old.getAkwh()));

                double diffkvah=data2-old.getCkvah();

                obj.setDdiffkvah((diffkvah));
                obj.setTime(mainobj.getTime());

                    obj.setHamount1( (diffkvah*constant[0].getC1()*constant[0].getC3()));
                    obj.setIamount2( ((diffkvah*constant[0].getC2()*constant[0].getC3()*24)/diff));


                myRef.setValue(obj);




                if(arr.size()>0) {
                    electricity_object temp=arr.remove(0);
                    startdate=enddate;
                    enddate=arr1.remove(0)+" "+temp.getTime();



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

                    electricity_object obj1 = new electricity_object();
                    obj1.setAkwh(temp.getAkwh());
                    obj1.setCkvah(temp.getCkvah());
                    obj1.setEmpf(temp.getEmpf());
                    obj1.setFppf(temp.getFppf());
                    obj1.setGcal_pf( (temp.getAkwh()-obj.getAkwh())/(temp.getCkvah()-obj.getCkvah()));
                    obj1.setBdiffkwh( (temp.getAkwh()-obj.getAkwh()));

                    diffkvah=temp.getCkvah()-obj1.getCkvah();

                    obj1.setDdiffkvah((diffkvah));
                    obj1.setTime(temp.getTime());

                    obj1.setHamount1( (diffkvah*constant[0].getC1()*constant[0].getC3()));
                    obj1.setIamount2( ((diffkvah*constant[0].getC2()*constant[0].getC3()*24)/diff));


                    myRef = database.getReference("ELECTRICITY"+path).child(enddate.substring(0,4)).child(Integer.valueOf(enddate.substring(4,6))+"").child(Integer.valueOf(enddate.substring(6,8))+"");


                    Log.d("DATES", "onClick: "+obj.getAkwh()+" "+obj.getBdiffkwh()+" "+obj.getCkvah()+" "+obj.getDdiffkvah()+" "+obj.getEmpf()+" "+obj.getFppf()+" "+obj.getGcal_pf()+" "+obj.getHamount1()+" "+obj.getIamount2()+" ");
                    Log.d("DATES", "onClick: "+obj1.getAkwh()+" "+obj1.getBdiffkwh()+" "+obj1.getCkvah()+" "+obj1.getDdiffkvah()+" "+obj1.getEmpf()+" "+obj1.getFppf()+" "+obj1.getGcal_pf()+" "+obj1.getHamount1()+" "+obj1.getIamount2()+" ");



                    myRef.setValue(obj1);




                }

                FancyToast.makeText(electricityoverwrite.this,"SUCCESSFUL OVERWRITING", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

                onBackPressed();

            }
        });




    }







    public void getprevoiusdata() {

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("ELECTRICITY" + path).child("CONSTANTS");


        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                constant[0] = dataSnapshot.getValue(electricityconstants.class);
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
            Date overwritedate =myFormat.parse(date);

            c.setTime(overwritedate);
            c.add(Calendar.DATE, -1);

            Date newDate = c.getTime();
            String getdate=myFormat.format(newDate);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("ELECTRICITY"+path);

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

                                    electricity_object obj=dataSnapshot.getValue(electricity_object.class);
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
        startActivity(new Intent(electricityoverwrite.this, admin.class));
        finish();
    }

}