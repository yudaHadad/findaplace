package com.example.yuda.findaplace;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LocationListener
{
    RecyclerView allPlacesRV;
    EditText searchET;
    LocationManager locationManager;
    Location currentLocation;
    LinearLayoutManager myLayoutManager;
    CheckBox nearbyCB;
    Intent intent;
    String search;
    double lat;
    double lon;
    mySQLHelper mySQLHelper;
    myAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);


        searchET = (EditText)view.findViewById(R.id.searchET);
        allPlacesRV = (RecyclerView) view.findViewById(R.id.allPlacesRV);
        myLayoutManager = new LinearLayoutManager(getActivity());
        myLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allPlacesRV.setLayoutManager(myLayoutManager);

        // listener to 'FINISHED' broadcast
        MyfinisedDownload myfinisedDownload = new MyfinisedDownload();
        IntentFilter filter = new IntentFilter("com.example.yuda.findaplace.FINISHED!");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myfinisedDownload, filter);

        // listener to 'Battery charge change' broadcast
        IntentFilter battryFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myfinisedDownload, filter);
        Toast.makeText(getActivity(), "battery canged", Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getActivity().getSystemService (LOCATION_SERVICE);
        //get last known location by gps
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(currentLocation == null)
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {

            }
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        //always check if not null
        if(currentLocation != null) {
            lat = currentLocation.getLatitude();
            lon = currentLocation.getLongitude();
        }

        // create 'mySQLHelper'
        mySQLHelper = new mySQLHelper(getActivity());

        // get table with name 'Places'
        mySQLHelper.getReadableDatabase().query(DBConstants.tableName, null, null, null, null, null, null);

        ((Button)view.findViewById(R.id.goBtn)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckConnection checkConnection = new CheckConnection(getActivity());
                if (checkConnection.isNetworkAvailable()) {

                    search = searchET.getText().toString();
                    intent = new Intent(getActivity(), MyIntentService.class);
                    nearbyCB = (CheckBox) view.findViewById(R.id.nearbyCB);

                    if (nearbyCB.isChecked()) {
                        //always check if not null
                        if (currentLocation != null) {
                            lat = currentLocation.getLatitude();
                            lon = currentLocation.getLongitude();
                        }

                        try {
                            search = URLEncoder.encode(searchET.getText().toString(), "UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "you cant search this word", Toast.LENGTH_SHORT).show();
                        }
                        intent.putExtra("searchWord", search);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lon", lon);
                        intent.setAction("isChecked");
                        getActivity().startService(intent);
                    } else {
                        try {
                            search = URLEncoder.encode(searchET.getText().toString(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra("searchWord", search);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lon);
                        intent.setAction("isNotChecked");
                        getActivity().startService(intent);
                    }
                }else {
//                    mySQLHelper mySqlHelper = new mySQLHelper(getActivity());
//                    // get table with name 'Places'
//                    mySqlHelper.getReadableDatabase().query(DBConstants.Lastknow, null, null, null, null, null, null);
//                    Intent getResulet = new Intent();
//                    ArrayList<Places> allsPlaces = getResulet.getParcelableArrayListExtra("results");
//                    adapter = new myAdapter(allsPlaces,getActivity());
//                    allPlacesRV.setAdapter(adapter);
//                    Toast.makeText(getActivity(), "you have no connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation=location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "provider enabled: "+provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "provider disabled: "+provider, Toast.LENGTH_SHORT).show();
    }


    class MyfinisedDownload extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ArrayList<Places> allsPlaces = intent.getParcelableArrayListExtra("results");
            myAdapter adapter = new myAdapter(allsPlaces,getActivity());
            allPlacesRV.setAdapter(adapter);
            Toast.makeText(context, "finish download", Toast.LENGTH_SHORT).show();
        }
    }
}
