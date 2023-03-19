package com.example.hunterqrhunter.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hunterqrhunter.page.AddQRScreen;
import com.example.hunterqrhunter.page.QRCameraScreen;

public class LocationUtils {
    // Constants
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;

    // Fields
    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private boolean mIsLocationPermissionGranted = false;

    // Constructor
    public LocationUtils(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Do nothing
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Do nothing
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Do nothing
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Do nothing
            }
        };
    }

    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            mIsLocationPermissionGranted = true;
            Log.d("Location", "Location permission granted");
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mIsLocationPermissionGranted = true;
                Log.d("Location", "Location permission granted");
            } else {
                mIsLocationPermissionGranted = false;
                Log.d("Location", "Location permission denied");
            }
        }
    }

    public boolean isLocationPermissionGranted() {
        return mIsLocationPermissionGranted;
    }

    public Location getLocation() {
        if (mIsLocationPermissionGranted) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    return location;
                }
            }
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
                Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    return location;
                }
            }
            Log.d("Location", "Could not get location");
        } else {
            Log.d("Location", "Location permission not granted");
        }
        return null;
    }

    public double getLongitude() {
        Location location = getLocation();
        if (location != null) {
            return location.getLongitude();
        } else {
            return 0.0;
        }
    }

    public double getLatitude() {
        Location location = getLocation();
        if (location != null) {
            return location.getLatitude();
        } else {
            return 0.0;
        }
    }
}
