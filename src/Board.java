import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.SwingUtilities;

/**
 * 
 * Board class, involves a 2d array made of tiles and boolean fields to signify
 * when the game ends.
 * 
 * @author Harshil Sharma
 * @version 5/27/21
 * 
 */
public class Board implements MouseListener {
	private Tile[][] board;
	private boolean gameOver;
	private boolean victory;

	/**
	 * Constructs a new board.
	 * 
	 * @param r number of rows
	 * @param c number of columns
	 */
	public Board(int r, int c) {
		gameOver = false;
		victory = false;
		board = new Tile[r][c];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new Tile(i, j);
				board[i][j].addMouseListener(this);
			}
		}
	}

	/**
	 * Generates mines for the board.
	 * 
	 * @param mines number of mines
	 */
	public void generateMines(int mines) {
		for (int m = 0; m < mines; m++) {
			int r = (int) (Math.random() * board.length);
			int c = (int) (Math.random() * board[0].length);
			while (board[r][c].isMine() || (board[r][c].getRow() == 4 && board[r][c].getCol() == 4)
					|| (board[r][c].getRow() == 5 && board[r][c].getCol() == 5)
					|| (board[r][c].getRow() == 4 && board[r][c].getCol() == 5)
					|| (board[r][c].getRow() == 5 && board[r][c].getCol() == 4)) {
				r = (int) (Math.random() * board.length);
				c = (int) (Math.random() * board[0].length);
			}
			board[r][c].setMine();
		}
	}

	/**
	 * Calculates the numbers of each tile, given the number of nearby mines.
	 */
	public void setNumbers() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				ArrayList<int[]> surrounding = new ArrayList<int[]>();
				if (!board[row][col].isMine()) {
					surrounding.add(new int[] { row - 1, col - 1 });
					surrounding.add(new int[] { row + 1, col + 1 });
					surrounding.add(new int[] { row - 1, col + 1 });
					surrounding.add(new int[] { row + 1, col - 1 });
					surrounding.add(new int[] { row + 1, col });
					surrounding.add(new int[] { row - 1, col });
					surrounding.add(new int[] { row, col + 1 });
					surrounding.add(new int[] { row, col - 1 });
				}
				for (int[] coords : surrounding) {
					try {
						if (board[coords[0]][coords[1]].isMine()) {
							board[row][col].setNum(board[row][col].getNum() + 1);
						}
					} catch (IndexOutOfBoundsException e) {
					}
				}
			}
		}
	}

	/**
	 * Reveals a tile. Uses a recursive algorithm for 0 tiles, which reveal
	 * everything around them (except mines).
	 * 
	 * @param t         tile to be revealed
	 * @param playSound determines whether sound should be played or not
	 */
	public void reveal(Tile t, boolean playSound) {
		if (t.isRevealed()) {
			return;
		}
		if (t.isFlagged()) {
			return;
		}
		t.setRevealed();
		if (t.isMine()) {
			t.icon("src/Resources/bomb.png");
			gameOver = true;
		} else if (t.getNum() == 0) {
			if (playSound) {
				playSound("src/Resources/reveal.wav");
			}
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (!(x == 0 && y == 0)) {
						try {
							reveal(board[t.getRow() + x][t.getCol() + y], false);
						} catch (IndexOutOfBoundsException e) {
						}
					} else {
						t.icon("src/Resources/0.png");
					}
				}
			}
		} else {
			if (playSound) {
				playSound("src/Resources/reveal.wav");
			}
			t.icon("src/Resources/" + t.getNum() + ".png");
		}
	}

	/**
	 * Reveals all tiles.
	 */
	public void revealAll() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				reveal(board[i][j], false);
			}
		}
	}

	/**
	 * Returns the 2d array of tiles.
	 * 
	 * @return the board array
	 */
	public Tile[][] getBoard() {
		return board;
	}

	/**
	 * Reveals/flags a tile if it was left/right clicked.
	 * 
	 * @param mouse input
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Tile t = (Tile) (e.getComponent());
		if (SwingUtilities.isRightMouseButton(e)) {
			t.flag();
		} else if (SwingUtilities.isLeftMouseButton(e)) {
			reveal(t, true);
		}
	}

	/**
	 * Returns whether or not game is lost.
	 * 
	 * @return true if game is lost, else false
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * determines if there is a victory
	 * 
	 * @return true if game is won, else false
	 */
	public boolean isVictory() {
		if (gameOver) {
			victory = false;
		} else {
			victory = true;
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					if (!board[i][j].isRevealed() && !board[i][j].isMine()) {
						victory = false;
					}
				}
			}
		}
		return victory;
	}

	/**
	 * Plays a given sound file.
	 * 
	 * @param s file address
	 */
	public void playSound(String s) {
		try {
			File f = new File(s);
			AudioInputStream audio = AudioSystem.getAudioInputStream(f);
			Clip sound = AudioSystem.getClip();
			sound.open(audio);
			sound.start();
		} catch (Exception e) {
		}
	}

	/**
	 * No functionality needed here
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * No functionality needed here
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * No functionality needed here
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * No functionality needed here
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
