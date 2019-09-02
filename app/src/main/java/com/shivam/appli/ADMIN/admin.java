package com.shivam.appli.ADMIN;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shivam.appli.MainActivity;
import com.shivam.appli.R;

public class admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        maintankoverwrite.done=true;

        ////////////getting from firebase/////////////////////////
        final String[] oilarray={"MAIN TANK","TUNNEL TANK 1","TUNNEL TANK 2","TUNNEL TANK 3"};
        final String[] gasarray={"MUKTA"};
        final String[] electricityarray={"MUKTA","MEENA"};
        //////////////////////////////////////////////////////////


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("NOTIFY");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token= FirebaseInstanceId.getInstance().getToken();
                boolean flag=false;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String str=postSnapshot.getValue(String.class);
                    if(str.contains(token)){
                     flag =true;
                        break;
                    }
                }
                if(!flag) {
                    myRef.push().setValue(token);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });






        Button goback=findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin.this, MainActivity.class));
                finish();
            }
        });



        LinearLayout oil=findViewById(R.id.useroil);
        LinearLayout gas=findViewById(R.id.usergas);
        LinearLayout electricity=findViewById(R.id.userelectricity);

        oil.setOnClickListener(new View.OnClickListener() {

            int selected=oilarray.length-1;

            @Override
            public void onClick(View v) {

                AlertDialog dialog=new AlertDialog.Builder(admin.this)
                        .setIcon(R.drawable.logoo)
                        .setTitle("              OIL")
                        .setSingleChoiceItems(oilarray, oilarray.length-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selected!=0) {
                                    Intent i = new Intent(admin.this, oil_output.class);
                                    i.putExtra("path", oilarray[selected]);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Intent i = new Intent(admin.this, MaintankOil_output.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            }
        });


        gas.setOnClickListener(new View.OnClickListener() {
            int selected=gasarray.length-1;
            @Override
            public void onClick(View v) {

                AlertDialog dialog=new AlertDialog.Builder(admin.this)
                        .setTitle("              GAS")
                        .setSingleChoiceItems(gasarray, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(admin.this, "You selected " + gasarray[selected], Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(admin.this,gas_output.class);
                                i.putExtra("path",gasarray[selected]);
                                startActivity(i);
                                finish();


                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            }
        });

        electricity.setOnClickListener(new View.OnClickListener() {
            int selected=electricityarray.length-1;
            @Override
            public void onClick(View v) {

                AlertDialog dialog=new AlertDialog.Builder(admin.this)
                        .setTitle("          ELECTRICITY")
                        .setSingleChoiceItems(electricityarray, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(admin.this, "You selected " + electricityarray[selected], Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(admin.this,electricity_output.class);
                                i.putExtra("path",electricityarray[selected]);
                                startActivity(i);
                                finish();


                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(admin.this, MainActivity.class));
        finish();
    }
}


