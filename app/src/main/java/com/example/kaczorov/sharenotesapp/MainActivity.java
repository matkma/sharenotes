package com.example.kaczorov.sharenotesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DropboxClient.getInstance().authenticate();
        enableLinkButton(!DropboxClient.getInstance().isLinked());
    }

    protected void onResume() {
        super.onResume();
        enableLinkButton(!DropboxClient.getInstance().isLinked());
    }

    public void buttonSearchClick(View view){
        this.onResume();
    }

    public void buttonLinkClick(View view){
        DropboxClient.getInstance().authenticate();
        enableLinkButton(!DropboxClient.getInstance().isLinked());
    }

    public void buttonPostClick(View view){
        try {
            DropboxClient.getInstance().getFolderNames(this);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    private void enableLinkButton(boolean value)
    {
        Button button = (Button)findViewById(R.id.buttonLink);
        button.setEnabled(value);
        if (value){
            button.setText("Połącz");
        } else{
            button.setText("Połączono");
        }
    }

    Handler taskMessagesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent(MainActivity.this, PostActivity.class);
                    intent.putExtra("Folder names", DropboxClient.getInstance().folderNames);
                    MainActivity.this.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}

