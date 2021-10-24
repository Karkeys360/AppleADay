package com.dh21.appleaday;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dh21.appleaday.analysis.EventAnalysis;
import com.dh21.appleaday.data.Event;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterEventDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterEventDialog extends DialogFragment {

    public static CharSequence[] EVENT_CATEGORIES = {"Flatulence", "Diarrhea", "Bloating", "Constipation"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Runnable callback;

    public EnterEventDialog(Runnable callback) {
        // Required empty public constructor
        this.callback = callback == null ? () -> {} : callback;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterEventDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterEventDialog newInstance(String param1, String param2) {
        EnterEventDialog fragment = new EnterEventDialog(null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_event_dialog, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int[] selectedItemId = {-1};

        return builder.setTitle("Enter Event")
                .setSingleChoiceItems(EVENT_CATEGORIES, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("HULLABALOO 1 ", "" + EVENT_CATEGORIES[id]);
                        selectedItemId[0] = id;
                    }
                })
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       EventAnalysis ea = EventAnalysis.getInstance();

                       // save the event
                       if (selectedItemId[0] >= 0) {
                           // Log.d(EVENT_CATEGORIES[selectedItemId[0]].toString() + "", "HULLO");
                           ea.addTime(new Event(EVENT_CATEGORIES[selectedItemId[0]].toString()));
                           callback.run();
                       }
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialog, int id) {
                       // don't save the event
                   }
               }).create();
    }
}