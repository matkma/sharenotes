package com.example.kaczorov.sharenotesapp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;

public class SearchSubActivity extends ListViewActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sub);
        listView = (ListView)findViewById(R.id.itemsListView);
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
                String path = DropboxClient.getInstance().currentFolder;
                DropboxClient.getInstance().currentFileToDownloadTo = new File(Environment.getExternalStorageDirectory().toString()
                                                                            + "/ShareNotesApp/Downloaded" + path, selectedItem);
                //DropboxClient.getInstance().currentFileToDownloadTo.delete();
                new DownloadTask(SearchSubActivity.this).execute(path + "/" + selectedItem);
            }
        });
    }
}
