package com.example.kaczorov.sharenotesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private final int POST_ACTIVITY = 0;
    private final int SEARCH_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DropboxClient.getInstance().authenticate();
        if (!isInternetAvailable()) {
            Toast.makeText(this, "Brak internetu", Toast.LENGTH_LONG).show();
        }
        startService(new Intent(this, BackgroundService.class));
    }

    protected void onResume() {
        super.onResume();
    }

    public void buttonSearchClick(View view) {
        if (!isInternetAvailable()) {
            Toast.makeText(this, "Brak internetu", Toast.LENGTH_LONG).show();
        } else {
            try {
                DropboxClient.getInstance().getFoldersFromDropbox(this, SEARCH_ACTIVITY);
                Toast.makeText(this, "Szukam notatek", Toast.LENGTH_LONG).show();
            } catch (DropboxException e) {
                e.printStackTrace();
                Toast.makeText(this, "Wystapił błąd", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void buttonPostClick(View view) {
        if (!isInternetAvailable()) {
            Toast.makeText(this, "Brak internetu", Toast.LENGTH_LONG).show();
        } else {
            try {
                DropboxClient.getInstance().getFoldersFromDropbox(this, POST_ACTIVITY);
            } catch (DropboxException e) {
                e.printStackTrace();
                Toast.makeText(this, "Wystąpił błąd", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void buttonOpenClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/ShareNotesApp/Downloaded");
        intent.setDataAndType(uri, "*/*");
        startActivity(intent);
    }

    Handler taskMessagesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.what) {
                case POST_ACTIVITY:
                    intent = new Intent(MainActivity.this, PostActivity.class);
                    intent.putExtra("Names", DropboxClient.getInstance().getFoldersMap().keySet().toArray(new String[0]));
                    MainActivity.this.startActivity(intent);
                    break;
                case SEARCH_ACTIVITY:
                    intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Names", DropboxClient.getInstance().getFoldersMap().keySet().toArray(new String[0]));
                    MainActivity.this.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    private boolean isInternetAvailable() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

