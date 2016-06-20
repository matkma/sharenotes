package com.example.kaczorov.sharenotesapp;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

/**
 * Created by kaczorov on 2016-06-20.
 */
public class DropboxClient {
    private static DropboxClient ourInstance = new DropboxClient();

    public static DropboxClient getInstance() {
        return ourInstance;
    }

    final static private String APP_KEY = "mhoy84bgzfk85zn";
    final static private String APP_SECRET = "y2z9b6g9sv6db7a";
    final static private String TOKEN = "hFypMrpR_xAAAAAAAAAAB_tIiWsQvGuQsk45mQAovhCZdpalYq6ZX9WIrw9JElO1";

    public String[] folderNames;
    public DropboxAPI<AndroidAuthSession> dbxApi;

    private DropboxClient() {
    }

    public void authenticate() {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dbxApi = new DropboxAPI<AndroidAuthSession>(session);
        dbxApi.getSession().setOAuth2AccessToken(TOKEN);
        if (session.authenticationSuccessful()){
            session.finishAuthentication();
        }
    }

    public boolean isLinked(){
        return dbxApi.getSession().isLinked();
    }

    public void getFolderNames(MainActivity activity) throws DropboxException {
        new GetFolderNamesTask(activity).execute();
    }
}
