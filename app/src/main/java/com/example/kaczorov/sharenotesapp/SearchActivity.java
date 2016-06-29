package com.example.kaczorov.sharenotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SearchActivity extends ListViewActivityBase {
    private final int SEARCH_SUB_ACTIVITY = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = (ListView) findViewById(R.id.searchListView);
        setUpListAdapter();
        setUpListeners();
        loadList();
    }

    @Override
    public void setUpListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listView.getItemAtPosition(position);
                selectedItem = (String) o;
                DropboxClient.getInstance().getItems(SearchActivity.this, SEARCH_SUB_ACTIVITY, selectedItem);
            }
        });
    }

    Handler taskMessagesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.what) {
                case SEARCH_SUB_ACTIVITY:
                    intent = new Intent(SearchActivity.this, SearchSubActivity.class);
                    intent.putExtra("Names", DropboxClient.getInstance().itemsNames);
                    SearchActivity.this.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
