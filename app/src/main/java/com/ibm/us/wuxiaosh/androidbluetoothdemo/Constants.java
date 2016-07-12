package com.ibm.us.wuxiaosh.androidbluetoothdemo;

/**
 * Created by wuxiaosh on 7/11/2016.
 */
public final class Constants {
    private Constants() {
    }
    public static final String SHARED_PREFERENCES_PREFIX = Constants.class.getPackage().getName();
    public static final String SHARED_PREFERENCES_NAME = Constants.SHARED_PREFERENCES_PREFIX + "_SHARED_PREFERENCES";

    public static final String SHARED_PREFERENCES_BLUETOOTH_DEVICES_KEY = Constants.SHARED_PREFERENCES_PREFIX + "_BLUETOOTH_DEVICES";
    public static final String SHARED_PREFERENCES_BLUETOOTH_ADDRESS_KEY = Constants.SHARED_PREFERENCES_PREFIX + "_BLUETOOTH_ADDRESS";
    public static final String SHARED_PREFERENCES_BLUETOOTH_SELECTION_KEY = Constants.SHARED_PREFERENCES_PREFIX + "_BLUETOOTH_SELECTION";
    public static final String SHARED_PREFERENCES_BLUETOOTH_SELECTION_ADDRESS_KEY = Constants.SHARED_PREFERENCES_PREFIX + "_BLUETOOTH_SELECTION_ADDRESS";
}
