package com.aut.presentationskills.emotiv.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import static com.aut.presentationskills.PresentationSkillsVRandAndroid.injector;

public class ResultActivity extends Activity {
    public static final String EXTRA_ACTION = "action";
    public static final String EXTRA_REQUEST_CODE = "requestCode";

    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injector.inject(this);

        String action = getIntent().getStringExtra(EXTRA_ACTION);
        requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, 0);

        if (action == null) {
            finish();
            return;
        }

        startActivityForResult(new Intent(action), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == this.requestCode) {
            finish();
        }
    }
}
