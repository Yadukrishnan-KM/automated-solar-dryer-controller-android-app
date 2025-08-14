package com.example.one.ui.home;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.one.MainActivity;
import com.example.one.R;
import com.example.one.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference serverDryerRoom1 = database.getReference("Dryer_Room_1");
    private DatabaseReference serverDryerRoom2 = database.getReference("Dryer_Room_2");
    private DatabaseReference serverOutside = database.getReference("Outside");
    private DatabaseReference serverOthers = database.getReference("Others");

    private TextView textRoom1Temperature,textRoom1Humidity,textRoom1SensorStatus,textRoom1TemperatureMax,textRoom1TemperatureMin,textRoom1HumidityMax,textRoom1HumidityMin,textRoom1Fan,textRoom1Valve;
    private TextView textRoom2Temperature,textRoom2Humidity,textRoom2SensorStatus,textRoom2TemperatureMax,textRoom2TemperatureMin,textRoom2HumidityMax,textRoom2HumidityMin,textRoom2Fan,textRoom2Valve;
    private TextView textOutsideTemperature,textOutsideHumidity,textOutsideSensorStatus,textOutsideTemperatureMax,textOutsideTemperatureMin,textOutsideHumidityMax,textOutsideHumidityMin,textOutsidePump;
    private TextView textLastUpdated1,textLastUpdated2,textLastUpdated3;

    private Switch switchRoom1Fan,switchRoom1Valve,switchRoom2Fan,switchRoom2Valve,switchOutsidePump;

    private String room1Temp,room1Hum,room1TempMax,room1TempMin,room1HumMax,room1HumMin,room1Fan,room1Valve,room1SensorStatus,room1FanStatus,room1ValveStatus;
    private String room2Temp,room2Hum,room2TempMax,room2TempMin,room2HumMax,room2HumMin,room2Fan,room2Valve,room2SensorStatus,room2FanStatus,room2ValveStatus;
    private String outsideTemp,outsideHum,outsideTempMax,outsideTempMin,outsideHumMax,outsideHumMin,outsidePump,outsideSensorStatus,outsidePumpStatus;
    private String lastUpdated;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textRoom1Temperature = root.findViewById(R.id.room1Card1LinearLayout1Table1Row3Text1);
        textRoom1Humidity = root.findViewById(R.id.room1Card1LinearLayout1Table1Row3Text2);
        switchRoom1Fan = root.findViewById(R.id.room1Card1LinearLayout1Table3Row1Switch1);
        switchRoom1Valve = root.findViewById(R.id.room1Card1LinearLayout1Table3Row1Switch2);
        textRoom2Temperature = root.findViewById(R.id.room2Card1LinearLayout1Table1Row3Text1);
        textRoom2Humidity = root.findViewById(R.id.room2Card1LinearLayout1Table1Row3Text2);
        switchRoom2Fan = root.findViewById(R.id.room2Card1LinearLayout1Table3Row1Switch1);
        switchRoom2Valve = root.findViewById(R.id.room2Card1LinearLayout1Table3Row1Switch2);
        textOutsideTemperature = root.findViewById(R.id.outside1Card1LinearLayout1Table1Row3Text1);
        textOutsideHumidity = root.findViewById(R.id.outside1Card1LinearLayout1Table1Row3Text2);
        switchOutsidePump = root.findViewById(R.id.outside1Card1LinearLayout1Table3Row1Switch1);
        textRoom1SensorStatus = root.findViewById(R.id.room1Card1LinearLayout1Table1Row1Text2);
        textRoom1TemperatureMax = root.findViewById(R.id.room1Card1LinearLayout1Table3Row1Text1);
        textRoom1TemperatureMin = root.findViewById(R.id.room1Card1LinearLayout1Table2Row1Text2);
        textRoom1HumidityMax = root.findViewById(R.id.room1Card1LinearLayout1Table3Row1Text3);
        textRoom1HumidityMin = root.findViewById(R.id.room1Card1LinearLayout1Table3Row1Text4);
        textRoom1Fan = root.findViewById(R.id.room1Card1LinearLayout1Table3_1Row1Text1);
        textRoom1Valve = root.findViewById(R.id.room1Card1LinearLayout1Table3_1Row1Text2);

        textRoom2SensorStatus = root.findViewById(R.id.room2Card1LinearLayout1Table1Row1Text2);
        textRoom2TemperatureMax = root.findViewById(R.id.room2Card1LinearLayout1Table3Row1Text1);
        textRoom2TemperatureMin = root.findViewById(R.id.room2Card1LinearLayout1Table2Row1Text2);
        textRoom2HumidityMax = root.findViewById(R.id.room2Card1LinearLayout1Table3Row1Text3);
        textRoom2HumidityMin = root.findViewById(R.id.room2Card1LinearLayout1Table3Row1Text4);
        textRoom2Fan = root.findViewById(R.id.room2Card1LinearLayout1Table3_1Row1Text1);
        textRoom2Valve = root.findViewById(R.id.room2Card1LinearLayout1Table3_1Row1Text2);

        textOutsideSensorStatus = root.findViewById(R.id.outside1Card1LinearLayout1Table1Row1Text2);
        textOutsideTemperatureMax = root.findViewById(R.id.outside1Card1LinearLayout1Table3Row1Text1);
        textOutsideTemperatureMin = root.findViewById(R.id.outside1Card1LinearLayout1Table2Row1Text2);
        textOutsideHumidityMax = root.findViewById(R.id.outside1Card1LinearLayout1Table3Row1Text3);
        textOutsideHumidityMin = root.findViewById(R.id.outside1Card1LinearLayout1Table3Row1Text4);
        textOutsidePump = root.findViewById(R.id.outside1Card1LinearLayout1Table3_1Row1Text1);

        textLastUpdated1 = root.findViewById(R.id.room1Card1LinearLayout1Table4Row1Text2);
        textLastUpdated2 = root.findViewById(R.id.room2Card1LinearLayout1Table4Row1Text2);
        textLastUpdated3 = root.findViewById(R.id.outside1Card1LinearLayout1Table4Row1Text2);
           // Read from the database Dryer_Room_1 node

        serverDryerRoom1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                room1SensorStatus = dataSnapshot.child("Sensor_Status").getValue().toString();
                if (room1SensorStatus.equals("1")){
                    textRoom1SensorStatus.setTextColor(Color.parseColor("#ffffff"));
                    textRoom1SensorStatus.setText("ONLINE");
                    room1Temp = dataSnapshot.child("Temperature").getValue().toString();
                    textRoom1Temperature.setTextColor(Color.parseColor("#000000"));
                    textRoom1Temperature.setText(room1Temp + "*C");
                    room1Hum = dataSnapshot.child("Humidity").getValue().toString();
                    textRoom1Humidity.setTextColor(Color.parseColor("#000000"));
                    textRoom1Humidity.setText(room1Hum + "%");
                    room1TempMax = dataSnapshot.child("Temperature_Max").getValue().toString();
                    textRoom1TemperatureMax.setText(room1TempMax + "*C");
                    room1TempMin = dataSnapshot.child("Temperature_Min").getValue().toString();
                    textRoom1TemperatureMin.setText(room1TempMin + "*C");
                    room1HumMax = dataSnapshot.child("Humidity_Max").getValue().toString();
                    textRoom1HumidityMax.setText(room1HumMax + "%");
                    room1HumMin = dataSnapshot.child("Humidity_Min").getValue().toString();
                    textRoom1HumidityMin.setText(room1HumMin + "%");
                }
                else {
                    textRoom1SensorStatus.setTextColor(Color.parseColor("#ff2800"));
                    textRoom1SensorStatus.setText("OFFLINE");
                    textRoom1Temperature.setTextColor(Color.parseColor("#ff2800"));
                    textRoom1Temperature.setText("--.-*C");
                    textRoom1Humidity.setTextColor(Color.parseColor("#ff2800"));
                    textRoom1Humidity.setText("--%");
                    textRoom1TemperatureMax.setText("--.-*C");
                    textRoom1TemperatureMin.setText("--.-*C");
                    textRoom1HumidityMax.setText("--%");
                    textRoom1HumidityMin.setText("--%");
                }
                room1Fan = dataSnapshot.child("Fan_Status").getValue().toString();
                if (room1Fan.equals("1"))switchRoom1Fan.setChecked(TRUE);
                else if(room1Fan.equals("0"))switchRoom1Fan.setChecked(FALSE);
                room1Valve = dataSnapshot.child("Valve_Status").getValue().toString();
                if (room1Valve.equals("1"))switchRoom1Valve.setChecked(TRUE);
                else if(room1Valve.equals("0"))switchRoom1Valve.setChecked(FALSE);
                room1FanStatus = dataSnapshot.child("Fan_Feedback").getValue().toString();
                if (room1FanStatus.equals("0")) textRoom1Fan.setText("Turned off");
                else if (room1FanStatus.equals("1")) textRoom1Fan.setText("Turning on...");
                else if (room1FanStatus.equals("2")) textRoom1Fan.setText("Buzzing...");
                else if (room1FanStatus.equals("3")) textRoom1Fan.setText("Turned on");
                else if (room1FanStatus.equals("4")) textRoom1Fan.setText("Power failure!");
                else textRoom1Fan.setText("Error");
                room1ValveStatus = dataSnapshot.child("Valve_Feedback").getValue().toString();
                if (room1ValveStatus.equals("0")) textRoom1Valve.setText("Turned off");
                else if (room1ValveStatus.equals("1")) textRoom1Valve.setText("Turning on...");
                else if (room1ValveStatus.equals("2")) textRoom1Valve.setText("Buzzing...");
                else if (room1ValveStatus.equals("3")) textRoom1Valve.setText("Turned on");
                else if (room1ValveStatus.equals("4")) textRoom1Valve.setText("Power failure!");
                else textRoom1Valve.setText("Error");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
          // Read from the database Dryer_Room_2 node

        serverDryerRoom2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                room2SensorStatus = dataSnapshot.child("Sensor_Status").getValue().toString();
                if (room2SensorStatus.equals("1")) {
                    textRoom2SensorStatus.setTextColor(Color.parseColor("#ffffff"));
                    textRoom2SensorStatus.setText("ONLINE");
                    room2Temp = dataSnapshot.child("Temperature").getValue().toString();
                    textRoom2Temperature.setTextColor(Color.parseColor("#000000"));
                    textRoom2Temperature.setText(room2Temp + "*C");
                    room2Hum = dataSnapshot.child("Humidity").getValue().toString();
                    textRoom2Humidity.setTextColor(Color.parseColor("#000000"));
                    textRoom2Humidity.setText(room2Hum + "%");
                    room2TempMax = dataSnapshot.child("Temperature_Max").getValue().toString();
                    textRoom2TemperatureMax.setText(room2TempMax + "*C");
                    room2TempMin = dataSnapshot.child("Temperature_Min").getValue().toString();
                    textRoom2TemperatureMin.setText(room2TempMin + "*C");
                    room2HumMax = dataSnapshot.child("Humidity_Max").getValue().toString();
                    textRoom2HumidityMax.setText(room2HumMax + "%");
                    room2HumMin = dataSnapshot.child("Humidity_Min").getValue().toString();
                    textRoom2HumidityMin.setText(room2HumMin + "%");
                }
                else {
                    textRoom2SensorStatus.setTextColor(Color.parseColor("#ff2800"));
                    textRoom2SensorStatus.setText("OFFLINE");
                    textRoom2Temperature.setTextColor(Color.parseColor("#ff2800"));
                    textRoom2Temperature.setText("--.-*C");
                    textRoom2Humidity.setTextColor(Color.parseColor("#ff2800"));
                    textRoom2Humidity.setText("--%");
                    textRoom2TemperatureMax.setText("--.-*C");
                    textRoom2TemperatureMin.setText("--.-*C");
                    textRoom2HumidityMax.setText("--%");
                    textRoom2HumidityMin.setText("--%");

                }

                room2Fan = dataSnapshot.child("Fan_Status").getValue().toString();
                if (room2Fan.equals("1"))switchRoom2Fan.setChecked(TRUE);
                else if(room2Fan.equals("0"))switchRoom2Fan.setChecked(FALSE);
                room2Valve = dataSnapshot.child("Valve_Status").getValue().toString();
                if (room2Valve.equals("1"))switchRoom2Valve.setChecked(TRUE);
                else if(room2Valve.equals("0"))switchRoom2Valve.setChecked(FALSE);
                room2FanStatus = dataSnapshot.child("Fan_Feedback").getValue().toString();
                if (room2FanStatus.equals("0")) textRoom2Fan.setText("Turned off");
                else if (room2FanStatus.equals("1")) textRoom2Fan.setText("Turning on...");
                else if (room2FanStatus.equals("2")) textRoom2Fan.setText("Buzzing...");
                else if (room2FanStatus.equals("3")) textRoom2Fan.setText("Turned on");
                else if (room2FanStatus.equals("4")) textRoom2Fan.setText("Power failure!");
                else textRoom2Fan.setText("Error");
                room2ValveStatus = dataSnapshot.child("Valve_Feedback").getValue().toString();
                if (room2ValveStatus.equals("0")) textRoom2Valve.setText("Turned off");
                else if (room2ValveStatus.equals("1")) textRoom2Valve.setText("Turning on...");
                else if (room2ValveStatus.equals("2")) textRoom2Valve.setText("Buzzing...");
                else if (room2ValveStatus.equals("3")) textRoom2Valve.setText("Turned on");
                else if (room2ValveStatus.equals("4")) textRoom2Valve.setText("Power failure!");
                else textRoom2Valve.setText("Error");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
          // Read from the database Outside node

        serverOutside.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                outsideSensorStatus = dataSnapshot.child("Sensor_Status").getValue().toString();
                if (outsideSensorStatus.equals("1")) {
                    textOutsideSensorStatus.setTextColor(Color.parseColor("#ffffff"));
                    textOutsideSensorStatus.setText("ONLINE");
                    outsideTemp = dataSnapshot.child("Temperature").getValue().toString();
                    textOutsideTemperature.setTextColor(Color.parseColor("#000000"));
                    textOutsideTemperature.setText(outsideTemp + "*C");
                    outsideHum = dataSnapshot.child("Humidity").getValue().toString();
                    textOutsideHumidity.setTextColor(Color.parseColor("#000000"));
                    textOutsideHumidity.setText(outsideHum + "%");
                    outsideTempMax = dataSnapshot.child("Temperature_Max").getValue().toString();
                    textOutsideTemperatureMax.setText(outsideTempMax + "*C");
                    outsideTempMin = dataSnapshot.child("Temperature_Min").getValue().toString();
                    textOutsideTemperatureMin.setText(outsideTempMin + "*C");
                    outsideHumMax = dataSnapshot.child("Humidity_Max").getValue().toString();
                    textOutsideHumidityMax.setText(outsideHumMax + "%");
                    outsideHumMin = dataSnapshot.child("Humidity_Min").getValue().toString();
                    textOutsideHumidityMin.setText(outsideHumMin + "%");
                }
                else {
                    textOutsideSensorStatus.setTextColor(Color.parseColor("#ff2800"));
                    textOutsideSensorStatus.setText("OFFLINE");
                    textOutsideTemperature.setTextColor(Color.parseColor("#ff2800"));
                    textOutsideTemperature.setText("--.-*C");
                    textOutsideHumidity.setTextColor(Color.parseColor("#ff2800"));
                    textOutsideHumidity.setText("--%");
                    textOutsideTemperatureMax.setText("--.-*C");
                    textOutsideTemperatureMin.setText("--.-*C");
                    textOutsideHumidityMax.setText("--%");
                    textOutsideHumidityMin.setText("--%");

                }


                outsidePump = dataSnapshot.child("Pump_Status").getValue().toString();
                if (outsidePump.equals("1"))switchOutsidePump.setChecked(TRUE);
                else if(outsidePump.equals("0"))switchOutsidePump.setChecked(FALSE);
                outsidePumpStatus = dataSnapshot.child("Pump_Feedback").getValue().toString();
                if (outsidePumpStatus.equals("0")) textOutsidePump.setText("Turned off");
                else if (outsidePumpStatus.equals("1")) textOutsidePump.setText("Turning on...");
                else if (outsidePumpStatus.equals("2")) textOutsidePump.setText("Buzzing...");
                else if (outsidePumpStatus.equals("3")) textOutsidePump.setText("Turned on");
                else if (outsidePumpStatus.equals("4")) textOutsidePump.setText("Power failure!");
                else textOutsidePump.setText("Error");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

