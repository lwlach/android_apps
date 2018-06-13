package com.example.leowlach.alertasti;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.net.Socket;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class myService extends Service{

    private String address;
    private int port;
    private String str;
    private int id = 0;
    private Socket client;

    public static final String ACTION = "com.example.androidintentservice.RESPONSE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            address = intent.getStringExtra("ip");
            port = intent.getIntExtra("port", 101);
        }
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
        pool.schedule(new SocketConnection(), 0, TimeUnit.SECONDS);
        return START_STICKY;
    }

    public class SocketConnection implements Runnable {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            try {
                client = new Socket(address, port);
                Log.i("MYLOG", "NEW CLIENT CREATED");
                byte[] buffer = new byte[256];
                while (client.getInputStream().read(buffer) != -1) {
                    str = new String(buffer, "UTF-8");
                    buffer = new byte[256];

                    //send result back to activity
                    Intent intentResponse = new Intent();
                    intentResponse.setAction(ACTION);
                    intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
                    intentResponse.putExtra("alerta", str);
                    sendBroadcast(intentResponse);

                    if(!MainActivity.active)
                        sendNotification();
                }
            }
            catch (Exception e){
                str = ("Sem conexão com o servidor");
                //send result back to activity

                Intent intentResponse = new Intent();
                intentResponse.setAction(ACTION);
                intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
                Log.d("CATCH", str);
                intentResponse.putExtra("alerta", str);
                sendBroadcast(intentResponse);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(){
        Notification my_not = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alerta TI!")
                .setContentText(str).build();

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(id, my_not);
        id++;
    }

}
