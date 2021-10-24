package com.dh21.appleaday.ui.trends;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dh21.appleaday.data.DataUtil;
import com.dh21.appleaday.data.IntervalIterator;
import com.dh21.appleaday.databinding.FragmentTrendsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TrendsFragment extends Fragment {

    private TrendsViewModel dashboardViewModel;
    private FragmentTrendsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(TrendsViewModel.class);

        binding = FragmentTrendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        LinearLayout layout = binding.chartList;

        // TODO: add spinner for interval at the top

        String[] keys = {"calories", "fats", "carbs", "proteins", "sodium", "fiber", "sugars"};

        for (int i = 0; i < keys.length; i++) {
            layout.addView(createChart(null, IntervalIterator.Interval.Week, keys[i], i == keys.length - 1));
        }

        return root;
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
        chartParams.bottomMargin = dpToPixels(last ? 60 : 20);
        chart.setLayoutParams(chartParams);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER;
        title.setLayoutParams(titleParams);

        return layout;
    }

    private String dateToLabel(long timestamp, IntervalIterator.Interval interval) {
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
                                        IntervalIterator.Interval interval) {
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

    private View createChart(IntervalIterator iterator, IntervalIterator.Interval interval,
                             String key, boolean last) {
        String titleKey = key.substring(0, 1).toUpperCase() + key.substring(1);
        // TODO: uncomment when using the actual mock data
        LinkedList<Entry> vals = new LinkedList<>(createCalorieData(interval));
//        List<Timed> entries;
//        while ((entries = iterator.next()) != null) {
//            float val = 0;
//            for (Timed entry : entries) {
//                if (entry instanceof Food) {
//                    Food food = (Food) entry;
//                    //noinspection ConstantConditions
//                    val += food.getMap().getOrDefault(key, 0.0);
//                }
//            }
//            vals.addFirst(val);
//        }
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

        TextView title = new TextView(ctx);
        title.setText(String.format("%s Consumed (%s per %s)", titleKey, DataUtil.getUnit(key),
                interval.name().toLowerCase()));

        return createChartAndTitle(title, chart, last);
    }

    private List<Entry> createCalorieData(IntervalIterator.Interval interval) {
        List<Entry> entries = new ArrayList<>();
        for (int i = -20; i <= 0; i++) {
            float val = (float) Math.random() * 1000;
            LocalDate ld = null;
            switch (interval) {
                case Day:
                    ld = LocalDate.now().plusDays(i);
                    break;

                case Week:
                    ld = LocalDate.now().plusDays(7 * i);
                    break;

                case Month:
                    ld = LocalDate.now().plusMonths(i);
                    break;
            }
            entries.add(new Entry(val, ld.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()));
        }
        System.out.println(entries.stream().map(e -> e.time).map(Date::new)
                .map(Date::toString).collect(Collectors.toList()));
        return entries;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class Entry {
        public float value;
        public long time;

        public Entry(float value, long time) {
            this.value = value;
            this.time = time;
        }
    }
}