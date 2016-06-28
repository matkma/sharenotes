package com.example.kaczorov.sharenotesapp;

import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.HashMap;
import java.util.Map;

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

            Map<String, String> values = new HashMap<>();
            for (DropboxAPI.Entry ent : entry.contents) {
                values.put(new String(ent.path), ent.size);
            }

            DropboxClient.getInstance().setFoldersMap(values);

            return val;
        } catch (DropboxException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (activity != null)
            activity.taskMessagesHandler.sendEmptyMessage((int)result);
    }
}
