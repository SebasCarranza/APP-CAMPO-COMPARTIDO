package com.focux.focuxRiego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class RiegoManual extends AppCompatActivity {

    static String MQTTHOST = "tcp://h8nzkq.messaging.internetofthings.ibmcloud.com:1883";
    static String USERNAME = "a-h8nzkq-w7ifvycf1a";
    static String PASSWORD = "ymW1NboBDR@a7VVpZP";
    static String clientId = "a:h8nzkq:APP-ANDROID";
    String topicStr = "iot-2/type/PNUD/id/device-nodered/evt/programa/fmt/json";
    MqttAndroidClient client;

    private ImageView iv_zona1,iv_zona2;
    int c1=0,c2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riego_manual);

        iv_zona1=(ImageView)findViewById(R.id.iv_z1);
        iv_zona2=(ImageView)findViewById(R.id.iv_z2);

        client = new MqttAndroidClient(this.getApplicationContext(),MQTTHOST,clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Toast.makeText(RiegoProgramado.this,"CONEXIÓN ESTABLECIDA",Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //Toast.makeText(RiegoProgramado.this,"ERROR DE CONEXIÓN",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        iv_zona1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1==0){
                    iv_zona1.setImageResource(R.drawable.vrgb); c1=1;
                    String topic =topicStr;
                    String message="M10";
                    try {
                        client.publish(topic,message.getBytes(),0,false);
                    } catch (MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    iv_zona1.setImageResource(R.drawable.vgray); c1=0;
                    String topic =topicStr;
                    String message="M11";
                    try {
                        client.publish(topic,message.getBytes(),0,false);
                    } catch (MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        iv_zona2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c2==0){
                    iv_zona2.setImageResource(R.drawable.vrgb); c2=1;
                    String topic =topicStr;
                    String message="M20";
                    try {
                        client.publish(topic,message.getBytes(),0,false);
                    } catch (MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    iv_zona2.setImageResource(R.drawable.vgray); c2=0;
                    String topic =topicStr;
                    String message="M21";
                    try {
                        client.publish(topic,message.getBytes(),0,false);
                    } catch (MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
