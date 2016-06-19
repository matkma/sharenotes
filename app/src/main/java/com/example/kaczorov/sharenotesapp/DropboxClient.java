package com.example.kaczorov.sharenotesapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.DropboxAPI.Entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by kaczorov on 2016-06-19.
 */
public class DropboxClient {
    private static DropboxClient ourInstance = new DropboxClient();

    public static DropboxClient getInstance() {
        return ourInstance;
    }

    private boolean isLinked;

    final static private String MAIL = "appsharenotes@gmail.com";
    final static private String PASSWORD = "apppassword";
    final static private String APP_KEY = "mhoy84bgzfk85zn";
    final static private String APP_SECRET = "y2z9b6g9sv6db7a";
    final static private String TOKEN = "hFypMrpR_xAAAAAAAAAAB_tIiWsQvGuQsk45mQAovhCZdpalYq6ZX9WIrw9JElO1";
    private DropboxAPI<AndroidAuthSession> dbxApi;

    private DropboxClient() {
        isLinked = false;
    }

    public void authenticate(AppCompatActivity activity) {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dbxApi = new DropboxAPI<AndroidAuthSession>(session);
        dbxApi.getSession().startOAuth2Authentication(activity);
        //dbxApi.getSession().setOAuth2AccessToken(TOKEN);
       // if (session.authenticationSuccessful()){
       //     session.finishAuthentication();
       // }
    }

    public void finishAuthentication(){
        if (dbxApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                dbxApi.getSession().finishAuthentication();

                String accessToken = dbxApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                //...
            }
        }
    }

    public boolean isLinked(){
        return dbxApi.getSession().isLinked();
    }

    public boolean UploadFile(Bitmap bitmap, String directory) throws FileNotFoundException {
        //File inputFile = new File()
        //FileInputStream stream = new FileInputStream(inputFile);
        //dbxApi.putFile(inputFile.getName())
        return true;
    }

    public String[] GetFolderNames() throws DropboxException {
        if (!dbxApi.getSession().isLinked()) return new String[0];
        Entry entry = dbxApi.metadata("/", 1000, null, true, null);
        ArrayList<String> dir = new ArrayList<String>();
        for (Entry ent : entry.contents)
        {
            dir.add(new String(ent.path));
        }
        String[] names = dir.toArray(new String[dir.size()]);
        return names;
    }
}
