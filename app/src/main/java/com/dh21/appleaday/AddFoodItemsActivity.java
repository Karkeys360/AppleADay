package com.dh21.appleaday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.dh21.appleaday.data.Food;
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

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddFoodItemsActivity extends AppCompatActivity {

    private Button openScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_items);
        openScanner = findViewById(R.id.openScanner);

        ActivityResultLauncher<Void> launcher = registerForActivityResult(new ScannerContract(), food -> {
            if (food != null) {
                Log.d("MainActivity", "Food: " + food.getName());
            } else {
                Log.d("MainActivity", "No food found");
            }
        });

        openScanner.setOnClickListener(v -> {
            launcher.launch(null);
        });

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddFoodItemsActivity.this, new String[] { Manifest.permission.CAMERA }, 0);
        } else {
            Toast.makeText(this, "Camera allowed!", Toast.LENGTH_SHORT).show();
        }
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
                return new Gson().fromJson(foodJson, Food.class);
            } else {
                return null;
            }
        }
    }
}