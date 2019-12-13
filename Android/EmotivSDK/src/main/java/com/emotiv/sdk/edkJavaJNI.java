package com.emotiv.sdk;

@SuppressWarnings("JniMissingFunction")
public class edkJavaJNI {
    static {
        System.loadLibrary("edk");
    }

    public static native int IEE_EngineConnect__SWIG_0(String strDevID);

    public static native int IEE_EngineDisconnect();

    public static native double IEE_GetSecurityCode();

    public static native int IEE_CheckSecurityCode(double securityCode);

    public static native long IEE_EmoEngineEventCreate();

    public static native long IEE_EmoStateCreate();

    public static native void IEE_EmoEngineEventFree(long hEvent);

    public static native void IEE_EmoStateFree(long hEmoState);

    public static native int IEE_EngineGetNextEvent(long hEvent);

    public static native int IEE_EmoEngineEventGetType(long hEvent);

    public static native int IEE_EmoEngineEventGetEmoState(long hEvent, long hEmoState);

    public static native int IEE_EmoEngineEventGetUserId(long hEvent, int hUserIdOut);

    public static native int IEE_GetAverageBandPowers(int userId, int channel, double theta, double alpha, double low_beta, double high_beta, double gamma);

    public static native int IS_GetContactQuality(long hEmoState, int inputChannel);

    public static native int IS_PerformanceMetricIsActive(long hEmoState, int type);

    public static native float IS_PerformanceMetricGetExcitementLongTermScore(long hEmoState);

    public static native float IS_PerformanceMetricGetInstantaneousExcitementScore(long hEmoState);

    public static native float IS_PerformanceMetricGetRelaxationScore(long hEmoState);

    public static native float IS_PerformanceMetricGetStressScore(long hEmoState);

    public static native float IS_PerformanceMetricGetEngagementBoredomScore(long hEmoState);

    public static native float IS_PerformanceMetricGetInterestScore(long hEmoState);

    public static native float IS_PerformanceMetricGetFocusScore(long hEmoState);

    public static native double CustomerSecurity_emotiv_func(double securityCode);
}
