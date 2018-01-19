package com.example.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sensor.FileHelper;
import static java.lang.System.currentTimeMillis;

/**
 * Created by SHIYONG on 2017/11/3.
 */

public class MainActivity extends Activity {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor accelerateSensor;
    private Sensor magneticSensor;
    private Sensor gyroSensor;

    private TextView lightLevel;
    private TextView gyroLevel;
    private TextView accelerateLevel;
    private TextView magneticLevel;

    private long lastTime;
    private int index;
    private Button switchBtn;
    private Button fileBtn;
    private boolean isOpen;
    private FileHelper fHelper;
    private boolean isFileOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        index=0;
        isOpen=false;
        isFileOpen=false;
        initView();
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        magneticSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerateSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(new MySensorEventlistener(),gyroSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new MySensorEventlistener(),lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new MySensorEventlistener(),accelerateSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new MySensorEventlistener(),magneticSensor,SensorManager.SENSOR_DELAY_NORMAL);

        try {
            fHelper = new FileHelper(this,"Data.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            fHelper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(sensorManager!=null){
            sensorManager.unregisterListener(new MySensorEventlistener());
        }
    }
    private void initView(){
        switchBtn=(Button)findViewById(R.id.switchBtn);
        fileBtn=(Button)findViewById(R.id.switchFile);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    switchBtn.setText("打开传感器");
                    isOpen=false;
                }else {
                    switchBtn.setText("关闭传感器");
                    isOpen=true;
                }
            }
        });
        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFileOpen){
                    fileBtn.setText("打开文件读写");
                    isFileOpen=false;
                }else{
                    fileBtn.setText("关闭文件读写");
                    isFileOpen=true;
                }
            }
        });
        lightLevel=(TextView)findViewById(R.id.light_result);
        accelerateLevel=(TextView)findViewById(R.id.accelerate_result);
        magneticLevel=(TextView)findViewById(R.id.magnetic_result);
        gyroLevel=(TextView)findViewById(R.id.gyroscope_result);
    }
    private class MySensorEventlistener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(!isOpen) return;
            float []values;
            switch (sensorEvent.sensor.getType()){
                case Sensor.TYPE_LIGHT: {
                    values = sensorEvent.values;
                    lightLevel.setText("光传感器的值：" + values[0] );
                    if(isFileOpen){
                        try {
                            fHelper.save("Light：" + values[0]+"\r\n",FileHelper.MODE_LIGHT);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case Sensor.TYPE_ACCELEROMETER:{
                    /*long time=System.currentTimeMillis();
                    long deltaTime=time-lastTime;
                    if(index==2){
                        Toast.makeText(MainActivity.this,""+deltaTime,Toast.LENGTH_SHORT).show();
                    }
                    lastTime=time;
                    index++;*/
                    values = sensorEvent.values;
                    accelerateLevel.setText("X轴加速度传感器的值：" + values[0] + "\n" +
                            "Y加速度传感器的值：" + values[1] + "\nZ轴加速度传感器的值：" + values[2]);
                    if(isFileOpen){
                        try {
                            fHelper.save("X：" + values[0] +
                                    " Y：" + values[1] + " Z：" + values[2]+"\r\n",FileHelper.MODE_ACCELEROMETER);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case Sensor.TYPE_MAGNETIC_FIELD:{
                    values = sensorEvent.values;
                    magneticLevel.setText("X轴地磁传感器的值：" + values[0] + "\n" +
                            "Y地磁传感器的值：" + values[1] + "\nZ轴地磁传感器的值：" + values[2]);
                    if(isFileOpen){
                        try {
                            fHelper.save("X：" + values[0] +
                                    " Y：" + values[1] + " Z：" + values[2]+"\r\n",FileHelper.MODE_MAGNETIC);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case Sensor.TYPE_GYROSCOPE:{
                    values = sensorEvent.values;
                    gyroLevel.setText("X轴陀螺仪的值：" + values[0] + "\n" +
                            "Y陀螺仪的值：" + values[1] + "\nZ轴陀螺仪的值：" + values[2]);
                    if(isFileOpen){
                        try {
                            fHelper.save("X：" + values[0] +
                                    " Y：" + values[1] + " Z：" + values[2]+"\r\n",FileHelper.MODE_GYROSCOPE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
