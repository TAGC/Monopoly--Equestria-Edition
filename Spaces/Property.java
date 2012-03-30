package Spaces;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import General.Player;
import General.Util;


public class Property implements Space{
	
	public static enum PropertyType 
	{ NORMAL, STATION, UTILITY };
	
	public static enum PropertyGroup
	{ FLUTTERSHY, RAINBOW_DASH, TWILIGHT_SPARKLE,
		RARITY, APPLEJACK, PINKIE_PIE, LUNA,
		CELESTIA
	};
	
	private static final double SELLVALCOEF   = 0.75;
	private static final double STABLEVALCOEF = 1.2;
	public static final int     MAXSTABLES    = 5; 
	
	private final Set<Player> players = new HashSet<Player>();
	
	private int value;
	private int numStables;
	
	private String name;
	private Player owner;
	private PropertyType type;
	private PropertyGroup group;
	
	public Property(String name, int value, PropertyType type,
			PropertyGroup group) {
		this.name = name;
		this.value = value;
		this.type = type;
		if (type == PropertyType.NORMAL) this.group = group;
		numStables = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player player) {
		owner = player;
	}
	
	public PropertyType getType() {
		return type;
	}
	
	public PropertyGroup getGroup() {
		return group;
	}
	
	public int getNumStables() {
		return numStables;
	}
	
	public void purchase(Player purchaser) {
		if (purchaser.getCash() >= value) {
			owner = purchaser;
			purchaser.addProperty(this);
			purchaser.changeCash(-value);
			
			System.out.println("*" + purchaser.getName() + " purchased " 
					+ name + " for " + value + " bits*");
		} else {
			System.out.println("You don't have enough money to purchase" +
					"this property");
		}
	}
	
	public void sell() {
		if (owner == null) return;
		
		owner.removeProperty(this);
		owner.changeCash(getSellValue());
		owner = null;
	}
	
	public int getSellValue() {
		double sellValue;
		sellValue  = value * SELLVALCOEF;
		sellValue += 50 * numStables; 
		return (int)sellValue;
	}
	
	@Override
	public void playerLands(Player renter) {
		if (renter.equals(owner)) return;
		
		if (owner == null && renter.getCash() > value) {
			System.out.println("Would you like to purchase this property" +
					" for " + value + " bits? Y/N");
			if (Util.getResponse()) {
				purchase(renter);
			}
			return;
		} else if (owner == null) return;
		
		Iterator<Property> iter;
		int lastRoll, multiplier, charge; 
		boolean setOwned;
		
		switch(type) {
		case NORMAL:
			//charge = 10% of value + additional for each stable
			iter = owner.getProperties().iterator();
			
			setOwned = true;
			while(iter.hasNext()) {
				Property nextProperty;
				
				nextProperty = iter.next();
				if (nextProperty.getType() == PropertyType.NORMAL 
					&& !nextProperty.getGroup().equals(getGroup()) 
					&& !nextProperty.getOwner().equals(owner)) {
						setOwned = false;
				}
			}
				
			//if owner possesses all properties of a group, increase rent
			if (setOwned) {
				charge = (int)(value / 5 * Math.pow(STABLEVALCOEF, numStables));
			} else {
				charge = (int)(value / 10 * Math.pow(STABLEVALCOEF, numStables));
			}
			break;
		case STATION:
			//charge = 100 if owner possesses no other stations,
			//         200 if owner possesses 1 other station,
			//         400 if owner possesses 2 other stations,
			//         800 if owner possesses all stations
			charge = 50;
			iter = owner.getProperties().iterator();
			
			while(iter.hasNext()) {
				Property nextProperty;
				
				nextProperty = iter.next();
				if (nextProperty.getType() == PropertyType.STATION 
						&& nextProperty.getOwner().equals(owner)) {
					charge *= 2;
				}
			}
			break;
		case UTILITY:
			//charge = 20 * renter dice roll value if owner 
			//              has just this utility
			//       = 50 * renter dice roll value if owner 
			//              has both utilities
			iter = owner.getProperties().iterator();
			
			multiplier = 10;
			
			while(iter.hasNext()) {
				Property nextProperty;
				
				nextProperty = iter.next();
				if (nextProperty.getType() == PropertyType.UTILITY 
						&& nextProperty.getOwner().equals(owner)) {
					if (multiplier == 10) {
						multiplier = 20;
					} else {
						multiplier = 50;
					}
				}
			}
			
			lastRoll = renter.getLastTotalRoll();
			charge   = multiplier * lastRoll;
			break;
		default:
			return;
		}
	
		renter.changeCash(-charge);
		owner.changeCash(charge);
		
		String outcome;
		outcome = String.format("%s pays %s %s bits\n", renter.getName(),
				owner.getName(), charge);
		System.out.println(outcome);
	}
	
