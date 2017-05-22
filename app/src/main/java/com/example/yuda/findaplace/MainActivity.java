package com.example.yuda.findaplace;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements ChangeFragment{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        getFragmentManager().beginTransaction().add(R.id.MyContainerLL, homeFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // make the option menu inflate
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if 'delete all' item was clicked delete any details Data Base
        if (item.getItemId() == R.id.Favorites) {
            // Show message - if you want to delete this row
            Intent i = new Intent(this, FavoritesActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.closeAppItem) {
            finish();
        }
        else if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void change(final Places places) {
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        transaction.addToBackStack("change map");
        transaction.replace(R.id.MyContainerLL , mapFragment).commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                LatLng latLng = new LatLng(places.geometry.location.lng, places.geometry.location.lng);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                googleMap.moveCamera(update);
            }
        });
    }

}
