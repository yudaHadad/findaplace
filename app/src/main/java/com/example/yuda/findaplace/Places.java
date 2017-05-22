package com.example.yuda.findaplace;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yuda on 26/04/2017.
 */

public class Places  implements Parcelable
{
    String name;
    String vicinity;
    ArrayList<photo> photos;
    Geometry geometry;
    String formatted_address;

    protected Places(Parcel in) {
        name = in.readString();
        vicinity = in.readString();
        formatted_address = in.readString();
    }

    public static final Creator<Places> CREATOR = new Creator<Places>() {
        @Override
        public Places createFromParcel(Parcel in) {
            return new Places(in);
        }

        @Override
        public Places[] newArray(int size) {
            return new Places[size];
        }
    };

    @Override
    public String toString() {return name + vicinity;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(vicinity);
        dest.writeString(formatted_address);
    }
}
