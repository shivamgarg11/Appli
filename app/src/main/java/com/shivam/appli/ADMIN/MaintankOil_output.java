package com.shivam.appli.ADMIN;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.Java_objects.Maintankobject;
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
import java.util.GregorianCalendar;
import java.util.HashMap;

import github.hotstu.sasuke.SasukeAdapter;
import github.hotstu.sasuke.SasukeView;





public class MaintankOil_output extends AppCompatActivity {


    final String[] gasDownload = new String[]{"Yearly", "Monthly", "Date Range"};
    final String[] settingoption = new String[]{"Purchase Constant","Issue  Tunnel Tank 1 Constant","Issue  Tunnel Tank 2 Constant","Issue  Tunnel Tank 3 Constant","CMS Constant"};
    int selected = gasDownload.length - 1;

    static String dateStart = "";
    static  String dateEnd = "";

    static String  sumdateStart = "";
    static  String sumdateEnd = "";

    private SasukeView sasuke;
    private int count = 0;
    int it=0;
    static ArrayList<Maintankobject> arr1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintank_oil_output);

        arr1=new ArrayList<>();
        summaryget();


        Button summary=findViewById(R.id.summary);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintankOil_output.this, MaintankOil_output.class));
                finish();
                /*arr1.clear();
                arr1=new ArrayList<>();
                summaryget();*/
            }
        });



        Button overwrite=findViewById(R.id.overwrite);
        overwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MaintankOil_output.this,maintankoverwrite.class);
                startActivity(i);
                finish();
            }
        });


        Button goback=findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintankOil_output.this, admin.class));
                finish();
            }
        });



        Button download = findViewById(R.id.download);

        download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selected = 0;
                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(MaintankOil_output.this)
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



        Button setting=findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected = 0;
                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(MaintankOil_output.this)
                        .setTitle("")
                        .setSingleChoiceItems(settingoption, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                       changeconstant(selected);

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







    public void changeconstant(final int selected) {

        final int sel=selected;


        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1= database1.getReference("OILMAINTANK").child("PURCHASE").child("CONSTANTS"); ;

        switch (sel){

            case 0:
                myRef1= database1.getReference("OILMAINTANK").child("PURCHASE").child("CONSTANTS");
                break;
            case 1:
                myRef1= database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel1").child("CONSTANTS");
                break;
            case 2:
                myRef1= database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel2").child("CONSTANTS");
                break;
            case 3:
                myRef1= database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel3").child("CONSTANTS");
                break;
            case 4:
                myRef1= database1.getReference("OILMAINTANK").child("CMS").child("CONSTANTS");
                break;

        }


        final HashMap<String,Integer> map=new HashMap<>();




        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(MaintankOil_output.this);
        View changeconstant = li.inflate(R.layout.maintankconstants, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MaintankOil_output.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(changeconstant);


        final EditText c1 = (EditText) changeconstant
                .findViewById(R.id.edit1);
        final EditText c2 = (EditText) changeconstant
                .findViewById(R.id.edit2);
        final TextView t1 =  changeconstant
                .findViewById(R.id.text1);
        final TextView t2 = changeconstant
                .findViewById(R.id.text2);
        final TextView head = changeconstant
                .findViewById(R.id.head);





        switch (sel){

            case 0:t1.setText("BLACK OIL:");
                   t2.setText("BLUE OIL:");
                myRef1= database1.getReference("OILMAINTANK").child("PURCHASE").child("CONSTANTS");
                break;
            case 1:head.setText("( Difference * C1 ) + ( C2 * min )");
                t1.setText("C1:");
                t2.setText("C2:");
                myRef1= database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel1").child("CONSTANTS");
                break;
            case 2:head.setText("( Difference * C1 ) + ( C2 * min )");
                t1.setText("C1:");
                t2.setText("C2:");
                myRef1= database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel2").child("CONSTANTS");
                break;
            case 3:head.setText("( Difference * C1 ) + ( C2 * min )");
                t1.setText("C1:");
                t2.setText("C2:");
                myRef1= database1.getReference("OILMAINTANK").child("ISSUE").child("tunnel3").child("CONSTANTS");
                break;
            case 4:
                t1.setText("CMS Index :");
                t2.setVisibility(View.GONE);
                c2.setVisibility(View.GONE);
                myRef1= database1.getReference("OILMAINTANK").child("CMS").child("CONSTANTS");
                break;



        }


        // set dialog message
        final DatabaseReference finalMyRef = myRef1;
        final DatabaseReference finalMyRef1 = myRef1;
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final String strc1 = c1.getText().toString() + "";
                                final String strc2 = c2.getText().toString() + "";


                                if (strc1.length() == 0 ) {
                                    FancyToast.makeText(MaintankOil_output.this, "INVALID INPUTS", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                } else {


                                    switch (sel){

                                        case 0:
                                            finalMyRef.child("BLACKDES").setValue(Double.valueOf(strc1));
                                            finalMyRef.child("BLUEDES").setValue(Double.valueOf(strc2));
                                            break;
                                        case 1:
                                            finalMyRef.child("C1").setValue(Double.valueOf(strc1));
                                            finalMyRef.child("C2").setValue(Double.valueOf(strc2));
                                            break;
                                        case 2:
                                            finalMyRef.child("C1").setValue(Double.valueOf(strc1));
                                            finalMyRef.child("C2").setValue(Double.valueOf(strc2));
                                            break;
                                        case 3:
                                            finalMyRef.child("C1").setValue(String.valueOf(strc1));
                                            finalMyRef.child("C2").setValue(String.valueOf(strc2));
                                            break;
                                        case 4:

                                            AlertDialog.Builder alert = new AlertDialog.Builder(MaintankOil_output.this);

                                            String st="null";
                                            if(map.containsKey(strc1))
                                            st=map.get(strc1)+"";



                                            alert.setTitle("CMS INDEX");
                                            alert.setMessage("OLD MAPPING : "+strc1+"="+st);

                                            final EditText input = new EditText(MaintankOil_output.this);
                                            alert.setView(input);


                                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                             finalMyRef1.child(strc1).setValue(Double.valueOf(input.getText().toString()));

                                                }
                                            });

                                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    // Canceled.
                                                }
                                            });

                                            alert.show();

                                            break;

                                    }




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

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                switch (sel){
                    case 0:
                        Log.d("TAGCON", "onDataChange: "+dataSnapshot.child("BLACKDES").getValue(Double.class));
                        c1.setText(dataSnapshot.child("BLACKDES").getValue(Double.class)+"");
                        c2.setText(dataSnapshot.child("BLUEDES").getValue(Double.class)+"");
                        break;
                    case 1:
                        c1.setText(dataSnapshot.child("C1").getValue(Double.class)+"");
                        c2.setText(dataSnapshot.child("C2").getValue(Double.class)+"");
                        break;
                    case 2:
                        c1.setText(dataSnapshot.child("C1").getValue(Double.class)+"");
                        c2.setText(dataSnapshot.child("C2").getValue(Double.class)+"");
                        break;
                    case 3:
                        c1.setText(dataSnapshot.child("C1").getValue(Double.class)+"");
                        c2.setText(dataSnapshot.child("C2").getValue(Double.class)+"");
                        break;
                    case 4:
                        for (DataSnapshot dayIter : dataSnapshot.getChildren()){
                            map.put(dayIter.getKey(),dayIter.getValue(Integer.class));
                        }



                        break;

                }

                alertDialog.show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    public void onClick(View view) {
        count += 1;
        sasuke.setAdapter(new MySasukeAdapter());
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView t;
        public VH(TextView itemView) {
            super(itemView);
            t = itemView;
        }
    }

    class MySasukeAdapter extends SasukeAdapter {
        @Override
        public int getRowCount() {
            return arr1.size()+1;
        }

        @Override
        public int getColumnCount() {
            return 8;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {


            if(row==0){

                String res="";

                switch (column) {
                    case 0:
                        res = "Date";
                        break;
                    case 1:
                        res = "Time";
                        break;
                    case 2:
                        res = "Particular";
                        ((VH) holder).t.setLayoutParams(new ViewGroup.LayoutParams(600, 80));
                        break;
                    case 3:
                        res = "Purchase (L)" ;
                        break;
                    case 4:
                        res = "Issue (L)";
                        break;
                    case 5:
                        res = "Balance";
                        break;
                    case 6:
                        res = "Volume check";
                        break;
                    case 7:
                        res = "Difference (L)";
                        break;
                }


                ((VH) holder).t.setText(res);
                ((VH) holder).t.setTextColor(Color.BLACK);
                ((VH) holder).t.setTextSize(20);
                ((VH) holder).t.setBackgroundColor(Color.WHITE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((VH) holder).t.setTextAppearance(R.style.Widget_AppCompat_Light_ActionBar_Solid);
                }


            }else {

                Maintankobject ob=arr1.get(row-1);
                String res="";

                switch (column) {
                    case 0:
                        res = ob.getAadate().substring(0,2)+"/"+ob.getAadate().substring(3,5)+"/"+ob.getAadate().substring(6);
                        break;
                    case 1:
                        res = ob.getAbtime();
                        break;
                    case 2:
                        res = ob.getBparticular();
                        ((VH) holder).t.setLayoutParams(new ViewGroup.LayoutParams(600, 80));
                        break;
                    case 3:
                        res = "     " + ob.getCpurchase();
                        break;
                    case 4:
                        res = "   " + String.format("%.2f",Math.abs(Double.valueOf(ob.getDissue())));
                        break;
                    case 5:
                        res = "   " + ob.getEbalance();
                        break;
                    case 6:
                        res = "      " + ob.getfCMS();
                        break;
                    case 7:
                        res = "      " + ob.getGdifference();
                        break;
                }


                ((VH) holder).t.setText(res);
                ((VH) holder).t.setTextColor(Color.BLACK);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(300, 80));
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(15);
            return new VH(tv);
        }

    }

    public void summaryget() {



        final AlertDialog.Builder builder = new AlertDialog.Builder(MaintankOil_output.this);
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
                new DatePickerDialog(MaintankOil_output.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 9)
                            sumdateStart = dayOfMonth + "/0" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        else
                            sumdateStart = dayOfMonth + "/" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        tvDateStart.setText(String.valueOf(dayOfMonth) + "/0" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));

                    }
                }, Integer.valueOf(datee.substring(4)), Integer.valueOf(datee.substring(2,4))-1, Integer.valueOf(datee.substring(0,2))).show();


            }
        });

        date2Im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MaintankOil_output.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 9)
                            sumdateEnd = dayOfMonth + "/0" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        else
                            sumdateEnd = dayOfMonth + "/" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
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
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sumdateStart);

                    date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sumdateEnd);
                    Log.d("Dates", "onClick: " + date1.toString() + date2.toString());

                    if (date1.after(date2)) {
                        Toast.makeText(MaintankOil_output.this, "Please Check the dates!", Toast.LENGTH_SHORT).show();
                        summaryget();
                    } else {
                        final Calendar calendar1 = new GregorianCalendar();
                        calendar1.setTime(date1);
                        final Calendar calendar2 = new GregorianCalendar();
                        calendar2.setTime(date2);
                        final int year1 = calendar1.get(Calendar.YEAR);
                        final int month1 = calendar1.get(Calendar.MONTH) + 1;
                        final int day1 = calendar1.get(Calendar.DAY_OF_MONTH);


                        final int year2 = calendar2.get(Calendar.YEAR);
                        final int month2 = calendar2.get(Calendar.MONTH) + 1;
                        final int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
                        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef1 = database1.getReference("OILMAINTANK").child("VALUES");
                        myRef1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    for (it = year1; it <= year2 + 1; it++) {

                                        Log.d("Years", String.valueOf(it) + "onDataChange: ");
                                        DatabaseReference myRef2 = database1.getReference("OILMAINTANK").child("VALUES").child(String.valueOf(it));
                                        myRef2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot yearIter : dataSnapshot.getChildren()) {


                                                    if(Integer.valueOf(yearIter.getKey().toString())>=month1&&Integer.valueOf(yearIter.getKey().toString())<=month2) {

                                                        Log.d("RangeDate", "onDataChange: " + yearIter.getKey().toString());

                                                        for (DataSnapshot monthIter : yearIter.getChildren()) {

                                                            Log.d("RangeDate month", "onDataChange: " + monthIter.getKey().toString());

///////////////////
                                                            String currentdate=monthIter.getKey()+"/"+yearIter.getKey()+"/"+dataSnapshot.getKey();
                                                            try {
                                                                Date currdate = new SimpleDateFormat("dd/MM/yyyy").parse(currentdate);

                                                                // if (Integer.valueOf(monthIter.getKey().toString()) >= day1 && Integer.valueOf(monthIter.getKey().toString()) <= day2)

                                                                Log.d("month", "onDataChange: " +date2+"  "+currdate+"  "+ date2.after(currdate)+"  "+date1.before(currdate)+"  "+date1.equals(currdate)+"  "+date2.equals(currdate));

                                                                if((date2.after(currdate)&&date1.before(currdate))||(date1.equals(currdate))||(date2.equals(currdate)))
                                                                {

                                                                for (DataSnapshot dayIter : monthIter.getChildren()) {


                                                                    Maintankobject obj = dayIter.getValue(Maintankobject.class);

                                                                    arr1.add(obj);
                                                                    Log.d("dayIIII", "onDataChange: " + arr1);
                                                                    sasuke = findViewById(R.id.sasuke);
                                                                    sasuke.setStickColumnHead(true);
                                                                    sasuke.setStickRowHead(true);
                                                                    sasuke.setStickColumnHead(true);
                                                                    sasuke.setStickRowHead(false);
                                                                    sasuke.setAdapter(new MySasukeAdapter());

                                                                }

                                                            }

                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }

                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                } else
                                    Toast.makeText(MaintankOil_output.this, "Entry Doesn't Exist", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                } catch (ParseException e) {
                    Toast.makeText(MaintankOil_output.this, "Dates could not be parsed, Please try again!", Toast.LENGTH_SHORT).show();
                    summaryget();
                }


            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();


    }


    int year = Calendar.getInstance().get(Calendar.YEAR);
    public ArrayList<String> arr = new ArrayList<>();


    public void selYear() {
        final String[] writeCSV = {""};
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MaintankOil_output.this);
        View view = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        builder.setTitle("Select Year to View Report");
        final Spinner spinner = view.findViewById(R.id.spinner);
        for (int i = year; i != year - 10; i--) {
            arr.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MaintankOil_output.this, R.layout.spinnerdropdown, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final FirebaseDatabase database1 = FirebaseDatabase.getInstance();

                writeCSV[0] += "DATE,TIME,PARTICULAR,PURCHASE,ISSUE,BALANCE,Volume check,DIFFERENCE,\n\n";
                final String spinnerval=String.valueOf(spinner.getSelectedItem().toString());
                final DatabaseReference myRef1 = database1.getReference("OILMAINTANK").child("VALUES").child(spinnerval);
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot yearIter : dataSnapshot.getChildren()) {
                                Log.d("201999", "onDataChange: " + yearIter.getKey() + "End");
                                String str="";
                                for (DataSnapshot monthIter : yearIter.getChildren()) {
                                   // writeCSV[0] += monthIter.getKey()+str + ",";

                                    for (DataSnapshot dayIter : monthIter.getChildren()) {
//                                            writeCSV += dayIter.

                                                boolean flag=false;
                                        for (DataSnapshot val : dayIter.getChildren()) {
                                                String value=val.getValue()+"";
                                                if(!flag) {
                                                    writeCSV[0] += value.substring(0, 2) + "/" + value.substring(3, 5) + "/" + (Integer.valueOf(value.substring(6, 8))-1) + ",";
                                                    flag=true;
                                                }else{
                                                    writeCSV[0] += value+",";
                                                }
                                               Log.d("TAGGON", "onDataChange: "+val.getValue());

                                        }
                                        writeCSV[0] += "\n";
                                    }

                                }

                                csvPart(writeCSV[0], "Year"+spinnerval);
                            }
                            Log.d("writecsv", "onDataChange: " + writeCSV[0]);

                        } else {
                            Toast.makeText(MaintankOil_output.this, "Entry Doesn't Exist", Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/OIL/MAINTANK/" + "Year" + ".csv");

            }
        });

        sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/OIL/MAINTANK/" + "Year" + ".csv");


        builder.setView(view);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        Log.d("Years", "selYear: " + arr.get(0) + arr.get(4));
    }

    public void selMonth() {
        Toast.makeText(this, "" + gasDownload[selected], Toast.LENGTH_SHORT).show();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MaintankOil_output.this);
        View view = getLayoutInflater().inflate(R.layout.two_spinner_dialog, null);
        builder.setTitle("Select Year and Month to View Report");
        final Spinner spinner = view.findViewById(R.id.spinner_one);
        for (int i = year; i != year - 10; i--) {
            arr.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MaintankOil_output.this, R.layout.spinnerdropdown, arr);
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

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MaintankOil_output.this, R.layout.spinnerdropdown, arrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        spinner.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MaintankOil_output.this, spinner.getSelectedItem().toString() + spinner2.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                final String spinnerval1=spinner.getSelectedItem().toString();
                 String spinnerval2="";
                if(spinner2.getSelectedItemPosition() + 1<10) {
                     spinnerval2 = "0"+String.valueOf(spinner2.getSelectedItemPosition() + 1);
                }else{
                    spinnerval2 = String.valueOf(spinner2.getSelectedItemPosition() + 1);
                }
                Log.d("TAGGING", "onClick: "+spinnerval1+" "+spinnerval2);

                final DatabaseReference myRef1 = database1.getReference("OILMAINTANK").child("VALUES").child(spinnerval1).child(spinnerval2);
                final String finalSpinnerval = spinnerval2;
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String csvWrite = "DATE,TIME,PARTICULAR,PURCHASE,ISSUE,BALANCE,Volume check,DIFFERENCE,\n\n";
                            String str="";
