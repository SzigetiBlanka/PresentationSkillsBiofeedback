package com.aut.presentationskills.ui.Unity;

import com.aut.presentationskills.emotiv.EmotivEEG;

public class EmotivSensor {
    private EmotivEEG emotiv;

    public EmotivEEG getEmotiv(){return emotiv;}

    public void setEmotiv(EmotivEEG emotiv){this.emotiv=emotiv;}
}
