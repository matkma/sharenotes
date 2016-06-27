package com.example.kaczorov.sharenotesapp;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends ListViewActivityBase {
    private final int SEARCH_SUB_ACTIVITY = 99;

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
                selectedItem += "/";
                DropboxClient.getInstance().getItemsFromFolder(SearchActivity.this, SEARCH_SUB_ACTIVITY, selectedItem);
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