/****************          ROOM 1 SWITCH CONTROL  ***********************/

        switchRoom1Fan.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v) {
                if (!switchRoom1Fan.isChecked()) {
                    if(isOnline()) {
                        serverDryerRoom1.child("Fan_Status").setValue("0")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        switchRoom1Fan.setChecked(TRUE);
                                        Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else {
                        switchRoom1Fan.setChecked(TRUE);
                        offlineDialogBox();
                    }
                }
                else if (switchRoom1Fan.isChecked()){
                    if(isOnline()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to Turn ON");
                        builder.setPositiveButton("Turn ON", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                               serverDryerRoom1.child("Fan_Status").setValue("1")
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               switchRoom1Fan.setChecked(FALSE);
                                               Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                           }
                                       });
                          }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchRoom1Fan.setChecked(FALSE);
                                dialog.dismiss();
                           }
                        });
                         builder.create();
                         builder.show();
                    }
                    else{
                        switchRoom1Fan.setChecked(FALSE);
                        offlineDialogBox();
                    }

                }
            }
        });
        switchRoom1Valve.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v) {
                if (!switchRoom1Valve.isChecked()) {
                    if(isOnline()){
                        serverDryerRoom1.child("Valve_Status").setValue("0")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        switchRoom1Valve.setChecked(TRUE);
                                        Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else {
                        switchRoom1Valve.setChecked(TRUE);
                        offlineDialogBox();
                    }

                }
                else if (switchRoom1Valve.isChecked()){
                    if(isOnline()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to Turn ON");
                        builder.setPositiveButton("Turn ON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                serverDryerRoom1.child("Valve_Status").setValue("1")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                switchRoom1Valve.setChecked(FALSE);
                                                Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchRoom1Valve.setChecked(FALSE);
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                    else{
                        switchRoom1Valve.setChecked(FALSE);
                        offlineDialogBox();
                    }

                }
            }
        });

/****************          ROOM 2 SWITCH CONTROL  ***********************/

        switchRoom2Fan.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v) {
                if (!switchRoom2Fan.isChecked()) {
                    if (isOnline()) {
                        serverDryerRoom2.child("Fan_Status").setValue("0")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        switchRoom2Fan.setChecked(TRUE);
                                        Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else{
                        switchRoom2Fan.setChecked(TRUE);
                        offlineDialogBox();
                    }
                }
                else if (switchRoom2Fan.isChecked()){
                    if (isOnline()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to Turn ON");
                        builder.setPositiveButton("Turn ON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                serverDryerRoom2.child("Fan_Status").setValue("1")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                switchRoom2Fan.setChecked(FALSE);
                                                Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchRoom2Fan.setChecked(FALSE);
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                    else{
                        switchRoom2Fan.setChecked(FALSE);
                        offlineDialogBox();
                    }
                }
            }
        });
        switchRoom2Valve.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v) {
                if (!switchRoom2Valve.isChecked()) {
                    if(isOnline()){
                        serverDryerRoom2.child("Valve_Status").setValue("0")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        switchRoom2Valve.setChecked(TRUE);
                                        Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else{
                        switchRoom2Valve.setChecked(TRUE);
                        offlineDialogBox();
                    }
                }
                else if (switchRoom2Valve.isChecked()){
                    if(isOnline()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to Turn ON");
                        builder.setPositiveButton("Turn ON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                serverDryerRoom2.child("Valve_Status").setValue("1")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                switchRoom2Valve.setChecked(FALSE);
                                                Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchRoom2Valve.setChecked(FALSE);
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                    else{
                        switchRoom2Valve.setChecked(FALSE);
                        offlineDialogBox();
                    }

                }
            }
        });

        /************************   OUTSIDE SWITCH CONTROL   ************************/

        switchOutsidePump.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v) {
                if (!switchOutsidePump.isChecked()) {
                    if (isOnline()){
                        serverOutside.child("Pump_Status").setValue("0")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        switchOutsidePump.setChecked(TRUE);
                                        Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else{
                        switchOutsidePump.setChecked(TRUE);
                        offlineDialogBox();
                    }

                }
                else if (switchOutsidePump.isChecked()){
                    if(isOnline()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to Turn ON");
                        builder.setPositiveButton("Turn ON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                serverOutside.child("Pump_Status").setValue("1")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Command Send Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                switchOutsidePump.setChecked(FALSE);
                                                Toast.makeText(getContext(), "Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchOutsidePump.setChecked(FALSE);
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                    else{
                        switchOutsidePump.setChecked(FALSE);
                        offlineDialogBox();
                    }
                }
            }
        });

        serverOthers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastUpdated = dataSnapshot.child("Last_Updated").getValue().toString();
                textLastUpdated1.setText(lastUpdated);
                textLastUpdated2.setText(lastUpdated);
                textLastUpdated3.setText(lastUpdated);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getActivity(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

      //  final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });return root;

    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return  true;
        }
        else{
            return  false;
        }
    }
    public void offlineDialogBox(){
        if(!isOnline()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setMessage("Internet not available, Check your connection and Try again ...");
            builder.setTitle("No Internet");
            builder.setIcon(R.mipmap.ic_nonetwok_foreground);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
    }

}
