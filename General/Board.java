package General;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Cards.EventCard;
import Cards.EventCard.CardEffect;
import Cards.EventCard.EventCardType;
import Cards.PropertyCard;
import Spaces.ChanceSpace;
import Spaces.FreeSpace;
import Spaces.GoSpace;
import Spaces.GoToMoonSpace;
import Spaces.HarmonySpace;
import Spaces.MoonSpace;
import Spaces.Property;
import Spaces.Property.PropertyGroup;
import Spaces.Property.PropertyType;
import Spaces.Space;
import Spaces.TaxSpace;

public class Board {
	
	private Player[] players;
	private static Space[] spaces;
	private static PropertyCard[] propertyCards;
	private static EventCard[] harmonyCards;
	private static EventCard[] chanceCards;
	public static boolean initialised = false;
	private Player currentPlayer;
	
	public static final int BOARDSPACES  = 40;
	public static final int PROPNUM      = 28;
	public static final int EVENTCARDNUM = 16;
	
	public Board(Player[] players) {
		if (!initialised) initialise();
		this.players = players;
		currentPlayer = players[0];
		
		for (EventCard card : harmonyCards) {
			card.addBoard(this);
		}
		
		for (EventCard card : chanceCards) {
			card.addBoard(this);
		}
		
		//initialises player positions
		for (Player player : players) {
			player.addBoard(this);
			player.setPosition(0, this);
		}
		
		//initialises property cards
		propertyCards = new PropertyCard[PROPNUM];
		int propCardIndex = 0;
		for (Space space : getSpaces()) {
			if (space instanceof Property) {
				Property aProperty = (Property)space;
				propertyCards[propCardIndex] = new PropertyCard(
						aProperty, this);
				propCardIndex++;
			}
		}
	}
	
