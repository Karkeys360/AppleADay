package com.dh21.appleaday;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dh21.appleaday.data.Food;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private Button goToCamera;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        goToCamera = findViewById(R.id.goToCamera);

        ActivityResultLauncher<Void> launcher = registerForActivityResult(new ScannerContract(), food -> {
            if (food != null) {
                Log.d("MainActivity", "Food: " + food.getName());
                text.setText("Food: " + food.getName());
            } else {
                Log.d("MainActivity", "No food found");
                text.setText("No food found");
            }
        });
        goToCamera.setOnClickListener(v -> {
            launcher.launch(null);
        });

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA }, 0);
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
            return new Intent(MainActivity.this, ScanningActivity.class);
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