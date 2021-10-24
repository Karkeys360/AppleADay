package com.dh21.appleaday;

import static com.dh21.appleaday.analysis.AnalysisUtil.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);

        StringBuilder sb = new StringBuilder();
        String string1 = "put this to disk!";
        sb.append(string1 + "\n");
        saveToDisk(this, string1);
        sb.append("-------\n");


        String string2 = "test";
        sb.append(string2 + "\n");
        string2 = getFromDisk(this) +  "test2";
        sb.append(string2);
        text.setText(sb.toString());
    }
}