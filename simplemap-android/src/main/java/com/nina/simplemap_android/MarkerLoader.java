package com.nina.simplemap_android;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ninamanalo on 14/03/2017.
 */

public class MarkerLoader extends AsyncTask<Void, Void, Void> {

    private static final String TAG = MarkerLoader.class.getSimpleName();

    private ReadableArray markers;
    private GoogleMap googleMap;
    private List<Bitmap> bitmaps;
    private ReactContext context;

    public MarkerLoader(ReactContext context, GoogleMap googleMap, ReadableArray markers) {
        this.markers = markers;
        this.googleMap = googleMap;
        this.bitmaps = new ArrayList<>();
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... params) {
        Looper.prepare();
        try {
            for(int i = 0; i < markers.size(); i++) {
                ReadableMap readableMap = markers.getMap(i);
                bitmaps.add(Glide.
                        with(context).
                        load(readableMap.getString("icon")).
                        asBitmap().
                        into(-1,-1).
                        get());
            }
        } catch (final ExecutionException e) {
            Log.e(TAG, "addMarkers() -- error=" + e.getMessage());
        } catch (final InterruptedException e) {
            Log.e(TAG, "addMarkers() -- error=" + e.getMessage());
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void dummy) {
        for(int i = 0; i < bitmaps.size(); i++) {
            ReadableMap readableMap = markers.getMap(i);
            Log.d(TAG, "addMarkers() -- readableMap=" + readableMap.toString());
            String id = readableMap.getString("id");
            double lat = readableMap.getDouble("lat");
            double lng = readableMap.getDouble("lng");
            LatLng location = new LatLng(lat, lng);
            Log.d(TAG, "addMarkers() -- sample=" + id);
            MarkerOptions options = new MarkerOptions();
            options.title(id);
            options.position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmaps.get(i)));
            googleMap.addMarker(options);
        }
    }
}
