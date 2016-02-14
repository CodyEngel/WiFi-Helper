package io.refact.wifihelper.model;

import android.net.wifi.ScanResult;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

/**
 * Created by cody on 11/27/15.
 */
@Table(name = "WiFi")
public class WiFi extends Model {

    private int mFrequency,
            mMaxScanLevels;

    private ArrayList<Integer> mLevels;

    @Column(name = "MAC_Address")
    private String mMacAddress;

    @Column(name = "SSID")
    private String mSSID;

    public WiFi() {
        super();
        mLevels = new ArrayList<>();
    }

    public WiFi(String macAddress, String ssid, int frequency, int maxScanLevels) {
        super();
        mMacAddress = macAddress;
        mSSID = ssid;
        mFrequency = frequency;
        mLevels = new ArrayList<>();
        mMaxScanLevels = maxScanLevels;
    }

    public void addScanLevel(Integer level) {
        mLevels.add(level);
        if(mLevels.size() > mMaxScanLevels) {
            mLevels.remove(0);
        }
    }

    /** Getters/Setters **/
    public int getFrequency() {
        return mFrequency;
    }

    public void setFrequency(int frequency) {
        mFrequency = frequency;
    }

    public ArrayList<Integer> getScanLevels() {
        return mLevels;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String ssid) {
        mSSID = ssid;
    }
}
