package com.dh21.appleaday.analysis;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class AnalysisUtil {

    public static void saveToDisk(Context context, String s) {
        File oldFile = new File(context.getFilesDir() + "/string");
        if (oldFile.exists()) {
            oldFile.delete();
        }
        File file = new File(context.getFilesDir(), "string");
        try (FileOutputStream outputStream = new FileOutputStream(file)){
            outputStream.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getFromDisk(Context context) {
        String s = null;
        File file = new File(context.getFilesDir() + "/string");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(context.openFileInput("string")).useDelimiter("\\Z")) {
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }
                s = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return s;
    }
}

