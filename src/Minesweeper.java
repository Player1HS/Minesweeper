import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

/**
 * 
 * Main class. Sets up GUI.
 * 
 * @author Harshil Sharma
 * @version 5/27/21
 * 
 */
public class Minesweeper {
	private Board b;
	private int width;
	private int length;
	private int mineCount;
	private JFrame ms;
	private JPanel grid;
	private JPanel menu;
	private JLabel timer;
	private Timer t;
	private int seconds;
	private Font digital;
	private JComboBox<String> difficulties;

	/**
	 * Constructs the GUI (made up of a board, and a menu containing the timer and
	 * difficulty selector).
	 * 
	 * @param l length of board
	 * @param w width of board
	 * @param m number of mines
	 */
	public Minesweeper(int l, int w, int m) {
		width = w;
		length = l;
		mineCount = m;

		grid = new JPanel(new GridLayout(width, length));
		genBoard(mineCount);

		ms = new JFrame();
		ms.setTitle("Minesweeper");
		ms.setSize(600, 660);
		ms.setLayout(new BorderLayout());
		ms.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menu = new JPanel(new BorderLayout());
		menu.setPreferredSize(new Dimension(600, 60));

		timer = new JLabel("");
		timer.setPreferredSize(new Dimension(110, 60));
		try {
			digital = Font.createFont(Font.TRUETYPE_FONT, new File("src/Resources/digital-7.ttf")).deriveFont(47f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(digital);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		timer.setFont(digital);
		timer.setForeground(Color.RED);

		difficulties = new JComboBox<String>(new String[] { "Easy", "Medium", "Hard" });
		difficulties.addActionListener(new ActionListener() {
			/**
			 * ActionListener method for the difficulty selector
			 * 
			 * @param e given action
			 */
			public void actionPerformed(ActionEvent e) {
				JComboBox selection = (JComboBox) e.getSource();
				String level = (String) selection.getSelectedItem();
				t.stop();
				seconds = 0;
				time();
				t.start();
				grid.removeAll();
				if (level == "Easy") {
					genBoard(10);
				} else if (level == "Medium") {
					genBoard(15);
				} else if (level == "Hard") {
					genBoard(20);
				}
			}
		});

		menu.setBackground(Color.BLACK);
		menu.add(difficulties, BorderLayout.LINE_START);
		menu.add(timer, BorderLayout.LINE_END);
		ms.add(menu, BorderLayout.PAGE_START);
		ms.add(grid, BorderLayout.CENTER);
		ms.setVisible(true);
		ms.setResizable(false);

		seconds = 0;
		time();
		t.start();

		while (!b.isGameOver()) {
			System.out.print("");
			if (b.isVictory()) {
				b.playSound("src/Resources/victory.wav");
				t.stop();
				JOptionPane.showMessageDialog(null, "YOU WON");
				System.exit(0);
			}
		}
		b.playSound("src/Resources/death.wav");
		t.stop();
		b.revealAll();
		JOptionPane.showMessageDialog(null, "YOU LOST");
		System.exit(0);
	}

	/**
	 * timing method
	 */
	public void time() {
		t = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (seconds < 10) {
					timer.setText("00" + Integer.toString(seconds));
				} else if (seconds < 100) {
					timer.setText("0" + Integer.toString(seconds));
				} else if (seconds < 1000) {
					timer.setText(Integer.toString(seconds));
				} else {
					timer.setText("999");
				}
				seconds++;
			}
		});
	}

	/**
	 * Generates a board.
	 * 
	 * @param m number of mines
	 */
	public void genBoard(int m) {
		b = new Board(width, length);
		b.generateMines(m);
		b.setNumbers();
		for (int i = 0; i < b.getBoard().length; i++) {
			for (int j = 0; j < b.getBoard()[i].length; j++) {
				grid.add(b.getBoard()[i][j]);
			}
		}
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Minesweeper(10, 10, 10);
	}
}
