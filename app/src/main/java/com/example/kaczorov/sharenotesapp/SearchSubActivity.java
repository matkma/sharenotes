package com.example.kaczorov.sharenotesapp;

import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class SearchSubActivity extends ListViewActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sub);
        listView = (ListView) findViewById(R.id.itemsListView);
        setUpListAdapter();
        setUpListeners();
        loadList();
    }

    @Override
    void setUpListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listView.getItemAtPosition(position);
                selectedItem = (String) o;
                String dropboxPath = DropboxClient.getInstance().currentFolder;
                CreateFile(dropboxPath);
                new DownloadTask(SearchSubActivity.this).execute(dropboxPath + "/" + selectedItem);
            }
        });
    }

    private void CreateFile(String dropboxPath) {
        String systemPath = Environment.getExternalStorageDirectory().toString() + "/ShareNotesApp";
        File subdir = new File(systemPath, "Downloaded");
        subdir.mkdirs();
        File dir = new File(subdir, dropboxPath);
        dir.mkdirs();
        DropboxClient.getInstance().currentFileToDownloadTo = new File(dir, selectedItem);
        try {
            DropboxClient.getInstance().currentFileToDownloadTo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
