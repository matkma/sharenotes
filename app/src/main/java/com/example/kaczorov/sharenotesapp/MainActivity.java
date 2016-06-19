package com.example.kaczorov.sharenotesapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DropboxClient.getInstance().Authenticate();
        enableLinkButton(!DropboxClient.getInstance().isLinked);
    }

    protected void onResume() {
        super.onResume();
        enableLinkButton(!DropboxClient.getInstance().isLinked);
    }

    public void buttonSearchClick(View view){

    }

    public void buttonLinkClick(View view){
        DropboxClient.getInstance().Authenticate();
        enableLinkButton(!DropboxClient.getInstance().isLinked);
    }

    public void buttonPostClick(View view){

    }

    private void enableLinkButton(boolean value)
    {
        Button button = (Button)findViewById(R.id.buttonLink);
        button.setEnabled(value);
        if (value){
            button.setText("Połącz");
            button.setBackgroundColor(Color.BLUE);
        }else{
            button.setText("Połączono");
            button.setBackgroundColor(Color.RED);
        }
    }
}
