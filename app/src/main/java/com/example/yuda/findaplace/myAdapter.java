package com.example.yuda.findaplace;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by yuda on 26/04/2017.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyPlacesViewHolder> {

    ArrayList <Places> allPlaces;
    Context context;

    public myAdapter(ArrayList<Places> allPlaces, Context context) {
        this.allPlaces = allPlaces;
        this.context = context;
    }

    @Override
    public myAdapter.MyPlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.one_item, null);
        MyPlacesViewHolder myplacesviewholder = new MyPlacesViewHolder(view);
        return myplacesviewholder;
    }

    @Override
    public void onBindViewHolder(myAdapter.MyPlacesViewHolder myPlacesViewHolder, int position)
    {
        // make contect between 'places' object and 'allplaces' ArreyList
        Places p = allPlaces.get(position);
        myPlacesViewHolder.onBindData(p);
    }

    @Override
    public int getItemCount()
    {
        try{
            return allPlaces.size();
        }catch (Exception ee){}

            return  0;

    }

    public class MyPlacesViewHolder extends RecyclerView.ViewHolder
    {
        ImageView placeIV;
        TextView nameTV;
        TextView adressTV;
        TextView distance;
        Places myPlace;

        public MyPlacesViewHolder(View itemView)
        {
            super(itemView);
            placeIV = (ImageView) itemView.findViewById(R.id.placeIV);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            adressTV = (TextView) itemView.findViewById(R.id.addressTV);
            distance = (TextView) itemView.findViewById(R.id.distanceTV);
        }

        public double haversine(double lat1, double lng1, double lat2, double lng2) {
            int r = 6371; // average radius of the earth in km
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double d = r * c;
            return d;
        }

        public void onBindData (final Places places)
        {
            if (places.vicinity==null)
            {
                adressTV.setText(places.formatted_address);
            }
            else
            {
                adressTV.setText(places.vicinity);
            }
            nameTV.setText(places.name);

            HomeFragment homeFragment = new HomeFragment();
            double seclng;
            double seclat;
            if (homeFragment.currentLocation!=null)
            {
                seclat = homeFragment.currentLocation.getLongitude();
                seclng = homeFragment.currentLocation.getLongitude();
            }
            else
            {
                seclat= 0;
                seclng = 0;
            }


            double dis = haversine(places.geometry.location.lat, places.geometry.location.lng, seclat, seclng);
            String dist = String.valueOf(dis);
            distance.setText(dist);
            if (places.photos!=null &&  places.photos.get(0).photo_reference!=null)
            {
                //download the image
                String imageLink = places.photos.get(0).photo_reference;
                Picasso.with(context)
                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference=" + imageLink + "&key=+AIzaSyBV-UGwkawBtAd5GrEUHC5kNVIzuEfZLpY")
                        .into(placeIV);
            }

            // change the fragment to map fragment
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ChangeFragment changeFragment = (ChangeFragment) context;
                        changeFragment.change(places);
                    }catch (ClassCastException c){
                        c.printStackTrace();
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(context, v);

                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            // if favorities item was cliecked
                            if (item.getItemId() == R.id.favorities) {
                                // add to favorities
                                myPlace = (Places) allPlaces.get(getAdapterPosition());

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DBConstants.NameColumn, myPlace.name);
                                if (places.vicinity == null) {
                                    contentValues.put(DBConstants.AddressColumn, myPlace.formatted_address);
                                } else if (places.formatted_address == null) {
                                    contentValues.put(DBConstants.AddressColumn, myPlace.vicinity);
                                }
                                contentValues.put(DBConstants.LatColumn, myPlace.geometry.location.lat);
                                contentValues.put(DBConstants.LngColumn, myPlace.geometry.location.lng);
                                if (myPlace.photos != null) {
                                    contentValues.put(DBConstants.imageColumn, myPlace.photos.get(0).photo_reference);
                                }

                                mySQLHelper mySqlHelper = new mySQLHelper(context);
                                mySqlHelper.getWritableDatabase().insert(DBConstants.tableName, null, contentValues);
                                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                            } else {
                                // share tis item
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Just find out " + places.name + " you should check it");
                                sendIntent.setType("text/plain");
                                itemView.getContext().startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.app_name)));
                            }
                            return true;
                        }
                    });
                    popup.show(); //showing popup menu
                    return true;
                }
            });
        }


    }
}