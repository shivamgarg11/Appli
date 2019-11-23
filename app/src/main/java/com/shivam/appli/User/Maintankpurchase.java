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

public class Maintankpurchase extends AppCompatActivity {

    String timeoil;
    AlertDialog alertDialog;
    RadioGroup oiltype,oilunit;
    double blackdes,bluedes;
    String lastvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintankpurchase);

        TextView datetime=findViewById(R.id.datetime);
        gettimedate(datetime);



        Button close =findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Maintankpurchase.this,user.class));
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


            FancyToast.makeText(this, "WAIT WHILE WE ARE MAKING EVERYTHING READY ", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

            String stroiltype="";
            int selectedId = oiltype.getCheckedRadioButtonId();
            if (selectedId==R.id.blackoil)
                stroiltype="BLACK OIL";
            else
                stroiltype="BLUE OIL";


            String strunit="";
            int selectedId1 = oilunit.getCheckedRadioButtonId();
            if (selectedId1==R.id.kg)
                strunit="Kg";
            else
                strunit="L";


            final String finalStroiltype = stroiltype;
            final String finalStrunit = strunit;
            final String finalStroiltype1 = stroiltype;
            final String finalStrunit1 = strunit;
            alertDialog = new AlertDialog.Builder(Maintankpurchase.this)
                    .setIcon(R.drawable.logoo)
                    .setTitle("CONFIRMATION")
                    .setMessage("TYPE :"+stroiltype+"\n\n Name : "+strname+"\n\n Gatepass No. :"+strgatepass+"\n\n Weight :"+strweight+" "+strunit)
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            double pur = Double.valueOf(strweight);
                            if(finalStrunit.length()==2&&finalStroiltype.length()==9){
                                pur/=blackdes;
                            }
                            if(finalStrunit.length()==2&&finalStroiltype.length()==8) {
                                pur/=bluedes;
                            }


                            Maintankobject obj=new Maintankobject(timeoil.substring(0,10),timeoil.substring(10), finalStroiltype1 +" : "+strname+" . #"+strgatepass+" . "+strweight+" "+ finalStrunit1,String.format("%.2f",pur),String.valueOf(0),String.format("%.2f",Double.valueOf(lastvalue)+pur),String.valueOf(0),String.valueOf(0));
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("OILMAINTANK").child("VALUES").child(timeoil.substring(6,10)).child(timeoil.substring(3,5)).child(timeoil.substring(0,2));
                            myRef.child(timeoil.substring(10)).setValue(obj);


                            final DatabaseReference myRef1 = database.getReference("OILMAINTANK").child("LASTVALUE");
                            myRef1.setValue(String.format("%.2f",Double.valueOf(lastvalue)+pur));

                            FancyToast.makeText(Maintankpurchase.this, "THANK YOU FOR UPDATING \n\n YOU HAVE BEEN LOGGED OUT", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            startActivity(new Intent(Maintankpurchase.this, MainActivity.class));
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
        startActivity(new Intent(Maintankpurchase.this, user.class));
        finish();
    }
}
