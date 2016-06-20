package com.example.kaczorov.sharenotesapp;

import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kaczorov on 2016-06-20.
 */
public class SendPictureTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        if (!DropboxClient.getInstance().dbxApi.getSession().isLinked()) return false;
        String folderName = (String)params[0];
        try {
            FileInputStream inputStream = new FileInputStream(DropboxClient.getInstance().currentFileToSend);
            createDirIfNotExist(folderName);
            DropboxClient.getInstance().dbxApi.putFile(folderName + "/"
                            + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()).toString() + ".jpg", inputStream,
                            DropboxClient.getInstance().currentFileToSend.length(), null, null);
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
        if ((boolean)result){
            DropboxClient.getInstance().currentFileToSend.delete();
        }
    }

    private void createDirIfNotExist(String folderName) throws DropboxException {
        boolean existFlag = false;
        DropboxAPI.Entry entry = DropboxClient.getInstance().dbxApi.metadata("/", 1000, null, true, null);
        for (DropboxAPI.Entry ent : entry.contents) {
            if (ent.path.trim().equals(folderName.trim())){
                existFlag = true;
                break;
            }
        }
        if (!existFlag){
            DropboxClient.getInstance().dbxApi.createFolder(folderName);
        }
    }
}
