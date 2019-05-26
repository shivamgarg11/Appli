package com.shivam.appli.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.Java_objects.Maintankobject;
import com.shivam.appli.MainActivity;
import com.shivam.appli.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MaintankCMS extends AppCompatActivity {


    String timeoil;
    AlertDialog alertDialog;
    String lastvalue;
    double c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintank_cms);


        TextView datetime = findViewById(R.id.datetime);
        gettimedate(datetime);


        Button close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintankCMS.this, user.class));
                finish();
            }
        });


        Button done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickbutton();

            }
        });
    }


    public void onclickbutton() {



        EditText readingcms=findViewById(R.id.readingcms);
        final String strreadingcms=readingcms.getText().toString();






        if(strreadingcms==null){

            FancyToast.makeText(this, "INPUT CANNOT BE LEFT BLANK ", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

        } else {


            FancyToast.makeText(this, "WAIT WHILE WE ARE MAKING EVERYTHING READY ", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();



            alertDialog = new AlertDialog.Builder(MaintankCMS.this)
                    .setIcon(R.drawable.logoo)
                    .setTitle("CONFIRMATION")
                    .setMessage("READING :"+strreadingcms)
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            Maintankobject obj=new Maintankobject(timeoil.substring(0,10),timeoil.substring(10), "Volume Check : "+strreadingcms+" cm",String.valueOf(0),String.valueOf(0),String.format("%.2f",Double.valueOf(lastvalue)),String.format("%.2f",c1),String.format("%.2f",Double.valueOf(lastvalue)-c1));
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("OILMAINTANK").child("VALUES").child(timeoil.substring(6,10)).child(timeoil.substring(3,5)).child(timeoil.substring(0,2));
                            myRef.child(timeoil.substring(10)).setValue(obj);


                            FancyToast.makeText(MaintankCMS.this, "THANK YOU FOR UPDATING \n\n YOU HAVE BEEN LOGGED OUT", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            startActivity(new Intent(MaintankCMS.this, MainActivity.class));
                            finish();

                        }
                    })
                    .create();

            getprevoiusdata(strreadingcms);


        }

    }







    public void getprevoiusdata(final String val){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("OILMAINTANK").child("LASTVALUE");


        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("OILMAINTANK").child("CMS").child("CONSTANTS");



        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                c1=dataSnapshot.child(val).getValue(Double.class);


            }
            @Override
            public void onCancelled(DatabaseError error) {


            }
        });





        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lastvalue=dataSnapshot.getValue(String.class);
                alertDialog.show();

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


    }





        public void gettimedate(final TextView datetime){

            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("dd MM yyyy HH:mm");
            timeoil = dateformat.format(c.getTime());
            String str=timeoil.substring(0,2)+"/"+timeoil.substring(3,5)+"/"+timeoil.substring(6,10)+"   -  "+timeoil.substring(10);
            datetime.setText(str);

        }

        @Override
        public void onBackPressed() {
            startActivity(new Intent(MaintankCMS.this, user.class));
            finish();
        }
    }