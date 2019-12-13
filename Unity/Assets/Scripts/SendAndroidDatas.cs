using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SendAndroidDatas : MonoBehaviour
{
  static AndroidJavaClass jc;
  static AndroidJavaObject currentActivity;


  public void SceneLoaded() {
    Debug.Log("Sending Event: scene loaded");
    try {
      if(Application.platform == RuntimePlatform.Android) {
        jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        currentActivity = jc.GetStatic<AndroidJavaObject>("currentActivity");
      }
      currentActivity.Call("sceneLoaded",new object[] { true });
    } catch(NullReferenceException e) {
      Debug.Log("Currentactivity null" + e);

    }
  }

  public void measuerementStarted() {
    Debug.Log("Sending Event: Measurement started");
    try {
      if(Application.platform == RuntimePlatform.Android) {
        jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        currentActivity = jc.GetStatic<AndroidJavaObject>("currentActivity");
      }
      currentActivity.Call("measuerementStarted",new object[] { true });
    } catch(NullReferenceException e) {
      Debug.Log("Currentactivity null" + e);

    }
  }

  public void videoChanged() {
    Debug.Log("Sending Event: Video Changed");
    try {
      if(Application.platform == RuntimePlatform.Android) {
        jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        currentActivity = jc.GetStatic<AndroidJavaObject>("currentActivity");
      }
      currentActivity.Call("videoChanged",new object[] { true });
    } catch(NullReferenceException e) {
      Debug.Log("Currentactivity null" + e);

    }
  }

  public void measuerementEnded() {
    Debug.Log("Sending Event: Measurement ended");
    try {
      if(Application.platform == RuntimePlatform.Android) {
        jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        currentActivity = jc.GetStatic<AndroidJavaObject>("currentActivity");
      }
      currentActivity.Call("measuerementEnded",new object[] { true });
    } catch(NullReferenceException e) {
      Debug.Log("Currentactivity null" + e);

    }
  }

}