	@Override
	public String toString() {
		String stringRep;
		if (getType() == PropertyType.NORMAL) {
			stringRep = String.format("%s: %s (stables: %s) (group: %s)",
					name, value, numStables, group.name().replace("_", " "));
		} else if (getType() == PropertyType.UTILITY) {
			stringRep = String.format("%s: %s (Utility)",
					name, value);
		} else {
			stringRep = String.format("%s: %s",
					name, value);
		}
		
		return stringRep;
	}

	@Override
	public void display(int line, boolean showPlayers) {
		String display;
		
		int displayLength = 37;
		int nameLength    = name.length();
		String propName;
		propName = "#";
		
		for (int i = 0; i < (displayLength/2 - 1) - (nameLength/2); i++) {
			propName += " ";
		}
		propName += name;
		for (int i = 0; i < (displayLength/2 - 1) - (nameLength/2); i++) {
			propName += " ";
		}
		
		while (propName.length() < displayLength - 1) { propName += " "; }
		propName += "#\n";
		
		String priceString = "Price: " + value + " bits";
		int priceLength = priceString.length();
		String propPrice;
		propPrice = "#";
		
		for (int i = 0; i < (displayLength/2 - 1) - (priceLength/2); i++) {
			propPrice += " ";
		}
		propPrice += priceString;
		for (int i = 0; i < (displayLength/2 - 1) - (priceLength/2); i++) {
			propPrice += " ";
		}
		
		while (propPrice.length() < displayLength - 1) { propPrice += " "; }
		propPrice += "#\n";
		
		String playerLine;
		
		if (players.isEmpty() || !showPlayers) {
			playerLine = "#                                   #\n";
		} else {
			playerLine = "#     O - ";
			Iterator<Player> iter = players.iterator();
			while (iter.hasNext()) {
				playerLine += iter.next().getName() + ", ";
			}
			
			playerLine = playerLine.substring(0, playerLine.length()-2);
			while (playerLine.length() < LINEWIDTH - 1) {
				playerLine += " ";
			}
			
			while (playerLine.length() > LINEWIDTH - 1) {
				playerLine = playerLine.substring(0,
						playerLine.length()-4);
				playerLine += "...";
			}
			
			playerLine += "#\n";
		}
		
		String groupName, groupString;
		if (getType() == PropertyType.NORMAL) {
			groupName = getGroup().name().replace("_", " ");
			groupString = " ";
			for (int i = 0; i < groupName.length(); i++) {
				groupString += groupName.charAt(i) + " ";
			}
			
			while (groupString.length() < LINEWIDTH - 5) {
				groupString = "@" + groupString + "@";
			}
			
			if (groupString.length() < LINEWIDTH - 4) groupString += "@";
			
			groupString = "# " + groupString + " #\n";
		} else {
			groupString = "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
		}
		
		display  = "#####################################\n";
		display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
		display += groupString;
		display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
		display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
		display += "#                                   #\n";
		display += propName;
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += playerLine;
		display += "#                                   #\n";
		display += propPrice;
		display += "#####################################\n";
		
		String[] lines;
		lines = display.split("\n");
		System.out.print(lines[line]);
	}
	
	@Override
	public void addPlayer(Player player) {
		players.add(player);
	}

	@Override
	public void removePlayer(Player player) {
		if (players.contains(player)) {
			players.remove(player);
		}
	}
}