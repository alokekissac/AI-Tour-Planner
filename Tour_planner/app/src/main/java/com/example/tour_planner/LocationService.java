package com.example.tour_planner;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;


/**
 * Keeps the latest device position in {@link #lati} / {@link #logi}.
 *
 * Safe to start at any time: if location permission has not been granted yet
 * (Android 6+ runtime permissions) it simply keeps the last known values and
 * retries every 5 seconds instead of crashing with a SecurityException.
 */
public class LocationService extends Service implements JsonResponse {

	private static final String TAG = "LocationService";
	private static final long INTERVAL_MS = 5000;

	private LocationManager locationManager;
	private final Handler handler = new Handler(Looper.getMainLooper());
	private boolean listening = false;

	public static Location curLocation;
	public static boolean isService = false;
	public static String place = "", address = "", lati = "", logi = "";

	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				updateFrom(location);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	public static boolean hasPermission(Context ctx) {
		return ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		isService = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handler.removeCallbacks(gpsFinder);
		handler.post(gpsFinder);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		handler.removeCallbacks(gpsFinder);
		if (locationManager != null && listening) {
			try {
				locationManager.removeUpdates(locationListener);
			} catch (SecurityException ignored) {
			}
		}
		listening = false;
		isService = false;
		super.onDestroy();
	}

	private final Runnable gpsFinder = new Runnable() {
		@Override
		public void run() {
			try {
				Location loc = getBestLocation();
				if (loc != null) {
					updateFrom(loc);
				}
			} catch (Exception e) {
				Log.w(TAG, "location poll failed", e);
			}
			handler.postDelayed(this, INTERVAL_MS);
		}
	};

	private void updateFrom(Location location) {
		curLocation = location;
		lati = String.valueOf(location.getLatitude());
		logi = String.valueOf(location.getLongitude());
		resolvePlace(location);
	}

	private void resolvePlace(final Location location) {
		if (!Geocoder.isPresent()) return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
					List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
					if (list != null && !list.isEmpty()) {
						Address a = list.get(0);
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i <= a.getMaxAddressLineIndex(); i++) {
							if (a.getAddressLine(i) != null) sb.append(a.getAddressLine(i)).append(' ');
						}
						address = sb.toString().trim();
						if (a.getFeatureName() != null) place = a.getFeatureName();
					}
				} catch (Exception e) {
					Log.w(TAG, "geocoder failed", e);
				}
			}
		}).start();
	}

	private Location getBestLocation() {
		if (locationManager == null || !hasPermission(this)) {
			return null;
		}
		Location gps = null, network = null;
		try {
			boolean gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean netOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!listening) {
				if (gpsOn) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL_MS, 0, locationListener, Looper.getMainLooper());
					listening = true;
				}
				if (netOn) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL_MS, 0, locationListener, Looper.getMainLooper());
					listening = true;
				}
			}
			if (gpsOn) gps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (netOn) network = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} catch (SecurityException | IllegalArgumentException e) {
			Log.w(TAG, "location unavailable", e);
			return null;
		}
		if (gps == null) return network;
		if (network == null) return gps;
		return gps.getTime() >= network.getTime() ? gps : network;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void response(JSONObject jo) {
		// Location updates are sent with the login request; nothing to handle here.
	}
}
