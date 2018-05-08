package com.example.diamond.reconrobot.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.diamond.reconrobot.Model.CommandModel;
import com.example.diamond.reconrobot.R;
import com.example.diamond.reconrobot.Service.ReconJoystick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;


public class MainActivity extends RosActivity {

    private ReconJoystick reconJoystick;
    private RosImageView rosImageView;
    private ToggleButton toggleButton;
    private boolean flagBoo = false;
    private int distance = 35;
    private String mapname;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private CommandModel commandModel;
    private ArrayList<CommandModel> arrCommand;

    public MainActivity() {
        super("app", "app");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFirebase();
        initListener();


    }

    public void initView() {
        rosImageView = (RosImageView) findViewById(R.id.RosImageView);
        rosImageView.setTopicName("/camera/data_throttled_image/compressed");
        rosImageView.setMessageType("sensor_msgs/CompressedImage");
        rosImageView.setMessageToBitmapCallable(new BitmapFromCompressedImage());

        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        reconJoystick = (ReconJoystick)findViewById(R.id.reconJoystick);
        reconJoystick.setTopicName("/cmd_vel");
        

    }

    public void initFirebase(){
        database      = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            mapname = extras.getString("MAPUSE");
            System.out.println("mapname "+ mapname);
            if(mapname!=null){
                arrCommand = new ArrayList<CommandModel>();
                myRef   = database.getReference().child("MAP").child(mapname);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                commandModel = new CommandModel();
                                commandModel.setCommand(ds.child("direction").getValue()+"");
                                commandModel.setDistance(Integer.parseInt(ds.child("distance").getValue()+""));
                                arrCommand.add(commandModel);
                            }
                            //change State
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        }
    }

    public void initListener(){
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    flagBoo=false;
                    if(arrCommand.size()>0)
                    {
                        removeCallBack();
                    }
                }
                else
                {
                    flagBoo=true;
                }
            }
        });

    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(reconJoystick, nodeConfiguration.setNodeName("myApp"));

        NodeConfiguration nodeConfiguration1 = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration1.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(rosImageView, nodeConfiguration1.setNodeName("myAppImage"));
    }

    private void removeCallBack(){
        if(flagBoo)
        {
            reconJoystick.onContactUp();
            return;
        }
        else
        {
            if (arrCommand.size() > 0) {
                reconJoystick.onContactDown();
                if(arrCommand.get(0).getCommand().equalsIgnoreCase("r")||arrCommand.get(0).getCommand().equalsIgnoreCase("l"))
                {
                    new CountDownTimer(880,10){

                        @Override
                        public void onTick(long l) {
                            reconJoystick.Autonomous(arrCommand.get(0).getCommand().toString());
                        }

                        @Override
                        public void onFinish() {
                            arrCommand.remove(0);
                            reconJoystick.onContactUp();
                            removeCallBack();
                        }
                    }.start();
                }
                else if(arrCommand.get(0).getCommand().equalsIgnoreCase("f")||arrCommand.get(0).getCommand().equalsIgnoreCase("b"))
                {
                    int timedown = arrCommand.get(0).getDistance();
                    timedown = timedown *distance;

                    new CountDownTimer(timedown, 10) {
                        @Override
                        public void onTick(long l) {

                            reconJoystick.Autonomous(arrCommand.get(0).getCommand().toString());
                        }

                        @Override
                        public void onFinish() {
                            arrCommand.remove(0);
                            reconJoystick.onContactUp();
                            removeCallBack();
                        }
                    }.start();
                }
            }
            if(arrCommand.size()==0){
                reconJoystick.Autonomous("stop");
                reconJoystick.onContactUp();
            }
        }


    }


}
