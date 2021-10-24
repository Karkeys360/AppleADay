package com.dh21.appleaday.ui.home;

import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProvider;

import com.dh21.appleaday.AddFoodItemsActivity;
import com.dh21.appleaday.EnterEventDialog;
import com.dh21.appleaday.EventEditorActivity;
import com.dh21.appleaday.FoodEditorActivity;
import com.dh21.appleaday.analysis.EventAnalysis;
import com.dh21.appleaday.data.Event;
import com.dh21.appleaday.data.Food;
import com.dh21.appleaday.data.Timed;
import com.dh21.appleaday.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private static final int ROW_PADDING = 5;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private static final int NUM_DISPLAYED = 10;
    private static final int FONT_COLOR = Color.WHITE;
    private static final int FONT_SIZE = 16;

    private enum Display {
        Food, Event
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.addMealBtn.setOnClickListener(this::openAddFoodItemsActivity);
        binding.enterEventBtn.setOnClickListener(this::openEnterEvent);


        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openAddFoodItemsActivity(View view) {
        Intent intent = new Intent(getActivity(), AddFoodItemsActivity.class);
        startActivity(intent);
    }

    public void openEnterEvent(View view) {
        DialogFragment fragment = new EnterEventDialog(() -> {
            Log.d("HomeFragment", "Event added!");
            populateEventData();
        });
        fragment.show(requireActivity().getSupportFragmentManager(), "enter_events");
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mealRow.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        binding.dateRow.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        binding.mealRow.setText("Food Item");
        binding.dateRow.setText("Date");
        binding.dateRow.setGravity(Gravity.CENTER);
        binding.dateRow.setTextColor(Color.WHITE);
        binding.mealRow.setGravity(Gravity.CENTER);
        binding.mealRow.setTextColor(Color.WHITE);

        binding.eventRow.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        binding.eventDateRow.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        binding.eventRow.setText("Event");
        binding.eventDateRow.setText("Date");
        binding.eventDateRow.setGravity(Gravity.CENTER);
        binding.eventDateRow.setTextColor(Color.WHITE);
        binding.eventRow.setGravity(Gravity.CENTER);
        binding.eventRow.setTextColor(Color.WHITE);

        populateFoodData();
        populateEventData();
    }

    private void populateEventData() {
        List<Timed> timeds = EventAnalysis.getInstance().getTimes();
        int added = 0;
        binding.eventsTable.removeAllViews();
        for (int i = timeds.size() - 1; i >= 0 && added < NUM_DISPLAYED; i--) {
            Timed t = timeds.get(i);
            if (t instanceof Event) {
                Event e = (Event) t;
                addRowToLayout(binding.eventsTable, e.getName(), e.getTime(), added);
                added++;
            }
        }
    }

    private void populateFoodData() {
        int added = 0;
        List<Timed> timeds = EventAnalysis.getInstance().getTimes();
        binding.mealsTable.removeAllViews();
        for (int i = timeds.size() - 1; i >= 0 && added < NUM_DISPLAYED; i--) {
            Timed t = timeds.get(i);
            if (t instanceof Food){
                Food e = (Food) t;
                addRowToLayout(binding.mealsTable, e.getName(), e.getTime(), added);
                added++;
            }
        }
        binding.mealsTable.invalidate();
    }

    private String titleCase(String str) {
        return Arrays.stream(str.split("\\s"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private void addRowToLayout(GridLayout gl, String name, long timestamp, int row) {
               TextView nameText = new TextView(requireContext());
        nameText.setText(titleCase(name));
        nameText.setGravity(Gravity.CENTER);
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE);
        nameText.setTextColor(FONT_COLOR);
        nameText.setClickable(true);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma MMM dd", Locale.US);
        String dateStr = sdf.format(new Date(timestamp));
        TextView dateText = new TextView(requireContext());
        dateText.setGravity(Gravity.CENTER);
        dateText.setText(dateStr);
        dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE);
        dateText.setTextColor(FONT_COLOR);
        dateText.setClickable(true);


        gl.addView(nameText);
        gl.addView(dateText);

        nameText.setLayoutParams(createGLParam(row, 0));
        dateText.setLayoutParams(createGLParam(row, 1));
    }

    private GridLayout.LayoutParams createGLParam(int r, int c) {
        GridLayout.LayoutParams param =new GridLayout.LayoutParams();
        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
        param.setMargins(0, dpToPixels(ROW_PADDING), 0, dpToPixels(ROW_PADDING));
        param.columnSpec = GridLayout.spec(c, 1f);
        param.rowSpec = GridLayout.spec(r);
        return param;
    }
    private int dpToPixels(int dp) {
        final float scale = requireContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}