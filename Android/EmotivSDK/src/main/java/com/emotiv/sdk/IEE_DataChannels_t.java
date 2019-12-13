package com.emotiv.sdk;

/**
 * Created by Mastre on 2018. 01. 10..
 */

public class IEE_DataChannels_t {
    public static final int IED_COUNTER = 0;           //!< Sample counter
    public static final int IED_INTERPOLATED = 1;      //!< Indicate if data is interpolated
    public static final int IED_RAW_CQ = 2;            //!< Raw contact quality value
    public static final int IED_AF3 = 3;               //!< Channel AF3
    public static final int IED_F7 = 4;                //!< Channel F7
    public static final int IED_F3 = 5;                //!< Channel F3
    public static final int IED_FC5 = 6;               //!< Channel FC5
    public static final int IED_T7 = 7;                //!< Channel T7
    public static final int IED_P7 = 8;                //!< Channel P7
    public static final int IED_Pz = 9;                //!< Channel Pz
    public static final int IED_O1 = 9;                //!< Channel O1 = Pz
    public static final int IED_O2 = 10;                //!< Channel O2
    public static final int IED_P8 = 11;                //!< Channel P8
    public static final int IED_T8 = 12;                //!< Channel T8
    public static final int IED_FC6 = 13;               //!< Channel FC6
    public static final int IED_F4 = 14;                //!< Channel F4
    public static final int IED_F8 = 15;                //!< Channel F8
    public static final int IED_AF4 = 16;               //!< Channel AF4
    public static final int IED_GYROX = 17;             //!< Gyroscope X-axis
    public static final int IED_GYROY = 18;             //!< Gyroscope Y-axis
    public static final int IED_TIMESTAMP = 19;         //!< System timestamp
    public static final int IED_MARKER_HARDWARE = 20;    //!< Marker from extender
    public static final int IED_ES_TIMESTAMP = 21;      //!< EmoState timestamp
    public static final int IED_FUNC_ID = 11;           //!< Reserved function id
    public static final int IED_FUNC_VALUE = 23;        //!< Reserved function value
    public static final int IED_MARKER = 24;            //!< Marker value from hardware
    public static final int IED_SYNC_SIGNAL = 25;       //!< Synchronisation signal
}
