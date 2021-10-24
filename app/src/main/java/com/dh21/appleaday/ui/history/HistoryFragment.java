package com.dh21.appleaday.ui.history;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProvider;

import com.dh21.appleaday.R;
import com.dh21.appleaday.analysis.EventAnalysis;
import com.dh21.appleaday.data.Event;
import com.dh21.appleaday.data.Food;
import com.dh21.appleaday.data.Timed;
import com.dh21.appleaday.databinding.FragmentHistoryBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HistoryFragment extends Fragment {

    private static final int DEF_BTN_RES = R.drawable.roundedbutton;
    private static final int PRESSED_BTN_RES = R.drawable.roundedbutton_pressed;

    private static final int NUM_DISPLAYED = 50;
    public static final int FONT_SIZE = 20;
    public static final int ROW_PADDING = 4;
    public static final int FONT_COLOR = Color.WHITE;

    private enum Display {
        Food, Event
    }

    private HistoryViewModel historyViewModel;
    private FragmentHistoryBinding binding;
    private Display display = Display.Food;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.eventsButton.setOnClickListener(this::eventsButtonClicked);
        binding.mealsButton.setOnClickListener(this::foodsButtonClicked);
        return root;
    }

    private void eventsButtonClicked(View v) {
        this.display = Display.Event;
        populateData();
    }

    private void foodsButtonClicked(View v) {
        this.display = Display.Food;
        populateData();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateData();
    }

    private void populateData() {
        if (display == Display.Event) {
            binding.mealsButton.setBackgroundResource(DEF_BTN_RES);
            binding.eventsButton.setBackgroundResource(PRESSED_BTN_RES);
        } else {
            binding.mealsButton.setBackgroundResource(PRESSED_BTN_RES);
            binding.eventsButton.setBackgroundResource(DEF_BTN_RES);
        }
        binding.contentGrid.removeAllViews();

        EventAnalysis ea = EventAnalysis.getInstance();
        List<Timed> timeds = ea.getTimes();
        int added = 0;
        for (int i = timeds.size() - 1; i >= 0 && added < NUM_DISPLAYED; i--) {
            Timed t = timeds.get(i);
            if (display == Display.Event && t instanceof Event) {
                Event e = (Event) t;
                addRowToLayout(binding.contentGrid, e.getName(), e.getTime(), added);
                added++;
            } else if (display == Display.Food && t instanceof Food) {
                Food f = (Food) t;
                addRowToLayout(binding.contentGrid, f.getName(), f.getTime(), added);
                added++;
            }
        }
    }

    private String titleCase(String str) {
        return Arrays.stream(str.split("\\s"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private int dpToPixels(int dp) {
        final float scale = requireContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void addRowToLayout(GridLayout gl, String name, long timestamp, int row) {
        TextView nameText = new TextView(requireContext());
        nameText.setText(titleCase(name));
        nameText.setGravity(Gravity.CENTER);
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE);
        nameText.setTextColor(FONT_COLOR);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma MMM dd", Locale.US);
        String dateStr = sdf.format(new Date(timestamp));
        TextView dateText = new TextView(requireContext());
        dateText.setGravity(Gravity.CENTER);
        dateText.setText(dateStr);
        dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE);
        dateText.setTextColor(FONT_COLOR);

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
//        param.rightMargin = 5;
//        param.topMargin = 5;
//        param.setGravity(Gravity.CENTER);
        param.columnSpec = GridLayout.spec(c, 1f);
        param.rowSpec = GridLayout.spec(r);
        return param;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}