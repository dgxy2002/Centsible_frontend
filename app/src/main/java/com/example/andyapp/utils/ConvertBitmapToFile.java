package com.example.andyapp.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConvertBitmapToFile {
    public static File convertToFile(Context context, Bitmap bitmap, String fileName) throws IOException {
        File file = new File(context.getCacheDir(), fileName);
        file.createNewFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitMapData = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitMapData);
        fos.flush();
        fos.close();

        return file;
    }
}
