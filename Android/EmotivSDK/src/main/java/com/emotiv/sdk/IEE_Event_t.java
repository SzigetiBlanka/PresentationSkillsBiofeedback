package com.emotiv.sdk;

public class IEE_Event_t {
    public static final int IEE_UnknownEvent = 0x0000;
    public static final int IEE_EmulatorError = 0x0001;
    public static final int IEE_ReservedEvent = 0x0002;
    public static final int IEE_UserAdded = 0x0010;
    public static final int IEE_UserRemoved = 0x0020;
    public static final int IEE_EmoStateUpdated = 0x0040;
    public static final int IEE_ProfileEvent = 0x0080;
    public static final int IEE_MentalCommandEvent = 0x0100;
    public static final int IEE_FacialExpressionEvent = 0x0200;
    public static final int IEE_InternalStateChanged = 0x0400;
    public static final int IEE_AllEvent = 0x07F0;
}
