import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * 
 * Represents an individual tile in the game of Minesweeper.
 * 
 * @author Harshil Sharma
 * @version 5/27/21
 * 
 */
public class Tile extends JButton {
	private int bombNum;
	private boolean mine;
	private boolean flagged;
	private boolean revealed;
	private int row;
	private int col;

	/**
	 * constructs a new tile
	 * 
	 * @param r the row of the tile
	 * @param c the column of the tile
	 */
	public Tile(int r, int c) {
		icon("src/Resources/facingDown.png");
		bombNum = 0;
		flagged = false;
		mine = false;
		revealed = false;
		row = r;
		col = c;
	}

	/**
	 * Helper method to set the icon for a tile.
	 * 
	 * @param s file address/icon
	 */
	public void icon(String s) {
		try {
			this.setIcon(new ImageIcon(ImageIO.read(new File(s)).getScaledInstance(60, 60, Image.SCALE_DEFAULT)));
		} catch (Exception e) {
		}
	}

	/**
	 * flags a tile
	 */
	public void flag() {
		if (!revealed) {
			flagged = !flagged;
			if (flagged) {
				icon("src/Resources/flag.png");
			} else {
				icon("src/Resources/facingDown.png");
			}
		}
	}

	/**
	 * returns the bomb number of this tile, or number of nearby mines
	 * 
	 * @return bomb number of this tile, or number of nearby mines
	 */
	public int getNum() {
		return bombNum;
	}

	/**
	 * Sets the bomb number of this tile.
	 * 
	 * @param i number to be set to
	 */
	public void setNum(int i) {
		bombNum = i;
	}

	/**
	 * makes this tile a mine
	 */
	public void setMine() {
		mine = true;
	}

	/**
	 * checks to see if this tile is a mine
	 * 
	 * @return true if this is a mine, else false
	 */
	public boolean isMine() {
		return mine;
	}

	/**
	 * sets this tile as revealed
	 */
	public void setRevealed() {
		revealed = true;
	}

	/**
	 * checks to see if tile is revealed
	 * 
	 * @return true if revealed, else false
	 */
	public boolean isRevealed() {
		return revealed;
	}

	/**
	 * returns the row of this tile
	 * 
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * returns the column of this tile
	 * 
	 * @return column
	 */
	public int getCol() {
		return col;
	}

	/**
	 * checks to see if this tile is flagged
	 * 
	 * @return true if flagged, else false
	 */
	public boolean isFlagged() {
		return flagged;
	}
}
