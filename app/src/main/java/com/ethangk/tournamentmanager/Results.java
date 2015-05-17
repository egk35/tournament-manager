package com.ethangk.tournamentmanager;

/**
 * This class holds results for every round
 */
public class Results {
	private Team[][] results;
    private Player[][] byes;

    /**
     * Construct a new Results
     *
     * @param rounds number of rounds
     * @param teams number of teams
     * @param numByes number of byes needed each round
     */
	public Results(int rounds, int teams, int numByes) {

        results = new Team[rounds][teams];
        byes = new Player[rounds][numByes];
	}

    /**
     * Add the teams to the results for the given round
     *
     * @param round round to add the teams
     * @param teams teams to add
     */
	public void addResults(int round, Team[] teams) {
		results[round] = teams;
	}

    /**
     * Add the players to the byes for the given round
     *
     * @param round round to add these byes
     * @param players players who had a bye this round
     */
    public void addByes(int round, Player[] players) {
        byes[round] = players;
    }

    /**
     * @param round round to return from
     * @return the teams for the given round
     */
	public Team[] getResults(int round) {
		return results[round];
	}

    /**
     * @param round round to return from
     * @return the players who have a bye this round
     */
    public Player[] getByes(int round) {
        return byes[round];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int round = 0; round < results.length; round++) {
            sb.append("Round " + (round + 1) + "\n\n");
            sb.append("Teams: \n");
            Team[] teams = results[round];
            for (Team t : teams) {
                sb.append(t + "\n");
            }
            sb.append("\n");
            sb.append("Byes: \n");
            Player[] bs = byes[round];
            for (Player p : bs) {
                sb.append(p + " ");
            }
            sb.append("\n\n");
        }
        return sb.toString();
    }
}
