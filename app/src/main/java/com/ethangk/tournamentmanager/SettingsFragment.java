package com.ethangk.tournamentmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * This fragment allows the user to edit current settings
 */
public class SettingsFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks. */
    public interface SettingsListener {
        public void onSave(int rounds, int teams, int teamSize);
    }

    SettingsListener listener;
    private int rounds;
    private int teams;
    private int teamSize;

    /**
     *  Override the Fragment.onAttach() method to instantiate the listener
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (SettingsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SettingsListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rounds = getArguments().getInt("ROUNDS");
        teams = getArguments().getInt("TEAMS");
        teamSize = getArguments().getInt("TEAMSIZE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Settings");

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        EditText e = (EditText) v.findViewById(R.id.num_rounds);
        e.setText(Integer.toString(rounds));
        e = (EditText) v.findViewById(R.id.num_teams);
        e.setText(Integer.toString(teams));
        e = (EditText) v.findViewById(R.id.team_size);
        e.setText(Integer.toString(teamSize));

        Button button = (Button) v.findViewById(R.id.save_button);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onSave();
                dismiss();
            }
        });

        return v;
    }

    /**
     * Call the listener with the current values
     */
    private void onSave() {
        int rounds = getValue((EditText) getDialog().findViewById(R.id.num_rounds));
        int teams = getValue((EditText) getDialog().findViewById(R.id.num_teams));
        int teamSize = getValue((EditText) getDialog().findViewById(R.id.team_size));
        listener.onSave(rounds, teams, teamSize);
    }

    /**
     * Return the value from the given EditText
     *
     * @param e and EditText
     * @return the value in it
     */
    private int getValue(EditText e) {
        String s = e.getText().toString();
        if (s.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }
}
