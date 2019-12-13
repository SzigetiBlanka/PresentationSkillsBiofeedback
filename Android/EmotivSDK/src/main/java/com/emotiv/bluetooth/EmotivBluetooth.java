package com.emotiv.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"JniMissingFunction", "MissingPermission"})
public class EmotivBluetooth {
    /* UUIDs for Insight */
    private final UUID DEVICE_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    // --Commented out by Inspection (5/16/16, 4:41 PM):private final UUID DEVICE_BATERY_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    private final UUID Serial_Characteristic_UUID = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
    private final UUID Firmware_Characteristic_UUID = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    private final UUID Setting_Characteristic_UUID = UUID.fromString("81072F44-9F3D-11E3-A9DC-0002A5D5C51B");
    // --Commented out by Inspection (5/16/16, 4:41 PM):private final UUID Manufac_Characteristic_UUID = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");

    private final UUID DATA_SERVICE_UUID = UUID.fromString("81072F40-9F3D-11E3-A9DC-0002A5D5C51B");
    private final UUID EEG_Characteristic_UUID = UUID.fromString("81072F41-9F3D-11E3-A9DC-0002A5D5C51B");
    private final UUID MEMS_Characteristic_UUID = UUID.fromString("81072F42-9F3D-11E3-A9DC-0002A5D5C51B");
    private final UUID Config_Characteristic_UUID = UUID.fromString("81072F43-9F3D-11E3-A9DC-0002A5D5C51B");
    private final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    // --Commented out by Inspection (5/16/16, 4:41 PM):private static final long SCAN_PERIOD = 1000;

    private int STATUS_CONNECTED = 1;
    private final int STATUS_NOTCONNECT = 0;

    private final Context mContext;
    public static EmotivBluetooth _emobluetooth = null;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private ScanCallback scan;
    private BluetoothLeScanner mLEScanner;
    private final Handler mHandler;
    private final Handler tHandler;
    private Handler handler;
    private int _TypeHeadset; // 0 for Insight ; 1 for EPOC

    private BluetoothGatt bluetoothGatt = null;
    private List<BluetoothDevice> list_device_epoc = new ArrayList<>();
    private final List<BluetoothDevice> list_device_insight = new ArrayList<>();
    private final HashMap<BluetoothDevice, Integer> status_device_insight = new HashMap<>();
    private final HashMap<BluetoothDevice, Integer> status_device_epoc = new HashMap<>();
    private final HashMap<BluetoothDevice, Integer> signal_insight = new HashMap<>();
    private final HashMap<BluetoothDevice, Integer> signal_epoc = new HashMap<>();

    private final UUID[] device_uuid = {
            DEVICE_SERVICE_UUID
    };
    private boolean lock = false;
    private boolean haveData = false;
    private int start_counter;

    private int valueMode;
    private double testnortifi = -1;
    private double CheckNortifiBLE = 0;
    private boolean checkUser = false;
    public boolean isSettingMode;
    private boolean isConnected = false;
    private boolean isNortifyMotion = false;
    // --Commented out by Inspection (5/16/16, 4:42 PM):public boolean isEnableScan = true;

    private final byte[] EEGBuffer = new byte[32];
    private final byte[] EEGBuffer_new_Insight = new byte[20];

    private final byte[] MEMSBuffer = new byte[20];

