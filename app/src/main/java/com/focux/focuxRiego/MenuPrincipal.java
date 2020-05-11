package com.focux.focuxRiego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MenuPrincipal extends AppCompatActivity {

   /* static String MQTTHOST = "tcp://s2ww4p.messaging.internetofthings.ibmcloud.com:1883";
    static String USERNAME = "use-token-auth";
    static String PASSWORD = "androidIOT";
    static String clientId = "d:s2ww4p:Android_IOT:Celular";
    String topicStr = "iot-2/evt/tiempo/fmt/json";
*/
    static String MQTTHOST = "tcp://h8nzkq.messaging.internetofthings.ibmcloud.com:1883";
    static String USERNAME = "a-h8nzkq-w7ifvycf1a";
    static String PASSWORD = "ymW1NboBDR@a7VVpZP";
    static String clientId = "a:h8nzkq:APP-ANDROID";
    String topicStr = "iot-2/type/PNUD/id/device-nodered/evt/programa/fmt/json";
    String topicStrDB = "iot-2/type/PNUD/id/device-nodered/evt/sensor/fmt/json";

    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(),MQTTHOST,clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                  //  Toast.makeText(MenuPrincipal.this,"CONEXIÓN ESTABLECIDA",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MenuPrincipal.this,"ERROR DE CONEXIÓN",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void programar(View view){
        Intent i = new Intent(this, RiegoProgramado.class);
        startActivity(i);
        String topic =topicStr;
        String message="P";
        try {
            client.publish(topic,message.getBytes(),0,false);
        } catch (MqttException e){
            e.printStackTrace();
        }
    }
    public void manual(View view){
        Intent i = new Intent(this, RiegoManual.class);
        startActivity(i);

    }
    public void sensor(View view){
        Intent i = new Intent(this, VerSensores.class);
        startActivity(i);
        String topic =topicStrDB;
        String message="DB";
        try {
            client.publish(topic,message.getBytes(),0,false);
        } catch (MqttException e){
            e.printStackTrace();
        }

    }

}
