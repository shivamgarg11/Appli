package com.shivam.appli.ADMIN;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.Fragments.gasMonthfrag;
import com.shivam.appli.Fragments.gassummaryfrag;
import com.shivam.appli.Java_objects.gas_object;
import com.shivam.appli.Java_objects.gasconstants;
import com.shivam.appli.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;

public class gas_output extends AppCompatActivity {
    int it = 0;
    // TODO: 06/02/19 resolve year fetch

    String pathway = "";
    final String[] gasDownload = new String[]{"Yearly", "Monthly", "Date Range"};
    int selected = gasDownload.length - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_output);

        pathway = getIntent().getStringExtra("path");
        TextView path = findViewById(R.id.path);
        path.setText("ADMIN/GAS/" + pathway);

/////////////////////////////


        Button today=findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
                final String todaydate = dateformat.format(c.getTime());
                String passingstr =todaydate.substring(0,4)+todaydate.substring(5,7)+todaydate.substring(8);
                android.app.FragmentManager fragmentManager = getFragmentManager();
                gassummaryfrag frag = new gassummaryfrag(passingstr,gas_output.this);
                fragmentManager.beginTransaction().replace(R.id.frame, frag).commit();
            }
        });

        today.performClick();

        Button yesterday=findViewById(R.id.yesterday);
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE,-1);
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
                final String yesterdaydate = dateformat.format(c.getTime());
                String passingstr =yesterdaydate.substring(0,4)+yesterdaydate.substring(5,7)+yesterdaydate.substring(8);
                android.app.FragmentManager fragmentManager = getFragmentManager();
                gassummaryfrag frag = new gassummaryfrag(passingstr,gas_output.this);
                fragmentManager.beginTransaction().replace(R.id.frame, frag).commit();
            }
        });



        final String[] date = {""};


        Button summary=findViewById(R.id.summary);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyyy");
                String datee=dateformat.format(c.getTime());


                new DatePickerDialog(gas_output.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if(monthOfYear<9)
                            date[0] =""+year+"0"+(monthOfYear+1)+dayOfMonth;
                        else
                            date[0] =""+year+(monthOfYear+1)+dayOfMonth;
                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        gassummaryfrag frag = new gassummaryfrag(date[0],gas_output.this);
                        fragmentManager.beginTransaction().replace(R.id.frame, frag).commit();
                    }
                }, Integer.valueOf(datee.substring(4)), Integer.valueOf(datee.substring(2,4))-1, Integer.valueOf(datee.substring(0,2))).show();
            }
        });


        Button month=findViewById(R.id.month);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(gas_output.this);
                View view = getLayoutInflater().inflate(R.layout.two_spinner_dialog, null);
                builder.setTitle("Select Year and Month to View Report");
                final Spinner spinner = view.findViewById(R.id.spinner_one);
                for (int i = year; i != year - 10; i--) {
                    arr.add(String.valueOf(i));
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(gas_output.this, R.layout.spinnerdropdown, arr);
                adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                final Spinner spinner2 = view.findViewById(R.id.spinner_two);
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("Jan\n");
                arrayList.add("Feb\n");
                arrayList.add("Mar\n");
                arrayList.add("Apr\n");
                arrayList.add("May\n");
                arrayList.add("June\n");
                arrayList.add("July\n");
                arrayList.add("Aug\n");
                arrayList.add("Sept\n");
                arrayList.add("Oct\n");
                arrayList.add("Nov\n");
                arrayList.add("Dec\n");
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(gas_output.this, R.layout.spinnerdropdown, arrayList);
                adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


                spinner.setAdapter(adapter1);
                spinner2.setAdapter(adapter2);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(gas_output.this, spinner.getSelectedItem().toString() + spinner2.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        final String year=spinner.getSelectedItem().toString();
                        final int month=spinner2.getSelectedItemPosition() + 1;

                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        gasMonthfrag frag = new gasMonthfrag(gas_output.this,Integer.valueOf(year),month);
                        fragmentManager.beginTransaction().replace(R.id.frame, frag).commit();

                    }
                });
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


        //////////////////////////////////
        Button setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            String[] arr = {"CHANGE CONSTANTS", "CHANGE RANGE", "PASSWORD RESET"};
            int selected = arr.length - 1;

            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(gas_output.this)
                        .setIcon(R.drawable.logoo)
                        .setTitle("              SETTING")
                        .setSingleChoiceItems(arr, arr.length - 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (selected == 0) {
                                    changeconstant();
                                }

                                if (selected == 1) {
                                    changerange();
                                }

                                if (selected == 2) {
                                    resetpassword();
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




        Button goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(gas_output.this, admin.class));
                finish();
            }
        });


        Button download = findViewById(R.id.download);

        download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selected = 0;
                AlertDialog dialog = new AlertDialog.Builder(gas_output.this)
                        .setTitle("")
                        .setSingleChoiceItems(gasDownload, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(gas_output.this, "You selected " + gasDownload[selected], Toast.LENGTH_SHORT).show();
                                if (selected == 0) {
                                    dialog.dismiss();
                                    selYear();
                                } else if (selected == 1) {
                                    dialog.dismiss();
                                    selMonth();
                                } else {
                                    dialog.dismiss();
                                    selRange();
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




    }

    public void resetpassword() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(gas_output.this);
        alertDialog.setTitle("RESET PASSWORD");
        alertDialog.setMessage("Enter Password");

        final EditText input = new EditText(gas_output.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.logoo);

        alertDialog.setPositiveButton("SUBMIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int password = Integer.valueOf(input.getText().toString());
                        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef1 = database1.getReference("USER").child("PASSWORD");
                        myRef1.child("strpassword").setValue(password);
                        myRef1.child("numpassword").setValue(5);
                    }});

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }


    public void changerange() {

        LayoutInflater li = LayoutInflater.from(gas_output.this);
        View changeconstant = li.inflate(R.layout.setgasrange, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                gas_output.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(changeconstant);

        final EditText c1 = (EditText) changeconstant
                .findViewById(R.id.from);
        final EditText c2 = (EditText) changeconstant
                .findViewById(R.id.to);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                final String strc1=c1.getText().toString()+"";
                                final String strc2=c2.getText().toString()+"";

                                if(strc1.length()==0||strc2.length()==0||Double.valueOf(strc1)>Double.valueOf(strc2)){
                                    FancyToast.makeText(gas_output.this,"INVALID INPUTS", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                }else{
                                    final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                    final DatabaseReference myRef1 = database1.getReference("GAS"+pathway).child("RANGE");

                                    Formatter fmt = new Formatter();
                                    myRef1.child("FROM").setValue(Float.valueOf(strc1));
                                    myRef1.child("TO").setValue(Float.valueOf(strc2));
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();


        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("GAS" + pathway).child("RANGE");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Formatter fmt = new Formatter();
                c1.setText(dataSnapshot.child("FROM").getValue(Float.class) + "");
                c2.setText(dataSnapshot.child("TO").getValue(Float.class) + "");
                alertDialog.show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


    }


    public void changeconstant() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(gas_output.this);
        View changeconstant = li.inflate(R.layout.setgasconstants, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                gas_output.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(changeconstant);

        final EditText c1 = (EditText) changeconstant
                .findViewById(R.id.c1);
        final EditText c2 = (EditText) changeconstant
                .findViewById(R.id.c2);
        final EditText c3 = (EditText) changeconstant
                .findViewById(R.id.c3);
        final EditText c4 = (EditText) changeconstant
                .findViewById(R.id.c4);
        final EditText c5 = (EditText) changeconstant
                .findViewById(R.id.c5);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final String strc1 = c1.getText().toString() + "";
                                final String strc2 = c2.getText().toString() + "";
                                final String strc3 = c3.getText().toString() + "";
                                final String strc4 = c4.getText().toString() + "";
                                final String strc5 = c5.getText().toString() + "";

                                if (strc1.length() == 0 || strc2.length() == 0 || strc3.length() == 0 || strc4.length() == 0 || strc5.length() == 0) {
                                    FancyToast.makeText(gas_output.this, "INVALID INPUTS", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                } else {
                                    final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                    final DatabaseReference myRef1 = database1.getReference("GAS" + pathway).child("CONSTANTS");
                                    gasconstants con = new gasconstants(Double.valueOf(strc1), Double.valueOf(strc2), Double.valueOf(strc3), Double.valueOf(strc4), Double.valueOf(strc5));
                                    myRef1.setValue(con);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("GAS" + pathway).child("CONSTANTS");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                gasconstants con = dataSnapshot.getValue(gasconstants.class);
                c1.setText(con.getC1() + "");
                c2.setText(con.getC2() + "");
                c3.setText(con.getC3() + "");
                c4.setText(con.getC4() + "");
                c5.setText(con.getC5() + "");
                alertDialog.show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    int year = Calendar.getInstance().get(Calendar.YEAR);
    public ArrayList<String> arr = new ArrayList<>();


    public void selYear() {
        final String[] writeCSV = {""};
        final AlertDialog.Builder builder = new AlertDialog.Builder(gas_output.this);
        View view = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        builder.setTitle("Select Year to View Report");
        final Spinner spinner = view.findViewById(R.id.spinner);
        for (int i = year; i != year - 10; i--) {
            arr.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(gas_output.this, R.layout.spinnerdropdown, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final FirebaseDatabase database1 = FirebaseDatabase.getInstance();

                writeCSV[0] += "DATE,TIME,INPUT,DIFFERENCE,SCM,MMBTO,RATE,BILL,\n\n";
                final String spinnerval=String.valueOf(spinner.getSelectedItem().toString());
                final DatabaseReference myRef1 = database1.getReference("GASMUKTA").child(spinnerval);
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot yearIter : dataSnapshot.getChildren()) {
                                Log.d("201999", "onDataChange: " + yearIter.getKey() + "End");
                               String str="";
                               str += " / "+spinner.getSelectedItem().toString();
                                str = " / "+ yearIter.getKey() + str;
                                for (DataSnapshot monthIter : yearIter.getChildren()) {
                                    writeCSV[0] += monthIter.getKey()+str + ",";

                                    // for (DataSnapshot dayIter : monthIter.getChildren()) {
//                                            writeCSV += dayIter.
                                  //      writeCSV[0] += dayIter.getValue() + ",";
                                  //  }
                                  //  writeCSV[0]=writeCSV[0].substring(0,writeCSV[0].length()-6);

                                    gas_object obj=monthIter.getValue(gas_object.class);
                                    writeCSV[0] +=obj.getTime()+","+obj.getAinput()+","+obj.getBdifference()+","+obj.getCscm()+","+obj.getDmmbto()+","+obj.getEride()+","+obj.getFbill()+",";




                                    writeCSV[0] += "\n";
                                }

                                csvPart(writeCSV[0], "Year"+spinnerval);
                            }
                            Log.d("writecsv", "onDataChange: " + writeCSV[0]);

                        } else {
                            Toast.makeText(gas_output.this, "Entry Doesn't Exist", Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/Gas/" + "Year" + ".csv");

            }
        });

        sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/Gas/" + "Year" + ".csv");


        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d("Years", "selYear: " + arr.get(0) + arr.get(4));
    }

    public void selMonth() {
        Toast.makeText(this, "" + gasDownload[selected], Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(gas_output.this);
        View view = getLayoutInflater().inflate(R.layout.two_spinner_dialog, null);
        builder.setTitle("Select Year and Month to View Report");
        final Spinner spinner = view.findViewById(R.id.spinner_one);
        for (int i = year; i != year - 10; i--) {
            arr.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(gas_output.this, R.layout.spinnerdropdown, arr);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        final Spinner spinner2 = view.findViewById(R.id.spinner_two);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Jan");
        arrayList.add("Feb");
        arrayList.add("Mar");
        arrayList.add("Apr");
        arrayList.add("May");
        arrayList.add("June");
        arrayList.add("July");
        arrayList.add("Aug");
        arrayList.add("Sept");
        arrayList.add("Oct");
        arrayList.add("Nov");
        arrayList.add("Dec");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(gas_output.this, R.layout.spinnerdropdown, arrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        spinner.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(gas_output.this, spinner.getSelectedItem().toString() + spinner2.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
               final String spinnerval1=spinner.getSelectedItem().toString();
               final String spinnerval2=String.valueOf(spinner2.getSelectedItemPosition() + 1);

                final DatabaseReference myRef1 = database1.getReference("GASMUKTA").child(spinnerval1).child(spinnerval2);
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String csvWrite = "DATE,TIME,INPUT,DIFFERENCE,SCM,MMBTO,RATE,BILL,\n\n";
                            String str="";
                            str += " / "+spinner2.getSelectedItem().toString() ;
                            str += " / "+spinner.getSelectedItem().toString() ;

                            for (DataSnapshot datesIter : dataSnapshot.getChildren()) {
                                csvWrite += datesIter.getKey() +str+ ",";

                                gas_object obj=datesIter.getValue(gas_object.class);
                                csvWrite +=obj.getTime()+","+obj.getAinput()+","+obj.getBdifference()+","+obj.getCscm()+","+obj.getDmmbto()+","+obj.getEride()+","+obj.getFbill()+",";

                                csvWrite += "\n";

                            }

                            Log.d("csvWrite", "onDataChange: " + csvWrite);
                            csvPart(csvWrite, "Month"+spinnerval1+"_"+spinnerval2);



                        } else
                            Toast.makeText(gas_output.this, "Entry Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                Log.d("DATEtime", spinner.getSelectedItem().toString() + " " + (spinner2.getSelectedItemPosition() + 1));

                sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/Gas/" + "Month" + ".csv");

            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    String dateStart = "";

    String dateEnd = "";
    String csvWrite = "DATE,TIME,INPUT,DIFFERENCE,SCM,MMBTO,RATE,BILL,\n\n";

    public void selRange() {
        Toast.makeText(this, "" + gasDownload[selected], Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(gas_output.this);
        View view = getLayoutInflater().inflate(R.layout.date_range, null);
        ImageView date1Im = view.findViewById(R.id.date_1_im);
        ImageView date2Im = view.findViewById(R.id.date_2_im);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyyy");
        final String datee=dateformat.format(c.getTime());

        final TextView tvDateStart = view.findViewById(R.id.date_1_tv);
        final TextView tvDateEnd = view.findViewById(R.id.date_2_tv);
        date1Im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(gas_output.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 9)
                            dateStart = dayOfMonth + "/0" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        else
                            dateStart = dayOfMonth + "/" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        tvDateStart.setText(String.valueOf(dayOfMonth) + "/0" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));

                    }
                }, Integer.valueOf(datee.substring(4)), Integer.valueOf(datee.substring(2,4))-1, Integer.valueOf(datee.substring(0,2))).show();


            }
        });

        date2Im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(gas_output.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 9)
                            dateEnd = dayOfMonth + "/0" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        else
                            dateEnd = dayOfMonth + "/" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        tvDateEnd.setText(String.valueOf(dayOfMonth) + "/0" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));

                    }
                }, Integer.valueOf(datee.substring(4)), Integer.valueOf(datee.substring(2,4))-1, Integer.valueOf(datee.substring(0,2))).show();


            }
        });


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final Date date2, date1;


                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateStart);

                    date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dateEnd);
                    Log.d("Dates", "onClick: " + date1.toString() + date2.toString());

                    if (date1.after(date2)) {
                        Toast.makeText(gas_output.this, "Please Check the dates!", Toast.LENGTH_SHORT).show();
                    } else {
                        Calendar calendar1 = new GregorianCalendar();
                        calendar1.setTime(date1);
                        Calendar calendar2 = new GregorianCalendar();
                        calendar2.setTime(date2);
                        final int year1 = calendar1.get(Calendar.YEAR);
                        int month1 = calendar1.get(Calendar.MONTH) + 1;
                        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);


                        final int year2 = calendar2.get(Calendar.YEAR);
                        int month2 = calendar2.get(Calendar.MONTH) + 1;
                        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
                        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef1 = database1.getReference("GASMUKTA");
                        myRef1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    for (it = year1; it <= year2 + 1; it++) {

                                        Log.d("Years", String.valueOf(it) + "onDataChange: ");
                                        DatabaseReference myRef2 = database1.getReference("GASMUKTA").child(String.valueOf(it));
                                        myRef2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot yearIter : dataSnapshot.getChildren()) {
                                                    Log.d("RangeDate", "onDataChange: " + yearIter.getKey().toString());
                                                    String str="";
                                                    str += " / "+String.valueOf(dataSnapshot.getKey()) ;
                                                    str= "/"+yearIter.getKey()+str ;
                                                    for (DataSnapshot monthIter : yearIter.getChildren()) {
                                                        csvWrite += monthIter.getKey() + str + ",";
                                                        Log.d("monthIIII", "onDataChange: " + monthIter.getKey());
                                                        Log.d("monthIIII", "onDataChange: " + monthIter.getValue());

                                                        gas_object obj=monthIter.getValue(gas_object.class);
                                                        csvWrite +=obj.getTime()+","+obj.getAinput()+","+obj.getBdifference()+","+obj.getCscm()+","+obj.getDmmbto()+","+obj.getEride()+","+obj.getFbill()+",";

                                                        csvWrite += "\n";
                                                        //csvWrite += String.valueOf(dataSnapshot.getKey()) + ",";
                                                    }
                                                }
                                               // csvWrite=csvWrite.substring(0,csvWrite.length()-6);
                                                csvWrite += "\n";

                                                Log.d("CSV", "selRange: " + csvWrite);
                                                csvPart(csvWrite, "Range"+date1.toString().substring(4,10)+"-"+date2.toString().substring(4,10));
                                                sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/Gas/" + "Range" + ".csv");
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                } else
                                    Toast.makeText(gas_output.this, "Entry Doesn't Exist", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                } catch (ParseException e) {
                    Toast.makeText(gas_output.this, "Dates could not be parsed, Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public static String csvPart(String data, String name) {

        //        String a = "1,2,4,5,6";
        String filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/Gas/" + name + ".csv";
//        String
        try {
            String content = "Separe here integers by semi-colon";
            File file = new File(filePath);
            // if file doesnt exists, then create it
            try {
                if (!file.exists()) {
                    new File(file.getParent()).mkdirs();
                    file.createNewFile();
                }
            } catch (IOException e) {
                Log.e("", "Could not create file.", e);
                return "";
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void sendNotif(String path)
    {

//        String path = android.os.Environment.getExternalStorageDirectory() + "/" + "App_Ka_Kaam/";
        Uri selectedUri = Uri.parse(android.os.Environment.getExternalStorageDirectory() + "/" + "App_Ka_Kaam/Gas/");
        Intent intent = new Intent(Intent.ACTION_VIEW,selectedUri);

        intent.setDataAndType(selectedUri, "text/csv");
        intent = Intent.createChooser(intent, "Open folder");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(gas_output.this, 1, intent,  PendingIntent.FLAG_CANCEL_CURRENT);
        createNotificationChannel();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.drawable.logoo)
                .setContentTitle("File Downloaded")
                .setContentText("Goto your File Manager in " + gas_output.this.getString(R.string.naam))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(0, mBuilder.build());
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "Desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(gas_output.this, admin.class));
        finish();
    }


}