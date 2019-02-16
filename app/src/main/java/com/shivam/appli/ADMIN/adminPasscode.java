package com.shivam.appli.ADMIN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.appli.MainActivity;
import com.shivam.appli.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class adminPasscode extends AppCompatActivity {

    int str=0;
    int[] ADMINPASSLEFT=new int[1];

    EditText passcode;
    TextView turnleft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_passcode);

        int xyz=getIntent().getIntExtra("turnsAdmin",0);

        final LinearLayout linearlogin=findViewById(R.id.linearlogin);


        Button goback=findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminPasscode.this, MainActivity.class));
                finish();
            }
        });


        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference("ADMIN").child("PASSWORD").child("numpassword");
        myRef11.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ADMINPASSLEFT[0] = dataSnapshot.getValue(Integer.class);

                final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                final DatabaseReference myRef1 = database1.getReference("ADMIN").child("PASSWORD").child("strpassword");

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
                final DatabaseReference myRef11 = database11.getReference("ADMIN").child("PASSWORD").child("numpassword");

                Log.e("TAG", "onClick:ukcm "+(str-passcodestr) );

                if(str-passcodestr==0){
                    passcode.setText("");
                    myRef11.setValue(5);
                    startActivity(new Intent(adminPasscode.this, admin.class));
                    finish();
                }else{
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                    String time = dateformat.format(c.getTime());
                    SharedPreferences sp = getSharedPreferences("appkakaam" , Context.MODE_PRIVATE);
                    sp.edit().putString("adminlastlogin",time).apply();

                    myRef11.setValue(--ADMINPASSLEFT[0]);
                    passcode.setText("");
                    FancyToast.makeText(adminPasscode.this,"INCORRECT PASSCODE", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    turnleft.setText("* YOU ARE LEFT WITH "+ ADMINPASSLEFT[0] +" CHANCES");


                    if(ADMINPASSLEFT[0]==2){
                        Log.e("TAG", "onClick: " );
                        startActivity(new Intent(adminPasscode.this,MainActivity.class));
                        finish();
                    }


                    if(ADMINPASSLEFT[0]==0){

                        int i= (int) (Math.random()*10000);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("ADMIN").child("PASSWORD");
                        myRef.child("numpassword").setValue(5);
                        myRef.child("strpassword").setValue(i);



                        GMailSender.withAccount("mukta.khurja@gmail.com", "0202mukta2019")
                                .withTitle("PASSCODE RESET")
                                .withBody("NEW PASSCODE FOR APPLICATION IS :"+i)
                                .withSender(getString(R.string.app_name))
                                .toEmailAddress("mukta.khurja@gmail.com") // one or multiple addresses separated by a comma
                                .withListenner(new GmailListener() {
                                    @Override
                                    public void sendSuccess() {
                                        Toast.makeText(adminPasscode.this, "Success", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void sendFail(String err) {
                                        Toast.makeText(adminPasscode.this, "Fail: " + err, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .send();

                        Log.e("TAG", "onClick: " );
                        startActivity(new Intent(adminPasscode.this,MainActivity.class));
                        finish();
                    }

                }


            }
        });



    }
}
