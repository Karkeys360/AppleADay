package com.dh21.appleaday.ui.trends;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dh21.appleaday.analysis.EventAnalysis;
import com.dh21.appleaday.data.DataUtil;
import com.dh21.appleaday.data.Food;
import com.dh21.appleaday.data.IntervalIterator;
import com.dh21.appleaday.data.Timed;
import com.dh21.appleaday.databinding.FragmentTrendsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.dh21.appleaday.data.IntervalIterator.Interval;

public class TrendsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int NUM_EVENT_SENSITIVITIES = 4;
    private static final double SENSITIVITY_THRESHOLD = 0.4;

    private TrendsViewModel dashboardViewModel;
    private FragmentTrendsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(TrendsViewModel.class);

        binding = FragmentTrendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner intervalSpinner = binding.intervalSpinner;
        String[] options = Arrays.stream(Interval.values())
                .map(Interval::name).toArray(String[]::new);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(adapter);
        intervalSpinner.setOnItemSelectedListener(this);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        int i = binding.intervalSpinner.getSelectedItemPosition();
        generateGraphs(Interval.values()[i]);
        generateSensitivities();
    }

    private View createRow(String event, String food, double sensitivity) {
        sensitivity = Math.random();
        ConstraintLayout layout = new ConstraintLayout(requireActivity());
        layout.setId(View.generateViewId());
        layout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        TextView eventText = new TextView(requireActivity());
        eventText.setId(View.generateViewId());
        eventText.setText(event);

        ProgressBar pbar = new ProgressBar(requireActivity(), null,
                android.R.attr.progressBarStyleHorizontal);
        pbar.setId(View.generateViewId());
        pbar.setIndeterminate(false);
        pbar.setProgress((int) Math.round(Math.min(1, sensitivity) * 100.0));

        TextView foodText = new TextView(requireActivity());
        foodText.setId(View.generateViewId());
        foodText.setText(food);



        Log.d("Trends", String.format("Ids: %s", Arrays.toString(new int[]{eventText.getId(), pbar.getId(), foodText.getId()})));

        layout.addView(eventText);
        cs.connect(eventText.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START);
        cs.connect(eventText.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        layout.addView(foodText);
        cs.connect(foodText.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END);
        cs.connect(foodText.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        layout.addView(pbar);
        cs.connect(pbar.getId(), ConstraintSet.START, eventText.getId(), ConstraintSet.END);
        cs.connect(pbar.getId(), ConstraintSet.END, foodText.getId(), ConstraintSet.START);
        cs.connect(pbar.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);

        cs.applyTo(layout);

        return layout;
    }

    private void generateSensitivities() {
        EventAnalysis ea = EventAnalysis.getInstance();
        Map<String, Integer> eventFreq = ea.getEventFreq();
        Set<String> foodFreq = ea.getFoodFreq().keySet();

        Log.d("Trends", "Gen!");

        List<String> events = eventFreq.keySet().stream()
                .sorted(Comparator.comparing(eventFreq::get).reversed())
                .collect(Collectors.toList());

        LinearLayout layout = binding.sensitivityList;
        layout.removeAllViews();

        for (String event : events) {
            String sensitiveFood = foodFreq.stream()
                    .max(Comparator.comparing(f -> ea.getEventGivenFoodProbability(event, f)))
                    .orElse(null);
            if (sensitiveFood != null) {
                double sensitivity = ea.getEventGivenFoodProbability(event, sensitiveFood);
                Log.d("Trends", String.format("Event=%s, Food=%s, sensitivity=%.3f", event, sensitiveFood, sensitivity));
                if (sensitivity >= SENSITIVITY_THRESHOLD) {
                    View row = createRow(event, sensitiveFood, sensitivity);
                    layout.addView(row);
                }
            }
        }
    }

    private void generateGraphs(Interval interval) {
        LinearLayout layout = binding.chartList;
        layout.removeAllViews();

        String[] keys = {"calories", "fats", "carbs", "proteins", "sodium", "fiber", "sugars"};

        for (int i = 0; i < keys.length; i++) {
            layout.addView(createChart(new IntervalIterator(EventAnalysis.getInstance().getTimes(),
                    interval), keys[i], i == keys.length - 1));
        }
    }

    private int dpToPixels(int dp) {
        final float scale = requireContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private ViewGroup createChartAndTitle(View title, View chart, boolean last) {
        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(title);
        layout.addView(chart);

        LinearLayout.LayoutParams chartParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPixels(200));
        chartParams.setMargins(dpToPixels(15), 0, dpToPixels(15), dpToPixels(last ? 0 : 20));
        chart.setLayoutParams(chartParams);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(dpToPixels(15), 0, 0, 0);
        title.setLayoutParams(titleParams);

        return layout;
    }

    private String dateToLabel(long timestamp, Interval interval) {
        if (timestamp == -1) {
            return "nope";
        }
        LocalDate ld = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        switch (interval) {
            case Day:
                return ld.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);
            case Week:
                // 1L = Sunday (for US)
                LocalDate first = ld.with(WeekFields.of(Locale.US).dayOfWeek(), 1L);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
                return first.format(formatter);
            case Month:
                return ld.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
            default:
                throw new RuntimeException();
        }
    }

    private ValueFormatter createDateVF(TreeMap<Float, Long> entryMap,
                                        Interval interval) {
        return new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                Float floor = entryMap.floorKey(value);
                Float ceil = entryMap.ceilingKey(value);
                float key;
                if (floor == null) {
                    key = ceil;
                } else if (ceil == null) {
                    key = floor;
                } else {
                    key = Math.abs(value - floor) <= Math.abs(value - ceil) ? floor : ceil;
                }
                return dateToLabel(Objects.requireNonNull(
                        entryMap.getOrDefault(key, -1L)), interval);
            }
        };
    }

    private View createChart(IntervalIterator iterator, String key, boolean last) {
        Interval interval = iterator.getInterval();
        String titleKey = key.substring(0, 1).toUpperCase() + key.substring(1);
        LinkedList<Entry> vals = new LinkedList<>();
        List<Timed> entries;
        while ((entries = iterator.next()) != null) {
            float val = 0;
            for (Timed entry : entries) {
                if (entry instanceof Food) {
                    Food food = (Food) entry;
                    //noinspection ConstantConditions
                    val += food.getMap().getOrDefault(key, 0.0);
                }
            }
            vals.addFirst(new Entry(val, entries.get(0).getTime()));
        }
        List<BarEntry> barEntries = new ArrayList<>(vals.size());
        TreeMap<Float, Long> entryMap = new TreeMap<>();
        int i = -vals.size() + 1;
        for (Entry entry : vals) {
            float x = i++;
            entryMap.put(x, entry.time);
            barEntries.add(new BarEntry(x, entry.value));
        }
        Context ctx = requireActivity();
        BarData data = new BarData(new BarDataSet(barEntries, titleKey));
        BarChart chart = new BarChart(ctx);
        chart.setData(data);
        chart.setDescription(null);
        chart.moveViewToX(0);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setValueFormatter(createDateVF(entryMap, interval));
        chart.setVisibleXRange(3, 7);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(true);
        // TODO: config bar colors

        TextView title = new TextView(ctx);
        title.setText(String.format("%s Consumed (%s per %s)", titleKey, DataUtil.getUnit(key),
                interval.name().toLowerCase()));

        return createChartAndTitle(title, chart, last);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Interval interval = Interval.values()[i];
        generateGraphs(interval);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private static class Entry {
        public float value;
        public long time;

        public Entry(float value, long time) {
            this.value = value;
            this.time = time;
        }
    }
}