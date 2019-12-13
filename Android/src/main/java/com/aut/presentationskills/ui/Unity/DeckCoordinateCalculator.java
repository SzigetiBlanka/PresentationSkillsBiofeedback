package com.aut.presentationskills.ui.Unity;

public class DeckCoordinateCalculator {

    public float x;
    public float y;
    public float z;

    public double a;
    public double b;

    private double imagesizex = 1920;
    private double imagesizey = 1080;
    double sphereD = 34.4;

    public void PresentationSetup(double topLeftX,double topLeftY,double bottomRightX,double bottomRightY) {

    topLeftY = imagesizey - topLeftY;
    bottomRightY = imagesizey - bottomRightY;

    double bigR = imagesizex / (2 * Math.PI); //szerver körének sugara
    double multiplierX = bigR / (sphereD / 2);
    double multiplierY = (imagesizey / (Math.PI)) / (sphereD / 2); // kör kerületének fele a vászon

    //calculate canvas size
    a = Math.abs((float)(bottomRightX - topLeftX)) / (multiplierX * 1.5f);
    b = Math.abs((float)(bottomRightY - topLeftY)) / (multiplierY * 0.9f);

    //normalize coordinates - 2D szerverről
    topLeftX = topLeftX / multiplierX;
    topLeftY = topLeftY / (multiplierY * 1.385f);
    bottomRightX = bottomRightX / multiplierX;
    bottomRightY = bottomRightY / (multiplierY * 1.385f);

        //calculate topleft x,y,z coordinates

        //x2 + z2 = r2

    double circleK = sphereD * Math.PI;
    double xCenter = Math.abs((float)(bottomRightX - topLeftX)) / 2 + topLeftX;
    //ivhossz = korkerulet/360*alfa -->alfa=(ivhossz*360)/korkerulet
    double alfa = xCenter / circleK*360.0;

    //xcoordinates
    double newX = Math.cos(alfa)/2; //* (sphereD/2);
    x = (float)newX+1f; //- Mathf.Abs((float)(bottomRightX - topLeftX)));

    //ycoordinates
    float y_curr = (float)(topLeftY - (b / 2));
    if(y_curr == (sphereD / 2))
      y = 0;
    else
      y = y_curr - (float)(sphereD / 2)+1f;

    //zcoordinates
        if(alfa>90&&alfa<180){
            alfa = alfa-90;
        }else if(alfa>180 && alfa<270){
            alfa =alfa-180;
        }else if(alfa>270 && alfa<360){
            alfa = alfa-270;
        }
    double newZ = Math.sin(alfa) * (sphereD / 2);
    z = (float)newZ+5.2f;

    }

}
