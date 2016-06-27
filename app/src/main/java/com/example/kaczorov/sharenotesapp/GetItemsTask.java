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
    private SearchActivity activity;

    public GetItemsTask(SearchActivity act) {
        activity = act;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if (!DropboxClient.getInstance().dbxApi.getSession().isLinked()) return -1;
        String path = (String)params[0];
        int val = (int)params[1];
        DropboxAPI.Entry entry = null;
        ArrayList<String> dir = new ArrayList<String>();
        try {
            entry = DropboxClient.getInstance().dbxApi.metadata(path, 1000, null, true, null);
            for (DropboxAPI.Entry ent : entry.contents)
            {
                dir.add(ent.fileName());
            }
            String[] names = dir.toArray(new String[dir.size()]);
            DropboxClient.getInstance().itemsNames = names;
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
