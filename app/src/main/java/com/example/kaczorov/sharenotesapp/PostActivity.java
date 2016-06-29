package com.example.kaczorov.sharenotesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends ListViewActivityBase {
    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        listView = (ListView) findViewById(R.id.listViewPost);
        textView = (EditText) findViewById(R.id.editText);
        setUpListAdapter();
        setUpListeners();
        loadList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            new SendPictureTask(PostActivity.this).execute(selectedItem);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void setUpListeners() {
        textView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (adapter.getPosition("/" + textView.getText().toString()) < 0) {
                        itemsNames.add("/" + textView.getText().toString());
                        loadList();
                    }
                    textView.setText("");
                    return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listView.getItemAtPosition(position);
                selectedItem = (String) o;
                takePicture();
            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            DropboxClient.getInstance().currentFileToSend = new File(Environment.getExternalStorageDirectory().toString() + "/ShareNotesApp/Sent",
                    new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()).toString() + ".jpg");
            DropboxClient.getInstance().currentFileToSend.delete();
            Uri mImageUri = Uri.fromFile(DropboxClient.getInstance().currentFileToSend);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