    //    boolean isEnableBLE = false;
//    boolean isScanDevice = false;
    public EmotivBluetooth(Context context) {
        isConnected = false;
        mContext = context;
        mHandler = new Handler();
        tHandler = new Handler();
        handler = new Handler();
        Toast toast = Toast.makeText(mContext, "Bluetooth not enabled", Toast.LENGTH_SHORT);
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(mContext, "Bluetooth LE not supported on this device",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        bluetoothManager =
                (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            return;
        } else {
            //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            //mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            handler.postDelayed(thread_check_adapter, 500);
        }
        if (!mBluetoothAdapter.isEnabled()) {
            toast.show();
        }
//        Thread checkBLEThread = new Thread() {@Override
//            public void run() {
//                // TODO Auto-generated method stub
//                super.run();
//                while (!isEnableBLE) {
//                    try {
//                        if (mBluetoothAdapter.isEnabled()) {
//                            handler.sendEmptyMessage(1);
//                            isEnableBLE = true;
//                            break;
//                        }
//                        Thread.sleep(1000);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        };
//        checkBLEThread.start();

//        Thread checkScanDevice = new Thread() {@Override
//            public void run() {
//                super.run();
//                while (true) {
//                    if (isScanDevice) {
//                        try {
//                            handler.sendEmptyMessage(2);
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            // TODO: handle exception
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        };
        //       checkScanDevice.start();
    }

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    scanLeDevice(true);
//                    break;
//                case 2:
//                    {
//
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    private final Runnable thread_check_adapter = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                if (!mBluetoothAdapter.isEnabled()) {
                    handler.postDelayed(this, 500);
                    //Log.e("edkJavaJNI bluetooth","check enable BLE");
                } else {
                    scanLeDevice(true);
                    handler.removeCallbacks(this);
                }
            } catch (NullPointerException e) {
                // TODO: handle exception
            }
        }
    };

    private final Runnable thread_retrive = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {

//                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                } else {
//                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(scan);
//                }
//                if (mBluetoothAdapter.isEnabled()) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        mBluetoothAdapter.startLeScan(device_uuid, mLeScanCallback);
//                    } else {
//                        mBluetoothAdapter.getBluetoothLeScanner().startScan(scan);
//                    }
//                }
                RetrieveConnect();
                mHandler.postDelayed(this, 500);
            } catch (NullPointerException e) {
                // TODO: handle exception
            }
        }
    };

    /* Bug on Android 4.4.3:
       Get disconnected event 10s after headset is turned off.
       So we must check nortifi manually and call function disconnect().
     */
    private final Runnable thread_checknortifi = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (haveData) {
                if (testnortifi != CheckNortifiBLE) {
                    testnortifi = CheckNortifiBLE;
                } else {
                    if (bluetoothGatt != null) {
                        //  Log.e("_____BLE____","Call disconnect");
                        Thread thread = new Thread() {
                            public void run() {
                                try {
                                    bluetoothGatt.disconnect();
                                    haveData = false;
                                } catch (NullPointerException e) {
                                    // TODO: handle exception
                                }
                            }

                        };
                        thread.start();
                    }
                }
                tHandler.postDelayed(this, 1000);
            } else {
                if (bluetoothGatt != null) {
                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                bluetoothGatt.disconnect();
                            } catch (NullPointerException e) {
                                // TODO: handle exception
                            }
                        }

                    };
                    thread.start();
                }
            }
        }
    };

    /* Get number of devices with EPOC+ type */
    public int GetNumberDeviceEpocPlus() {
        return list_device_epoc.size();
    }

    /* Get number of devices with Insight type */
    public int GetNumberDeviceInsight() {
        return list_device_insight.size();
    }

    /* Get device name of EPOC+ headset */
    public String GetNameDeviceEpocPlus(int index) {
        String name = "";
        try {
            if (index < list_device_epoc.size()) {
                name = list_device_epoc.get(index).getName();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return name;
    }

    /* Get device name of Insight headset */
    public String GetNameDeviceInsight(int index) {
        String name = "";
        try {
            if (index < list_device_insight.size()) {
                name = list_device_insight.get(index).getName();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return name;
    }

    /* Feature Setting Mode when use EPOC+
     *   value = 0 -> epoc 14bit
     *   value = 1 -> epoc 16bit No motion
     *   value = 2 -> epoc 16bit 32hz motion
     *   value = 3 -> epoc 16bit 64hz motion
     */
    public int EmoSettingMode(int value) {
        if (value != 0 && value != 1 && value != 2) {
            return 0;
        }
        switch (value) {
            case 0:
                valueMode = 256;
                break;
            case 1:
                valueMode = 384;
                break;
            case 2:
                valueMode = 388;
                break;
            case 3:
                valueMode = 392;
                break;
            default:
                break;
        }
        return 1;
    }

    /* Connecting device */
    public boolean EmoConnectDevice(int idDevice, int indexDevice) {
        BluetoothDevice device = null;
        bluetoothGatt = null;
        switch (idDevice) {
            case 0:
                if (!list_device_insight.isEmpty() && indexDevice < list_device_insight.size()) {
                    device = list_device_insight.get(indexDevice);
                }
                break;
            case 1:
                if (!list_device_epoc.isEmpty() && indexDevice < list_device_epoc.size()) {
                    device = list_device_epoc.get(indexDevice);
                }
            default:
                break;
        }
        if (device != null && !isConnected) {
            mBluetoothAdapter.cancelDiscovery();
            bluetoothGatt = device.connectGatt(mContext, false, mBluetoothGattCallback);
        }
        if (bluetoothGatt != null) {
            scanLeDevice(false);
            list_device_epoc.clear();
            list_device_insight.clear();
            return true;
        } else {
            return false;
        }
    }

    /*Disconnect current device*/
    public boolean DisconnectHeadset() {
        if (bluetoothGatt != null) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        bluetoothGatt.disconnect();
                    } catch (NullPointerException e) {
                        // TODO: handle exception
                    }
                }

            };
            thread.start();
            return true;
        }
        return false;
    }

    /* Get signal strength from Insight */
    public int GetSignalStrengthBLEInsight(int index) {
        if (index < 0 || index >= list_device_insight.size()) return 0;
        return signal_insight.get(list_device_insight.get(index));
    }

    /* Get signal strength from EPOC+ */
    public int GetSignalStrengthBLEEPOCPLUS(int index) {
        if (index < 0 || index >= list_device_epoc.size()) return 0;
        return signal_epoc.get(list_device_epoc.get(index));
    }

    public int GetStatusDeviceInsight(int index) {
        if (index < 0 || index >= list_device_insight.size()) return -1;
        return status_device_insight.get(list_device_insight.get(index));
    }

    public int GetStatusDeviceEpoc(int index) {
        if (index < 0 || index >= list_device_epoc.size()) return -1;
        return status_device_epoc.get(list_device_epoc.get(index));
    }

    public int SettingModeForHeadset(int value) {
        if (list_device_epoc.isEmpty()) {
            return 0;
        } else {
            valueMode = value;
        }
        return 1;
    }

    public void RefreshScanDevice() {
        try {
            for (int i = 0; i < list_device_insight.size(); i++) {
                BluetoothDevice device = list_device_insight.get(i);
                if (status_device_insight.get(device) == STATUS_NOTCONNECT) {
                    signal_insight.remove(device);
                    status_device_insight.remove(device);
                    list_device_insight.remove(device);
                }
            }
            for (int i = 0; i < list_device_epoc.size(); i++) {
                BluetoothDevice device = list_device_epoc.get(i);
                if (status_device_epoc.get(device) == STATUS_NOTCONNECT) {
                    signal_epoc.remove(device);
                    status_device_epoc.remove(device);
                    list_device_epoc.remove(device);
                }
            }

            if (!mBluetoothAdapter.isEnabled()) return;

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                if (mLEScanner != null) {
                    InitCallbackFunction();
                    mLEScanner.stopScan(scan);
                }
            }

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.startLeScan(device_uuid, mLeScanCallback);
            } else {
                if (mLEScanner != null) {
                    InitCallbackFunction();
                    mLEScanner.startScan(scan);
                }
            }
        } catch (NullPointerException e) {
            // TODO: handle exception
        }
    }

    /*Retrieve Connect Device*/
    private void RetrieveConnect() {
        List<BluetoothDevice> devicelist;
        devicelist = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        if (devicelist.size() > 0) {
            for (int i = 0; i < list_device_insight.size(); i++) {
                BluetoothDevice insight_device = list_device_insight.get(i);
                if (!devicelist.contains(insight_device) && status_device_insight.get(insight_device) == STATUS_CONNECTED) {
                    list_device_insight.remove(insight_device);
                    status_device_insight.remove(insight_device);
                    signal_insight.remove(insight_device);
                }
            }
            for (int i = 0; i < list_device_epoc.size(); i++) {
                BluetoothDevice epoc_device = list_device_epoc.get(i);
                if (!devicelist.contains(epoc_device) && status_device_epoc.get(epoc_device) == STATUS_CONNECTED) {
                    list_device_epoc.remove(epoc_device);
                    status_device_epoc.remove(epoc_device);
                    signal_epoc.remove(epoc_device);
                }
            }
            for (int i = 0; i < devicelist.size(); i++) {
                BluetoothDevice device = devicelist.get(i);
                if (device.getName().contains("Insight")) {
                    if (!list_device_insight.contains(device)) {
                        list_device_insight.add(device);
                    }
                    signal_insight.put(device, 0);
                    status_device_insight.put(device, STATUS_CONNECTED);
                } else if (device.getName().contains("EPOC+")) {
                    if (!list_device_epoc.contains(device)) {
                        list_device_epoc.add(device);
                    }
                    status_device_epoc.put(device, STATUS_CONNECTED);
                    signal_epoc.put(device, 0);
                }
            }

        } else {
            for (int i = 0; i < list_device_insight.size(); i++) {
                BluetoothDevice device = list_device_insight.get(i);
                if (status_device_insight.get(device) == STATUS_CONNECTED) {
                    list_device_insight.remove(device);
                    status_device_insight.remove(device);
                    signal_insight.remove(device);
                }
            }
            for (int i = 0; i < list_device_epoc.size(); i++) {
                BluetoothDevice device = list_device_epoc.get(i);
                if (status_device_epoc.get(device) == STATUS_CONNECTED) {
                    list_device_epoc.remove(device);
                    status_device_epoc.remove(device);
                    signal_epoc.remove(device);
                }
            }
        }
    }

    /* Scan BTLE devices */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            //Log.e("EmotivBluetooth","Scan device");
            mHandler.postDelayed(thread_retrive, 500);
            //isScanDevice = true;
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.startLeScan(device_uuid, mLeScanCallback);
            } else {
                try {
                    List<ScanFilter> scanFilter = new ArrayList<>();
                    ScanSettings.Builder scanSettingBuilder = new ScanSettings.Builder();
                    //scanSettingBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
                    ScanSettings scanSettings = scanSettingBuilder.build();
                    mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                    if (mLEScanner != null) {
                        InitCallbackFunction();
                        mLEScanner.startScan(scanFilter, scanSettings, scan);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        } else {
            //isScanDevice = false;
            mHandler.removeCallbacks(thread_retrive);
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else try {
                if (mLEScanner != null) {
                    InitCallbackFunction();
                    mLEScanner.stopScan(scan);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    /* Callback for the BTLE device scan */
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        if (device.getName().contains("Insight")) {
                            //Log.e("EmoBluetooth", "add Insight");
                            //SignalStrengthBLE[0] = rssi;
                            //CounterScanInsight++;
                            if (!list_device_insight.contains(device)) {
                                list_device_insight.add(device);
                            }
                            status_device_insight.put(device, STATUS_NOTCONNECT);
                            signal_insight.put(device, rssi);

                        }
                        if (device.getName().contains("EPOC+")) {
                            //SignalStrengthBLE[1] = rssi;
                            //CounterScanEpoc++;
                            if (!list_device_epoc.contains(device)) {
                                list_device_epoc.add(device);
                            }
                            status_device_epoc.put(device, STATUS_NOTCONNECT);
                            signal_epoc.put(device, rssi);
                        }
                    } catch (NullPointerException e) {
                        // TODO: handle exception
                    }
                }

            };

            thread.start();

        }
    };

    private void InitCallbackFunction() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP)
            return;
        if (scan == null) scan = new ScanCallback() {
            @Override
            public void onBatchScanResults(List<ScanResult> results) {

                Log.i("bluetooth", "The batch result is " + results.size());
            }

            @SuppressLint("NewApi")
            @Override
            public void onScanResult(int callbackType, final ScanResult result) {
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            BluetoothDevice device = result.getDevice();
                            if (device.getName().contains("Insight")) {
                                //SignalStrengthBLE[0] = result.getRssi();
                                //CounterScanInsight++;
                                //	Log.e("EmoBluetooth", "Insight device");
                                if (!list_device_insight.contains(device)) {
                                    list_device_insight.add(device);
                                }
                                signal_insight.put(device, result.getRssi());
                                status_device_insight.put(device, STATUS_NOTCONNECT);

                            }
                            if (device.getName().contains("EPOC+")) {
                                //SignalStrengthBLE[1] = result.getRssi();
                                //CounterScanEpoc++;
                                if (!list_device_epoc.contains(device)) {
                                    list_device_epoc.add(device);
                                }
                                signal_epoc.put(device, result.getRssi());
                                status_device_epoc.put(device, STATUS_NOTCONNECT);
                            }
                        } catch (NullPointerException e) {
                            // TODO: handle exception
                        }
                    }

                };

                thread.start();
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };
    }

    /* Callback for services discovery */
    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (_TypeHeadset == 0) {
                        ReadValueForCharacteristec(bluetoothGatt, DEVICE_SERVICE_UUID, Firmware_Characteristic_UUID);
                    }
                    if (_TypeHeadset == 1) {
                        if (!isSettingMode) {
                            ReadValueForCharacteristec(bluetoothGatt, DATA_SERVICE_UUID, Setting_Characteristic_UUID);
                        } else {
                            SetNortifyValue(bluetoothGatt, 2);
                        }
                    }

                }
            };

            thread.start();
        }

        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (characteristic.getUuid().equals(Serial_Characteristic_UUID)) {
                byte[] serial = characteristic.getValue();
                if (serial != null && serial.length > 0) {
                    SendSerialNumber(serial);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!checkUser) {
                    tHandler.postDelayed(thread_checknortifi, 1000);
                    checkUser = true;
                    SetNortifyValue(gatt, 0);
                }
            } else if (characteristic.getUuid().equals(Setting_Characteristic_UUID)) {
                byte[] setting = characteristic.getValue();
                if (setting != null && setting.length > 0) {
                    ReadUserConfig(characteristic.getValue());
                }
                ReadValueForCharacteristec(gatt, DEVICE_SERVICE_UUID, Firmware_Characteristic_UUID);
            } else if (characteristic.getUuid().equals(Firmware_Characteristic_UUID)) {
                byte[] firmware = characteristic.getValue();
                if (firmware != null && firmware.length > 0) {
                    isConnected = true;
                    SendFirmWareVersion(characteristic.getValue());
                }
                ReadValueForCharacteristec(gatt, DEVICE_SERVICE_UUID, Serial_Characteristic_UUID);
            }

        }

        /* Callback for getting data from BTLE */
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            byte[] oldData = characteristic.getValue();
            if (characteristic.getUuid().equals(EEG_Characteristic_UUID)) {
                haveData = true;
                CheckNortifiBLE++;
                if (!isNewDataFormat()) {
                    if (!lock) {
                        start_counter = (int) oldData[0];
                        lock = true;
                        int chunk_start = (int) oldData[1];
                        if (chunk_start == 1) System.arraycopy(oldData, 2, EEGBuffer, 0, 16);
                    }
                    if (start_counter == (int) oldData[0]) {
                        // receive 1 packet
                        System.arraycopy(oldData, 2, EEGBuffer, 16 * ((int) oldData[1] - 1), 16);
                        if ((int) oldData[1] == 2) {
                            WriteEEG(EEGBuffer);
//                            if (start_counter >= 127) {
//                                start_counter = 0;
//                            } else {
//                                start_counter++;
//                            }
                        }
                    } else {
                        //if ((int) oldData[0] < 128 && (int) oldData[0] >= 0) {
                        start_counter = (int) oldData[0];
                        System.arraycopy(oldData, 2, EEGBuffer, 16 * ((int) oldData[1] - 1), 16);
                        //}
                    }
                } else {
                    System.arraycopy(oldData, 0, EEGBuffer_new_Insight, 0, oldData.length);
                    WriteEEG(EEGBuffer_new_Insight);
                }
            } else if (characteristic.getUuid().equals(MEMS_Characteristic_UUID)) {
                System.arraycopy(oldData, 0, MEMSBuffer, 0, oldData.length);
                WriteMEMS(MEMSBuffer);
                Arrays.fill(MEMSBuffer, (byte) 0);
            }
        }

        public void onConnectionStateChange(final BluetoothGatt gatt,
                                            final int status, final int newState) {

            if (newState == BluetoothGatt.STATE_CONNECTED) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    bluetoothGatt.disconnect();
                    bluetoothGatt.close();
                    list_device_insight.clear();
                    list_device_epoc.clear();
                } else {
                    /*this callback not stable . So, isConnected = true only set when read success an characteristic*/
                    //isConnected = true;
                    try {
                        if (gatt.getDevice().getName().contains("Insight")) {
                            SetTypeHeadset(0);
                            _TypeHeadset = 0;
                        }
                        if (gatt.getDevice().getName().contains("EPOC+")) {
                            SetTypeHeadset(1);
                            _TypeHeadset = 1;
                        }

                    } catch (NullPointerException e) {
                        // TODO: handle exception
                    }
                    lock = false;
                    list_device_insight.clear();
                    list_device_epoc.clear();
                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            gatt.discoverServices();
                        }

                    };
                    thread.start();
                }
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                isConnected = false;
                //Log.e("EmotivBluetooth","device disconnected");
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        gatt.close();
                        bluetoothGatt = null;
                        if (checkUser) {
                            checkUser = false;
                            DisconnectDevice();
                            tHandler.removeCallbacks(thread_checknortifi);
                        }
                        if (mBluetoothAdapter.isEnabled()) {
                            scanLeDevice(true);
                        } else {
                            handler.postDelayed(thread_check_adapter, 500);
                        }
                    }
                };
                thread.start();
            }
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (!isSettingMode) {
                if (_TypeHeadset != 0 && !isNortifyMotion) {
                    SetNortifyValue(gatt, 1);
                    isNortifyMotion = true;
                }
            } else {
                WriteValueForCharacteristic(gatt, valueMode);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                byte result[] = characteristic.getValue();
                if (result[0] == 0x01) {
                    isSettingMode = false;
                    ReadValueForCharacteristec(gatt, DATA_SERVICE_UUID, Setting_Characteristic_UUID);
                }
            }
        }

        /* Setting read charesteristic */
        private void ReadValueForCharacteristec(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
            try {
                BluetoothGattCharacteristic characteristic =
                        gatt.getService(serviceUUID).getCharacteristic(characteristicUUID);
                gatt.readCharacteristic(characteristic);
            } catch (NullPointerException e) {
                // TODO: handle exception
            }
        }

        /* Setting write charesteristic */
        private void WriteValueForCharacteristic(BluetoothGatt gatt, int value) {
            try {
                BluetoothGattCharacteristic characteristic = gatt.getService(DATA_SERVICE_UUID)
                        .getCharacteristic(Config_Characteristic_UUID);
                BigInteger data = BigInteger.valueOf(value);
                if (characteristic != null) {
                    characteristic.setValue(data.toByteArray());
                    gatt.writeCharacteristic(characteristic);
                }
            } catch (NullPointerException e) {
                // TODO: handle exception
            }
        }

        /* Setting notifiy charesteristic */
        private void SetNortifyValue(BluetoothGatt gatt, int state) {
            BluetoothGattCharacteristic characteristic;
            switch (state) {
                case 0: {
                    characteristic = gatt.getService(DATA_SERVICE_UUID)
                            .getCharacteristic(EEG_Characteristic_UUID);
                }
                break;
                case 1: {
                    characteristic = gatt.getService(DATA_SERVICE_UUID)
                            .getCharacteristic(MEMS_Characteristic_UUID);
                }
                break;
                case 2: {
                    characteristic = gatt.getService(DATA_SERVICE_UUID)
                            .getCharacteristic(Config_Characteristic_UUID);
                }
                break;
                default:
                    return;
            }

            // Enable local notifications
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gatt.setCharacteristicNotification(characteristic, true);

            // Enabled remote notifications
            BluetoothGattDescriptor desc = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(desc);
        }
    };

    /* Native function call in C++ */
    private native void SendSerialNumber(byte[] serial);

    private native void SendFirmWareVersion(byte[] value);

    private native void WriteEEG(byte[] serial);

    private native void WriteMEMS(byte[] serial);

    private native void SetTypeHeadset(int type);

    private native void ReadUserConfig(byte[] data);

    private native void DisconnectDevice();

    private native boolean isNewDataFormat();

    static {
        System.loadLibrary("edk");
    }
}
