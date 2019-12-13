using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ConnectionTest : MonoBehaviour
{

  public RawImage internet_rawimg;
  public RawImage emotiv_rawimg;
  public RawImage controller_rawimg;
  public RawImage data_rawimg;

  public Texture done_img;
  public Texture notDone_img;

  private VideoManager videoManager;
  public AdaptedEventHandler eventHandler;
  public SendAndroidDatas androidDatas;

  public Button startBtn;

  private void Start() {
    videoManager = GameObject.Find("VideoMNG").GetComponent<VideoManager>();
    androidDatas.SceneLoaded();
    startBtn.enabled = false;
  }
   bool internet=false;
   bool cont = false;
   bool data = false;
   bool sensor = false;
  void Update()
    {
      
     
      
    //check internet connection
      if(Application.internetReachability != NetworkReachability.NotReachable) {
      internet_rawimg.texture = done_img;
      internet = true;
      } else {
        internet_rawimg.texture = notDone_img;
      internet = false;
    }
      //check loadeddata
      if(videoManager.videoDownloaded) {
        data = true;
        data_rawimg.texture = done_img;
      } else {
      data = false;
      data_rawimg.texture = notDone_img;
      }

      //check sensor status
      if(eventHandler.sensorStatus != "OFFLINE") {
      sensor = true;
        emotiv_rawimg.texture = done_img;
      } else {
      sensor = false;
      emotiv_rawimg.texture = notDone_img;
      }

    //controller check
      GvrConnectionState state = GvrControllerInput.State;
      if(state == GvrConnectionState.Connected) {
      cont = true;
        controller_rawimg.texture = done_img;
      } else {
      cont = false;
      controller_rawimg.texture = notDone_img;
      }

    //startbtn enable
    if(internet &&cont&&sensor&&data) {
      startBtn.enabled = true;
    }

    }
}
