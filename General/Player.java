package General;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Spaces.GoSpace;
import Spaces.Property;
import Spaces.Space;


public class Player {
	private String name;
	private int cash;
	private int boardPosition;
	private int lastRoll1, lastRoll2;
	private int banishmentAvoidChances;
	private int doubleStreak;
	private boolean bankrupt;
	private boolean banished;
	private boolean missTurn;
	private boolean extraTurn;
	private List<Property> ownedProperties;
	
	public Player(String name, int startCash) {
		this.name              = name;
		cash                   = startCash;
		bankrupt               = false;
		banished               = false;
		missTurn               = false;
		extraTurn              = false;
		lastRoll1              = 0;
		lastRoll2              = 0;
		banishmentAvoidChances = 0;
		doubleStreak           = 0;
		boardPosition          = 0; //Pass Go
		ownedProperties        = new LinkedList<Property>();
	}
	
	public String getName() {
		return name;
	}
	
	public int getCash() {
		return cash;
	}
	
	public int getBoardPosition() {
		return boardPosition;
	}
	
	public void setLastRoll(int roll1, int roll2) {
		lastRoll1 = roll1;
		lastRoll2 = roll2;
	}
	
	public int[] getLastRolls() {
		int[] rolls = new int[]{lastRoll1, lastRoll2};
		return rolls;
	}
	
	public int getLastTotalRoll() {
		return lastRoll1 + lastRoll2;
	}
	
	public boolean isBankrupt() {
		return bankrupt;
	}
	
	public boolean isBanished() {
		return banished;
	}
	
	public boolean missingTurn() {
		return missTurn;
	}
	
	public boolean getExtraTurn() {
		return extraTurn;
	}
	
	public void setExtraTurn(boolean extraTurn) {
		this.extraTurn = extraTurn;
	}
	
	public void setBanished(boolean banished) {
		this.banished = banished;
	}
	
	public void setMissTurn(boolean missTurn) {
		this.missTurn = missTurn;
	}
	
	public int getBanishmentAvoidChances() {
		return banishmentAvoidChances;
	}
	
	public void changeBanishmentAvoidChances(int change) {
		banishmentAvoidChances += change;
	}
	
	public void move(Board board) {
		if (isBankrupt()) return;
		
		int[] rolls = getLastRolls();
		if (rolls[0] == rolls[1] && rolls[0] != 0) {
			doubleStreak++;
			if (doubleStreak == 3) {
				doubleStreak = 0;
				System.out.println("You've rolled three straight " +
						"doubles; go directly to the Moon, do not Pass Go, " +
						"do not collect 200 bits");
				setBanished(true);
				return;
			} else {
				setExtraTurn(true);
			}
		} else {
			doubleStreak = 0;
		}
		
		Space currentSpace, newSpace;
		currentSpace = board.getSpaces()[boardPosition];
		currentSpace.removePlayer(this);
		
		int newPosition = boardPosition + getLastTotalRoll();
		newPosition %= Board.BOARDSPACES;
		
		if (newPosition < boardPosition) {
			System.out.println("*" + getName() + " passed go " +
					"and collects " + GoSpace.PASSGOBONUS + " bits*");
			changeCash(GoSpace.PASSGOBONUS);
		}
		
		boardPosition = newPosition;
		
		newSpace = board.getSpaces()[boardPosition];
		newSpace.addPlayer(this);
		
		board.handleEvents(this);
	}
	
	public void setPosition(int position, Board board) {
		Space currentSpace;
		
		currentSpace = board.getSpaces()[getBoardPosition()];
		currentSpace.removePlayer(this);
		
		while (position < 0) position = Board.BOARDSPACES + position;
		position %= Board.BOARDSPACES;
		boardPosition = position;
		
		currentSpace = board.getSpaces()[getBoardPosition()];
		currentSpace.addPlayer(this);
	}
	
	public void changeCash(int amount) {
		if (isBankrupt()) return;
		
		cash += amount;
		if (cash < 0) {
			bankrupt = true;
			cash = 0;
			System.out.println(getName() + " is bankrupt!");
		}
	}
	
	public List<Property> getProperties() {
		return ownedProperties;
	}
	
	public void addProperty(Property property) {
		ownedProperties.add(property);
	}
	
	public void removeProperty(Property property) {
		if (ownedProperties.contains(property)) {
			ownedProperties.remove(property);
		}
	}
	
	public void banish() {
		if (getBanishmentAvoidChances() > 0) {
			System.out.println("Would you like to spend a " +
					"banishment avoid chance to avoid banishment?");
			if (Util.getResponse()) {
				System.out.println("You avoid banishment");
				changeBanishmentAvoidChances(-1);
				return;
			} 
		}
		setBanished(true);
		System.out.println("*" + getName() + " is banished*");
	}
	
	@Override
	public String toString() {
		String stringRep;
		stringRep = String.format("%s: %s (Owns: %s)", name, cash,
				Arrays.deepToString(ownedProperties.toArray()));
		return stringRep;
	}
}
