package com.aut.presentationskills;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aut.presentationskills.communication.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelper;

    TextView dataReceived;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataReceived = (TextView) findViewById(R.id.dataReceived);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttMessage message = new MqttMessage();
                /*{
                  "measurementId": "5cae4eab430e5611dce0bbde",
                  "data": [
                    {
                      "name": "Brainwave2",
                      "value": 1.73
                    },
                    {
                      "name": "Brainwave2",
                      "value": 1.73
                    }
                  ]
                }
                */
                message.setPayload("{foo: bar, lat: 0.23443, long: 12.3453245}".getBytes());
                try {
                    mqttHelper.mqttAndroidClient.publish("foo", message);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        startMqtt();

    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                dataReceived.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }
}
