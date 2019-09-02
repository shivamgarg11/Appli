package com.shivam.appli.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.MainActivity;
import com.shivam.appli.R;

public class user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ////////////getting from firebase/////////////////////////
        final String[] oilarray={"MAIN TANK","TUNNEL TANK 1","TUNNEL TANK 2","TUNNEL TANK 3"};
        final String[] maintankoilarray={"PURCHASE","ISSUE","VOLUME CHECK"};
        final String[] gasarray={"MUKTA"};
        final String[] electricityarray={"MUKTA","MEENA"};
        //////////////////////////////////////////////////////////


        Button goback=findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user.this, MainActivity.class));
                finish();
            }
        });



        final LinearLayout oil=findViewById(R.id.useroil);
        LinearLayout gas=findViewById(R.id.usergas);
        LinearLayout electricity=findViewById(R.id.userelectricity);

        oil.setOnClickListener(new View.OnClickListener() {

            int selected=oilarray.length-1;

            @Override
            public void onClick(View v) {

                AlertDialog dialog=new AlertDialog.Builder(user.this)
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
                                    Intent i = new Intent(user.this, oil_input.class);
                                    i.putExtra("path", oilarray[selected]);
                                    startActivity(i);
                                    finish();
                                }else{

                                    selected=maintankoilarray.length-1;
                                    AlertDialog dialogsub=new AlertDialog.Builder(user.this)
                                            .setIcon(R.drawable.logoo)
                                            .setTitle("              OIL/MAIN TANK")
                                            .setSingleChoiceItems(maintankoilarray, maintankoilarray.length-1, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    selected = which;
                                                }
                                            })
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                   if(selected==0){
                                                          Intent i = new Intent(user.this, Maintankpurchase.class);
                                                          startActivity(i);
                                                          finish();
                                                   }
                                                   if(selected==1){
                                                       Intent i = new Intent(user.this, Maintankissue.class);
                                                       startActivity(i);
                                                       finish();
                                                   }
                                                   if(selected==2){
                                                       Intent i = new Intent(user.this, MaintankCMS.class);
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
                                    dialogsub.show();
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

                AlertDialog dialog=new AlertDialog.Builder(user.this)
                        .setIcon(R.drawable.logoo)
                        .setTitle("              GAS")
                        .setSingleChoiceItems(gasarray, gasarray.length-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(user.this,gas_input.class);
                                i.putExtra("path",gasarray[selected]);
                                startActivity(i);
                                finish();                            }
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

                AlertDialog dialog=new AlertDialog.Builder(user.this)
                        .setIcon(R.drawable.logoo)
                        .setTitle("          ELECTRICITY")
                        .setSingleChoiceItems(electricityarray, electricityarray.length-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(user.this,electricity_input.class);
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
        startActivity(new Intent(user.this, MainActivity.class));
        finish();
    }
}
