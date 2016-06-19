package com.example.kaczorov.sharenotesapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DropboxClient.getInstance().authenticate(this);
        enableLinkButton(!DropboxClient.getInstance().isLinked());
    }

    protected void onResume() {
        super.onResume();
        DropboxClient.getInstance().finishAuthentication();
        enableLinkButton(!DropboxClient.getInstance().isLinked());
    }

    public void buttonSearchClick(View view){
        this.onResume();
    }

    public void buttonLinkClick(View view){
        DropboxClient.getInstance().authenticate(this);
        enableLinkButton(!DropboxClient.getInstance().isLinked());
    }

    public void buttonPostClick(View view){
        Intent intent = new Intent(this, PostActivity.class);
        try {
            String[] names = DropboxClient.getInstance().GetFolderNames();
            intent.putExtra("Folder names", names);
        } catch (DropboxException e) {
            //...
        }
        this.startActivity(intent);
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
}
