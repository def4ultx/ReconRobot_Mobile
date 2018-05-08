package com.example.diamond.reconrobot.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.diamond.reconrobot.Model.CommandModel;
import com.example.diamond.reconrobot.R;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private Button buttonStart ;
    private Button buttonSetting ;
    private String mapname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initView();
        initListener();

    }
    public void initView(){
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            mapname = extras.getString("MAPNAME");
        }
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonSetting  = (Button) findViewById(R.id.buttonSetting);

    }
    public void initListener(){
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class).putExtra("MAPUSE",mapname));

            }
        });
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, SettingActivity.class));
//                finish();

            }
        });
    }
}
