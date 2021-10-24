package com.dh21.appleaday;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.dh21.appleaday.data.Food;
import com.google.gson.Gson;


public class ParseFoodFacts {
    public static Map<String, Double> grades = new HashMap<>();

    static {
        grades.put("a", 0.0);
        grades.put("b", 1.0);
        grades.put("c", 2.0);
        grades.put("d", 3.0);
        grades.put("e", 4.0);
    }

    public static void getFoodFacts(String barcode, CompletableFuture<Food> callback) {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.thenApply(ParseFoodFacts::parseFood).thenAccept(callback::complete)
                .exceptionally(e -> {
                    callback.completeExceptionally(e);
                    return null;
                });
        HttpQuery.getFoodFactsAsync(barcode, future);
    }

    public static Food parseFood(String json) {
        Gson parser = new Gson();
        Map map = parser.fromJson(json, Map.class);
        Map product = (Map) map.get("product");
        String name = (String) product.get("product_name");
        Set<String> ing = getIngredients(product);
        String grade = (String) product.get("nutriscore_grade");
        Map nutrients = (Map) product.get("nutriments");

        double calories = getNutrient(nutrients, "energy-kcal_serving");
        double fats = getNutrient(nutrients, "fat_serving");
        double protein = getNutrient(nutrients, "proteins_serving");
        double fiber = getNutrient(nutrients, "fiber_serving");
        double sodium = getNutrient(nutrients, "sodium_serving");
        double sugars = getNutrient(nutrients, "sugars_serving");
        double carbs = getNutrient(nutrients, "carbohydrates_serving");

        return new Food(name, grades.getOrDefault(grade, -1.0), calories, fats, carbs,
                protein, sugars, fiber, sodium, ing);
    }

    public static Double getNutrient(Map nutrients, String data) {
        return (double) (nutrients.get(data) != null ? nutrients.get(data) : -1.0);
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

}
