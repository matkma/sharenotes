package com.example.kaczorov.sharenotesapp;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.util.Map;

public class DropboxClient {
    private static DropboxClient ourInstance = new DropboxClient();

    private DropboxClient() {
    }

    public static DropboxClient getInstance() {
        return ourInstance;
    }

    final static private String APP_KEY = "mhoy84bgzfk85zn";
    final static private String APP_SECRET = "y2z9b6g9sv6db7a";
    final static private String TOKEN = "hFypMrpR_xAAAAAAAAAAB_tIiWsQvGuQsk45mQAovhCZdpalYq6ZX9WIrw9JElO1";

    public String[] itemsNames;
    public Map<String, String> foldersMap;
    public DropboxAPI<AndroidAuthSession> dbxApi;
    public File currentFileToSend;
    public File currentFileToDownloadTo;
    public String currentFolder;

    public void authenticate() {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dbxApi = new DropboxAPI<AndroidAuthSession>(session);
        dbxApi.getSession().setOAuth2AccessToken(TOKEN);
        if (session.authenticationSuccessful()) {
            session.finishAuthentication();
        }
    }

    public boolean isLinked() {
        return dbxApi.getSession().isLinked();
    }

    public void getFoldersFromDropbox(MainActivity activity, int val) throws DropboxException {
        new GetFolderNamesTask(activity).execute(val);
    }

    public void getItems(SearchActivity activity, int val, String path) {
        currentFolder = path;
        new GetItemsTask(activity).execute(path, val);
    }

    public Map<String, String> getFoldersMap() {
        return foldersMap;
    }

    public void setFoldersMap(Map<String, String> foldersMap) {
        this.foldersMap = foldersMap;
    }
}
