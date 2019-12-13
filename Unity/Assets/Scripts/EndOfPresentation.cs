﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EndOfPresentation : MonoBehaviour
{
  public GameObject canvas;
  public GameObject sphere;
  public GameObject start;
  public GameObject reStart;
  public GameObject cube;

  public SendAndroidDatas toAndroidData;

    // Start is called before the first frame update
    void Start()
    {
    
    sphere.SetActive(false);
    canvas.SetActive(true);
    start.SetActive(false);
    reStart.SetActive(true);
    cube.SetActive(true);
    toAndroidData.measuerementEnded();
  }

    // Update is called once per frame
    void Update()
    {
        
    }
}
