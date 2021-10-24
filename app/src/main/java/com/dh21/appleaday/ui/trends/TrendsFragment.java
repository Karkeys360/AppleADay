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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dh21.appleaday.data.DataUtil;
import com.dh21.appleaday.data.IntervalIterator;
import com.dh21.appleaday.databinding.FragmentTrendsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class TrendsFragment extends Fragment {

    private com.dh21.appleaday.ui.trends.TrendsViewModel dashboardViewModel;
    private FragmentTrendsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(com.dh21.appleaday.ui.trends.TrendsViewModel.class);

        binding = FragmentTrendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        LinearLayout layout = binding.chartList;

        // TODO: add spinner for interval at the top

        String[] keys = {"calories", "fats", "carbs", "proteins", "sodium", "fiber", "sugars"};

        for (int i = 0; i < keys.length; i++) {
            layout.addView(createChart(null, IntervalIterator.Interval.Day, keys[i], i == keys.length - 1));
        }

        return root;
    }

    private int dpToPixels(int dp) {
        final float scale = requireContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private ViewGroup createChartAndTitle(View title, View chart, boolean last) {
        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(title);
        layout.addView(chart);

        LinearLayout.LayoutParams chartParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPixels(200));
        chartParams.bottomMargin = dpToPixels(last ? 60 : 20);
        chart.setLayoutParams(chartParams);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER;
        title.setLayoutParams(titleParams);

        return layout;
    }

    private View createChart(IntervalIterator iterator, IntervalIterator.Interval interval, String key, boolean last) {
        String titleKey = key.substring(0, 1).toUpperCase() + key.substring(1);
//        List<Timed> entries;
//        LinkedList<Float> vals = new LinkedList<>();
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
//        List<BarEntry> barEntries = new ArrayList<>(vals.size());
//        int i = -vals.size() + 1;
//        for (float val : vals) {
//            barEntries.add(new BarEntry(i++, val));
//        }
        Context ctx = requireActivity();
        BarData data = createCalorieData(); // new BarData(new BarDataSet(barEntries, titleKey));
        BarChart chart = new BarChart(ctx);
        chart.setData(data);
        chart.setDescription(null);
//        chart.moveViewToX(20);
        chart.moveViewToX(0);
        chart.getXAxis().setDrawLabels(true);
        chart.setVisibleXRange(3, 7);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(true);

        TextView title = new TextView(ctx);
        title.setText(String.format("%s Consumed (%s per %s)", titleKey, DataUtil.getUnit(key),
                interval.name().toLowerCase()));

        return createChartAndTitle(title, chart, last);
    }

    private BarData createCalorieData() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = -20; i <= 0; i++) {
            BarEntry be = new BarEntry((float) i, (float) Math.random() * 1000);
            entries.add(be);
        }
        BarDataSet bds = new BarDataSet(entries, "Calories");
        return new BarData(bds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}