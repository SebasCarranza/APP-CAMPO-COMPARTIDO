package com.focux.focuxRiego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RiegoProgramado extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static String MQTTHOST = "tcp://h8nzkq.messaging.internetofthings.ibmcloud.com:1883";
    static String USERNAME = "a-h8nzkq-w7ifvycf1a";
    static String PASSWORD = "ymW1NboBDR@a7VVpZP";
    static String clientId = "a:h8nzkq:APP-ANDROID";
    String topicStr = "iot-2/type/PNUD/id/device-nodered/evt/programa/fmt/json";
    MqttAndroidClient client;
    private TextView tv_zonas,tv_dias,tv_hora,tv_duracion;
    private Spinner spinner_programas;
    String p1z="",p1d="",p1h="",p1t="",p2z="",p2d="",p2h="",p2t="",p3z="",p3d="",p3h="",p3t="",item="",programa1="",programa2="",programa3="";

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riego_programado);
        tv_dias=(TextView)findViewById(R.id.tv_dias);
        tv_duracion=(TextView)findViewById(R.id.tv_duracion);
        tv_hora=(TextView)findViewById(R.id.tv_hora);
        tv_zonas=(TextView)findViewById(R.id.tv_zonas);
        spinner_programas=(Spinner)findViewById(R.id.spinner_programas);
        spinner_programas.setOnItemSelectedListener(this);

        String [] prog={"ELEGIR PROGRAMA","PROGRAMA 1","PROGRAMA 2","PROGRAMA 3"};
        ArrayAdapter <String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,prog);
        spinner_programas.setAdapter(adapter);
        spinner_programas.setEnabled(false);

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
                    setSubscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(RiegoProgramado.this,"ERROR DE CONEXIÓN",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        String texto=new String(message.getPayload());

                        try {JSONObject obj =new JSONObject(texto);
                            programa1=obj.getString("programa-01");
                            programa2=obj.getString("programa-02");
                            programa3=obj.getString("programa-03");

                            JSONObject prog1=new JSONObject(programa1);
                            JSONObject prog2=new JSONObject(programa2);
                            JSONObject prog3=new JSONObject(programa3);

                            p1d=prog1.getString("dia");
                            p1h=prog1.getString("hora");
                            p1t=prog1.getString("tiempo");
                            p1z=prog1.getString("zona");

                            p2d=prog2.getString("dia");
                            p2h=prog2.getString("hora");
                            p2t=prog2.getString("tiempo");
                            p2z=prog2.getString("zona");

                            p3d=prog3.getString("dia");
                            p3h=prog3.getString("hora");
                            p3t=prog3.getString("tiempo");
                            p3z=prog3.getString("zona");

                        }catch (JSONException e) {
                           // Toast.makeText(RiegoProgramado.this,"OBTENIEDO PROGRAMAS", 500).show();
                        }
                        //tv_zonas.setText(texto);
                        spinner_programas.setEnabled(true);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });

            }
        },500);


    }
    private void setSubscription(){
        try{
            client.subscribe(topicStr,0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            item=parent.getItemAtPosition(position).toString();
            String zonas="",hora="",duracion="",dias="";
            if (item.equals("ELEGIR PROGRAMA")){
                tv_zonas.setText("");
                tv_hora.setText("");
                tv_duracion.setText("");
                tv_dias.setText("");

            }
            if(item.equals("PROGRAMA 1")){
                if(p1d.substring(0,1).equals("L")){dias="Lunes\n"; }
                if(p1d.substring(1,2).equals("M")){dias=dias+"Martes\n";}
                if(p1d.substring(2,3).equals("M")){dias=dias+"Miércoles\n";}
                if(p1d.substring(3,4).equals("J")){dias=dias+"Jueves\n";}
                if(p1d.substring(4,5).equals("V")){dias=dias+"Viernes\n";}
                if(p1d.substring(5,6).equals("S")){dias=dias+"Sábado\n";}
                if(p1d.substring(6,7).equals("D")){dias=dias+"Domingo\n";}

                if(p1z.substring(0,2).equals("01")){zonas="Zona 1\n";}
                if(p1z.substring(2,4).equals("02")){zonas=zonas+"Zona 2";}
                if(zonas.equals("")){zonas="Desactivadas";}

                tv_duracion.setText(p1t+" min");
                tv_hora.setText(p1h+" Hrs");
                tv_zonas.setText(zonas);
                tv_dias.setText(dias);
            }
        if(item.equals("PROGRAMA 2")){
            if(p2d.substring(0,1).equals("L")){dias="Lunes\n"; }
            if(p2d.substring(1,2).equals("M")){dias=dias+"Martes\n";}
            if(p2d.substring(2,3).equals("M")){dias=dias+"Miércoles\n";}
            if(p2d.substring(3,4).equals("J")){dias=dias+"Jueves\n";}
            if(p2d.substring(4,5).equals("V")){dias=dias+"Viernes\n";}
            if(p2d.substring(5,6).equals("S")){dias=dias+"Sábado\n";}
            if(p2d.substring(6,7).equals("D")){dias=dias+"Domingo\n";}

            if(p2z.substring(0,2).equals("01")){zonas="Zona 1\n";}
            if(p2z.substring(2,4).equals("02")){zonas=zonas+"Zona 2";}

            tv_duracion.setText(p2t+" min");
            tv_hora.setText(p2h+" Hrs");
            tv_zonas.setText(zonas);
            tv_dias.setText(dias);
        }
        if(item.equals("PROGRAMA 3")){
            if(p3d.substring(0,1).equals("L")){dias="Lunes\n"; }
            if(p3d.substring(1,2).equals("M")){dias=dias+"Martes\n";}
            if(p3d.substring(2,3).equals("M")){dias=dias+"Miércoles\n";}
            if(p3d.substring(3,4).equals("J")){dias=dias+"Jueves\n";}
            if(p3d.substring(4,5).equals("V")){dias=dias+"Viernes\n";}
            if(p3d.substring(5,6).equals("S")){dias=dias+"Sábado\n";}
            if(p3d.substring(6,7).equals("D")){dias=dias+"Domingo\n";}

            if(p3z.substring(0,2).equals("01")){zonas="Zona 1\n";}
            if(p3z.substring(2,4).equals("02")){zonas=zonas+"Zona 2";}

            tv_duracion.setText(p3t+" min");
            tv_hora.setText(p3h+" Hrs");
            tv_zonas.setText(zonas);
            tv_dias.setText(dias);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void modificar(View v){

        if(item.equals("ELEGIR PROGRAMA")){Toast.makeText(RiegoProgramado.this,"ELIJA UN PROGRAMA",Toast.LENGTH_SHORT).show();}
        else{Intent i=new Intent(this,Modificar.class);
        i.putExtra("programa",item);
        i.putExtra("programa1",programa1);
        i.putExtra("programa2",programa2);
        i.putExtra("programa3",programa3);
        startActivity(i);}
    }
}
