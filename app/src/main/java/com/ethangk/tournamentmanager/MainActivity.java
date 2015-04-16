package com.ethangk.tournamentmanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

/**
 * The homepage. Displays prompts for number of men and women.
 *
 * @author Ethan Goldman-Kirst
 */
public class MainActivity extends ActionBarActivity implements
                SettingsFragment.SettingsListener {

    public static final int defaultPlayers = 10;
    public static final int defaultRounds = 15;
    public static final int defaultTeams = 4;
    public static final int defaultTeamSize = 4;

    private int numMen;
    private int numWomen;
    private int numRounds;
    private int numTeams;
    private int teamSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numMen = defaultPlayers;
        numWomen = defaultPlayers;
        numRounds = defaultRounds;
        numTeams = defaultTeams;
        teamSize = defaultTeamSize;

        EditText men = (EditText) findViewById(R.id.num_men);
        men.setText(Integer.toString(numMen));
        EditText women = (EditText) findViewById(R.id.num_women);
        women.setText(Integer.toString(numWomen));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            openSettingsFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user hits the "go" button. Display results to the user.
     *
     * @param view the clicked button
     */
    public void onStartPairings(View view) {
        numMen = getValue((EditText) findViewById(R.id.num_men));
        numWomen = getValue((EditText) findViewById(R.id.num_women));
        if (numMen + numWomen < numTeams * teamSize) {
            displayError("Not enough players to fill teams");
            return;
        } else if (numTeams % 2 != 0) {
            displayError("Number of teams must be even");
            return;
        }
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("TEAMS", numTeams);
        intent.putExtra("TEAMSIZE", teamSize);
        intent.putExtra("ROUNDS", numRounds);
        intent.putExtra("MEN", numMen);
        intent.putExtra("WOMEN", numWomen);
        startActivity(intent);
    }

    /**
     * Open the settings fragment.
     */
    private void openSettingsFragment() {
        DialogFragment newFragment = new SettingsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("ROUNDS", numRounds);
        bundle.putInt("TEAMS", numTeams);
        bundle.putInt("TEAMSIZE", teamSize);
        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "settings");
    }

    /**
     * Called when the user clicks save in the settings fragment.
     * Update local variables accordingly.
     *
     * @param rounds rounds entered in fragment
     * @param teams teams entered in fragment
     * @param teamSize team size entered in fragment
     */
    @Override
    public void onSave(int rounds, int teams, int teamSize) {
        numRounds = rounds;
        numTeams = teams;
        this.teamSize = teamSize;
    }

    /**
     * Returns the value contained in the given EditText
     *
     * @param e an EditText
     * @return the value inside it
     */
    private int getValue(EditText e) {
        String s = e.getText().toString();
        if (s.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }

    /**
     * Display the given message to the user
     *
     * @param message a message to display
     */
    private void displayError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
