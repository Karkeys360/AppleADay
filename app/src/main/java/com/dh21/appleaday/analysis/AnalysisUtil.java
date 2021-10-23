package com.dh21.appleaday.analysis;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class AnalysisUtil {

    public void saveToDisk(Context context, Map<String, Double> map) {

        Gson gson = new Gson();
        gson.toJson(map, new FileWriter(context.getFilesDir() + "map.json"));

        File dir = new File(context.getFilesDir(), "analytics");
    }

    public Map<String, Double> getFromDisk() {
        Map<String, Double> map = new HashMap<>();
        return map;
    }

}

