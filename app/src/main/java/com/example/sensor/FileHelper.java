package com.example.sensor;

/**
 * Created by Tony on 2017/12/16.
 */

import android.content.Context;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileHelper {
    public static final int MODE_LIGHT=0;
    public static final int MODE_ACCELEROMETER=1;
    public static final int MODE_MAGNETIC=2;
    public static final int MODE_GYROSCOPE=3;
    private Context mContext;
    private BufferedWriter lightWriter;
    private BufferedWriter accelerometerWriter;
    private BufferedWriter magneticWriter;
    private BufferedWriter gyroWriter;
    //空参数构造函数，传入的值为空时，不出错
    public FileHelper() {
    }

    public FileHelper(Context mContext,String name) throws Exception{
        super();
        FileOutputStream lightOutput = mContext.openFileOutput("Light"+name, Context.MODE_PRIVATE);
        FileOutputStream gyroOutput = mContext.openFileOutput("Gyroscope"+name, Context.MODE_PRIVATE);
        FileOutputStream magneticOutput = mContext.openFileOutput("Magnetic"+name, Context.MODE_PRIVATE);
        FileOutputStream accelerometerOutput = mContext.openFileOutput("Accelerometer"+name, Context.MODE_PRIVATE);
        lightWriter=new BufferedWriter(new OutputStreamWriter(lightOutput));
        accelerometerWriter=new BufferedWriter(new OutputStreamWriter(accelerometerOutput));
        magneticWriter=new BufferedWriter(new OutputStreamWriter(magneticOutput));
        gyroWriter=new BufferedWriter(new OutputStreamWriter(gyroOutput));
        this.mContext = mContext;
    }

    /*
    * 定义文件保存的方法，写入到文件中，所以是输出流
    * */
    public void save(String content,final int mode) throws Exception {
        //Context.MODE_PRIVATE权限，只有自身程序才能访问，而且写入的内容会覆盖文本内原有内容
        switch (mode){
            case MODE_LIGHT: {
                lightWriter.write(content);
                break;
            }
            case MODE_ACCELEROMETER: {
                accelerometerWriter.write(content);
                break;
            }
            case MODE_MAGNETIC: {
                magneticWriter.write(content);
                break;
            }
            case MODE_GYROSCOPE: {
                gyroWriter.write(content);
                break;
            }
        }
        //将String字符串以字节流的形式写入到输出流
    }
    public void close() throws Exception{
        lightWriter.close();//关闭输出流
        gyroWriter.close();
        accelerometerWriter.close();
        magneticWriter.close();
    }
    /*
    * 定义文件读取的方法
    * */
    public String read(String filename) throws IOException {
        //打开文件输入流
        FileInputStream input = mContext.openFileInput(filename);
        //定义1M的缓冲区
        byte[] temp = new byte[1024];
        //定义字符串变量
        StringBuilder sb = new StringBuilder("");
        int len = 0;
        //读取文件内容，当文件内容长度大于0时，
        while ((len = input.read(temp)) > 0) {
            //把字条串连接到尾部
            sb.append(new String(temp, 0, len));
        }
        //关闭输入流
        input.close();
        //返回字符串
        return sb.toString();
    }
}
