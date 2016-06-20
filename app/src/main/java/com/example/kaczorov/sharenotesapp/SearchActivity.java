package com.example.kaczorov.sharenotesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends ListViewActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = (ListView)findViewById(R.id.listViewSearch);
        textView = (EditText)findViewById(R.id.requestTextField);
        setUpListAdapter();
        setUpListeners();
        loadList();
    }

    @Override
    public void setUpListeners() {

    }
}
