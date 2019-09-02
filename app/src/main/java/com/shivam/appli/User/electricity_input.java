package com.shivam.appli.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.Java_objects.electricitylastvalue;
import com.shivam.appli.Java_objects.electricityconstants;
import com.shivam.appli.Java_objects.electricity_object;
import com.shivam.appli.MainActivity;
import com.shivam.appli.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class electricity_input extends AppCompatActivity {

    String timeelectricity="";
    String pathway;
    electricitylastvalue[] lastvalue = new electricitylastvalue[1];
    electricityconstants[] constant = new electricityconstants[1];
    AlertDialog alertDialog;
    ProgressBar loader;
    double from,to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_input);

        loader=findViewById(R.id.loader);

         pathway=getIntent().getStringExtra("path");
        TextView path=findViewById(R.id.path);
        path.setText("USER/ELECTRICITY/"+pathway);

getrange();

        TextView datetime=findViewById(R.id.datetime);
        gettimedate(datetime);



        Button close =findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(electricity_input.this,user.class));
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



    public  void onclickbutton() {

        EditText electricityinput1 = findViewById(R.id.electricityinput1);
        String datastr1=electricityinput1.getText().toString()+"";


        EditText electricityinput2 = findViewById(R.id.electricityinput2);
        String datastr2=electricityinput2.getText().toString()+"";


        EditText electricityinput3 = findViewById(R.id.electricityinput3);
        String datastr3=electricityinput3.getText().toString()+"";


        EditText electricityinput4 = findViewById(R.id.electricityinput4);
        String datastr4=electricityinput4.getText().toString()+"";



        if (datastr1.length() == 0||datastr2.length()==0||datastr3.length()==0||datastr4.length()==0) {
            FancyToast.makeText(this, "PLEASE ENTER THE INPUT ", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

        } else {
            FancyToast.makeText(this, "WAIT WHILE WE ARE MAKING EVERYTHING READY ", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            loader.setVisibility(View.VISIBLE);
            final double data1 = Double.valueOf(datastr1);
            final double data2 = Double.valueOf(datastr2);
            final double data3 = Double.valueOf(datastr3);
            final double data4 = Double.valueOf(datastr4);

            alertDialog = new AlertDialog.Builder(electricity_input.this)
                    .setTitle("CONFIRMATION:")
                    .setMessage("\nKWH: " + data1 + "\n\n" + "KVAH: " + data2 + "\n\n" + "Meter P.F: " + data3 + "\n\n" + "Panel P.F: " + data4)
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            final int year = Integer.valueOf(timeelectricity.substring(6, 10));
                            final int month = Integer.valueOf(timeelectricity.substring(3, 5));
                            final int date = Integer.valueOf(timeelectricity.substring(0, 2));

                            //writing value
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("ELECTRICITY" + pathway).child(year + "").child(month + "");


///////////////////////////////////////////////////////
                            SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
                            String inputString1 = lastvalue[0].getDate();
                            String inputString2 = timeelectricity;

                            try {
                                Date date1 = myFormat.parse(inputString1);
                                Date date2 = myFormat.parse(inputString2);
                                long diff = TimeUnit.MILLISECONDS.toHours(date2.getTime() - date1.getTime());
                                if(diff<24){
                                    FancyToast.makeText(electricity_input.this,"YOU HAVE ALREADY ENTERED THE DATA", Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
                                    startActivity(new Intent(electricity_input.this, MainActivity.class));
                                    finish();
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
///////////////////////////////////////////////////////





                            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef1 = database1.getReference("ELECTRICITY" + pathway).child("LASTVALUE");

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild(date + "")) {
                                        electricity_object obj = insertvalues(data1, data2, data3, data4);
                                        myRef.child(date + "").setValue(obj);

                                        LayoutInflater inflater = getLayoutInflater();
                                        View layout = inflater.inflate(R.layout.my_custom_toast,
                                                (ViewGroup) findViewById(R.id.custom_toast_layout));

                                        TextView text = (TextView) layout.findViewById(R.id.textToShow);

                                        text.setText("Calculated P.F : "+String.format("PF: "+"%.2f",Double.valueOf(obj.getGcal_pf())));

                                        Toast toast = new Toast(getApplicationContext());
                                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();




                                        if (Double.valueOf(obj.getGcal_pf()) < from || Double.valueOf(obj.getGcal_pf()) > to) {
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            final DatabaseReference myRef = database.getReference("NOTIFY");
                                            myRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        String str = postSnapshot.getValue(String.class);
                                                        setnotify(str);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError error) {

                                                }
                                            });

                                        }

                                        FancyToast.makeText(electricity_input.this, "THANK YOU FOR UPDATING \n\n YOU HAVE BEEN LOGGED OUT", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        electricitylastvalue obj1 = new electricitylastvalue(timeelectricity, String.format("%.2f",data1), String.format("%.2f",data2));
                                        myRef1.setValue(obj1);
                                        startActivity(new Intent(electricity_input.this, MainActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("TAG", "Failed to read value.", error.toException());
                                }
                            });
                        }
                    })
                    .create();

            getprevoiusdata();

        }
    }



    public  void setnotify(String token){
        final String Legacy_SERVER_KEY = "AIzaSyAvM2ng-9ZVbHUzR2boRRQyBNnzSd9O1K0";
        String msg = "USER HAS ENTERED OUT OF RANGE DATA in ELECTRICITY " + pathway;
        String title = "WARNING";
        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", R.drawable.warning); //   icon_name image must be there in drawable
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }




    public void getrange(){
        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference("ELECTRICITY" + pathway).child("RANGE");
        myRef11.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                from =dataSnapshot.child("FROM").getValue(Double.class);
                to =dataSnapshot.child("TO").getValue(Double.class);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    public  electricity_object insertvalues(double input1,double input2,double input3,double input4){
        electricity_object obj = new electricity_object();

        DecimalFormat df = new DecimalFormat("#.000");


        obj.setAkwh( String.format("%.2f",input1));
        obj.setCkvah( String.format("%.2f",input2));
        obj.setEmpf( String.format("%.2f",input3));
        obj.setFppf( String.format("%.2f",input4));
        obj.setGcal_pf( String.format("%.2f",((input1-Double.valueOf(lastvalue[0].getKwh()))/(input2-Double.valueOf(lastvalue[0].getKvah())))));
        obj.setBdiffkwh( String.format("%.2f",input1-Double.valueOf(lastvalue[0].getKwh())));
        double diffkvah=input2-Double.valueOf(lastvalue[0].getKvah());
        obj.setDdiffkvah( String.format("%.2f",diffkvah));
        obj.setTime(timeelectricity.substring(10));
        obj.setGlastval(lastvalue[0].getDate().substring(0,10));

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        String inputString1 = lastvalue[0].getDate();
        String inputString2 = timeelectricity;

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = TimeUnit.MILLISECONDS.toHours(date2.getTime() - date1.getTime());
            if(diff==0)
                diff=1;

            obj.setHamount1( String.format("%.2f",(diffkvah*constant[0].getC1()*constant[0].getC3()*24)/diff));
            obj.setIamount2( String.format("%.2f",(diffkvah*constant[0].getC2()*constant[0].getC3()*24)/diff));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  obj;

    }


    public void getprevoiusdata(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("ELECTRICITY"+pathway).child("LASTVALUE");

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("ELECTRICITY"+pathway).child("CONSTANTS");


        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                constant[0] =dataSnapshot.getValue(electricityconstants.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });



        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lastvalue[0] =dataSnapshot.getValue(electricitylastvalue.class);
                alertDialog.show();
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


    }



    public  void gettimedate(final TextView datetime){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd MM yyyy HH:mm");
        timeelectricity = dateformat.format(c.getTime());
        String str=timeelectricity.substring(0,2)+"/"+timeelectricity.substring(3,5)+"/"+timeelectricity.substring(6,10)+"   -  "+timeelectricity.substring(10);
                    datetime.setText(str);
                    loader.setVisibility(View.GONE);}


    @Override
    public void onBackPressed() {
        startActivity(new Intent(electricity_input.this, user.class));
        finish();
    }

}






