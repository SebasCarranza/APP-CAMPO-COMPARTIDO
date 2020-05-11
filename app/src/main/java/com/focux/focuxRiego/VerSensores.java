package com.focux.focuxRiego;
//#202732
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VerSensores extends AppCompatActivity {
    static String MQTTHOST = "tcp://h8nzkq.messaging.internetofthings.ibmcloud.com:1883";
    static String USERNAME = "a-h8nzkq-w7ifvycf1a";
    static String PASSWORD = "ymW1NboBDR@a7VVpZP";
    static String clientId = "a:h8nzkq:APP-ANDROID";
    String topicStr = "iot-2/type/PNUD/id/device-nodered/evt/sensor/fmt/json";
    MqttAndroidClient client;
    String p0="",p1="",p2="",p3="",p4="",p5="",p6="",p7="",p8="",p9="",texto="";
    int indice=0;
    float [] v;
    float v0=0,v1=0,v2=0,v3=0,v4=0,v5=0,v6=0,v7=0,v8=0,v9=0;
    long t0=0,t1=0,t2=0,t3=0,t4=0,t5=0,t6=0,t7=0,t8=0,t9=0;


    SimpleDateFormat sdf =new SimpleDateFormat("mm:ss");

    LineChart grafica_sensor1;
    public TextView tv_ver;
    private ImageView i_carga;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_sensores);

        tv_ver=(TextView)findViewById(R.id.tv_ver);
        grafica_sensor1=(LineChart)findViewById(R.id.grafica_s1);
        i_carga=(ImageView)findViewById(R.id.i_carga);

        client = new MqttAndroidClient(this.getApplicationContext(),MQTTHOST,clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());


        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(VerSensores.this,"CONEXIÓN ESTABLECIDA",Toast.LENGTH_SHORT).show();
                    setSubscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(VerSensores.this,"ERROR DE CONEXIÓN",Toast.LENGTH_LONG).show();

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
                        texto=new String(message.getPayload());
                        i_carga.setVisibility(View.INVISIBLE);


                        //tv_ver.setText(texto);

                        try{JSONArray lista=new JSONArray(texto);

                            indice=20;
                            String [] p=new String[indice];
                            JSONObject [] object =new JSONObject[indice];
                            v= new float[indice];
                            long [] t=new long[indice];
                                for(int i=0;i<indice;i++)
                                {
                                   p[i]=lista.getString(i);
                                   object[i]=new JSONObject(p[i]);
                                   v[i]=new Float(object[i].getString("VALOR"));
                                   t[i]=Long.parseLong(object[i].getString("TIEMPO"));
                                }
                            /*p0=lista.getString(0);
                            p1=lista.getString(1);
                            p2=lista.getString(2);
                            p3=lista.getString(3);
                            p4=lista.getString(4);
                            p5=lista.getString(5);
                            p6=lista.getString(6);
                            p7=lista.getString(7);
                            p8=lista.getString(8);
                            p9=lista.getString(9);

                            JSONObject object0=new JSONObject(p0);
                            JSONObject object1=new JSONObject(p1);
                            JSONObject object2=new JSONObject(p2);
                            JSONObject object3=new JSONObject(p3);
                            JSONObject object4=new JSONObject(p4);
                            JSONObject object5=new JSONObject(p5);
                            JSONObject object6=new JSONObject(p6);
                            JSONObject object7=new JSONObject(p7);
                            JSONObject object8=new JSONObject(p8);
                            JSONObject object9=new JSONObject(p9);
                            v0=new Float(object0.getString("VALOR"));
                            v1=new Float(object1.getString("VALOR"));
                            v2=new Float(object2.getString("VALOR"));
                            v3=new Float(object3.getString("VALOR"));
                            v4=new Float(object4.getString("VALOR"));
                            v5=new Float(object5.getString("VALOR"));
                            v6=new Float(object6.getString("VALOR"));
                            v7=new Float(object7.getString("VALOR"));
                            v8=new Float(object8.getString("VALOR"));
                            v9=new Float(object9.getString("VALOR"));
                            t0=Long.parseLong(object0.getString("TIEMPO"));
                            t1=Long.parseLong(object1.getString("TIEMPO"));
                            t2=Long.parseLong(object2.getString("TIEMPO"));
                            t3=Long.parseLong(object3.getString("TIEMPO"));
                            t4=Long.parseLong(object4.getString("TIEMPO"));
                            t5=Long.parseLong(object5.getString("TIEMPO"));
                            t6=Long.parseLong(object6.getString("TIEMPO"));
                            t7=Long.parseLong(object7.getString("TIEMPO"));
                            t8=Long.parseLong(object8.getString("TIEMPO"));
                            t9=Long.parseLong(object9.getString("TIEMPO"));*/

                            ///////////////////////////
                            //String var1=String.valueOf(v0);
                            //String var2=""+t0;
                           // tv_ver.setText("valor: "+var1+", tiempo: "+var2);

                            XAxis xAxis=grafica_sensor1.getXAxis();
                            YAxis yAxisLeft=grafica_sensor1.getAxisLeft();
                            YAxis yAxisRight=grafica_sensor1.getAxisRight();
                            xAxis.setValueFormatter(new MyAxisValueFormatter());

                            LineDataSet lineDataSet1 = new LineDataSet(dataValues1(),"SENSOR 1");
                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(lineDataSet1);
                            LineData data=new LineData(dataSets);
                            lineDataSet1.setValueTextSize(5);
                            //lineDataSet1.setLineWidth(4);
                            //lineDataSet1.enableDashedLine(5,10,0);

                            Description description=new Description();
                            description.setText("CAUDALÍMETROS");
                            description.setTextColor(Color.BLUE);
                            description.setEnabled(false);

                            grafica_sensor1.setDescription(description);
                            lineDataSet1.setValueFormatter(new MyValueFormatter());
                            grafica_sensor1.setData(data);
                            grafica_sensor1.invalidate();
                            /////////////////////////////

                            //tv_ver.setText(lista.toString());

                        }
                        catch (JSONException e){}

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            }
        },500);





        //LineDataSet lineDataSet1 = new LineDataSet(dataValues1(),"Data Set 1");
