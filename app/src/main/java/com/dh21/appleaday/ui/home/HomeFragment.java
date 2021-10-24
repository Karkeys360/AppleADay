package com.dh21.appleaday.ui.home;

import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dh21.appleaday.AddFoodItemsActivity;
import com.dh21.appleaday.EnterEventDialog;
import com.dh21.appleaday.MainActivity;
import com.dh21.appleaday.R;
import com.dh21.appleaday.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.addMealBtn.setOnClickListener(this::openAddFoodItemsActivity);
        binding.enterEventBtn.setOnClickListener(this::openEnterEvent);

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
        DialogFragment fragment = new EnterEventDialog();
        fragment.show(getActivity().getSupportFragmentManager(), "enter_events");
    }
}