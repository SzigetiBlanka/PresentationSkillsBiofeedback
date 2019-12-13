package com.aut.presentationskills.ui.measurement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aut.presentationskills.PresentationSkillsVRandAndroid;
import com.aut.presentationskills.R;
import com.aut.presentationskills.emotiv.Constants;
import com.aut.presentationskills.emotiv.EmotivEEG;
import com.aut.presentationskills.model.Deck;
import com.aut.presentationskills.model.Measurement;
import com.aut.presentationskills.model.User;
import com.aut.presentationskills.model.Video;
import com.aut.presentationskills.ui.Unity.DeckCoordinateCalculator;
import com.aut.presentationskills.ui.Unity.UnityMainActivity;
import com.aut.presentationskills.ui.signin.SignInActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class MeasurementActivity extends AppCompatActivity implements MeasurementScreen, View.OnClickListener {

    @Inject
    MeasurementPresenter measurementPresenter;

    String jwt = null;
    TextView measurementNameTxt;
    TextView measurementDescTxt;
    TextView sensorconnectedTxt;
    TextView statustxt;
    Button start_btn;
    Button tryAgain_btn;
    User user;
    ProgressBar progbar;

    ProgressDialog dialog;

    List<Deck> decks;
    List<Video> videos;

    String measurementID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        PresentationSkillsVRandAndroid.injector.inject(this);

        //orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        measurementNameTxt = findViewById(R.id.measurementNameTxt);
        measurementDescTxt = findViewById(R.id.measurementDescTxt);
        sensorconnectedTxt = findViewById(R.id.connectionTxt);
        statustxt =findViewById(R.id.statusTxt);
        progbar = findViewById(R.id.progressbar);
        sensorconnectedTxt.setText(R.string.disconnected);
        sensorconnectedTxt.setTextColor(getResources().getColor(R.color.disconnectedSensor));
        start_btn = findViewById(R.id.start_btn);
        tryAgain_btn = findViewById(R.id.tryagain_btn);
        findViewById(R.id.start_btn).setOnClickListener(this);
        findViewById(R.id.tryagain_btn).setOnClickListener(this);
        //később törölni
        //start_btn.setEnabled(true);
        measurementID= new String();
        progbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        measurementPresenter.attachScreen(this);

        ((PresentationSkillsVRandAndroid)this.getApplication()).setEmotiv(new EmotivEEG());
        ((PresentationSkillsVRandAndroid)this.getApplication()).getEmotiv().connect();
        statustxt.setText(((PresentationSkillsVRandAndroid)this.getApplication()).getEmotiv().status.toString());
        tryToLoadMeasurement();
    }

    private void tryToLoadMeasurement(){
        progbar.setVisibility(View.VISIBLE);
        //CreateDialog();
        jwt =this.getIntent().getStringExtra(SignInActivity.KEY_JWT);
        Log.w("measurementActivity jwt: ", jwt);
        measurementPresenter.userInteractor.setJwt(jwt);
        if(user !=null) {
            Log.w("user id: ", user.getId());
            measurementPresenter.refreshUser(user.getId());
            getMeasurementDataswithUserId(user);
        }else
            measurementPresenter.refreshUser("");
        if(((PresentationSkillsVRandAndroid)this.getApplication()).getEmotiv().status == Constants.Status.ONLINE){
            sensorconnectedTxt.setTextColor(getResources().getColor(R.color.connectedSensor));
            tryAgain_btn.setEnabled(false);
        }else{
            tryAgain_btn.setEnabled(true);
        }
        CreateStatusTimer();
    }

    private void CreateStatusTimer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          statusSetText();
                                      }
        },0,2000);
    }

    private void statusSetText(){

        statustxt.setText(((PresentationSkillsVRandAndroid)this.getApplication()).getEmotiv().status.toString());
        Log.w("Android: ", "status updated");
    }

    @Override
    protected void onStop() {
        super.onStop();
        measurementPresenter.detachScreen();
    }

    @Override
    public void onResume() {
        super.onResume();
        //measurementPresenter.refreshUser();
        if(((PresentationSkillsVRandAndroid)this.getApplication()).getEmotiv().status== Constants.Status.ONLINE){
            sensorconnectedTxt.setText(R.string.connected);
            sensorconnectedTxt.setTextColor(getResources().getColor(R.color.connectedSensor));
            tryAgain_btn.setEnabled(false);
        }else {
            ((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv().connect();
            if (((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv().connectfail){
                progbar.setVisibility(View.INVISIBLE);
            }
        }
        /*while(((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv().connectfail==false &&
                ((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv().status== Constants.Status.CONNECTING){
            progbar.setVisibility(View.VISIBLE);
        }*/

    }

    private void CreateDialog() {
        dialog=new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    private void HideDialog(){
        dialog.hide();
    }

    @Override
    public void getMeasurementDataswithUserId(User user) {
        measurementPresenter.getMeasurementData(user.getId());
    }

    @Override
    public void loadAgainMeasurementDatay() {
        /*if(dialog!=null)
            HideDialog();*/
        progbar.setVisibility(View.INVISIBLE);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("There is no measurement assigned to you... Try again?")
                .setCancelable(true)
                .setNegativeButton("no", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tryToLoadMeasurement();
                    }
                }).create();

        alertDialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.disconnectedSensor));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.connectedSensor));


    }

    @Override
    public void showMeasurementDatas(Measurement measurement) {
        //HideDialog();
        progbar.setVisibility(View.INVISIBLE);
        measurementDescTxt.setText(measurement.getDescription());
        measurementNameTxt.setText(measurement.getTitle());
        decks = measurement.getDecks();
        videos = measurement.getScene().getVideos();
        start_btn.setEnabled(true);
        measurementID = measurement.getId();
    }

    @Override
    public void showNetworkError(String errorMsg) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                startUnity();
                break;
            case R.id.tryagain_btn:
                progbar.setVisibility(View.VISIBLE);
                ((PresentationSkillsVRandAndroid)this.getApplication()).getEmotiv().connect();
                if (((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv().connectfail){
                    progbar.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
    private String[] videoUrlTags = {"videoUrls1","videoUrls2","videoUrls3","videoUls4","videoUrls5",
                                     "videoUrls6","videoUrls7","videoUrls8","videoUrls9","videoUrls10"};
    private void startUnity() {
        Intent intent = new Intent(this, UnityMainActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        String videolist;
        if(!videos.isEmpty()) {
            intent.putExtra("videoUrlsNum",videos.size());
            /*for (int i = 0; i < videos.size(); i++) {
                intent.putExtra(videoUrlTags[i], videos.get(i).getVideoUrl());
                Log.w(videoUrlTags[i], videos.get(i).getVideoUrl());
            }/*/
            videolist= TextUtils.join("@", videos);
            intent.putExtra("setVideoListSize", videolist);
        }
        String imagepathsize =  getImagePathSize(Deck.DeckTpye.DECK);
        if(imagepathsize!=null) {
            intent.putExtra("setDeckListSize", imagepathsize);
            intent.putExtra("setDeckList", createImagesPathString(Deck.DeckTpye.DECK));
            intent.putExtra("setPositionDeck", createPostionString(Deck.DeckTpye.DECK));
        }
        imagepathsize =  getImagePathSize(Deck.DeckTpye.PROMPTER);
        if(imagepathsize!=null) {
            intent.putExtra("setPromterListSize",imagepathsize);
            intent.putExtra("setPromterList",createImagesPathString(Deck.DeckTpye.PROMPTER));
            intent.putExtra("setPositionPromter",createPostionString(Deck.DeckTpye.PROMPTER));
        }
        intent.putExtra("measurementID",measurementID);
        startActivity(intent);
    }


    private String getImagePathSize(Deck.DeckTpye type){
        String result = null;
        if(!decks.isEmpty()) {

            for (int i = 0; i < decks.size(); i++) {
                if (decks.get(i).getType() == type) {
                    result = Integer.toString(decks.get(i).getImagePaths().size());
                    break;
                }
            }
        }
        //Log.w("getImagePathSize", result);
        return result;
    }

    private String createImagesPathString(Deck.DeckTpye type){
        String[] array = null;
        String result= null;
        if(!decks.isEmpty()) {

            for (int i = 0; i < decks.size(); i++) {
                if(decks.get(i).getType()==type){
                    array = new String[decks.get(i).getImagePaths().size()];
                    for(int j = 0; j < decks.get(i).getImagePaths().size(); j++){
                        array[j] = decks.get(i).getImagePaths().get(j);
                        Log.w("imagespath: ", array[j]);
                    }
                    break;
                }
            }
            if(array!=null)
                result = TextUtils.join("@", array);
        }
        Log.w("createImagesPathString",result);
        return result;
    }

    private String createPostionString(Deck.DeckTpye type){
        Double[] result = null;
        String resultstr= null;
        if(!videos.isEmpty()) {
            //TODO: egyenlőre csak egy videón lévő pozíciókat ad át
            result = new Double[videos.get(0).getDeckPositions().size()*4];
            int idx=0;
            for(int j = 0; j < videos.get(0).getDeckPositions().size(); j++) {
                if(videos.get(0).getDeckPositions().get(j).getType()==type) {
                    for (int i = 0; i < videos.get(0).getDeckPositions().size(); i++) {
                        if (videos.get(0).getDeckPositions().get(i).getType() == type) {
                            result[idx++] = videos.get(0).getDeckPositions().get(i).getTopLeft().getX();
                            Log.w("Topleft x: ", result[idx - 1].toString());
                            result[idx++] = videos.get(0).getDeckPositions().get(i).getTopLeft().getY();
                            Log.w("Topleft y: ", result[idx - 1].toString());
                            result[idx++] = videos.get(0).getDeckPositions().get(i).getBottomRight().getX();
                            Log.w("BottomRight x: ", result[idx - 1].toString());
                            result[idx++] = videos.get(0).getDeckPositions().get(i).getBottomRight().getY();
                            Log.w("BottomRight y: ", result[idx - 1].toString());
                            break;
                        }
                    }
                }
            }
            if (result!=null) {
                DeckCoordinateCalculator calculator= new DeckCoordinateCalculator();
                calculator.PresentationSetup(result[0], result[1], result[2], result[3]);
                String[] newCoordinates = new String[]{Double.toString(calculator.a), Double.toString(calculator.b),
                                                        Float.toString(calculator.x), Float.toString(calculator.y),
                                                        Float.toString(calculator.z)};
                resultstr = TextUtils.join("@", newCoordinates);
            }
        }


        Log.w("createPostionString",resultstr);
        return resultstr;
    }
}
