package com.dh21.appleaday;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpQuery {
    public static String HTTP_ENDPOINT_PREFIX = "https://world.openfoodfacts.org/api/v0/product/";

    public static String getFoodFacts(String barcode) {
        String targetURL = HTTP_ENDPOINT_PREFIX + barcode + ".json";

        HttpURLConnection connection = null;
        try {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            int status = connection.getResponseCode();

            // Get response
            InputStreamReader isr;
            if (status > 299) {
                isr = new InputStreamReader(connection.getErrorStream());
            } else {
                isr = new InputStreamReader(connection.getInputStream());
            }
            BufferedReader br = new BufferedReader(isr);
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            br.close();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}