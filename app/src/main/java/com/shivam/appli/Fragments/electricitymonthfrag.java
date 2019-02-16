package com.shivam.appli.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.appli.Java_objects.electricity_object;
import com.shivam.appli.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class electricitymonthfrag extends Fragment {


    ArrayList<electricity_object> dataarray=new ArrayList<>();
    String[] datastr;
    ArrayList<String>dates=new ArrayList<>();
    Context context;
  String path;
  double from,to;

     int year;
     int month;

    public electricitymonthfrag() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public electricitymonthfrag(Context context,String path) {
        this.context=context;
        this.path=path;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View rootview= inflater.inflate(R.layout.fragment_electricitymonthfrag, container, false);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,0);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        final String strdate = dateformat.format(c.getTime());

         year=Integer.valueOf(strdate.substring(0,4));
         month=Integer.valueOf(strdate.substring(4,6));




        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference(path).child("RANGE");
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





        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference(path).child(String.valueOf(year)).child(String.valueOf(month));

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    dataarray.add(postSnapshot.getValue(electricity_object.class));
                    dates.add(postSnapshot.getKey());
                }

                datastr=new String[dataarray.size()];
                for(int i=0;i<dataarray.size();i++) {
                    datastr[i]= dates.get(i)+"/" + month + "/" + year + "     " + (float)dataarray.get(i).getGcal_pf() + "       " + (float) dataarray.get(i).getIamount2();
                    Log.e("TAGhvb", "onDataChange: "+datastr[i]);
                }
                //String[] users = { "Suresh Dasari", "Rohini Alavala", "Trishika Dasari", "Praveen Alavala", "Madav Sai"};

               // ArrayAdapter itemsAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,datastr);

                ListView listView = (ListView) rootview.findViewById(R.id.electricitylist);
                listView.setAdapter(new electricitymonthfrag.CustomListAdapter(context, dataarray));



            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });



        return rootview;
    }






    public class CustomListAdapter extends BaseAdapter {
        private ArrayList<electricity_object> listData;
        private LayoutInflater layoutInflater;
        public CustomListAdapter(Context aContext, ArrayList<electricity_object> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(aContext);
        }
        @Override
        public int getCount() {
            return listData.size();
        }
        @Override
        public electricity_object getItem(int position) {
            return listData.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View v, ViewGroup vg) {
            ViewHolder holder;
            if (v == null) {
                v = layoutInflater.inflate(R.layout.gaslistitem, null);
                holder = new ViewHolder();
                holder.uName =  v.findViewById(R.id.date);
                holder.uDesignation =  v.findViewById(R.id.val1);
                holder.uLocation =  v.findViewById(R.id.val2);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.uName.setText(dates.get(position)+"/"+month+"/"+year);
            holder.uDesignation.setText((float)listData.get(position).getGcal_pf()+"");
            holder.uLocation.setText((float)listData.get(position).getIamount2()+"");

            if((float)listData.get(position).getGcal_pf()>=from&&(float)listData.get(position).getGcal_pf()<=to){
                holder.uDesignation.setTextColor(Color.GREEN);
                holder.uLocation.setTextColor(Color.GREEN);
                holder.uName.setTextColor(Color.GREEN);
            }else{
                holder.uDesignation.setTextColor(Color.RED);
                holder.uLocation.setTextColor(Color.RED);
                holder.uName.setTextColor(Color.RED);
            }

            return v;
        }
        class ViewHolder {
            TextView uName;
            TextView uDesignation;
            TextView uLocation;
        }
    }

}

