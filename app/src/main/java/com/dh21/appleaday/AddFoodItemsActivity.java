package com.dh21.appleaday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.dh21.appleaday.analysis.EventAnalysis;
import com.dh21.appleaday.data.Food;
import com.dh21.appleaday.data.Timed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.gridlayout.widget.GridLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class AddFoodItemsActivity extends AppCompatActivity {

    private static final int FONT_SIZE = 18;
    private static final int FONT_COLOR = Color.WHITE;
    private static final int ROW_PADDING = 4;

    private GridLayout foodGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_items);
        Button openScanner = findViewById(R.id.openScanner);
        foodGrid = findViewById(R.id.foodGrid);
        Button finishButton = findViewById(R.id.finishButton);

        ActivityResultLauncher<Void> launcher = registerForActivityResult(new ScannerContract(), food -> {
            if (food != null) {
                addRowToLayout(foodGrid, food.getName(), food.getTime(), foodGrid.getRowCount());
                Log.d("MainActivity", "Food: " + food.getName());
                EventAnalysis.getInstance().addTime(food);
            } else {
                Log.d("MainActivity", "No food found");
            }
        });

        openScanner.setOnClickListener(v -> launcher.launch(null));

        finishButton.setOnClickListener(v -> finish());

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddFoodItemsActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            Toast.makeText(this, "Camera allowed!", Toast.LENGTH_SHORT).show();
        }

    }

    private String titleCase(String str) {
        return Arrays.stream(str.split("\\s"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private int dpToPixels(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void addRowToLayout(GridLayout gl, String name, long timestamp, int row) {
        TextView nameText = new TextView(this);
        nameText.setText(titleCase(name));
        nameText.setGravity(Gravity.CENTER);
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE);
        nameText.setTextColor(FONT_COLOR);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma MMM dd", Locale.US);
        String dateStr = sdf.format(new Date(timestamp));
        TextView dateText = new TextView(this);
        dateText.setGravity(Gravity.CENTER);
        dateText.setText(dateStr);
        dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE);
        dateText.setTextColor(FONT_COLOR);

        gl.addView(nameText);
        gl.addView(dateText);

        nameText.setLayoutParams(createGLParam(row, 0));
        dateText.setLayoutParams(createGLParam(row, 1));
    }

    private GridLayout.LayoutParams createGLParam(int r, int c) {
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
        param.setMargins(0, dpToPixels(ROW_PADDING), 0, dpToPixels(ROW_PADDING));
        param.columnSpec = GridLayout.spec(c, 1f);
        param.rowSpec = GridLayout.spec(r);
        return param;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ScannerContract extends ActivityResultContract<Void, Food> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            return new Intent(AddFoodItemsActivity.this, ScanningActivity.class);
        }

        @Override
        public Food parseResult(int resultCode, @Nullable Intent intent) {
            if (intent != null) {
                String foodJson = intent.getStringExtra("food");
                Log.d("HELLO", foodJson);
                return new Gson().fromJson(foodJson, Food.class);
            } else {
                return null;
            }
        }
    }
}