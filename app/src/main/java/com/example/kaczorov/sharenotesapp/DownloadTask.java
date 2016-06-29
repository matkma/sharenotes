package com.example.kaczorov.sharenotesapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DownloadTask extends AsyncTask {
    private Context context;

    public DownloadTask(Context c) {
        context = c;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if (!DropboxClient.getInstance().dbxApi.getSession().isLinked()) return false;
        String fullPath = (String) params[0];
        File file = DropboxClient.getInstance().currentFileToDownloadTo;
        try {
            FileOutputStream outputStream = new FileOutputStream(DropboxClient.getInstance().currentFileToDownloadTo);
            DropboxClient.getInstance().dbxApi.getFile(fullPath, null, outputStream, null);
        } catch (DropboxException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Object result) {
        if ((boolean) result) {
            Toast.makeText(context, "Pobrano pomyślnie", Toast.LENGTH_SHORT).show();
        }
    }
}
