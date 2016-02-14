package io.refact.wifihelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by cody on 12/6/15.
 */
public class WiFiActivity extends AppCompatActivity {

    private static final String TAG = "WiFiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_wifi);
    }
}
