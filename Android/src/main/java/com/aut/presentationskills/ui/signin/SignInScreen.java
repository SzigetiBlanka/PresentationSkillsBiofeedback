package com.aut.presentationskills.ui.signin;

public interface SignInScreen {
    void startMeasurement(String jwt);
    void showNetworkError(String errorMsg);
}
