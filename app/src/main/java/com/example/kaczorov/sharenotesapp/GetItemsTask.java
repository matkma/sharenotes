package com.example.kaczorov.sharenotesapp;

import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaczorov on 2016-06-27.
 */
public class GetItemsTask extends AsyncTask {
    private MainActivity activity;

    public GetItemsTask(MainActivity act) {
        activity = act;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if (!DropboxClient.getInstance().dbxApi.getSession().isLinked()) return -1;
        String val = (String)params[0];
        DropboxAPI.Entry entry = null;
        try {
            entry = DropboxClient.getInstance().dbxApi.metadata("/", 1000, null, true, null);
            HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
            for (DropboxAPI.Entry ent : entry.contents)
            {
                map.put(new String(ent.path), null);
            }
            for (String path : map.keySet()){
                entry = DropboxClient.getInstance().dbxApi.metadata(path, 1000, null, true, null);
                ArrayList<String> items = new ArrayList<String>();
                for (DropboxAPI.Entry ent : entry.contents)
                {
                    items.add(new String(ent.fileName()));
                }
                map.put(path, items);
            }
            DropboxClient.getInstance().itemsMap = map;
            return val;
        } catch (DropboxException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        activity.taskMessagesHandler.sendEmptyMessage((int)result);
    }
}
