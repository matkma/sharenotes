package com.example.kaczorov.sharenotesapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    private Uri mImageUri;
    private String selectedFolder;
    private ListView listView;
    private EditText textView;
    private ArrayList<String> folderNames = new ArrayList<String>();
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        listView = (ListView)findViewById(R.id.listViewPost);
        textView = (EditText)findViewById(R.id.editText);
        setUpListAdapter();
        setUpListeners();
        loadList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode==RESULT_OK) {
            new SendPictureTask().execute(selectedFolder);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void loadList() {
        Collections.sort(folderNames);
        adapter.clear();
        adapter.addAll(folderNames);
        adapter.notifyDataSetChanged();
    }

    private void setUpListAdapter() {
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String[] names = (String[])extras.get("Folder names");
            for(String name : names){
                folderNames.add(name);
            }
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
    }

    private void setUpListeners() {
        textView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (adapter.getPosition("/" + textView.getText().toString()) < 0) {
                        folderNames.add("/" + textView.getText().toString());
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
                selectedFolder = (String) o;
                takePicture();
            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            DropboxClient.getInstance().currentFileToSend = new File(Environment.getExternalStorageDirectory().toString() + "/ShareNotesApp/Sent",
                                                                    new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()).toString()  + ".jpg");
            DropboxClient.getInstance().currentFileToSend.delete();
            mImageUri = Uri.fromFile(DropboxClient.getInstance().currentFileToSend);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
