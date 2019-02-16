package com.shivam.appli.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shivam.appli.MainActivity;
import com.shivam.appli.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class userpasscode extends AppCompatActivity {
    int str=0;
     int[] USERPASSLEFT=new int[1];

    EditText passcode;
    TextView turnleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpasscode);

        int xyz=getIntent().getIntExtra("turns",0);

        final LinearLayout linearlogin=findViewById(R.id.linearlogin);


        Button goback=findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userpasscode.this, MainActivity.class));
                finish();
            }
        });



        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference("USER").child("PASSWORD").child("numpassword");
        myRef11.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                USERPASSLEFT[0] = dataSnapshot.getValue(Integer.class);

                final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                final DatabaseReference myRef1 = database1.getReference("USER").child("PASSWORD").child("strpassword");

                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        str=dataSnapshot.getValue(Integer.class);
                        linearlogin.setVisibility(View.VISIBLE);
                        Log.e("TAG", "onDataChange: "+str );

                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });







        turnleft=findViewById(R.id.turnleft);
        turnleft.setText("* YOU ARE LEFT WITH "+xyz+" CHANCES");

        passcode=findViewById(R.id.userpasscode);

        Button passsubmit=findViewById(R.id.passsubmit);
    passsubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("TAG", "onClick:ukcm " );

            final int passcodestr=Integer.valueOf(passcode.getText().toString());

            final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
            final DatabaseReference myRef11 = database11.getReference("USER").child("PASSWORD").child("numpassword");

            Log.e("TAG", "onClick:ukcm "+(str-passcodestr) );

            if(str-passcodestr==0){
                passcode.setText("");
                myRef11.setValue(5);
                startActivity(new Intent(userpasscode.this,user.class));
                finish();
            }else{
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                String time = dateformat.format(c.getTime());
                SharedPreferences sp = getSharedPreferences("appkakaam" , Context.MODE_PRIVATE);
                sp.edit().putString("userlastlogin",time).apply();

                myRef11.setValue(--USERPASSLEFT[0]);
                passcode.setText("");
                FancyToast.makeText(userpasscode.this,"INCORRECT PASSCODE", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                turnleft.setText("* YOU ARE LEFT WITH "+ USERPASSLEFT[0] +" CHANCES");
            }

            if(USERPASSLEFT[0]==2){
                Log.e("TAG", "onClick: " );
                startActivity(new Intent(userpasscode.this,MainActivity.class));
                finish();
            }


            if(USERPASSLEFT[0]==0){
                Log.e("TAG", "onClick: " );
                startActivity(new Intent(userpasscode.this,MainActivity.class));
                finish();
            }
        }
    });

    }

}
