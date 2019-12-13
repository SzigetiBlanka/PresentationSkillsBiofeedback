package com.aut.presentationskills.emotiv.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
}
