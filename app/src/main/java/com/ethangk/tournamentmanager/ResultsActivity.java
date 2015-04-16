package com.ethangk.tournamentmanager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import android.view.Gravity;
import android.app.DialogFragment;
import android.util.TypedValue;

/**
 * This activity displays results for each round
 */
public class ResultsActivity extends Activity
                implements WarningFragment.WarningDialogListener {

    public static final String[] teamColors =
        {"#8f0000", "#00008f", "#006b24", "#cc9900", "#7a007a", "#009999", "#cc7a29", "#663300"};

    private int teams;
    private int teamSize;
    private int rounds;
    private int numMen;
    private int numWomen;
    private int currRound;
    private Results results;

    /**
     * Calculate results and create the display for the current round
     *
     * @param savedInstanceState
     *          bundle containing teams, team size, rounds, men, and women
     * @requires teams % 2 == 0 && men + women >= teams * team size
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        teams = intent.getIntExtra("TEAMS", 0);
        teamSize = intent.getIntExtra("TEAMSIZE", 0);
        rounds = intent.getIntExtra("ROUNDS", 0);
        numMen = intent.getIntExtra("MEN", 0);
        numWomen = intent.getIntExtra("WOMEN", 0);
        currRound = 0;

        TournamentManager tm = new TournamentManager(numMen, numWomen, rounds, teams, teamSize);
        tm.run();
        results = tm.getResults();

        setContentView(setRound(currRound));
    }

    /**
     * Prompt user with warning dialog after pressing back button
     */
    @Override
    public void onBackPressed() {
        DialogFragment newFragment = new WarningFragment();
        newFragment.show(getFragmentManager(), "warning");
    }

    /**
     * Called when the user accepts the warnings after hitting the back button
     */
    public void onWarningPositiveClick(DialogFragment dialog) {
        super.onBackPressed();
    }

    /**
     * returns a display of the given round
     *
     * @param round the round to display
     * @return the View containing the given round
     */
    private View setRound(int round) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_results, null);

        TextView title = (TextView) v.findViewById(R.id.round_title);
        title.setText("Round " + (round + 1) + " of " + rounds);

        if (round == rounds - 1) {
            Button next = (Button) v.findViewById(R.id.next_button);
            next.setEnabled(false);
        }
        if (round == 0) {
            Button prev = (Button) v.findViewById(R.id.prev_button);
            prev.setEnabled(false);
        }

        LinearLayout content = (LinearLayout) v.findViewById(R.id.results_content);

        LinearLayout byes_holder = (LinearLayout) v.findViewById(R.id.byes_holder);
        byes_holder = setByes(byes_holder, results.getByes(round));

        for (int i = 0; i < teams; i+=2) {
            View pairing = inflater.inflate(R.layout.pairing_layout, null);
            LinearLayout team1_holder = (LinearLayout) pairing.findViewById(R.id.team1_holder);
            LinearLayout team2_holder = (LinearLayout) pairing.findViewById(R.id.team2_holder);

            View team1 = inflater.inflate(R.layout.team_layout, null);
            team1 = setPlayers(team1, results.getResults(round)[i], i);
            team1_holder.addView(team1);
            View team2 = inflater.inflate(R.layout.team_layout, null);
            team2 = setPlayers(team2, results.getResults(round)[i + 1], i + 1);
            team2_holder.addView(team2);

            if (i == 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                int value = dpToPixel(5);
                params.setMargins(value, value, value, value);
                pairing.findViewById(R.id.pairing_holder).setLayoutParams(params);
            }

            content.addView(pairing);
        }

        return v;
    }

    /**
     * adds players to the given team view and returns it
     *
     * @param team the view to modify
     * @param players the players to add to the view
     * @param teamNum the team number
     * @return the given view with players added
     */
    private View setPlayers(View team, Team players, int teamNum) {
        for(int player = 0; player < ((ViewGroup) team).getChildCount(); player++) {
            View nextPlayer = ((ViewGroup) team).getChildAt(player);
            if (player >= teamSize) {
                nextPlayer.setVisibility(View.GONE);
            } else {
                nextPlayer.setVisibility(View.VISIBLE);
                if (teamNum % 2 == 1) {
                    ((LinearLayout) nextPlayer).setGravity(Gravity.RIGHT);
                }
                TextView tv = (TextView) ((ViewGroup) nextPlayer).getChildAt(0);
                tv.setText(players.getPlayer(player).toString());
                tv.setTextColor(getColor(teamNum));
            }
        }
        return team;
    }

    /**
     * Adds players to the holder and returns it
     *
     * @param holder the holder for the byes
     * @param players the players who have byes this round
     * @return the given view with players added
     */
    private LinearLayout setByes(LinearLayout holder, Player[] players) {
        for (Player p : players) {
            TextView tv = new TextView(this);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.player_text_size));
            tv.setText(p.toString());
            holder.addView(tv);
        }
        return holder;
    }

    /**
     * Called when the user clicks the next round button.
     * Sets the view to show the next round.
     *
     * @param view the clicked button
     */
    public void onNextRound(View view) {
        currRound++;
        setContentView(setRound(currRound));
    }

    /**
     * Called when the user clicks the prev round button.
     * Sets the view to show the previous round.
     *
     * @param view the clicked button
     */
    public void onPrevRound(View view) {
        currRound--;
        setContentView(setRound(currRound));
    }

    /**
     * returns the color for the given team number
     *
     * @param team number of a team
     * @return the color for the given team
     */
    private int getColor(int team) {
        return Color.parseColor(teamColors[team % (teamColors.length)]);
    }

    /**
     * returns the pixel value of the given dp value
     *
     * @param dpValue a dp value
     * @return the pixel value
     */
    private int dpToPixel(int dpValue) {
        float d = getResources().getDisplayMetrics().density;
        return (int)(dpValue * d); // margin in pixels
    }
}
