package com.ethangk.tournamentmanager;

/**
 * A Player represents a player in a tournament
 */
public class Player {
	private int id;
	private Gender gender;
	private boolean[] byes;
	private int[] playCounts;
    private int men;

    /**
     * Construct a new player
     *
     * @param g gender of this player
     * @param id id of this player
     * @param rounds number of rounds in the tournament
     * @param men number of men in the tournament
     * @param women number of women in the tournament
     */
	public Player(Gender g, int id, int rounds, int men, int women) {
		this.id = id;
		gender = g;
		byes = new boolean[rounds];
		playCounts = new int[men + women];
        this.men = men;
	}

    /**
     * @return this player's id
     */
	public int getId() {
		return id;
	}

    /**
     * @return this player's gender
     */
	public Gender getGender() {
		return gender;
	}

    /**
     * Mark that this player has a bye in the given round
     *
     * @param round round to set a bye
     */
	public void setBye(int round) {
		byes[round] = true;
	}

    /**
     * @param round round to check for a bye
     * @return whether this player has a bye in the given round
     */
	public boolean hasBye(int round) {
		return byes[round];
	}

    /**
     * Mark that this player played with the given player
     *
     * @param p another player
     */
	public void playWith(Player p) {
		playCounts[p.getId()]++;
	}

    /**
     * @param p player to check for
     * @return the number of times this player has played with player p
     */
	public int timesPlayedWith(Player p) {
		return playCounts[p.getId()];
	}
	
	@Override
	public String toString() {
		if (gender == Gender.MALE) {
			return "m" + (id + 1);
		} else {
			return "f" + (id + 1 - men);
		}
	}

    /********* Debugging Methods **********/

    public String debugPlayCounts() {
        String res = "[ ";
        for (int p : playCounts) {
            res += p + " ";
        }
        return res + "]";
    }

    public String debugByes() {
        String res = "[ ";
        for (int i = 0; i < byes.length; i++) {
            if (hasBye(i)) {
                res += i + " ";
            }
        }
        return res + "]";
    }
}
