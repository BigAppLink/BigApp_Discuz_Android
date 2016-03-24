package com.kit.utils;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GetLongitudeAndLatitudeGoogle extends Activity {

	private LocationListener mLocationListener;
	private LocationManager mLocationManager;
	private String mProviderName;
	private ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		Boolean isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			final Intent i = new Intent();
			mDialog = ProgressDialog.show(this, null, "loading", true, true);

			// 声明LocationManager对象
			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			mLocationListener = new LocationListener() {
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					Toast.makeText(GetLongitudeAndLatitudeGoogle.this,
							"Status Changed!", Toast.LENGTH_SHORT).show();
				}

				public void onProviderEnabled(String provider) {
					Toast.makeText(GetLongitudeAndLatitudeGoogle.this,
							"start GPS or Wireless", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onProviderDisabled(String provider) {
					Toast.makeText(GetLongitudeAndLatitudeGoogle.this,
							"closed GPS or Wireless", Toast.LENGTH_SHORT)
							.show();
				}

				// 当位置变化时触发
				@Override
				public void onLocationChanged(Location location) {
					// 使用新的location更新TextView显示
					updateWithNewLocation(i, location);
				}
			};
		} else {
			Toast.makeText(GetLongitudeAndLatitudeGoogle.this,
					"check your network", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	private void updateWithNewLocation(Intent i, Location location) {
		String latLongString;
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			// lat = Tools.pixelToLng(Tools.lngToPixel(lat,15),15);
			// lng = Tools.pixelToLat(Tools.latToPixel(lng,15),15);
			System.out.println(lat + "::::" + lng);
			i.putExtra("yes", new double[] { lat, lng });
		} else {
			latLongString = "Positioning failure!";
			i.putExtra("no", latLongString);
		}
		setResult(20, i);
		mDialog.dismiss();
		mDialog.cancel();
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 判断Use GPS satellites.是否勾选
		boolean isGpsEnabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 判断Use wireless networks 是否勾选
		boolean isWIFIEnabled = mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!isGpsEnabled && !isWIFIEnabled) {
			Toast.makeText(GetLongitudeAndLatitudeGoogle.this,
					"Neither GPS nor Wireless is opened.", Toast.LENGTH_SHORT)
					.show();
		} else {
			Location lastKnownLocation = null;
			// 如果只是Use GPS satellites勾选，即指允许使用GPS定位
			if (isGpsEnabled && !isWIFIEnabled) {
				lastKnownLocation = mLocationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				mProviderName = LocationManager.GPS_PROVIDER;
			} else if (!isGpsEnabled && isWIFIEnabled) {
				// 如果只是Use wireless networks勾选，即只允许使用网络定位。
				lastKnownLocation = mLocationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				mProviderName = LocationManager.NETWORK_PROVIDER;
			} else if (isGpsEnabled && isWIFIEnabled) {
				// 如果二者都勾选，优先使用GPS,因为GPS定位更精确。
				lastKnownLocation = mLocationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				mProviderName = LocationManager.GPS_PROVIDER;
			}
			if (lastKnownLocation == null) {
				lastKnownLocation = mLocationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				mProviderName = LocationManager.NETWORK_PROVIDER;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if (!mProviderName.equals(null)) {
				// 当GPS定位时，在这里注册requestLocationUpdates监听就非常重要而且必要。没有这句话，定位不能成功。
				try {
					mLocationManager.requestLocationUpdates(mProviderName,
							1000, 1, mLocationListener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 取消注册监听
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(mLocationListener);
		}
	}

}