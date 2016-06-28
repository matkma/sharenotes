package com.example.kaczorov.sharenotesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.exception.DropboxException;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private final int POST_ACTIVITY = 0;
    private final int SEARCH_ACTIVITY = 1;

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
        startService(new Intent(this, BackgroundService.class));
        try {
            DropboxClient.getInstance().getFoldersFromDropbox(this, SEARCH_ACTIVITY);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    public void buttonLinkClick(View view){
        DropboxClient.getInstance().authenticate();
        enableLinkButton(!DropboxClient.getInstance().isLinked());
    }

    public void buttonPostClick(View view){
        try {
            DropboxClient.getInstance().getFoldersFromDropbox(this, POST_ACTIVITY);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    public void buttonOpenClick(View view){
        //TODO trzeba poprawić ścieżkę (?)
        String path = Environment.getExternalStorageDirectory().toString() + "/ShareNotesApp/Downloaded";
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "*/*");
        startActivity(Intent.createChooser(intent, "Twoje notatki"));
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

}

