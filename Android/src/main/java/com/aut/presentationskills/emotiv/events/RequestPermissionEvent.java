package com.aut.presentationskills.emotiv.events;

import java.util.HashMap;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class RequestPermissionEvent {
    private final int requestCode;
    private final Map<String, Boolean> grantResults;

    public RequestPermissionEvent(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.grantResults = new HashMap<>();

        int length = Math.min(permissions.length, grantResults.length);

        for (int i = 0; i < length; i++) {
            this.grantResults.put(permissions[i], grantResults[i] == PERMISSION_GRANTED);
        }
    }

    public int getRequestCode() {
        return requestCode;
    }

    public boolean hasGranted(String permission) {
        Boolean result = grantResults.get(permission);
        return result != null ? result : false;
    }
}
