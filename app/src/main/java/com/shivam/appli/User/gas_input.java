package com.shivam.appli.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shivam.appli.Java_objects.gas_object;
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
import com.shivam.appli.Java_objects.gasconstants;
import com.shivam.appli.Java_objects.gaslastvalue;
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

public class gas_input extends AppCompatActivity {

     String  timegas="";
    String pathway="";
     gaslastvalue[] lastvalue = new gaslastvalue[1];
     gasconstants[] constant = new gasconstants[1];
    AlertDialog alertDialog;
    ProgressBar loader;
    double from,to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_input);

        loader=findViewById(R.id.loader);

        pathway=getIntent().getStringExtra("path");
        TextView path=findViewById(R.id.path);
        path.setText("USER/GAS/"+pathway);

        getrange();

        TextView datetime=findViewById(R.id.datetime);
        gettimedate(datetime);


        Button close =findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(gas_input.this,user.class));
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
        final EditText gasinput = findViewById(R.id.gasinput);
        final String data = gasinput.getText().toString();

        if (data.length() == 0) {
            FancyToast.makeText(this, "PLEASE ENTER THE INPUT ", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

        } else {

            loader.setVisibility(View.VISIBLE);
            FancyToast.makeText(this, "WAIT WHILE WE ARE MAKING EVERYTHING READY ", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();


            alertDialog = new AlertDialog.Builder(gas_input.this)
                    .setIcon(R.drawable.logoo)
                    .setTitle("CONFIRMATION")
                    .setMessage("\nDATA : " + data + "\n\n")
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //WRITING TO FIREBASE

                            //writing value
                            final int year = Integer.valueOf(timegas.substring(6, 10));
                            final int month = Integer.valueOf(timegas.substring(3, 5));
                            final int date = Integer.valueOf(timegas.substring(0, 2));

                            //writing value
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("GAS" + pathway).child(year + "").child(month + "");

///////////////////////////////////////////////////////
                            SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
                            String inputString1 = lastvalue[0].getDate();
                            String inputString2 = timegas;

                            try {
                                Date date1 = myFormat.parse(inputString1);
                                Date date2 = myFormat.parse(inputString2);
                                long diff = TimeUnit.MILLISECONDS.toHours(date2.getTime() - date1.getTime());
                                if(diff<24){
                                    FancyToast.makeText(gas_input.this,"YOU HAVE ALREADY ENTERED THE DATA", Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
                                    startActivity(new Intent(gas_input.this, MainActivity.class));
                                    finish();
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
///////////////////////////////////////////////////////



                            //Writing lastvalue
                            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef1 = database1.getReference("GAS" + pathway).child("LASTVALUE");

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild(date + "")) {
                                        gas_object obj = insertvalues(Double.valueOf(data));
                                        myRef.child(date + "").setValue(obj);

                                        if(Double.valueOf(obj.getFbill())<from||Double.valueOf(obj.getFbill())>to) {
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


                                        FancyToast.makeText(gas_input.this, "THANK YOU FOR UPDATING \n\n YOU HAVE BEEN LOGGED OUT", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        gaslastvalue obj1 = new gaslastvalue(timegas, Double.valueOf(data).toString());
                                        myRef1.setValue(obj1);
                                        startActivity(new Intent(gas_input.this, MainActivity.class));
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


    public void getrange(){
        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference("GAS" + pathway).child("RANGE");
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



    public  void setnotify(String token){
        final String Legacy_SERVER_KEY = "AIzaSyB-XTKZWELMtRv88aVymt7EOTVWdrJuOnA";
        String msg = "USER HAS ENTERED OUT OF RANGE DATA in GAS MUKTA";
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
            objData.put("icon","logoo"); //   icon_name image must be there in drawable
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


    public  gas_object insertvalues(double input){

           gas_object obj = new gas_object();
           obj.setAinput(String.format("%.2f",input));
           obj.setBdifference(String.format("%.2f",input-Double.valueOf(lastvalue[0].getValue())));
           obj.setCscm(String.format("%.2f",Double.valueOf(obj.getBdifference())*constant[0].getC1()));
           obj.setDmmbto(String.format("%.2f",(Double.valueOf(obj.getCscm())*constant[0].getC2()*constant[0].getC3())/constant[0].getC5()));
           obj.setGlastval(lastvalue[0].getDate().substring(0,10));
           obj.setTime(timegas.substring(10));

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        String inputString1 = lastvalue[0].getDate();
        String inputString2 = timegas;

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = TimeUnit.MILLISECONDS.toHours(date2.getTime() - date1.getTime());
           if(diff==0)
            diff=1;
            obj.setEride(String.format("%.2f",(Double.valueOf(obj.getDmmbto())*constant[0].getC4()*24)/diff));
            obj.setFbill(String.format("%.2f",(Double.valueOf(obj.getEride())*15*24)/diff));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  obj;

    }


    public void getprevoiusdata(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("GAS"+pathway).child("LASTVALUE");

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("GAS"+pathway).child("CONSTANTS");



        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                constant[0] =dataSnapshot.getValue(gasconstants.class);
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

                lastvalue[0] =dataSnapshot.getValue(gaslastvalue.class);
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


    public void gettimedate(final TextView datetime){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd MM yyyy HH:mm");
        timegas = dateformat.format(c.getTime());
        String str=timegas.substring(0,2)+"/"+timegas.substring(3,5)+"/"+timegas.substring(6,10)+"   -  "+timegas.substring(10);
                    datetime.setText(str);
                    loader.setVisibility(View.GONE);}


    @Override
    public void onBackPressed() {
        startActivity(new Intent(gas_input.this, user.class));
        finish();
    }
}
