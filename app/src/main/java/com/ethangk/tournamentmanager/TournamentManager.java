package com.ethangk.tournamentmanager;

import java.util.Arrays;


public class TournamentManager {
	private Player[] players;
	private int numPlayers;
	private int rounds;
	private int teams;
	private int teamSize;
	private Results results;
	
	public TournamentManager(int men, int women, int rounds, int teams, int teamSize) {
		numPlayers = men + women;
		this.rounds = rounds;
		this.teams = teams;
		this.teamSize = teamSize;
		results = new Results(rounds, teams, numPlayers - teams * teamSize);
		
		players = new Player[numPlayers];
		int i;
		for (i = 0; i < men; i++) {
			Player p = new Player(Gender.MALE, i, rounds, men, women);
			players[i] = p;
		}
		for (int j = i; j < numPlayers; j++) {
			Player p = new Player(Gender.FEMALE, j, rounds, men, women);
			players[j] = p;
		}
		
		assignByes();
	}
	
	/**
	 * Assign byes to each player
	 */
	private void assignByes() {
		int numByes = numPlayers - teams * teamSize;
		if (numByes < 0) {
			return;
		}
		
		// create the order in which to assign byes to players
		Player[] byeOrder = new Player[rounds * numByes];
		boolean[] playersAssigned = new boolean[numPlayers];
		int assigned = 0;
		int leftStep = 1;
		int rightStep = 2;
        int maxStep = 6;
		int leftIndex = 0;
		int rightIndex = numPlayers - 1;
		boolean leftStepped = false;
		boolean rightStepped = false;
		for (int i = 0; i < rounds * numByes; i++, assigned++) {
			if (assigned == numPlayers) {
				// assigned a bye to all players, so reset
				assigned = 0;
				leftIndex = 0;
				rightIndex = numPlayers - 1;
                leftStep = leftStep % (maxStep - 1) + 1;
                rightStep = rightStep % maxStep + 1;
				leftStepped = false;
				rightStepped = false;
				Arrays.fill(playersAssigned, false);
			}
			
			// bounds checking
			if (leftIndex >= numPlayers / 2) {
				leftIndex = numPlayers / 2 - 1;
			} 
			if (rightIndex <= numPlayers / 2) {
				rightIndex = numPlayers / 2 + 1;
			}
			
			if (i % 2 == 0) {
				// assign player from the left
				while (playersAssigned[leftIndex]) {
					leftIndex++;
					leftStepped = false;
					if (leftIndex == numPlayers) {
						leftIndex = 0;
					}
				}
				byeOrder[i] = players[leftIndex];
				playersAssigned[leftIndex] = true;
				if (leftStepped && leftStep > 1) {
					leftIndex -= (leftStep - 1);
					leftStepped = false;
				} else {
					leftIndex += leftStep;
					leftStepped = true;
				}
			} else {
				// assign player from the right
				while (playersAssigned[rightIndex]) {
					rightIndex--;
					rightStepped = false;
					if (rightIndex == -1) {
						rightIndex = numPlayers - 1;
					}
				}
				byeOrder[i] = players[rightIndex];
				playersAssigned[rightIndex] = true;
				if (rightStepped && rightStep > 1) {
					rightIndex += (rightStep - 1);
					rightStepped = false;
				} else {
					rightIndex -= rightStep;
					rightStepped = true;
				}
			}
		}
		
		// assign byes according to the order
		for (int round = 0; round < rounds; round++) {
			for (int i = 0; i < numByes; i++) {
				byeOrder[round * numByes + i].setBye(round);
			}
		}
	}
	
	public void run() {
		for (int round = 0; round < rounds; round++) {
			runRound(round);
		}
	}
	
	public void runRound(int round) {
		Player[] activePlayers = new Player[teams * teamSize];
        Player[] byes = new Player[numPlayers - teams * teamSize];
		int i = 0;
        int j = 0;
		for (Player p : players) {
			if (!p.hasBye(round)) {
				activePlayers[i] = p;
				i++;
			} else {
                byes[j] = p;
                j++;
            }
		}
		RoundManager rm = new RoundManager(activePlayers, teams, teamSize);
		rm.run();
		results.addResults(round, rm.getTeams());
        results.addByes(round, byes);
		rm.updatePlayers();
	}
	
	public Results getResults() {
		return results;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for (Player p : players) {
			sb.append(p + " ");
		}
		sb.append("}");
		return sb.toString();
	}
}
