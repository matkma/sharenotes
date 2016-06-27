package com.example.kaczorov.sharenotesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kaczorov on 2016-06-20.
 */
public abstract class ListViewActivityBase extends AppCompatActivity {
    String selectedItem = null;
    ListView listView = null;
    EditText textView = null;
    ArrayList<String> itemsNames = new ArrayList<String>();
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter = null;

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    protected void loadList() {
        Collections.sort(itemsNames);
        adapter.clear();
        adapter.addAll(itemsNames);
        adapter.notifyDataSetChanged();
    }

    protected void setUpListAdapter() {
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String[] names = (String[])extras.get("Names");
            for(String name : names){
                itemsNames.add(name);
            }
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
    }

    abstract void setUpListeners();
}
