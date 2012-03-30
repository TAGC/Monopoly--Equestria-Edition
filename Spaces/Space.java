package Spaces;
import General.Player;

public interface Space {
	
	public static final int DISPLAYLINES = 18;
	public static final int LINEWIDTH    = 37;
	
	/*
	 * @param player
	 *        	the player that lands on this space
	 *        
	 * This method should add players to a set to keep
	 * track of which players are on the space
	 */
	public void addPlayer(Player player);
	
	/*
	 * @param player
	 *        	the player that leaves this space
	 *        
	 * This method should remove players from a set as
	 * they leave the space
	 */
	public void removePlayer(Player player);
	
	/*
	 * @param player
	 *        the player that lands on a space
	 * 
	 * This method should be called when a player 
	 * lands on a space after moving
	 * 
	 * The method should handle events depending on the
	 * type of space landed on
	 */
	public void playerLands(Player player);
	
	/*
	 * @param line
	 *     	  	the line of ASCII characters to display
	 *
	 * This method should be called when a graphical
	 * representation of the board is needed.
	 * 
	 * The method should allow a space to be printed 
	 * out one line at a time
	 * 
	 * Space displays should consist of 18 lines of 
	 * ASCII, all of 37 character width
	 */
	public void display(int line);
}
