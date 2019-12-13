using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Linq;
using YoutubeExtractor;
using UnityEngine.Video;
using System.IO;
using System;

public class YoutubeVideoManagerPlayer : MonoBehaviour
{
  public int quality;
  internal string[] allvideos;
  public static MediaPlayerCtrl mediaPlayerCtrl;
  private VideoPlayer videoPlayer;
  public SendAndroidDatas toAndroidData;

  public bool videoSaved;
  private bool Isfirstvideo;

  public string currentUrl;
  private string localUrl;
  private string videoName;
  
  public bool nowPlay;

  public string nextUrl;
  private string nextLocalUrl;
  private string nextVideoName;

  private Dictionary<string,string> urlsDictionary = new Dictionary<string,string>();

    // Start is called before the first frame update
    void Start()
    {
    nowPlay = true;
    Isfirstvideo = true;
    videoPlayer = GetComponent<VideoPlayer>();
    videoName = ReplaceVideoName(currentUrl);
    localUrl = Application.persistentDataPath + "/" + videoName + ".mp4";
    urlsDictionary.Add(currentUrl,localUrl);
    videoSaved = false;
    run(currentUrl,localUrl);
    }

  public void LoadNextVideo(string nexturl) {
    Isfirstvideo = false;
    nextLocalUrl = nexturl;
    if(!urlsDictionary.ContainsKey(nextLocalUrl)) {
      nextVideoName = ReplaceVideoName(nextUrl);
      nextLocalUrl = Application.persistentDataPath + "/" + nextVideoName + ".mp4";
      urlsDictionary.Add(nextUrl,nextLocalUrl);
      videoSaved = false;
      run(nextUrl,nextLocalUrl);
    } else {

    }
  }

  public void LoadAllVideo() {
    nowPlay = false;
    for(int i = 1; i < allvideos.Length; i++) {
      LoadNextVideo(allvideos[i]);
    }
  }

  private string ReplaceVideoName(string url) {
    string result=null;
    if(url != null || url != "") {
       result = url;
      if(result.Contains("https://www.youtube.com/watch?v=")) { 
        result = result.Replace("https://www.youtube.com/watch?v=","");
      }
      result = result.Replace('/','_');
      result = result.Replace('\\','_');
      result = result.Replace(':','_');
      result = result.Replace('.','_');
      result = result.Replace('?','_');
    }
    return result;
  }

  public async void run(string url, string locurl) {
    IEnumerable<VideoInfo> videoInfos = await DownloadUrlResolver.GetDownloadUrlsAsync(url);
    VideoInfo video = videoInfos.First(info => info.VideoType == VideoType.Mp4 && info.Resolution == quality);

    if(video.RequiresDecryption) {
      DownloadUrlResolver.DecryptDownloadUrl(video);
    }
    MediaPlayerCtrl mediaPlayerCtrl = GetComponent<MediaPlayerCtrl>();
    mediaPlayerCtrl.m_strFileName = video.DownloadUrl;
    Debug.Log(mediaPlayerCtrl.m_strFileName);
    Debug.Log("File exist: " + FileChk(locurl));
    if(!FileChk(locurl)) {
      StartCoroutine(testVideoDownload(mediaPlayerCtrl.m_strFileName));
    } else {
      videoSaved = true;
    }
  }

  public void Stop() {
    videoPlayer.Stop();
  }

  public void Pause() {
    videoPlayer.Pause();
  }
  public void Play() {
    videoPlayer.Play();
  }

  public bool FileChk(string locurl) {
    string filePath = locurl;

    if(System.IO.File.Exists(filePath)) {
      return true;
    } else {
      return false;
    }
  }


    IEnumerator testVideoDownload(string url) {
    Debug.Log("url: " + url);
    var www = new WWW(url);
    Debug.Log("Downloading!");
    
    while(!www.isDone) {
      if((www.progress * 100) % 10 == 0) {
        Debug.Log("downloaded " + (((int)www.progress) * 100).ToString() + "%...");
      }
      yield return null;
    }
    yield return www;
    File.WriteAllBytes(Application.persistentDataPath + "/" + videoName + ".mp4",www.bytes);
    /*if(string.IsNullOrEmpty(www.error)) {
      var filebytes = www.bytes;
      System.IO.MemoryStream stream = new System.IO.MemoryStream(filebytes);
    } else {
      Debug.Log(string.Format("An error occurred while downloading the file: {0}",www.error));
    }*/
    Debug.Log("File Saved! ");
    if(Isfirstvideo) {
      UrlToVideo(localUrl);
      LoadAllVideo();
    } /*else {
      UrlToVideo(nextLocalUrl);
    }*/
    videoSaved = true;
  }

  public void UrlToVideo() {
    UrlToVideo(localUrl);
  }

  public void UrlToVideo(string url) {
    Debug.Log("Bejutottam");
    videoPlayer.source = VideoSource.Url;
    videoPlayer.url = url;
    Debug.Log(url);
    videoPlayer.Prepare();
    videoPlayer.prepareCompleted += VideoPlayer_prepareCompleted;
    videoPlayer.loopPointReached += EndReached;
  }

  private void EndReached(VideoPlayer source) {
    Debug.Log("video end");
    if(!nowPlay) {
      Play();
    } else {
      UrlToVideo(localUrl);
    }
    
  }

  private void VideoPlayer_prepareCompleted(VideoPlayer source) {
    Debug.Log("mindjárt indul");
    if(Isfirstvideo) {
      toAndroidData.measuerementStarted();
      Play();
    }
    if(nowPlay) {
      nowPlay = false;
      Play();
    }
  }

  internal void PlayNextVideo(string newUrl) {
    nowPlay = true;
    if(urlsDictionary.ContainsKey(newUrl)) {
      localUrl = urlsDictionary[newUrl];
    } else {
      LoadNextVideo(newUrl);
    }
  }
}
