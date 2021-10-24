package com.dh21.appleaday.analysis;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;


public class AnalysisUtil {

    public static void saveToDisk(Context context, String s) {
        File oldFile = new File(context.getFilesDir() + "/string");
        if (oldFile.exists()) {
            oldFile.delete();
        }
        File file = new File(context.getFilesDir(), "string");
        try (FileOutputStream outputStream = new FileOutputStream(file)){
            outputStream.write(s.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFromDisk(Context context) {
        String s = "";
        File file = new File(context.getFilesDir() + "/string");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(context.openFileInput("string")).useDelimiter("\\Z")) {
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }
                s = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }
}

