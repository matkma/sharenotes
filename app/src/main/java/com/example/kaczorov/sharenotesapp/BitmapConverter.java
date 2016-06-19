package com.example.kaczorov.sharenotesapp;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kaczorov on 2016-06-19.
 */
public final class BitmapConverter {
    private BitmapConverter() {}
    public static File toFile(Bitmap bitmap, String fileName) throws IOException {
        File file = new File(Environment.getDownloadCacheDirectory().toString() + "/Pictures", fileName);
        file.createNewFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        FileOutputStream fos = new FileOutputStream(file);

        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return file;
    }
}
