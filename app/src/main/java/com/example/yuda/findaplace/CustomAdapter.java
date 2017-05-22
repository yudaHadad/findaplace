package com.example.yuda.findaplace;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by yuda on 08/05/2017.
 */

public class CustomAdapter extends CursorAdapter {

    public CustomAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // inflat the one item in view
        View v = LayoutInflater.from(context).inflate(R.layout.one_item, null);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // make conntect between adapter and the data
        String theNameOfThePlace = cursor.getString(cursor.getColumnIndex(DBConstants.NameColumn));
        TextView itemNameTV = (TextView) view.findViewById(R.id.nameTV);
        itemNameTV.setText(theNameOfThePlace);

        String theAddressOfThePlace = cursor.getString(cursor.getColumnIndex(DBConstants.AddressColumn));
        TextView addressTV = (TextView) view.findViewById(R.id.addressTV);
        addressTV.setText(theAddressOfThePlace);
    }
}
