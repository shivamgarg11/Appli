package com.shivam.appli.ADMIN;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.Java_objects.Maintankobject;
import com.shivam.appli.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class maintankoverwrite extends AppCompatActivity {

    String time="";
    Maintankobject overwriteobj;

    double blackdes,bluedes,c11,c12,c21,c22,c31,c32;
    HashMap<String,Integer> map=new HashMap<>();

    double balance=0,issue=0,purchase=0,cms=0,difference=0;
    String particular="";

    LinearLayout linearpurchase,linearissue,linearcms;

    Button submit;
    int i=0;

   public static boolean done=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintankoverwrite);
        done=false;

        linearpurchase=findViewById(R.id.linearpurchase);
        linearissue=findViewById(R.id.linearissue);
        linearcms=findViewById(R.id.linearcms);


        getallconstants();


        final View dialogView = View.inflate(maintankoverwrite.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(maintankoverwrite.this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());


                TextView datetime=findViewById(R.id.datetime);
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd - HH:mm");
                time = df.format(calendar.getTimeInMillis())+"";
                datetime.setText(time);



                try {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, 1);
                    Date overwritedate =df.parse(time);
                    Date currentDate = c.getTime();
                    long diff = TimeUnit.MILLISECONDS.toDays(currentDate.getTime() - overwritedate.getTime());

                    if(diff>=7) {
                        FancyToast.makeText(maintankoverwrite.this,"PERMISSION DENIED (older than 7 days)  ", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        onBackPressed();
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }








                gettoverwriteobj();

                alertDialog.dismiss();
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();


        submit=findViewById(R.id.done);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i==1){
                    setpurchase();
                }else if(i==2){
                    setissue();
                }else if(i==3){
                    setcms();
                }

            }
        });


        Button close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }




    public void setpurchase(){

        RadioGroup oiltype,oilunit;

        oiltype=findViewById(R.id.oiltype);
        oilunit=findViewById(R.id.oilunit);


        EditText name=findViewById(R.id.name);
        final String strname=name.getText().toString();

        EditText gatepass=findViewById(R.id.gatepassno);
        final String strgatepass=gatepass.getText().toString();

        EditText weight=findViewById(R.id.weight);
        final String strweight=weight.getText().toString();


        if(strgatepass==null||strname==null||strweight==null){

            FancyToast.makeText(this, "INPUT CANNOT BE LEFT BLANK ", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

        } else {

            String stroiltype = "";
            int selectedId = oiltype.getCheckedRadioButtonId();
            if (selectedId == R.id.blackoil)
                stroiltype = "BLACK OIL";
            else
                stroiltype = "BLUE OIL";


            String strunit = "";
            int selectedId1 = oilunit.getCheckedRadioButtonId();
            if (selectedId1 == R.id.kg)
                strunit = "Kg";
            else
                strunit = "L";


            final String finalStroiltype = stroiltype;
            final String finalStrunit = strunit;
            final String finalStroiltype1 = stroiltype;
            final String finalStrunit1 = strunit;


            double pur = Double.valueOf(strweight);
            if(finalStrunit.length()==2&&finalStroiltype.length()==9){
                pur/=blackdes;
            }
            if(finalStrunit.length()==2&&finalStroiltype.length()==8) {
                pur/=bluedes;
            }


            particular=finalStroiltype1 +"-"+strname+"-"+strgatepass+"-"+strweight+"-"+ finalStrunit1;

            purchase=pur;

            writting();


        }










    }


    public void setissue(){

        RadioGroup tankissue;
        tankissue=findViewById(R.id.tankissue);


        EditText beforeissue=findViewById(R.id.beforeissue);
        final String strbeforeissue=beforeissue.getText().toString();

        EditText afterissue=findViewById(R.id.afterissue);
        final String strafterissue=afterissue.getText().toString();

        EditText time=findViewById(R.id.time);
        final String strtime=time.getText().toString();

        if(strbeforeissue==null||strafterissue==null||strtime==null){

            FancyToast.makeText(this, "INPUT CANNOT BE LEFT BLANK ", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

        } else {

            int selectedId = tankissue.getCheckedRadioButtonId();
            if (selectedId == R.id.tunnel1) {
                particular = "ISSUE TO : Tunnel Tank 1";
                issue=(Double.valueOf(strafterissue)-Double.valueOf(strbeforeissue))*c11 + (c12*Integer.valueOf(strtime));

            } else if (selectedId == R.id.tunnel2) {
                particular = "ISSUE TO : Tunnel Tank 2";
                issue=(Double.valueOf(strafterissue)-Double.valueOf(strbeforeissue))*c21 + (c22*Integer.valueOf(strtime));

            } else {
                particular = "ISSUE TO : Tunnel Tank 3";
                issue=(Double.valueOf(strafterissue)-Double.valueOf(strbeforeissue))*c31 + (c32*Integer.valueOf(strtime));

            }

            writting();


        }





    }



    public void setcms(){

        EditText readingcms=findViewById(R.id.readingcms);
        final String strreadingcms=readingcms.getText().toString();


        if(strreadingcms==null){

            FancyToast.makeText(this, "INPUT CANNOT BE LEFT BLANK ", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

        } else {

            cms=map.get(strreadingcms);
            particular="CMS "+strreadingcms;
            writting();

        }



    }



    public void writting(){

        final boolean[] match = {false};

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("OILMAINTANK").child("VALUES");


        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        String today = myFormat.format(c.getTime());

        Log.d("DATES", "getallobjects: "+today);


        try {
            Date overwritedate =myFormat.parse(time);

            c.setTime(overwritedate);
            c.add(Calendar.DATE,0);

            Date newDate = c.getTime();
            String getdate=myFormat.format(newDate);

           // Log.d("DATES", "getallobjects: "+getdate);


            while (getdate.substring(0,11).compareTo(today.substring(0,11))<=0){


                final String getstrdate=getdate;
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(getstrdate.substring(0,4)).exists()){
                            dataSnapshot=dataSnapshot.child(getstrdate.substring(0,4));


                            if(dataSnapshot.child(getstrdate.substring(5,7)).exists()){
                                dataSnapshot=dataSnapshot.child(getstrdate.substring(5,7));


                                if(dataSnapshot.child(getstrdate.substring(8,10)).exists()) {
                                    dataSnapshot = dataSnapshot.child(getstrdate.substring(8, 10));


                                    for (DataSnapshot dayIter : dataSnapshot.getChildren()) {

                                        Maintankobject obj = dayIter.getValue(Maintankobject.class);
                                       // Log.w("DATESs", "OBJECTS 123: "+obj.getAadate()+obj.getAbtime());

                                        if(!done) {

                                            if (match[0]) {
                                                obj.setEbalance(String.format("%.2f", balance - Double.valueOf(obj.getDissue()) + Double.valueOf(obj.getCpurchase())));
                                                balance = Double.valueOf(obj.getEbalance());

                                                if (Double.valueOf(obj.getfCMS()) != 0)
                                                    obj.setGdifference(String.format("%.2f", balance - Double.valueOf(obj.getfCMS())));

                                            } else {

                                                if (dayIter.getKey().compareTo(time.substring(12)) == 0) {
                                                    match[0] = true;
                                                    obj.setCpurchase(String.format("%.2f", purchase));
                                                    obj.setDissue(String.format("%.2f", issue));
                                                    obj.setfCMS(String.format("%.2f", cms));
                                                    obj.setEbalance(String.format("%.2f", balance - Double.valueOf(obj.getDissue()) + Double.valueOf(obj.getCpurchase())));
                                                    obj.setBparticular(particular);
                                                    if (cms != 0)
                                                        obj.setGdifference(String.format("%.2f", Double.valueOf(obj.getEbalance()) - cms));


                                                    balance = Double.valueOf(obj.getEbalance());

                                                }


                                            }


                                            Log.d("DATESs", "OBJECTS 123:123 "+balance);
                                            myRef.child(getstrdate.substring(0, 4)).child(getstrdate.substring(5, 7)).child(getstrdate.substring(8, 10)).child(dayIter.getKey()).setValue(obj);
                                            final DatabaseReference myRef1 = database.getReference("OILMAINTANK").child("LASTVALUE");
                                            myRef1.setValue(String.format("%.2f",balance));


                                        }
                                    }

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








              //  Log.d("DATES", "getallobjects: "+getdate);

                c.add(Calendar.DATE, 1);
                newDate = c.getTime();
                getdate=myFormat.format(newDate);


            }

            balance=0;
            //done=true;
            FancyToast.makeText(maintankoverwrite.this,"SUCCESSFUL OVERWRITING", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
            startActivity(new Intent(maintankoverwrite.this, admin.class));
            finishAffinity();


        } catch (ParseException e) {
            e.printStackTrace();
        }


        //done=true;
        FancyToast.makeText(maintankoverwrite.this,"SUCCESSFUL OVERWRITING", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
        onBackPressed();




    }


    public void gettoverwriteobj(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("OILMAINTANK").child("VALUES");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(time.substring(0,4)).exists()){
                    dataSnapshot=dataSnapshot.child(time.substring(0,4));


                    if(dataSnapshot.child(time.substring(5,7)).exists()){
                        dataSnapshot=dataSnapshot.child(time.substring(5,7));


                        if(dataSnapshot.child(time.substring(8,10)).exists()){
                            dataSnapshot=dataSnapshot.child(time.substring(8,10));


                            if(dataSnapshot.child(time.substring(12)).exists()){
                                dataSnapshot=dataSnapshot.child(time.substring(12));

                                overwriteobj=dataSnapshot.getValue(Maintankobject.class);
                                balance=Double.valueOf(overwriteobj.getEbalance())-Double.valueOf(overwriteobj.getCpurchase())+Double.valueOf(overwriteobj.getDissue());



                                if(overwriteobj.getBparticular().charAt(0)=='B'){
                                    linearpurchase.setVisibility(View.VISIBLE);
                                    i=1;

                                }else  if(overwriteobj.getBparticular().charAt(0)=='I'){
                                    linearissue.setVisibility(View.VISIBLE);
                                    i=2;

                                }else  if(overwriteobj.getBparticular().charAt(0)=='C'){
                                    linearcms.setVisibility(View.VISIBLE);
                                    i=3;
                                }




                            }

                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {


            }
        });

    }


    public void getallconstants(){


        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        final DatabaseReference myRef1 = database1.getReference("OILMAINTANK").child("PURCHASE").child("CONSTANTS");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                blackdes=dataSnapshot.child("BLACKDES").getValue(Double.class);
                bluedes=dataSnapshot.child("BLUEDES").getValue(Double.class);


            }
            @Override
            public void onCancelled(DatabaseError error) {


            }
        });


        final DatabaseReference myRef2 = database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel1").child("CONSTANTS");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                c11=dataSnapshot.child("C1").getValue(Double.class);
                c12=dataSnapshot.child("C2").getValue(Double.class);


            }
            @Override
            public void onCancelled(DatabaseError error) {


            }
        });


        final DatabaseReference myRef3 = database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel2").child("CONSTANTS");
        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                c21=dataSnapshot.child("C1").getValue(Double.class);
                c22=dataSnapshot.child("C2").getValue(Double.class);


            }
            @Override
            public void onCancelled(DatabaseError error) {


            }
        });


        final DatabaseReference myRef4 = database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel3").child("CONSTANTS");
        myRef4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                c31=dataSnapshot.child("C1").getValue(Double.class);
                c32=dataSnapshot.child("C2").getValue(Double.class);


            }
            @Override
            public void onCancelled(DatabaseError error) {


            }
        });


        final DatabaseReference myRef5= database1.getReference("OILMAINTANK").child("CMS").child("CONSTANTS");
        myRef5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dayIter : dataSnapshot.getChildren()){
                    map.put(dayIter.getKey(),dayIter.getValue(Integer.class));
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {



            }
        });



    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(maintankoverwrite.this, admin.class));
        finish();
    }






}