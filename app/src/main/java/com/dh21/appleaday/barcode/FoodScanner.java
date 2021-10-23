package com.dh21.appleaday.barcode;

import android.util.Log;

import com.dh21.appleaday.data.Food;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FoodScanner {
    public static void scanBarcode(InputImage image, CompletableFuture<Food> callback) {
        BarcodeScanner scanner = BarcodeScanning.getClient();
        scanner.process(image).addOnSuccessListener(barcodes -> {
            barcodes.forEach(b -> Log.d("Scanner", b.getRawValue()));
            Optional<Barcode> result = barcodes.stream().filter(FoodScanner::isValid).findFirst();
            if (result.isPresent()) {
                lookupFood(result.get(), callback);
            } else {
                callback.cancel(false);
            }
        }).addOnFailureListener(callback::completeExceptionally);
    }

    private static void lookupFood(Barcode barcode, CompletableFuture<Food> callback) {
        // TODO: implement
        callback.complete(new Food("blah"));
//        throw new UnsupportedOperationException("Not implemented!");
    }

    private static boolean isValid(Barcode barcode) {
        // TODO: implement
        return true;
//        throw new UnsupportedOperationException("Not implemented!");
    }
}
