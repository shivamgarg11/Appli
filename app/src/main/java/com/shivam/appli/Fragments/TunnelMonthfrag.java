package com.shivam.appli.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.appli.Java_objects.Tunneltank_object;
import com.shivam.appli.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TunnelMonthfrag extends Fragment {

    ArrayList<Tunneltank_object> dataarray=new ArrayList<>();
    String[] datastr;
    ArrayList<String>dates=new ArrayList<>();
    Context context;
    int year;
    int month;
    double from,to;
    String pathway="";


    public TunnelMonthfrag() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public TunnelMonthfrag(Context context ,int year,int month,String pathway) {
        this.context=context;
        this.year=year;
        this.month=month;
        this.pathway=pathway;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview= inflater.inflate(R.layout.fragment_tunnel_monthfrag, container, false);
        final FirebaseDatabase database11 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef11 = database11.getReference("OIL"+pathway).child("RANGE");
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
        final DatabaseReference myRef1 = database1.getReference("OIL"+pathway).child(String.valueOf(year)).child(String.valueOf(month));

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    dataarray.add(postSnapshot.getValue(Tunneltank_object.class));
                    dates.add(postSnapshot.getKey());
                }

                datastr=new String[dataarray.size()];
                for(int i=0;i<dataarray.size();i++) {
                    datastr[i]= dates.get(i)+"/" + month + "/" + year + "          " + dataarray.get(i).getBreading() + "           " + Double.valueOf( dataarray.get(i).getFoutput2());
                    Log.e("TAGhvb", "onDataChange: "+datastr[i]);
                }

                GridView listView = rootview.findViewById(R.id.oillist);
                listView.setAdapter(new TunnelMonthfrag.CustomListAdapter(context, dataarray));


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
        private ArrayList<Tunneltank_object> listData;
        private LayoutInflater layoutInflater;
        public CustomListAdapter(Context aContext, ArrayList<Tunneltank_object> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(aContext);
        }
        @Override
        public int getCount() {
            return listData.size();
        }
        @Override
        public Tunneltank_object getItem(int position) {
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
                holder.layout=v.findViewById(R.id.gasitemlayout);
                holder.gridframecolor=v.findViewById(R.id.gridframecolor);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.uName.setText(dates.get(position)+"/"+month+"/"+year);
            holder.uDesignation.setText(listData.get(position).getBreading()+"");

            NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "IN"));
            String moneyString = formatter.format(Double.valueOf(listData.get(position).getFoutput2()));
            holder.uLocation.setText(moneyString+" l");

            if(Double.valueOf(listData.get(position).getFoutput2())>=from&&Double.valueOf(listData.get(position).getFoutput2())<=to){
                holder.gridframecolor.setBackgroundColor(Color.rgb(14,131,19));
                holder.uLocation.setTextColor(Color.rgb(14,131,19));
            }else{
                holder.gridframecolor.setBackgroundColor(Color.RED);
                holder.uLocation.setTextColor(Color.RED);
            }

            final int pos=position;
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String mon="";
                    if(month<10)
                        mon="0"+month;
                    else
                        mon=month+"";

                    FragmentManager fragmentManager = getFragmentManager();
                    Tunnelsummaryfrag frag = new Tunnelsummaryfrag(""+year+mon+dates.get(pos),context,pathway);
                    fragmentManager.beginTransaction().replace(R.id.frame, frag).commit();
                }
            });


            return v;
        }
        class ViewHolder {
            TextView uName;
            TextView uDesignation;
            TextView uLocation;
            LinearLayout layout;
            FrameLayout gridframecolor;
        }
    }





}
