package io.refact.wifihelper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.refact.wifihelper.adapter.WiFiRecyclerAdapter;
import io.refact.wifihelper.model.WiFi;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int REQUEST_LOCATION = 1503,
                        MAX_SCAN_LEVELS = 10;

    private HashMap<String, WiFi> mWiFiMap;

    private RecyclerView rvWiFi;
    private WiFiRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        mWiFiMap = new HashMap<>();

        rvWiFi = (RecyclerView) findViewById(R.id.rvWiFi);

        mLayoutManager = new LinearLayoutManager(this);
        rvWiFi.setLayoutManager(mLayoutManager);

        mAdapter = new WiFiRecyclerAdapter(new ArrayList<>(mWiFiMap.values()));
        rvWiFi.setAdapter(mAdapter);

        requestLocationPermission();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
        mAdapter = new WiFiRecyclerAdapter(new ArrayList<>(mWiFiMap.values()));
        rvWiFi.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // handle permission granted
                    retrieveScanResults();
                } else {
                    // handle permission denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestLocationPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            String locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
            int hasPermission = checkSelfPermission(locationPermission);
            String[] permissions = new String[]{locationPermission};
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "We require location access in order to scan for wireless access points. Please allow this permission request.", Toast.LENGTH_LONG).show();
                }
                requestPermissions(permissions, REQUEST_LOCATION);
            } else {
                retrieveScanResults();
            }
        }
    }

    private void retrieveScanResults() {
        final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() { // http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Timer.html#scheduleAtFixedRate%28java.util.TimerTask,%20long,%20long%29
            @Override
            public void run() {
                handleScanResults(wifiManager.getScanResults());
            }
        }, 0, 1000);
    }

    private void outputWiFiMap() {
        Log.i(TAG, "=== Output of WiFi Map ===");
        Log.i(TAG, "WiFiMap Size: " + mWiFiMap.size());
        for(WiFi wifi : mWiFiMap.values()) {
            Log.i(TAG, "SSID: " + wifi.getSSID() + " MAC Address: " + wifi.getMacAddress() + " Scan Levels: " + wifi.getScanLevels().size());
        }
        Log.i(TAG, "=== End Output of WiFi Map ===");
    }

    private void handleScanResults(List<ScanResult> scanResults) {
        for (ScanResult scanResult : scanResults) {
            handleScanResult(scanResult);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.update(new ArrayList<>(mWiFiMap.values()));
            }
        });
    }

    private void handleScanResult(ScanResult scanResult) {
        if(mWiFiMap.containsKey(scanResult.BSSID)) { // add this result to that
            mWiFiMap.get(scanResult.BSSID).addScanLevel(scanResult.level);
        } else { // create new wifi object, adding this scan result
            WiFi wifi = new WiFi(scanResult.BSSID, scanResult.SSID, scanResult.frequency, MAX_SCAN_LEVELS);
            wifi.addScanLevel(scanResult.level);
            mWiFiMap.put(wifi.getMacAddress(), wifi);
        }
    }
}
