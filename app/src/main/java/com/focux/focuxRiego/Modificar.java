package com.focux.focuxRiego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

public class Modificar extends AppCompatActivity {
    private TextView tv_ponerprograma;
    private CheckBox cb_zona1,cb_zona2,cb_lunes,cb_martes,cb_miercoles,cb_jueves,cb_viernes,cb_sabado,cb_domingo;
    private Spinner spinner_hora,spinner_minuto;
    private EditText et_duracion;
    String programa1="",programa2="",programa3="",dato="";

    static String MQTTHOST = "tcp://h8nzkq.messaging.internetofthings.ibmcloud.com:1883";
    static String USERNAME = "use-token-auth";
    static String PASSWORD = "auto-token";
    static String clientId = "d:h8nzkq:PNUD:device-nodered";
    String topicStr = "iot-2/evt/programa/fmt/json";
    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        client = new MqttAndroidClient(this.getApplicationContext(),MQTTHOST,clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Toast.makeText(Modificar.this,"CONEXIÓN ESTABLECIDA",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(Modificar.this,"ERROR DE CONEXIÓN",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        tv_ponerprograma=(TextView)findViewById(R.id.tv_ponerprograma);
        cb_zona1=(CheckBox)findViewById(R.id.cb_zona1);
        cb_zona2=(CheckBox)findViewById(R.id.cb_zona2);
        cb_lunes=(CheckBox)findViewById(R.id.cb_lunes);
        cb_martes=(CheckBox)findViewById(R.id.cb_martes);
        cb_miercoles=(CheckBox)findViewById(R.id.cb_miercoles);
        cb_jueves=(CheckBox)findViewById(R.id.cb_jueves);
        cb_viernes=(CheckBox)findViewById(R.id.cb_viernes);
        cb_sabado=(CheckBox)findViewById(R.id.cb_sabado);
        cb_domingo=(CheckBox)findViewById(R.id.cb_domingo);
        spinner_hora=(Spinner)findViewById(R.id.spinner_hora);
        spinner_minuto=(Spinner)findViewById(R.id.spinner_minuto);
        et_duracion=(EditText)findViewById(R.id.et_duracion);

        dato=getIntent().getStringExtra("programa");
        programa1=getIntent().getStringExtra("programa1");
        programa2=getIntent().getStringExtra("programa2");
        programa3=getIntent().getStringExtra("programa3");

        tv_ponerprograma.setText(dato);

        String [] horitas={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String [] minutitos={"00","01","02","03","04","05","06","07","08","09",
                "10","11","12","13","14","15","16","17","18","19",
                "20","21","22","23","24","25","26","27","28","29",
                "30","31","32","33","34","35","36","37","38","39",
                "40","41","42","43","44","45","46","47","48","49",
                "50","51","52","53","54","55","56","57","58","59"};

        ArrayAdapter <String> adapterH =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,horitas);
        ArrayAdapter <String> adapterM =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,minutitos);
        spinner_hora.setAdapter(adapterH);
        spinner_minuto.setAdapter(adapterM);
    }
    public void aceptar(View view){
        String zona="",dia="",horaH="",horaM="",hora="",d="",duracion="";
        if(cb_zona1.isChecked()){zona="01";}else{zona="--";}
        if(cb_zona2.isChecked()){zona=zona+"02------------";}else{zona=zona+"--------------";}
        if(cb_lunes.isChecked()){dia="L";}else{dia="-";}
        if(cb_martes.isChecked()){dia=dia+"M";}else{dia=dia+"-";}
        if(cb_miercoles.isChecked()){dia=dia+"M";}else{dia=dia+"-";}
        if(cb_jueves.isChecked()){dia=dia+"J";}else{dia=dia+"-";}
        if(cb_viernes.isChecked()){dia=dia+"V";}else{dia=dia+"-";}
        if(cb_sabado.isChecked()){dia=dia+"S";}else{dia=dia+"-";}
        if(cb_domingo.isChecked()){dia=dia+"D";}else{dia=dia+"-";}

        horaH=spinner_hora.getSelectedItem().toString();
        horaM=spinner_minuto.getSelectedItem().toString();
        hora=horaH+":"+horaM;
        d=et_duracion.getText().toString();
        if(d.length()==0){Toast.makeText(Modificar.this,"ESTABLECER TIEMPO",Toast.LENGTH_SHORT).show();}
        else{
            int dura=Integer.parseInt(d);
            if(dura==0){ Toast.makeText(Modificar.this,"TIEMPO NO VÁLIDO",Toast.LENGTH_SHORT).show();}
            else {
                if(dura<10){d="0"+d;}
                if(dia.equals("-------")){Toast.makeText(Modificar.this,"ESTABLECER DÍAS",Toast.LENGTH_SHORT).show();}
                else{
                    duracion=d;
                    String topic = topicStr;
                    JSONObject  subprograma_enviar= new JSONObject();
                    try {
                        subprograma_enviar.put("dia",dia);
                        subprograma_enviar.put("hora",hora);
                        subprograma_enviar.put("tiempo",duracion);
                        subprograma_enviar.put("zona",zona);
                        subprograma_enviar.put("bomba","0102");

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    String subprograma_enviar_str = subprograma_enviar.toString();

                    JSONObject programa_enviar = new JSONObject();
                    if(dato.equals("PROGRAMA 1")){
                        try{
                            programa_enviar.put("programa-01",subprograma_enviar_str);
                            programa_enviar.put("programa-02",programa2);
                            programa_enviar.put("programa-03",programa3);
                        }catch(JSONException e){e.printStackTrace();}

                    }else if(dato.equals("PROGRAMA 2")){
                        try{
                            programa_enviar.put("programa-01",programa1);
                            programa_enviar.put("programa-02",subprograma_enviar_str);
                            programa_enviar.put("programa-03",programa3);
                        }catch(JSONException e){e.printStackTrace();}

                    }else if(dato.equals("PROGRAMA 3")){
                        try{
                            programa_enviar.put("programa-01",programa1);
                            programa_enviar.put("programa-02",programa2);
                            programa_enviar.put("programa-03",subprograma_enviar_str);
                        }catch(JSONException e){e.printStackTrace();}
                    }
                    String programa_enviar_str=programa_enviar.toString();
                    try {
                        client.publish(topic,programa_enviar_str.getBytes(),0,false);
                    } catch (MqttException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(Modificar.this,"PROGRAMA MODIFICADO",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, MenuPrincipal.class);
                    startActivity(i);
                }


            }
        }


    }
    public void cancelar (View v){
        super.onBackPressed();
    }
}



