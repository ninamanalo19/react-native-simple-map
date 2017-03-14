package com.nina.simplemap_android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by ninamanalo on 14/03/2017.
 */

public class SimpleMapView extends SimpleViewManager<MapView> implements OnMapReadyCallback {
    private static final String TAG = SimpleMapView.class.getSimpleName();

    private MapView mapView;
    private LocationManager locationManager;
    private boolean showUserLocation = false;
    private GoogleMap googleMap;
    private ReactContext reactContext;
    private ReadableArray markers;

    @Override
    public String getName() {
        return "SimpleMapView";
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {
        this.reactContext = reactContext;
        mapView = new MapView(reactContext);
        mapView.onCreate(null);
        mapView.onResume();

        if (ContextCompat.checkSelfPermission(reactContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) reactContext.getSystemService(Context.LOCATION_SERVICE);
        }

        return mapView;
    }

    @ReactProp(name = "showUserLocation")
    public void setShowUserLocation(MapView map, boolean showUserLocation) {
        this.showUserLocation = showUserLocation;
        map.getMapAsync(this);
    }

    @ReactProp(name = "markers")
    public void setMarkers(MapView map, ReadableArray markers) {
        Log.d(TAG, "setMarkers() -- size=" + markers.size());
        for (int i = 0; i < markers.size(); i++) {
            ReadableMap readableMap = markers.getMap(i);
            Log.d(TAG, "setMarkers() -- readableMap=" + readableMap.toString());
            if (!hasReadableMapMarkerProperKeys(readableMap)) {
                Log.d(TAG, "setMarkers() -- no proper keys=" + readableMap.toString());
                return;
            } else {
                Log.d(TAG, "setMarkers() -- has proper keys=" + readableMap.toString());
            }
        }
        this.markers = markers;
    }

    private boolean hasReadableMapMarkerProperKeys(ReadableMap readableMap) {
        return readableMap.hasKey("id") && readableMap.hasKey("icon")
                && readableMap.hasKey("lat") && readableMap.hasKey("lng");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady() -- ready");
        this.googleMap = googleMap;
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(reactContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(reactContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(this.showUserLocation);
            addMarkers();
        }
    }

    private void addMarkers() {
        if (markers != null) {
            if (googleMap != null) {
                new MarkerLoader(reactContext, googleMap, markers).execute();
            } else {
                Log.d(TAG, "addMarkers() -- google map is null ..");
            }
        }
    }
}
