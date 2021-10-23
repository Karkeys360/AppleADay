package com.dh21.appleaday.barcode;

import com.dh21.appleaday.data.Food;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Scanner {
    public static void scanBarcode(InputImage image, CompletableFuture<Food> callback) {
        BarcodeScanner scanner = BarcodeScanning.getClient();
        scanner.process(image).addOnSuccessListener(barcodes -> {
            Optional<Barcode> result =  barcodes.stream().filter(Scanner::isValid).findFirst();
            if (result.isPresent()) {
                callback.complete(lookupFood(result.get()));
            } else {
                callback.cancel(false);
            }
        }).addOnFailureListener(callback::completeExceptionally);
    }

    private static Food lookupFood(Barcode barcode) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented!");
    }

    private static boolean isValid(Barcode barcode) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented!");
    }
}
