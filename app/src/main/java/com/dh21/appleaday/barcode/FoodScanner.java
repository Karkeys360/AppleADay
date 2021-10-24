package com.dh21.appleaday.barcode;

import android.graphics.ImageDecoder;
import android.media.Image;
import android.util.Log;

import androidx.camera.core.ImageInfo;
import androidx.camera.core.ImageProxy;

import com.dh21.appleaday.ParseFoodFacts;
import com.dh21.appleaday.data.Food;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FoodScanner {
    public static void scanBarcode(ImageProxy imageProxy, CompletableFuture<Food> callback) {
        BarcodeScanner scanner = BarcodeScanning.getClient();
        InputImage inputImage = InputImage.fromMediaImage(
                Objects.requireNonNull(imageProxy.getImage()),
                imageProxy.getImageInfo().getRotationDegrees());
        scanner.process(inputImage).addOnSuccessListener(barcodes -> {
            barcodes.forEach(b -> Log.d("Scanner", b.getRawValue()));
            Optional<Barcode> result = barcodes.stream().filter(FoodScanner::isValid).findFirst();
            if (result.isPresent()) {
                lookupFood(result.get(), callback);
            } else {
                callback.completeExceptionally(new Exception("No barcodes found!"));
            }
        }).addOnFailureListener(callback::completeExceptionally).addOnCompleteListener(r -> {
            Log.d("FoodScanner", "Closing image!");
            imageProxy.close();
        });
    }

    private static void lookupFood(Barcode barcode, CompletableFuture<Food> callback) {
        String val = barcode.getRawValue();
        Log.d("FoodScanner", "Barcode: " + val);
        ParseFoodFacts.getFoodFacts(val, callback);
    }

    private static boolean isValid(Barcode barcode) {
        Log.d("FoodScanner", "Valid: " + (barcode.getValueType() == Barcode.TYPE_PRODUCT));
        return barcode.getValueType() == Barcode.TYPE_PRODUCT;
    }
}
