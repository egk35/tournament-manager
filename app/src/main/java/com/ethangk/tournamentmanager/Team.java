package com.ethangk.tournamentmanager;

/**
 * A Team object represents a team of players
 */
public class Team {
	private int size;
	private Player[] players;
	private int numPlayers;

    /**
     * Construct a new Team
     *
     * @param s the size of the team
     */
	public Team(int s) {
		size = s;
		players = new Player[size];
		numPlayers = 0;
	}

    /**
     * Add a player to the team if not full
     *
     * @param p the player to add
     */
	public void addPlayer(Player p) {
		if (!isFull()) {
			players[numPlayers] = p;
			numPlayers++;
		}
	}

    /**
     * Return the player in the ith position on the team
     *
     * @param i index of player
     * @return return player at index i on team
     */
    public Player getPlayer(int i) {
        if (i < 0 || i >= size) {
            return null;
        }
        return players[i];
    }

    /**
     * @return whether team is full
     */
	public boolean isFull() {
		return numPlayers == size;
	}
	
	/**
	 * @return the number of times player p has played with the members of this team
	 */
	public int timesPlayedWith(Player p) {
		int res = 0;
		for (int i = 0; i < numPlayers; i++) {
			res += p.timesPlayedWith(players[i]);
		}
		return res;
	}
	
	/**
	 * @return a rating for player p's potential fit on the team. 
	 * 			a lower rating means a better fit. 
	 */
	public int rating(Player p) {
		return timesPlayedWith(p);
	}

    /**
     * @return the number of players on this team
     */
	public int numPlayers() {
		return numPlayers;
	}

    /**
     * @return the number of men on this team
     */
	public int numMen() {
		int res = 0;
		for (int i = 0; i < numPlayers; i++) {
			if (players[i].getGender() == Gender.MALE) {
				res++;
			}
		}
		return res;
	}

    /**
     * @return the number of women on this team
     */
	public int numWomen() {
		int res = 0;
		for (int i = 0; i < numPlayers; i++) {
			if (players[i].getGender() == Gender.FEMALE) {
				res++;
			}
		}
		return res;
	}

    /**
     * Update the players of this team with their teammates
     */
	public void updatePlayers() {
		for (Player p : players) {
			for (Player teammate : players) {
				if (teammate != p) {
					p.playWith(teammate);
				}
			}
		}
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
