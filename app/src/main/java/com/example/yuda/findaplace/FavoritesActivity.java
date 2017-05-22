package com.example.yuda.findaplace;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

public class FavoritesActivity extends AppCompatActivity {

    CustomAdapter adapter;
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        mySQLHelper mySqlHelper = new mySQLHelper(this);
        // get table with name 'Places'
        Cursor cursor = mySqlHelper.getReadableDatabase().query(DBConstants.tableName, null, null, null, null, null, null);

        adapter = new CustomAdapter(this, cursor);
        myListView = (ListView) findViewById(R.id.myLv);
        myListView.setAdapter(adapter);
    }
}