//                            str += " / "+spinner2.getSelectedItem().toString() ;
//                            str += " / "+spinner.getSelectedItem().toString() ;

                            for (DataSnapshot datesIter : dataSnapshot.getChildren()) {
                               // csvWrite += datesIter.getKey() +str+ ",";

                                for (DataSnapshot dayIter : datesIter.getChildren()) {

                                    boolean flag=false;
                                    for (DataSnapshot val : dayIter.getChildren()) {


                                        String value=val.getValue()+"";
                                        if(!flag) {
                                            csvWrite += value.substring(0, 2) + "/" + value.substring(3, 5) + "/" + (Integer.valueOf(value.substring(6, 8))-1) + ",";
                                            flag=true;
                                        }else{
                                            csvWrite += value+",";
                                        }
                                            Log.d("dateIter", "onDataChange: " + dayIter.getValue());

                                    }
                                    csvWrite += "\n";

                                }


                            }

                            Log.d("csvWrite", "onDataChange: " + csvWrite);
                            csvPart(csvWrite, "Month"+spinnerval1+"_"+ finalSpinnerval);



                        } else
                            Toast.makeText(MaintankOil_output.this, "Entry Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                Log.d("DATEtime", spinner.getSelectedItem().toString() + " " + (spinner2.getSelectedItemPosition() + 1));

                sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/OIL/MAINTANK/" + "Month" + ".csv");

            }
        });
        builder.setView(view);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    String csvWrite = "DATE,TIME,PARTICULAR,PURCHASE,ISSUE,BALANCE,Volume check,DIFFERENCE,\n\n";

    public void selRange() {
        Toast.makeText(this, "" + gasDownload[selected], Toast.LENGTH_SHORT).show();
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MaintankOil_output.this);
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
                new DatePickerDialog(MaintankOil_output.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 9)
                            dateStart = dayOfMonth + "/0" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        else
                            dateStart = dayOfMonth + "/" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        tvDateStart.setText(String.valueOf(dayOfMonth) + "/0" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));

                    }
                },  Integer.valueOf(datee.substring(4)), Integer.valueOf(datee.substring(2,4))-1, Integer.valueOf(datee.substring(0,2))).show();


            }
        });

        date2Im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MaintankOil_output.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 9)
                            dateEnd = dayOfMonth + "/0" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        else
                            dateEnd = dayOfMonth + "/" + String.valueOf(Integer.valueOf(monthOfYear + 1)) + "/" + year;
                        tvDateEnd.setText(String.valueOf(dayOfMonth) + "/0" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));

                    }
                },  Integer.valueOf(datee.substring(4)), Integer.valueOf(datee.substring(2,4))-1, Integer.valueOf(datee.substring(0,2))).show();


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
                        Toast.makeText(MaintankOil_output.this, "Please Check the dates!", Toast.LENGTH_SHORT).show();
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
                        final DatabaseReference myRef1 = database1.getReference("OILMAINTANK").child("VALUES");
                        myRef1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    for (it = year1; it <= year2 + 1; it++) {

                                        Log.d("Years", String.valueOf(it) + "onDataChange: ");
                                        DatabaseReference myRef2 = database1.getReference("OILMAINTANK").child("VALUES").child(String.valueOf(it));
                                        myRef2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot yearIter : dataSnapshot.getChildren()) {
                                                    Log.d("RangeDate", "onDataChange: " + yearIter.getKey().toString());
                                                    String str="";
                                                   // str += " / "+String.valueOf(dataSnapshot.getKey()) ;
                                                    //str= "/"+yearIter.getKey()+str ;
                                                    for (DataSnapshot monthIter : yearIter.getChildren()) {
                                                       // csvWrite += monthIter.getKey() + str + ",";
                                                        Log.d("monthIIII", "onDataChange: " + monthIter.getKey());
                                                        Log.d("monthIIII", "onDataChange: " + monthIter.getValue());

                                                        boolean flag=false;
                                                        for (DataSnapshot dayIter : monthIter.getChildren()) {

                                                            int count=0;
                                                            for (DataSnapshot val : dayIter.getChildren()) {
                                                                if(count!=1) {

                                                                    String value=val.getValue()+"";
                                                                    if(!flag) {
                                                                        csvWrite += value.substring(0, 2) + "/" + value.substring(3, 5) + "/" + (Integer.valueOf(value.substring(6, 8))-1) + ",";
                                                                        flag=true;
                                                                    }else{
                                                                        csvWrite += value+",";
                                                                    }
                                                                    Log.d("dayIIII", "onDataChange: " + dayIter.getKey());
                                                                    Log.d("dayIIII", "onDataChange: " + dayIter.getValue());
                                                                }

                                                            }

                                                            Log.d("day", "onDataChange: " + csvWrite);
                                                            csvWrite += "\n";

                                                        }

                                                        //csvWrite += String.valueOf(dataSnapshot.getKey()) + ",";
                                                    }
                                                }
                                                //csvWrite=csvWrite.substring(0,csvWrite.length()-6);
                                                csvWrite += "\n";

                                                Log.d("CSV", "selRange: " + csvWrite);
                                                csvPart(csvWrite, "Range"+date1.toString().substring(4,10)+"-"+date2.toString().substring(4,10));
                                                sendNotif(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/OIL/MAINTANK/" + "Range" + ".csv");
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                } else
                                    Toast.makeText(MaintankOil_output.this, "Entry Doesn't Exist", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                } catch (ParseException e) {
                    Toast.makeText(MaintankOil_output.this, "Dates could not be parsed, Please try again!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.setView(view);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();


    }


    public static String csvPart(String data, String name) {

        //        String a = "1,2,4,5,6";
        String filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App_Ka_Kaam/OIL/MAINTANK/" + name + ".csv";
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
        PendingIntent pendingIntent = PendingIntent.getActivity(MaintankOil_output.this, 1, intent,  PendingIntent.FLAG_CANCEL_CURRENT);
        createNotificationChannel();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.drawable.logoo)
                .setContentTitle("File Downloaded")
                .setContentText("Goto your File Manager in " + MaintankOil_output.this.getString(R.string.naam))
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
        startActivity(new Intent(MaintankOil_output.this, admin.class));
        finish();
    }



}