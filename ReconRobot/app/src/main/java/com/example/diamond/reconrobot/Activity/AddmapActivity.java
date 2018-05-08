package com.example.diamond.reconrobot.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.diamond.reconrobot.Adapter.CMDAdapter;
import com.example.diamond.reconrobot.Model.CommandModel;
import com.example.diamond.reconrobot.R;
import com.example.diamond.reconrobot.Service.RecyclerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddmapActivity extends AppCompatActivity {
    private Button buttonSave;
    private Button buttonCancel;
    private EditText editTextCMD;
    private Button buttonForward;
    private Button buttonBackward;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonAdd;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private CMDAdapter adapter;

    private ArrayList<String> arrCMD;
    private ArrayList<CommandModel> arrCommand;

    private String[] str_CMD;
    private String mapname = "";

    private CommandModel commandModel;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmap);
        initFirebase();
        initView();
        initListener();
    }

    public void initFirebase() {
        database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mapname = extras.getString("SELECTED_MAP");
            myRef = database.getReference().child("MAP").child(mapname);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.child("direction").getValue() != null && ds.child("distance") != null) {
                                commandModel = new CommandModel();
                                commandModel.setCommand(ds.child("direction").getValue() + "");
                                commandModel.setDistance(Integer.parseInt(ds.child("distance").getValue() + ""));
                                arrCommand.add(commandModel);
                                adapter.setModel(arrCommand);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void initView() {
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancle);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonForward = (Button) findViewById(R.id.buttonForward);
        buttonBackward = (Button) findViewById(R.id.buttonBackward);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);

        editTextCMD = (EditText) findViewById(R.id.editTextCMD);

        adapter = new CMDAdapter(this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        arrCMD = new ArrayList<String>();
        arrCommand = new ArrayList<CommandModel>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclewView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }

    public void initListener() {

        buttonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCMD.setText("f");
                editTextCMD.setSelection(1);
            }
        });

        buttonBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCMD.setText("b");
                editTextCMD.setSelection(1);

            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCMD.setText("l");
                editTextCMD.setSelection(1);

            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCMD.setText("r");
                editTextCMD.setSelection(1);

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextCMD.getText().toString().equalsIgnoreCase("")) {
                    try {
                        String direction = editTextCMD.getText().toString().substring(0, 1).trim();

                        int distance = 0;

                        if (direction.equalsIgnoreCase("f") || direction.equalsIgnoreCase("b")) {
                            if (editTextCMD.getText().toString().substring(1).length() > 0) {
                                distance = Integer.parseInt(editTextCMD.getText().toString().substring(1));
                            }
                        }
                        else if(direction.equalsIgnoreCase("r") || direction.equalsIgnoreCase("l"))
                        {
                            if(editTextCMD.getText().toString().length()>1) throw new Exception();
                        }
                        else throw new Exception();
                        commandModel = new CommandModel();
                        commandModel.setCommand(direction);
                        commandModel.setDistance(distance);
                        arrCommand.add(commandModel);
                        adapter.setModel(arrCommand);
                        editTextCMD.setText("");
                        recyclerView.scrollToPosition(arrCommand.size() - 1);
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddmapActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("Please check input ");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editTextCMD.setText("");
                            }
                        });
                        builder.show();
                    }

                }

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(AddmapActivity.this);
                final EditText editTextMapName = new EditText(AddmapActivity.this);

                dialog.setMessage("Enter Map Name");
                dialog.setTitle("Confirm");
                dialog.setView(editTextMapName);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!mapname.equalsIgnoreCase("")) {
                            myRef = database.getReference().child("MAP").child(mapname);
                            myRef.removeValue();
                        }

                        String newMapName = editTextMapName.getText().toString();
                        myRef = database.getReference().child("MAP").child(newMapName);
                        int j = 0;
                        while (j < arrCommand.size()) {
                            String key = myRef.push().getKey();
                            Log.d("Key",key);
                            myRef.child(key).child("direction").setValue(arrCommand.get(j).getCommand());
                            myRef.child(key).child("distance").setValue(arrCommand.get(j).getDistance());
                            j++;

                        }
                        finish();

                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();


            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddmapActivity.this);
                builder.setMessage("Delete this command ?");
                builder.setTitle("Confirm ?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrCommand.remove(position);
                        adapter.setModel(arrCommand);
                    }
                });
                builder.show();
            }
        }));
    }
}
