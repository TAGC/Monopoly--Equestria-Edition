package Cards;

import General.Board;
import Spaces.Property;
import Spaces.Space;

public class PropertyCard {
	
	private Property property;
	private Board    board;
	
	public PropertyCard(Property property, Board board) {
		this.property = property;
		this.board    = board;
	}
	
	public Property getProperty() {
		return property;
	}
	
	public void display() {
		String display;
		String groupString, groupName, propName, stableCostString;
		String rentLine;
		String[] rentLines;
		int stableCost;
		
		propName = property.getName();
		while (propName.length() < Space.LINEWIDTH - 3) {
			propName = " " + propName + " ";
		}
		
		if (propName.length() < Space.LINEWIDTH - 2) propName += " ";
		propName = "#" + propName + "#\n";
		
		switch(property.getType()) {
		case NORMAL:
			groupName = property.getGroup().name().replace("_", " ");
			groupString = " ";
			for (int i = 0; i < groupName.length(); i++) {
				groupString += groupName.charAt(i) + " ";
			}
			
			while (groupString.length() < Space.LINEWIDTH - 5) {
				groupString = "@" + groupString + "@";
			}
			
			if (groupString.length() < Space.LINEWIDTH - 4) {
				groupString += "@";
			}
			
			groupString = "# " + groupString + " #\n";
			
			stableCost = board.findPropertyIndex(property.getName())
					/ (Board.BOARDSPACES / 4) + 1;
			
			stableCost *= 50;
			
			stableCostString = "Stables cost " + stableCost + " bits each";
			
			while (stableCostString.length() < Space.LINEWIDTH - 3) {
				stableCostString = " " + stableCostString + " ";
			}
			
			if (stableCostString.length() < Space.LINEWIDTH - 2) {
				stableCostString += " ";
			}
			
			stableCostString = "#" + stableCostString + "#\n";
			
			rentLines = new String[Property.MAXSTABLES+1];
			for (int i = 0; i < rentLines.length; i++) {
				rentLine = String.format("# %s stable/s: %s bits",
						i, property.getCharge(0, i, false));
				while (rentLine.length() < Space.LINEWIDTH - 1) {
					rentLine += " ";
				}
				rentLine += "#\n";
				rentLines[i] = rentLine;
			}

			display  = "#####################################\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += groupString;
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "#                                   #\n";
			display += propName;
			display += "#                                   #\n";
			display += "# Rent with:                        #\n";
			for (String line : rentLines) display += line;
			display += "#                                   #\n";
			display += stableCostString;
			display += "#                                   #\n";
			display += "# Note: If a whole property set is  #\n";
			display += "# owned, rent is doubled on all     #\n";
			display += "# UNIMPROVED properties in that     #\n";
			display += "# set                               #\n";
			display += "#                                   #\n";
			display += "#####################################";
			break;
			
		case STATION:
			rentLines = new String[4];
			for (int i = 0; i < rentLines.length; i++) {
				rentLine = String.format("# %s station/s are owned: %s bits",
						(i+1), ((int)(100*Math.pow(2, i))));
				while (rentLine.length() < Space.LINEWIDTH - 1) {
					rentLine += " ";
				}
				rentLine += "#\n";
				rentLines[i] = rentLine;
			}
			
			display  = "#####################################\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "#                                   #\n";
			display += propName;
			display += "#                                   #\n";
			display += "# Rent if:                          #\n";
			for (String line : rentLines) display += line;
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#####################################";
			break;
		
		case UTILITY:
			display  = "#####################################\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ #\n";
			display += "#                                   #\n";
			display += propName;
			display += "#                                   #\n";
			display += "# Rent if:                          #\n";
			display += "# 1 utility owned:                  #\n";
			display += "#   20 * renter's total roll value  #\n";
			display += "# 2 utilities owned:                #\n";
			display += "#   50 * renter's total roll value  #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#                                   #\n";
			display += "#####################################";
			break;
		
		default:
			return;
		}
		
		System.out.println(display + "\n");
	}
}
