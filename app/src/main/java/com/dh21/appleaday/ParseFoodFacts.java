package com.dh21.appleaday;

import java.sql.Array;
import java.sql.SQLOutput;
import java.util.*;

import com.dh21.appleaday.data.Food;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class ParseFoodFacts {
    public static String fullJson;


    public static Map getFoodFacts(String barcode) {
        fullJson = HttpQuery.getFoodFacts(barcode);

        Gson parser = new Gson();
        Map map = parser.fromJson(fullJson, Map.class);
        Map product = (Map) map.get("product");
        String name = (String) product.get("product_name");
        Set<String> ing = getIngredients(product);
        String grade = (String) product.get("nutriscore_grade");
        Map nutrients = (Map) product.get("nutriments");
        double Calories = getNutrient(nutrients,"energy-kcal_serving");
        double fats = getNutrient(nutrients, "fat_serving");
        double protein = getNutrient(nutrients, "proteins_serving");
        double fiber = getNutrient(nutrients,"fiber_serving");
        double sodium = getNutrient(nutrients, "sodium_serving");
        double sugars = getNutrient(nutrients, "sugars_serving");
        double carbs = getNutrient(nutrients, "carbohydrates_serving");



        return product;
    }

    public static Double getNutrient(Map nutrients, String data) {
        return (double) nutrients.get(data);
    }

    public static Set<String> getIngredients(Map product) {
        ArrayList<Map> ingredientsObj = (ArrayList<Map>) product.get("ingredients");
        Set<String> ingredients = new HashSet<>();
        for (int i = 0; i < ingredientsObj.size(); i++) {
            String item = (String) ingredientsObj.get(i).get("id");
            ingredients.add(item.substring(3));
        }
        return ingredients;
    }


    public static void main(String[] args) {
        String barcode = "044000033279";
        Map m = ParseFoodFacts.getFoodFacts(barcode);
        // System.out.println(ParseFoodFacts.getFoodFacts(barcode));
        //System.out.println(m);
    }


}
