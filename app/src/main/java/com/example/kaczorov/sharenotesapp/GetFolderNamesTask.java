package com.example.kaczorov.sharenotesapp;

import android.app.Activity;
import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;

/**
 * Created by kaczorov on 2016-06-20.
 */
public class GetFolderNamesTask extends AsyncTask {
    private MainActivity activity;

    public GetFolderNamesTask(MainActivity act) {
            activity = act;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if (!DropboxClient.getInstance().dbxApi.getSession().isLinked()) return -1;
        int val = (int)params[0];
        DropboxAPI.Entry entry = null;
        try {
            entry = DropboxClient.getInstance().dbxApi.metadata("/", 1000, null, true, null);
            ArrayList<String> dir = new ArrayList<String>();
            for (DropboxAPI.Entry ent : entry.contents)
            {
                dir.add(new String(ent.path));
            }
            String[] names = dir.toArray(new String[dir.size()]);
            DropboxClient.getInstance().folderNames = names;
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
