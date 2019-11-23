package com.shivam.appli;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.ADMIN.adminPasscode;
import com.shivam.appli.User.userpasscode;
import com.shivam.appli.sampleUtil.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(!checkPermission()){
            requestPermission();
        }
        else
        {
            afterPerm();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted)  afterPerm();

                    else {

                        FancyToast.makeText(this, "Permission Denied!", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION,WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void afterPerm()
    {
        fetchData();
        ImageView adminbtn=findViewById(R.id.admin);
        ImageView userbtn=findViewById(R.id.user);


        final int[] numAd = {0};
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("USER").child("PASSWORD").child("numpassword");


        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference myRef2 = database1.getReference("ADMIN").child("PASSWORD").child("numpassword");
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        numAd[0] =dataSnapshot.getValue(Integer.class);
                        if(numAd[0]==2) {

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat dateformat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                            String time = dateformat.format(c.getTime());

                            SharedPreferences sp = getSharedPreferences("appkakaam" , Context.MODE_PRIVATE);
                            String sc  = sp.getString("adminlastlogin","01 01 2000 00:00:00");


                            try {
                                Date date1 = dateformat.parse(time);
                                Date date2 = dateformat.parse(sc);
                                double diff = TimeUnit.MILLISECONDS.toSeconds(date1.getTime() - date2.getTime());
                                if(diff>=120){
                                    Intent i=new Intent(MainActivity.this,adminPasscode.class);
                                    i.putExtra("turnsAdmin",numAd[0]);
                                    startActivity(i);
                                    finish();
                                }else{
                                    FancyToast.makeText(MainActivity.this,"PLEASE WAIT FOR "+(120 -diff)+" SECONDS", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                        }else{
                            Intent i=new Intent(MainActivity.this, adminPasscode.class);
                            i.putExtra("turnsAdmin",numAd[0]);
                            startActivity(i);
                            finish();}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        final int[] num = {0};

        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        num[0] =dataSnapshot.getValue(Integer.class);
                        if(num[0]<=0){
                            FancyToast.makeText(MainActivity.this,"PLEASE CONTACT ADMIN FOR RESET OF PASSWORD", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }else if(num[0]==2) {
                            Toast.makeText(MainActivity.this, "H", Toast.LENGTH_SHORT).show();

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat dateformat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                            String time = dateformat.format(c.getTime());

                            SharedPreferences sp = getSharedPreferences("appkakaam" ,Context.MODE_PRIVATE);
                            String sc  = sp.getString("userlastlogin","01 01 2000 00:00:00");


                            try {
                                Date date1 = dateformat.parse(time);
                                Date date2 = dateformat.parse(sc);
                                double diff = TimeUnit.MILLISECONDS.toSeconds(date1.getTime() - date2.getTime());
                                if(diff>=120){
                                    Intent i=new Intent(MainActivity.this,userpasscode.class);
                                    i.putExtra("turns",num[0]);
                                    startActivity(i);
                                    finish();
                                }else{
                                    FancyToast.makeText(MainActivity.this,"PLEASE WAIT FOR "+(120 -diff)+" SECONDS", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                        }else{
                            Intent i=new Intent(MainActivity.this,userpasscode.class);
                            i.putExtra("turns",num[0]);
                            startActivity(i);
                            finish();}
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });


            }
        });
        //////////////////////////////////////////////////////////

//        String s = String.valueOf(FirebaseInstanceId.getInstance().getToken());
//        Log.d("TokenHere", "onCreate: "+s);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription(Constants.CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {100,200,300,400,500,400,300,200,400});
            notificationManager.createNotificationChannel(channel);
        }
    }

    Map<String,String> value;

    public void fetchData()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gasRef = database.getReference();
        DatabaseReference electMeena = database.getReference("ELECTRICITYMEENA");
        DatabaseReference electMukta = database.getReference("ELECTRICITYMUKTA");

        electMukta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> value = (Map<String,String>)dataSnapshot.getValue();
                Log.d("FetchedElectMukta", "Value is: " + value.toString());
                Log.d("FetchedELectMukta", "onDataChange: "+ Arrays.toString(value.entrySet().toArray()));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        electMeena.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> value = (Map<String,String>)dataSnapshot.getValue();
                Log.d("FetchedElectMeena", "Value is: " + value.toString());
                Log.d("FetchedELectMeena", "onDataChange: "+ Arrays.toString(value.entrySet().toArray()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                value = (Map<String,String>)dataSnapshot.getValue();
                Log.d("FetchedGas", "Value is: " + value.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
