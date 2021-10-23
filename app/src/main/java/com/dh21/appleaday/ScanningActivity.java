package com.dh21.appleaday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageInfo;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dh21.appleaday.barcode.FoodScanner;
import com.dh21.appleaday.data.Food;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ScanningActivity extends AppCompatActivity {
    private static final String LOG_TAG = "ScanningActivity";

    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private Preview preview;
    private ImageCapture imageCapture;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "hello!");
        setContentView(R.layout.activity_scanning);
        previewView = findViewById(R.id.previewView);
        scanButton = findViewById(R.id.scanButton);

        scanButton.setOnClickListener(this::scanButtonClicked);

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                    ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(() -> {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindAll();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.getMessage() == null ? "Error occurred!" : e.getMessage());
                }
            }, ContextCompat.getMainExecutor(this));
        } else {
            Toast.makeText(this, "Camera permissions not granted!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void scanButtonClicked(View v) {
        if (BuildConfig.DEBUG && !(v.getId() == scanButton.getId())) {
            throw new AssertionError("Assertion failed");
        }

        CompletableFuture<ImageAndInfo> imgCallback = new CompletableFuture<>();
        imgCallback.thenCompose(imageAndInfo -> {
            Image image = imageAndInfo.image;
            ImageInfo info = imageAndInfo.info;
            InputImage inputImage = InputImage.fromMediaImage(image, info.getRotationDegrees());
            CompletableFuture<Food> foodCallback = new CompletableFuture<>();
            FoodScanner.scanBarcode(inputImage, foodCallback);
            return foodCallback;
        }).thenAccept(this::handleResult);
        imageCapture.takePicture(
                ContextCompat.getMainExecutor(this), new ImageListener(imgCallback));
    }

    void handleResult(Food food) {
        Log.d("Scanning_Activity", "Food: " + food.toString());
        setResult();
        finish();
    }

    void bindAll() {
        preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Display d = getDisplay();
        int rot = d == null ? Surface.ROTATION_0 : d.getRotation();
        imageCapture = new ImageCapture.Builder().setTargetRotation(rot).build();

        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageCapture, preview);
    }

    private static class ImageAndInfo {
        private Image image;
        private ImageInfo info;

        public ImageAndInfo(Image image, ImageInfo info) {
            this.image = image;
            this.info = info;
        }
    }

    private static class ImageListener extends ImageCapture.OnImageCapturedCallback {

        private CompletableFuture<ImageAndInfo> callback;

        public ImageListener(CompletableFuture<ImageAndInfo> callback) {
            this.callback = callback;
        }

        @Override
        public void onError(@NonNull ImageCaptureException exception) {
            callback.completeExceptionally(exception);
        }

        @Override
        public void onCaptureSuccess(@NonNull ImageProxy imageProxy) {
            Image image = imageProxy.getImage();
            if (image == null) {
                callback.completeExceptionally(
                        new ImageCaptureException(
                                ImageCapture.ERROR_CAPTURE_FAILED, "Capture failed!", null));
            } else {
                callback.complete(new ImageAndInfo(image, imageProxy.getImageInfo()));
            }
        }
    }
}
