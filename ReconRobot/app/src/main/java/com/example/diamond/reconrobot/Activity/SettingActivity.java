package com.example.diamond.reconrobot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.diamond.reconrobot.Model.CommandModel;
import com.example.diamond.reconrobot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private Button buttonSave;
    private Button buttonEdit;
    private Button buttonAdd;

    private Spinner spinner;

    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String selectMapname;

    private Intent intent;

    private ArrayList<CommandModel> arrCommand;

    private CommandModel commandModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initFirebase();
        initListener();
    }

    public void initFirebase(){
        System.out.println("InitFirebase");
        database    = FirebaseDatabase.getInstance();
        myRef       = database.getReference().child("MAP");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    arrayList.clear();
                    System.out.println("ONDATACHANGE");
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Log.d("DSS :",ds.getKey());
                        arrayList.add(ds.getKey());
                    }
                    spinner.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void initView(){
        buttonSave  = (Button)findViewById(R.id.buttonSaveSetting);
        buttonAdd   = (Button)findViewById(R.id.buttonNewMap);
        buttonEdit  = (Button)findViewById(R.id.buttonEditMap);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        spinner     = (Spinner)findViewById(R.id.spinner);


    }

    public void initListener(){

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position    = spinner.getSelectedItemPosition();
                selectMapname   = arrayList.get(position).toString();
                Log.d("DSS map",selectMapname);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent  = new Intent(SettingActivity.this,AddmapActivity.class);
                startActivity(intent);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent  = new Intent(SettingActivity.this,AddmapActivity.class);
                intent.putExtra("SELECTED_MAP",selectMapname);
                startActivity(intent);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent  = new Intent(SettingActivity.this,MenuActivity.class);
                intent.putExtra("MAPNAME",selectMapname);
                startActivity(intent);
                finish();
            }
        });
    }

}
