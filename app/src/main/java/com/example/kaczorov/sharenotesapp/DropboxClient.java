package com.example.kaczorov.sharenotesapp;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

/**
 * Created by kaczorov on 2016-06-19.
 */
public class DropboxClient {
    private static DropboxClient ourInstance = new DropboxClient();

    public static DropboxClient getInstance() {
        return ourInstance;
    }

    public boolean isLinked;

    final static private String MAIL = "appsharenotes@gmail.com";
    final static private String PASSWORD = "apppassword";
    final static private String APP_KEY = "mhoy84bgzfk85zn";
    final static private String APP_SECRET = "y2z9b6g9sv6db7a";
    final static private String TOKEN = "hFypMrpR_xAAAAAAAAAAB_tIiWsQvGuQsk45mQAovhCZdpalYq6ZX9WIrw9JElO1";
    private DropboxAPI<AndroidAuthSession> dbxApi;

    private DropboxClient() {
        isLinked = false;
    }

    public void Authenticate() {

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dbxApi = new DropboxAPI<AndroidAuthSession>(session);
        dbxApi.getSession().setOAuth2AccessToken(TOKEN);
        if (session.authenticationSuccessful()){
            session.finishAuthentication();
        }
        isLinked = session.isLinked();
    }
}
