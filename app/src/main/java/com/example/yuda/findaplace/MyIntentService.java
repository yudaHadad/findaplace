package com.example.yuda.findaplace;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyIntentService extends IntentService
{
    String JsonResponse;
    String url;
    double lat;
    double lon;
    String search;
    int id;
    String photo_reference;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction()=="isChecked")
        {
            lat = intent.getDoubleExtra("lat",0);
            lon =  intent.getDoubleExtra("lon",0);
            search = intent.getStringExtra("searchWord");
            url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius=50000&keyword="+search+"&key=AIzaSyBW_2RtNq6Zhay-BjURFiqLsrWWOYfxumQ";
        }
        else
        {
            search = intent.getStringExtra("searchWord");
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+search+"&key=AIzaSyBW_2RtNq6Zhay-BjURFiqLsrWWOYfxumQ";
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        JsonResponse = "";

        try
        {
            client.newCall(request).execute();
            Response response = client.newCall(request).execute();
            JsonResponse= response.body().string();
        }
        catch (IOException interntEX)
        {
            interntEX.printStackTrace();
        }
        Gson gson = new Gson();
        JsonResponse jsonResponse = gson.fromJson(JsonResponse, JsonResponse.class);


        Intent finishedDownload = new Intent("com.example.yuda.findaplace.FINISHED!");

        ArrayList <Places> allPlaces = jsonResponse.results;
        finishedDownload.putParcelableArrayListExtra("results", allPlaces);

        for (int i =0 ; i <allPlaces.size(); i++)
        {
            String name = allPlaces.get(i).name;
            String formatted_address = allPlaces.get(i).formatted_address;
            String vicinity = allPlaces.get(i).vicinity;
            double lat = allPlaces.get(i).geometry.location.lat;
            double lng = allPlaces.get(i).geometry.location.lng;

            if(allPlaces.get(i).photos!=null   && allPlaces.get(i).photos.get(0) != null) {
                if (allPlaces.get(i).photos.get(0).photo_reference != null) {
                    photo_reference = allPlaces.get(i).photos.get(0).photo_reference;
                }
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.NameColumn, name);
            contentValues.put(DBConstants.imageColumn, photo_reference);
            contentValues.put(DBConstants.LatColumn, lat);
            contentValues.put(DBConstants.LngColumn, lng);
            if (vicinity == null ) {
                contentValues.put(DBConstants.AddressColumn, formatted_address);
            }
            else
            {
                contentValues.put(DBConstants.AddressColumn, vicinity);
            }
            mySQLHelper mySQLHelper = new mySQLHelper(this);
            mySQLHelper.getWritableDatabase().insert(DBConstants.Lastknow, null, contentValues);

            // if this row is new - insert this row to db
            if (id == -1) {
                mySQLHelper.getWritableDatabase().insert(DBConstants.tableName, null, contentValues);
            }
            // if this row is not new - update this row
            else {
                mySQLHelper.getWritableDatabase().update(DBConstants.tableName, contentValues, "_id=?", new String[]{"" + id});
            }
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(finishedDownload);
    }
}