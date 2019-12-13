package com.aut.presentationskills.emotiv.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import static com.aut.presentationskills.PresentationSkillsVRandAndroid.injector;

public class RequestPermissionActivity extends Activity {
    public static final String EXTRA_REQUEST_CODE = "requestCode";
    public static final String EXTRA_PERMISSIONS = "permissions";

    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injector.inject(this);

        requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, 0);
        String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);

        if (permissions == null || permissions.length == 0) {
            finish();
            return;
        }

        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.requestCode) {
            finish();
        }
    }
}
