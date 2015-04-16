package com.ethangk.tournamentmanager;

import java.util.HashSet;

/**
 * A RoundManager controls one round of a tournament
 */
public class RoundManager {
	private Player[] players;
	private int teamSize;
	private Team[] teams;
	private HashSet<Player> men;
	private HashSet<Player> women;
	private HashSet<Player> allPlayers;
	
	/**
	 * Construct a new RoundManager
     *
	 * @param p the active players in this round
     * @param numTeams the number of teams
     * @param teamSize the size of each team
	 * @requires p.size == numTeams * teamSize
	 */
	public RoundManager(Player[] p, int numTeams, int teamSize) {
		players = p;
		this.teamSize = teamSize;
		teams = new Team[numTeams];
		for (int i = 0; i < numTeams; i++) {
			teams[i] = new Team(teamSize);
		}
		men = new HashSet<Player>();
		women = new HashSet<Player>();
		allPlayers = new HashSet<Player>();
		createSets();
	}

    /**
     * Run the round. Assign players to each team.
     */
	public void run() {
		int numMen = men.size();
		int numWomen = women.size();
		int numPlayers = allPlayers.size();
		int maxMen = (int) Math.ceil((double) numMen * teamSize / numPlayers);
		int maxWomen = (int) Math.ceil((double) numWomen * teamSize / numPlayers);
		HashSet<Player> available = new HashSet<Player>();
		
		for (Team team : teams) {
			
			// pick the first player by who is the worst fit on the other teams
			available = allPlayers;
			Player worstFit = null;
			int worstRating = -1;
			for (Player p : available) {
				int rating = 0;
				for (Team t : teams) {
					rating += t.rating(p);
				}
				if (rating > worstRating) {
					worstFit = p;
					worstRating = rating;
				}
			}
			team.addPlayer(worstFit);
			allPlayers.remove(worstFit);
			men.remove(worstFit);
			women.remove(worstFit);
		}

        // now pick the other players for each team
		for (int i = 1; i < teamSize; i++) {
			for (Team team : teams) {
			
				// set which gender to pick from (or both)
				available = allPlayers;
				if (team.numMen() >= maxMen) {
					available = women;
				} else if (team.numWomen() >= maxWomen) {
					available = men;
				}
				
				// find best fit for team
				int bestRating = Integer.MAX_VALUE;
				Player bestFit = null;
				for (Player p : available) {
					int rating = team.rating(p);
					if (rating < bestRating) {
						bestFit = p;
						bestRating = rating;
					}
				}
				
				// add to team and remove from players left
				team.addPlayer(bestFit);
				allPlayers.remove(bestFit);
				men.remove(bestFit);
				women.remove(bestFit);
				if (bestFit.getGender() == Gender.MALE && team.numMen() >= maxMen) {
					numMen--;
					maxMen = (int) Math.ceil((double) (numMen) * teamSize / numPlayers);
				} else if (bestFit.getGender() == Gender.FEMALE && team.numWomen() >= maxWomen) {
					numWomen--;
					maxWomen = (int) Math.ceil((double) (numWomen) * teamSize / numPlayers);
				}
			}
		}
	}

    /**
     * Update all players in this round
     */
	public void updatePlayers() {
		for (Team t : teams) {
			t.updatePlayers();
		}
	}

    /**
     * @return the teams. Will be empty if run() has not been called
     */
	public Team[] getTeams() {
		return teams;
	}

    /**
     * Fill men, women, and allPlayers lists.
     */
	private void createSets() {
		for (Player p : players) {
			if (p.getGender() == Gender.MALE) {
				men.add(p);
			} else {
				women.add(p);
			}
			allPlayers.add(p);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Team t : teams) {
			sb.append(t + "\n");
		}
		return sb.toString();
	}
}