/*
        XAxis xAxis=grafica_sensor1.getXAxis();
        YAxis yAxisLeft=grafica_sensor1.getAxisLeft();
        YAxis yAxisRight=grafica_sensor1.getAxisRight();
        xAxis.setValueFormatter(new MyAxisValueFormatter());

        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(),"SENSOR 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        LineData data=new LineData(dataSets);
        lineDataSet1.setLineWidth(4);
        lineDataSet1.enableDashedLine(5,10,0);

        Description description=new Description();
        description.setText("CAUDALÍMETROS");
        description.setTextColor(Color.BLUE);
        description.setEnabled(false);

        grafica_sensor1.setDescription(description);
        lineDataSet1.setValueFormatter(new MyValueFormatter());
        grafica_sensor1.setData(data);
        grafica_sensor1.invalidate();
*/
    }
    private ArrayList<Entry> dataValues1(){
        ArrayList<Entry> dataVals=new ArrayList<Entry>();

        for(int j=0;j<indice;j++)
        {
            dataVals.add(new Entry(j,v[indice-1-j]));
        }

        /*dataVals.add(new Entry(0,v9));
        dataVals.add(new Entry(1,v8));
        dataVals.add(new Entry(2,v7));
        dataVals.add(new Entry(3,v6));
        dataVals.add(new Entry(4,v5));
        dataVals.add(new Entry(5,v4));
        dataVals.add(new Entry(6,v3));
        dataVals.add(new Entry(7,v2));
        dataVals.add(new Entry(8,v1));
        dataVals.add(new Entry(9,v0));*/

        return dataVals;
    }

    private class MyValueFormatter  implements IValueFormatter{

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value + "L/min";
        }
    }

    private class MyAxisValueFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            Calendar calendar=Calendar.getInstance();
            //long x=calendar.getTimeInMillis();
            long milis=0;
            int indice=Math.round(value);
          // tv_ver.setText(indice);

           // String currentDate= DateFormat.getDateInstance().format(calendar.getTime());
           // String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
           axis.setLabelCount(5, true);
            try{
                JSONArray dat=new JSONArray(texto);
                String info=dat.getString(9-indice);
                JSONObject objeto=new JSONObject(info);
                milis=Long.parseLong(objeto.getString("TIEMPO"));

            }catch (JSONException e){}

            calendar.setTimeInMillis(milis);

           //String currentDate= DateFormat.getDateInstance(DateFormat.TIMEZONE_FIELD).format(milis);
            String timeString=new SimpleDateFormat("EHH:mm:ss").format(calendar.getTime());
            return timeString;
            //return timeString;
            //return DateFormat.getDateInstance().format(value) ;

        }
    }


        private void setSubscription(){
        try{
            client.subscribe(topicStr,0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