	public boolean gameFinished() {
		int players_left;
		
		players_left = 0;
		for (Player player : players) {
			if (!player.isBankrupt()) players_left++;
		}
		return players_left <= 1;
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Space[] getSpaces() {
		return spaces;
	}
	
	public PropertyCard getPropertyCard(String propertyName) {
		for (PropertyCard propCard : propertyCards) {
			if (propCard.getProperty().getName().equals(propertyName)) {
				return propCard;
			}
		}
		
		return null;
	}
	
	public void completeTurn() {
		if (currentPlayer.getExtraTurn()) {
			currentPlayer.setExtraTurn(false);
			return;
		}
		
		for (int i = 0; i < players.length; i++) {
			if (currentPlayer == players[i]) {
				currentPlayer = players[(i+1) % players.length];
				break;
			}
		}
	}
	
	public Property[] getGroupProperties(Property property) {
		PropertyGroup group = property.getGroup();
		List<Property> groupProperties;
		Property[] properties;
		Property currentProperty;
		
		groupProperties = new LinkedList<Property>();
		for (Space space : getSpaces()) {
			if (space instanceof Property) {
				currentProperty = (Property)space;
				if (currentProperty.getType() != PropertyType.NORMAL) {
					continue;
				}
				
				if (currentProperty.getGroup().equals(group)) {
					groupProperties.add(currentProperty);
				}
			}
		}
		
		properties = new Property[]{};
		properties = groupProperties.toArray(properties);
		return properties;
	}
	
	public static EventCard getEventCard(EventCardType type) {
		Random gen;
		int index;
		EventCard card;
		
		gen = new Random();
		switch(type) {
		case CHANCE:
			index = gen.nextInt(chanceCards.length);
			card = chanceCards[index];
			return card;
		case HARMONY:
			index = gen.nextInt(harmonyCards.length);
			card = harmonyCards[index];
			return card;
		default:
			return null;
		}
	}
	
	public void handleEvents(Player player) {
		int boardPosition;
		Space space;
		
		boardPosition = player.getBoardPosition();
		space         = spaces[boardPosition];
		
		if (space instanceof Property
			&& ((Property)space).getOwner() == null) {
			
			getPropertyCard(((Property)space).getName()).display();
		} else if (!(space instanceof ChanceSpace
		          || space instanceof HarmonySpace)) {
			for (int i = 0; i < Space.DISPLAYLINES; i++) {
				space.display(i, false);
				System.out.print("\n");
			}
			System.out.println();
		}
		
		space.playerLands(player);
	}
	
	public int findPropertyIndex(String propertyName) {
		Space space;
		Property property;
		int i;
		
		i = 0;
		while (true) {
			space = getSpaces()[i];
			if (space instanceof Property) {
				property = (Property)space;
				if (property.getName().equals(propertyName)) {
					return i;
				}
			} 
			i++;
		}
	}
	
	public void display() {
		Space space;
		
		//top row of spaces
		for (int i = 0; i < Space.DISPLAYLINES; i++) {
			for (int j = (BOARDSPACES/4); j <= (BOARDSPACES/4 + 10); j++) {
				space = spaces[j];
				space.display(i, true);
			}
			System.out.print("\n");
		}
		
		//middle spaces
		Space pairing;
		for (int i = (BOARDSPACES/4 - 1); i > 0; i--) {
			for (int j = 0; j < Space.DISPLAYLINES; j++) {
				space = spaces[i];
				pairing = spaces[BOARDSPACES/2 + (BOARDSPACES/4 - i)];
				space.display(j, true);
				for (int k = 0; k < (9 * Space.LINEWIDTH); k++) {
					System.out.print(" ");
				}
				pairing.display(j, true);
				System.out.print("\n");
			}
		}
		
		//bottom row of spaces
		for (int i = 0; i < Space.DISPLAYLINES; i++) {
			for (int j = 0; j <= BOARDSPACES/4; j++) {
				space = spaces[(BOARDSPACES - j) % BOARDSPACES];
				space.display(i, true);
			}
			System.out.print("\n");
		}
	}
	
	/*
	 * Constructs the properties / Chance cards / Harmony Cards
	 */
	public static void initialise() {
		spaces = new Space[BOARDSPACES];
		
		spaces[0] = new GoSpace();
		
		spaces[1] = new Property("Sweet Apple Acres", 
				50, PropertyType.NORMAL,
				PropertyGroup.APPLEJACK);
		
		spaces[2] = new HarmonySpace();
		
		spaces[3] = new Property("Appleloosa Acres",
				75, PropertyType.NORMAL,
				PropertyGroup.APPLEJACK);
		
		spaces[4] = new TaxSpace("Income Tax", 150,
				"Income tax is due; pay up");
		
		spaces[5] = new Property("Appleloosa Station",
				200, PropertyType.STATION,
				null);
		
		spaces[6] = new Property("Woodland Cottage",
				100, PropertyType.NORMAL,
				PropertyGroup.FLUTTERSHY);
		
		spaces[7] = new ChanceSpace();
		
		spaces[8] = new Property("Froggy Bottom Bog",
				100, PropertyType.NORMAL,
				PropertyGroup.FLUTTERSHY);
		
		spaces[9] = new Property("Everfree Clearing",
				120, PropertyType.NORMAL,
				PropertyGroup.FLUTTERSHY);
		
		spaces[10] = new MoonSpace();
		
		spaces[11] = new Property("Prank Store",
				160, PropertyType.NORMAL,
				PropertyGroup.PINKIE_PIE);
		
		spaces[12] = new Property("Rainbow Factory",
				250, PropertyType.UTILITY,
				null);
		
		spaces[13] = new Property("Rock Farm",
				160, PropertyType.NORMAL,
				PropertyGroup.PINKIE_PIE);
		
		spaces[14] = new Property("Sugarcube Corner",
				180, PropertyType.NORMAL,
				PropertyGroup.PINKIE_PIE);
		
		spaces[15] = new Property("Trottingham Station",
				200, PropertyType.STATION,
				null);
		
		spaces[16] = new Property("Cloud House",
				220, PropertyType.NORMAL,
				PropertyGroup.RAINBOW_DASH);
		
		spaces[17] = new HarmonySpace();
		
		spaces[18] = new Property("Wonderbolt Stadium Track",
				220, PropertyType.NORMAL,
				PropertyGroup.RAINBOW_DASH);
		
		spaces[19] = new Property("Cloudsdale Colosseum",
				240, PropertyType.NORMAL,
				PropertyGroup.RAINBOW_DASH);
		
		spaces[20] = new FreeSpace();
		
		spaces[21] = new Property("Ponyville Gem Field",
				280, PropertyType.NORMAL,
				PropertyGroup.RARITY);
		
		spaces[22] = new ChanceSpace();
		
		spaces[23] = new Property("Aloe & Lotus' Day Spa",
				280, PropertyType.NORMAL,
				PropertyGroup.RARITY);
		
		spaces[24] = new Property("Carousel Boutique",
				300, PropertyType.NORMAL,
				PropertyGroup.RARITY);
		
		spaces[25] = new Property("Ponyville Station",
				200, PropertyType.STATION,
				null);
		
		spaces[26] = new Property("Quills and Sofas Store",
				340, PropertyType.NORMAL,
				PropertyGroup.TWILIGHT_SPARKLE);
		
		spaces[27] = new Property("Canterlot Home",
				340, PropertyType.NORMAL,
				PropertyGroup.TWILIGHT_SPARKLE);
		
		spaces[28] = new Property("Equestria Mail",
				250, PropertyType.UTILITY,
				null);
		
		spaces[29] = new Property("Tree Library",
				360, PropertyType.NORMAL,
				PropertyGroup.TWILIGHT_SPARKLE);
		
		spaces[30] = new GoToMoonSpace();
		
		spaces[31] = new Property("Luna's Observatory",
				400, PropertyType.NORMAL,
				PropertyGroup.LUNA);
		
		spaces[32] = new Property("Canterlot Archives",
				400, PropertyType.NORMAL,
				PropertyGroup.LUNA);
		
		spaces[33] = new HarmonySpace();
		
		spaces[34] = new Property("Canterlot Gardens",
				420, PropertyType.NORMAL,
				PropertyGroup.LUNA);
		
		spaces[35] = new Property("Canterlot Station",
				200, PropertyType.STATION,
				null);
		
		spaces[36] = new ChanceSpace();
		
		spaces[37] = new Property("School For Gifted Unicorns",
				500, PropertyType.NORMAL,
				PropertyGroup.CELESTIA);
		
		spaces[38] = new TaxSpace("Super Tax", 100,
				"Cough up dem bits!");
		
		spaces[39] = new Property("Canterlot Main Square",
				600, PropertyType.NORMAL,
				PropertyGroup.CELESTIA);
		
		//initialise harmony cards
		harmonyCards = new EventCard[EVENTCARDNUM];
		EventCardType type;
		
		type = EventCardType.HARMONY;
		harmonyCards[0] = new EventCard(
				"We Are Amused",
				"Your antics please Luna. You can keep this card; it's " +
				"good for one free escape from the Moon",
				CardEffect.HARMONY_1,
				type);
		
		harmonyCards[1] = new EventCard(
				"I Just Don't Know What Went Wrong",
				"Derpy has an accident; pay 50 bits for every stable " +
				"you own",
				CardEffect.HARMONY_2,
				type);
		
		harmonyCards[2] = new EventCard(
				"Don't Feed The Parasprites",
				"Celestia hires you to sort out the parasprite " +
				"infestation in Fillydelphia. Receive 400 bits",
				CardEffect.HARMONY_3,
				type);
		
		harmonyCards[3] = new EventCard(
				"Defeat The TimberWolves",
				"If Sweet Apple Acres is owned by another player, " +
				"receive 50 bits from them, otherwise you receive the " +
				"property itself",
				CardEffect.HARMONY_4,
				type);
		
		harmonyCards[4] = new EventCard(
				"Spike's Birthday",
				"All players pay 100 bits to the bank",
				CardEffect.HARMONY_5,
				type);
		
		harmonyCards[5] = new EventCard(
				"Catch The Equestria Train",
				"Advance to the nearest station. If it's owned by " +
				"someone else, pay them twice the amount you'd " +
				"otherwise have paid",
				CardEffect.HARMONY_6,
				type);
		
		harmonyCards[6] = new EventCard(
				"Great Friendship Report",
				"Gain 100 bits from Celestia for a good report",
				CardEffect.HARMONY_7,
				type);
		
		harmonyCards[7] = new EventCard(
				"Exploring Everfree Ruins",
				"You're exploring the Everfree Ruins. If your last " +
				"roll totalled an even number, a cockatrice chases " +
				"you, forcing you to leave behind 150 bits worth of " +
				"equipment.\nIf your last roll totalled an odd " +
				"number, you discover 600 bit's worth of treasure",
				CardEffect.HARMONY_8,
				type);
		
		harmonyCards[8] = new EventCard(
				"Attend The Grand Galloping Gala",
				"Earn 100 bits and miss a turn",
				CardEffect.HARMONY_9,
				type);
		
		harmonyCards[9] = new EventCard(
				"Discorded Dice",
				"Move back twice the number of spaces you moved in " +
				"your current turn",
				CardEffect.HARMONY_10,
				type);
		
		harmonyCards[10] = new EventCard(
				"Bringing Home The Southern Birds",
				"You receive 50 bits for helping out during Winter " +
				"Wrap Up in Ponyville",
				CardEffect.HARMONY_11,
				type);
		
		harmonyCards[11] = new EventCard(
				"Upset Fluttershy",
				"You made Fluttershy cry; go straight to the Moon, do " +
				"not Pass Go, do not collect " + GoSpace.PASSGOBONUS +
				" bits",
				CardEffect.HARMONY_12,
				type);
		
		harmonyCards[12] = new EventCard(
				"Zap Apple Harvest",
				"It's Zap Apple season! Receive 25 bits for helping out " +
				"during the harvest and an additional 50 bits for every " +
				"Applejack property you own",
				CardEffect.HARMONY_13,
				type);
		
		harmonyCards[13] = new EventCard(
				"The Fun Has Been Doubled!",
				"Get an extra turn",
				CardEffect.HARMONY_14,
				type);
		
		harmonyCards[14] = new EventCard(
				"Not An Egghead; Just Well-Read",
				"Pay 25 bits for every Applejack or Rainbow Dash " +
				"property you own, but receive 50 bits for every " +
				"Twilight Sparkle property you own",
				CardEffect.HARMONY_15,
				type);
		
		harmonyCards[15] = new EventCard(
				"Flutterstare",
				"Fluttershy gives you The Stare. Miss a turn",
				CardEffect.HARMONY_16,
				type);
		
		chanceCards = new EventCard[EVENTCARDNUM];
		type = EventCardType.CHANCE;
		
		chanceCards[0] = new EventCard(
				"Scootaloo Switch-A-Roo",
				"A property of yours is randomly swapped with " +
				"another player's property of equal or lower " +
				"value. If this is not possible, the card has no " +
				"effect",
				CardEffect.CHANCE_1,
				type);
		
		chanceCards[1] = new EventCard(
				"The Nighttine has begun!",
				"Nightmare Moon returns to cover Equestria in a " +
				"veil of night. Miss a turn whilst recovering " +
				"the Elements of Harmony to defeat her again",
				CardEffect.CHANCE_2,
				type);
		
		chanceCards[2] = new EventCard(
				"Poisonjoke",
				"You've been afflicted with a random ailment. Roll " +
				"a die to determine the effects",
				CardEffect.CHANCE_3,
				type);
		
		chanceCards[3] = new EventCard(
				"Super Cider Squeeze-Off",
				"The Flim Flam Brothers visit town. You compete with " +
				"them, you win, and you earn 500 bits in profits from " +
				"all the cider you sell afterwards",
				CardEffect.CHANCE_4,
				type);
		
		chanceCards[4] = new EventCard(
				"Made Somepony Smile!",
				"Draw a Harmony card",
				CardEffect.CHANCE_5,
				type);
		
		chanceCards[5] = new EventCard(
				"Heavy Order",
				"You placed an order for new dresses with Rarity; pay " +
				"owner of the Carousel Boutique 150 bits, or pay the " +
				"bank 100 bits if no one owns it",
				CardEffect.CHANCE_6,
				type);
		
		chanceCards[6] = new EventCard(
				"Party At Pinkie's Place",
				"Go to Sugarcube Corner; if you Pass Go, collect " +
				GoSpace.PASSGOBONUS + " bits",
				CardEffect.CHANCE_7,
				type);
		
		chanceCards[7] = new EventCard(
				"Friendly Favour",
				"Pay the player that rolls after you 50 bits and roll again",
				CardEffect.CHANCE_8,
				type);
		
		chanceCards[8] = new EventCard(
				"Friendship Is Courage",
				"Stand up for your friends. Stare down a dragon. Receive " +
				"150 bits for your bravery",
				CardEffect.CHANCE_9,
				type);
		
		chanceCards[9] = new EventCard(
				"Do You Like Bananas?",
				"Yes? No? Not sure? Whatever the case, Celestia sends you " +
				"to the Moon! Do not Pass Go, do not collect " + 
				GoSpace.PASSGOBONUS + " bits",
				CardEffect.CHANCE_10,
				type);
		
		chanceCards[10] = new EventCard(
				"We Are Amused",
				"Your antics please Luna. You can keep this card; it's " +
				"good for one free escape from the Moon",
				CardEffect.CHANCE_11,
				type);
		
		chanceCards[11] = new EventCard(
				"Becoming Popular",
				"Receive 25 bits from every player or draw a Harmony card",
				CardEffect.CHANCE_12,
				type);
		
		chanceCards[12] = new EventCard(
				"Trixie's In Town",
				"Miss a turn to watch The Great and Powerful Trixie's " +
				"Magic Show",
				CardEffect.CHANCE_13,
				type);
		
		chanceCards[13] = new EventCard(
				"Broken Promises",
				"You broke a Pinkie Pie promise! Pay all other players " +
				"50 bits each",
				CardEffect.CHANCE_14,
				type);
		
		chanceCards[14] = new EventCard(
				"Happy Derpy Day",
				"Receive 100 bits (and a muffin)",
				CardEffect.CHANCE_15,
				type);
		
		chanceCards[15] = new EventCard(
				"Rainbow Crash",
				"Pay 50 bits to the owner of the Tree Library or 75 bits " +
				"to the bank if no one owns it",
				CardEffect.CHANCE_16,
				type);
		
		initialised = true;
	}
}
