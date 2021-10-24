package com.dh21.appleaday.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dh21.appleaday.R;
import com.dh21.appleaday.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    private static final int DEF_BTN_RES = R.drawable.roundedbutton;
    private static final int PRESSED_BTN_RES = R.drawable.roundedbutton_pressed;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}