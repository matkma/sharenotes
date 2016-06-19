package com.example.kaczorov.sharenotesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    ListView listView;
    String[] folderNames;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            folderNames = (String[]) extras.get("Folder names");
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView = (ListView)findViewById(R.id.listViewPost);
        listView.setAdapter(adapter);
        loadList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {
        adapter.clear();
        adapter.addAll(folderNames);
        adapter.add("Dodaj nowy przedmiot...");
        adapter.notifyDataSetChanged();
    }
}
