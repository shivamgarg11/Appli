package com.shivam.appli.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class Maintankissue extends AppCompatActivity {


    String timeoil;
    AlertDialog alertDialog;
    RadioGroup tankissue;
    double c1,c2;
    double lastvalue;
    String tank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintankissue);

        TextView datetime=findViewById(R.id.datetime);
        gettimedate(datetime);


        Button close =findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Maintankissue.this,user.class));
                finish();
            }
        });


        Button done =findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickbutton();

            }
        });

    }


    public void onclickbutton() {

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


            FancyToast.makeText(this, "WAIT WHILE WE ARE MAKING EVERYTHING READY ", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

            String strissuetype="";
            int selectedId = tankissue.getCheckedRadioButtonId();
            if (selectedId==R.id.tunnel1)
                strissuetype="tunnel1";
            else if (selectedId==R.id.tunnel2)
                strissuetype="tunnel2";
            else
                strissuetype="tunnel3";

            tank=strissuetype;

            final String finalstrissuetype = strissuetype;

            alertDialog = new AlertDialog.Builder(Maintankissue.this)
                    .setIcon(R.drawable.logoo)
                    .setTitle("CONFIRMATION")
                    .setMessage("ISSUE TO :"+finalstrissuetype+"\n\n READING BEFORE ISSUE :"+strbeforeissue+"\n\n READING AFTER ISSUE :"+strafterissue+"\n\n TIME  :"+strtime+" min")
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                           double pur=(Double.valueOf(strafterissue)-Double.valueOf(strbeforeissue))*c1 + (c2*Integer.valueOf(strtime));


                            Maintankobject obj=new Maintankobject(timeoil.substring(0,10),timeoil.substring(10), "ISSUE TO :"+finalstrissuetype,0,pur,lastvalue-pur,0,0);
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("OILMAINTANK").child("VALUES").child(timeoil.substring(6,10)).child(timeoil.substring(3,5)).child(timeoil.substring(0,2));
                            myRef.child(timeoil.substring(10)).setValue(obj);


                            final DatabaseReference myRef1 = database.getReference("OILMAINTANK").child("LASTVALUE");
                            myRef1.setValue(lastvalue-pur);

                            FancyToast.makeText(Maintankissue.this, "THANK YOU FOR UPDATING \n\n YOU HAVE BEEN LOGGED OUT", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            startActivity(new Intent(Maintankissue.this, MainActivity.class));
                            finish();

                        }
                    })
                    .create();

            getprevoiusdata();


        }

    }







    public void getprevoiusdata(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("OILMAINTANK").child("LASTVALUE");


        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("OILMAINTANK").child("ISSUE").child(tank).child("CONSTANTS");

myRef1.child("C1").setValue(40);
myRef1.child("C2").setValue(0.35);

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                c1=dataSnapshot.child("C1").getValue(Double.class);
                c2=dataSnapshot.child("C2").getValue(Double.class);


            }
            @Override
            public void onCancelled(DatabaseError error) {


            }
        });





        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lastvalue=dataSnapshot.getValue(Double.class);
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
        startActivity(new Intent(Maintankissue.this, user.class));
        finish();
    }
}
