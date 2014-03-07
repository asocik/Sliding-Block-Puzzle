/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		March 2014
 * 		CS 342 Software Design
 * 							
 * This class sets up the various features of the GUI using the Java Swing
 * library. All button actions are also implemented in this class.
 * ------------------------------------------------------------------------*/
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import javax.swing.*;

public class Window extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel infoBar;
	private JPanel infoBarButtons;
	private JPanel tiles;
	
	// Information bar items
	private JLabel numOfMovesLabel;
	private JLabel minNumOfMovesLabel;
	private JButton resetButton;
	private JButton hintButton;
	private int numOfMoves;
	private int minNumOfMoves;
	
	//Menu bar items
	private JMenuBar menuBar;
	private JMenu game;
	private JMenu help;
	private JMenuItem hint;
	private JMenuItem solve;
	private JMenuItem reset;
	private JMenuItem exit;
	private JMenuItem rules;
	private JMenuItem about;
	
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	public Window() 
	{
		//---------------------------------------------------------
		// Set up the menu bar
		//---------------------------------------------------------
		menuBar = new JMenuBar();
		game = new JMenu("Game");
		help = new JMenu("Help");
		hint = new JMenuItem("Hint");
		solve = new JMenuItem("Solve");
		reset = new JMenuItem("Reset");
		exit = new JMenuItem("Exit");
		rules = new JMenuItem("Rules");
		about = new JMenuItem("About");
		
		game.add(hint);
		game.add(solve);
		game.add(reset);
		game.add(exit);
		help.add(rules);
		help.add(about);
		menuBar.add(game);
		menuBar.add(help);
		
		// Add action listeners
		hint.addActionListener(this);
		solve.addActionListener(this);
		reset.addActionListener(this);
		exit.addActionListener(this);
		rules.addActionListener(this);
		about.addActionListener(this);

		// Change some of the colors to make the menu stand out
		game.setBackground(Color.DARK_GRAY);
		help.setBackground(Color.DARK_GRAY);
		menuBar.setBackground(Color.DARK_GRAY);
		
		setJMenuBar(menuBar);	// Add menuBar to the frame
		
		//---------------------------------------------------------
		// Set up the information bar bar
		//---------------------------------------------------------
		infoBar = new JPanel(new BorderLayout());
		infoBarButtons = new JPanel(new GridLayout(1, 2));
		numOfMoves = 0;
		minNumOfMoves = 10; 	// TO DO: call function to calculate this value
		numOfMovesLabel = new JLabel("Move Count: " + numOfMoves);
		minNumOfMovesLabel = new JLabel("Minimum Moves Needed: " + minNumOfMoves);
		numOfMovesLabel.setHorizontalAlignment(JLabel.CENTER);
		minNumOfMovesLabel.setHorizontalAlignment(JLabel.CENTER);
		
		hintButton = new JButton("Hint");
		hintButton.addActionListener(this);
		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		
		infoBar.add(numOfMovesLabel, BorderLayout.WEST);
		infoBar.add(minNumOfMovesLabel, BorderLayout.CENTER);
		infoBarButtons.add(hintButton);
		infoBarButtons.add(resetButton);
		infoBar.add(infoBarButtons, BorderLayout.EAST);
		add(infoBar, BorderLayout.NORTH);
		
		//---------------------------------------------------------
		// Set up tiles for the game
		//---------------------------------------------------------
		tiles = new JPanel();
		
		JButton button = new JButton("Tiles will go here");
		tiles.add(button);
		// TO DO: create a method or class to read from file and set up tiles
		
		add(tiles, BorderLayout.WEST);
		//---------------------------------------------------------
		// Set up the out side window to redirect output from 
		// console to text area in GUI
		//---------------------------------------------------------
		textArea = new JTextArea(10, 20);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
        add(scrollPane, BorderLayout.EAST);
	}
	
	/**------------------------------------------------------------------------
	 * Action listeners for various buttons
	 * ------------------------------------------------------------------------*/
	public void incNumOfMoves()
	{
		numOfMoves++;
		numOfMovesLabel.setText("Move Count: " + numOfMoves);
	}
	
	/**------------------------------------------------------------------------
	 * Zero out the move count
	 * ------------------------------------------------------------------------*/
	public void clearMoveCout()
	{
		numOfMoves = 0;
		numOfMovesLabel.setText("Move Count: " + numOfMoves);
	}
	
	/**------------------------------------------------------------------------
	 * Updates the minimum number of moves in the GUI
	 * 
	 * @param	update	New value for minimum number of moves 
	 * ------------------------------------------------------------------------*/
	public void updateMinNumOfMoves(int update)
	{
		minNumOfMoves = update;
		minNumOfMovesLabel.setText("Minimum Moves Needed: " + minNumOfMoves);
	}
	
	/**------------------------------------------------------------------------
	 * Action listeners for various buttons
	 * ------------------------------------------------------------------------*/
	public void actionPerformed(ActionEvent e) 
	{
		// Action listener for Exit menu item
		if (e.getSource() == exit)
			System.exit(0);
		
		// Action listener for Solve menu item
		if (e.getSource() == solve)
		{
			
		}
		
		// Action listener for Rules menu item
		if (e.getSource() == rules)
		{
			String output = "To solve the puzzle move the the goal piece\n"
						  + "all the way to the right side of the board.\n"
						  + "Clear the way by moving the other pieces.\n\n"
						  + "For a challenge try to match the minimum\n"
						  + "moves needed.";
			JOptionPane.showMessageDialog(null, output, "Rules", EXIT_ON_CLOSE);
		}
		
		// Action listener for About menu item
		if (e.getSource() == about)
		{
			JLabel output = new JLabel();	// JLable was used for text positioning 
			// Used html to create newlines in JLabel - can't just use '\n'
			output.setText("<html>Program Developers:<br>Adam Socik<br>Plaimanus "
						+ "Lueondee<br>Khurratul-Ain Naseer<br><br>"
						+ "UIC CS 342 Software Design Spring 2014</html>");
			
			output.setHorizontalAlignment(JLabel.CENTER);
			JOptionPane.showMessageDialog(null, output, "About", EXIT_ON_CLOSE);
		}
		
		// Action listener for Reset options
		if (e.getSource() == reset || e.getSource() == resetButton)
		{
			
		}
		
		// Action listener for Hint options
		if (e.getSource() == hint || e.getSource() == hintButton)
		{
			
		}
	}
}












