package com.dh21.appleaday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import com.dh21.appleaday.data.Food;
import com.google.gson.Gson;

import java.util.Calendar;

public class FoodEditorActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText caloriesText;
    private EditText fatsText;
    private EditText carbsText;
    private EditText proteinsText;
    private EditText sugarsText;
    private EditText fibersText;
    private EditText sodiumText;
    private CalendarView calendar;
    private TimePicker tp;

    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_editor);
        getIds();
        Intent intent = getIntent();
        String foodString = intent.getStringExtra("food");
        Gson gson = new Gson();
        food = gson.fromJson(foodString, Food.class);
        setDisplay();
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                food.setName(s.toString());
                setDisplay();
            }
        });
        caloriesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                Double num = Double.valueOf(value);
                food.setCalories(num);
                setDisplay();
            }
        });
        fatsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                Double num = Double.valueOf(value);
                food.setFats(num);
                setDisplay();
            }
        });
        carbsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                Double num = Double.valueOf(value);
                food.setCarbs(num);
                setDisplay();
            }
        });
    }

    private void setDisplay() {
        nameText.setText(food.getName());
        if (food.getCalories() == -1) {
            caloriesText.setText("");
        } else {
            caloriesText.setText(String.valueOf(food.getCalories()));
        }
        if (food.getFats() == -1) {
            fatsText.setText("");
        } else {
            fatsText.setText(String.valueOf(food.getFats()));
        }
        if (food.getCarbs() == -1) {
            carbsText.setText("");
        } else {
            carbsText.setText(String.valueOf(food.getCarbs()));
        }
        if (food.getProteins() == -1) {
            proteinsText.setText("");
        } else {
            proteinsText.setText(String.valueOf(food.getProteins()));
        }
        if (food.getSugars() == -1) {
            sugarsText.setText("");
        } else {
            sugarsText.setText(String.valueOf(food.getSugars()));
        }
        if (food.getFiber() == -1) {
            fibersText.setText("");
        } else {
            fibersText.setText(String.valueOf(food.getFiber()));
        }
        if (food.getSodium() == -1) {
            sodiumText.setText("");
        } else {
            sodiumText.setText(String.valueOf(food.getSodium()));
        }
        long time = food.getTime();
        calendar.setDate(time);
        Calendar javaCalendar = Calendar.getInstance();
        javaCalendar.setTimeInMillis(time);
        tp.setHour(javaCalendar.get(Calendar.HOUR));
        tp.setMinute(javaCalendar.get(Calendar.MINUTE));
    }

    private void getIds() {
        nameText = findViewById(R.id.editTextName);
        caloriesText = findViewById(R.id.editTextCalories);
        fatsText = findViewById(R.id.editTextFats);
        carbsText = findViewById(R.id.editTextCarbs);
        proteinsText = findViewById(R.id.editTextProtein);
        sugarsText = findViewById(R.id.editTextSugar);
        fibersText = findViewById(R.id.editTextFiber);
        sodiumText = findViewById(R.id.editTextSodium);
        calendar = findViewById(R.id.calendarView200);
        tp = findViewById(R.id.simpleTimePicker200);
    }

}