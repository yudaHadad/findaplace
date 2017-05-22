package com.example.yuda.findaplace;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by yuda on 26/04/2017.
 */

public class JsonResponse implements Parcelable
{
    ArrayList<Places> results;

    protected JsonResponse(Parcel in) {
    }

    public static final Creator<JsonResponse> CREATOR = new Creator<JsonResponse>() {
        @Override
        public JsonResponse createFromParcel(Parcel in) {
            return new JsonResponse(in);
        }

        @Override
        public JsonResponse[] newArray(int size) {
            return new JsonResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
