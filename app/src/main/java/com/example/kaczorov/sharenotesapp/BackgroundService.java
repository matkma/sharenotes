package com.example.kaczorov.sharenotesapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.*;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private NotificationManager myNotificationManager;
    private int notificationId = 111;
    int counter = 0;
    private  Map <String, String> previousMap;
    private Map <String, String> currentMap;
    private Long currentTime;
    private Long previousTime;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        currentTime = new Date().getTime();
        previousTime = new Date().getTime();
        DropboxClient.getInstance().authenticate();
        try {
            DropboxClient.getInstance().getFoldersFromDropbox(null, 0);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if(counter >= 60000){
                    counter = 0;
                    currentMap = DropboxClient.getInstance().getFoldersMap();
                    try {
                        DropboxClient.getInstance().getFoldersFromDropbox(null, 0);
                    } catch (DropboxException e) {
                        e.printStackTrace();
                    }

                    List<String> notifications = new ArrayList<String>();

                    if (previousMap != null){
                        for (Map.Entry<String,String> entry : currentMap.entrySet()){
                            boolean keyPresent = previousMap.containsKey(entry.getKey());
                            if (!keyPresent){
                                notifications.add(entry.getKey());
                            }
                        }
                    }

                    if (notifications.size() != 0){
                        displayNotification(notifications);
                    }
                    previousMap = currentMap;
                } else {
                    currentTime = new Date().getTime();
                    long deltaTime = currentTime - previousTime;
                    counter += deltaTime;
                    previousTime = currentTime;
                }
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {}

    @Override
    public void onStart(Intent intent, int startid) {}

    protected void displayNotification(List<String> notifications) {
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Nowe notatki!");
        String messageText = "";

        for (String key : notifications){
            messageText += key;
        }

        mBuilder.setContentText(messageText);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(notificationId, mBuilder.build());
    }
}