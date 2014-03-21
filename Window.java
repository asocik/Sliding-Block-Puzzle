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
import javax.swing.border.BevelBorder;

public class Window extends JFrame implements MouseListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel infoBar;
	private JPanel infoBarButtons;
	private JPanel blockPanel;
	private PuzzleCreator puzzle;
	
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
	private JMenuItem addPuzzle;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	// Items to control the GUI block panel
	private JButton blockButtons[];
	private GridBagConstraints constraints;
	private String command;
	private PuzzleSolver puzzleSolver;
	private volatile int pressedX, pressedY, releasedX, releasedY;

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
		addPuzzle = new JMenuItem("Add Puzzle");
		
		game.add(hint);
		game.add(solve);
		game.add(reset);
		game.add(addPuzzle);
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
		addPuzzle.addActionListener(this);

		// Change some of the colors to make the menu stand out
		game.setBackground(Color.GRAY);
		help.setBackground(Color.GRAY);
		menuBar.setBackground(Color.GRAY);
		
		setJMenuBar(menuBar);	// Add menuBar to the frame
		
		//---------------------------------------------------------
		// Set up the information bar bar
		//---------------------------------------------------------
		infoBar = new JPanel(new BorderLayout());
		infoBarButtons = new JPanel(new GridLayout(1, 2));
		numOfMoves = 0;
		minNumOfMoves = 0; 	
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
		// Set up the out side window to redirect output from 
		// console to text area in GUI
		//---------------------------------------------------------
		textArea = new JTextArea(50, 20);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
        add(scrollPane, BorderLayout.EAST);
        
        //---------------------------------------------------------
		//  Read in data from files and set up tiles for the game
		//---------------------------------------------------------
        puzzle = new PuzzleCreator();	// Reads from file
        blockPanel = new JPanel();
		
		puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
		constraints = new GridBagConstraints();
		blockButtons = new JButton[puzzle.blocks.size()];
		blockPanel.setLayout(new GridBagLayout());
		blockPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		updateGUI();
		setMinNumOfMoves(puzzleSolver.minNumberOfMoves());
		
		add(blockPanel, BorderLayout.CENTER);
		
		
		// Welcome pop out to the user with instructions on how to play
		String output = "Welcome to the game of sliding block puzzles!\n\n"
				  + "To solve the puzzle move the the goal piece (Z)\n"
				  + "all the way to the right side of the board.\n"
				  + "Clear the way by moving the other pieces.\n\n"
				  + "To move  block hover over it with you mouse,\n"
				  + "press it, and slide it up, down, left, or right\n"
				  + "then release. Make sure to keep you mouse over\n"
				  + "the block you are trying to slide.\n\n"
				  + "For a challenge, try to match the minimum\n"
				  + "moves needed.";
		JOptionPane.showMessageDialog(null, output, "Welcome", EXIT_ON_CLOSE);
	}
	
	/**------------------------------------------------------------------------
	 * Increments the number of moves taken
	 * ------------------------------------------------------------------------*/
	public void incNumOfMoves()
	{
		numOfMoves++;
		numOfMovesLabel.setText("Move Count: " + numOfMoves);
	}
	
	/**------------------------------------------------------------------------
	 * Zero out the move count
	 * ------------------------------------------------------------------------*/
	public void clearMoveCount()
	{
		numOfMoves = 0;
		numOfMovesLabel.setText("Move Count: " + numOfMoves);
	}
	
	/**------------------------------------------------------------------------
	 * Updates the minimum number of moves in the GUI
	 * 
	 * @param update	New value for minimum number of moves 
	 * ------------------------------------------------------------------------*/
	public void setMinNumOfMoves(int update)
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
			puzzleSolver.printSolution();
		
		// Action listener for Hint options
		if (e.getSource() == hint || e.getSource() == hintButton)
			puzzleSolver.printHint();
		
		// Action listener for Rules menu item
		if (e.getSource() == rules)
		{
			String output = "To solve the puzzle move the the goal piece (Z)\n"
						  + "all the way to the right side of the board.\n"
						  + "Clear the way by moving the other pieces.\n\n"
						  + "To move  block hover over it with you mouse,\n"
						  + "press it, and slide it up, down, left, or right\n"
						  + "then release. Make sure to keep you mouse over\n"
						  + "the block you are trying to slide.\n\n"
						  + "For a challenge, try to match the minimum\n"
						  + "moves needed.";
			JOptionPane.showMessageDialog(null, output, "Rules", EXIT_ON_CLOSE);
		}
		
		// Action listener for About menu item
		if (e.getSource() == about)
		{			
			String output = "Program Developer: Adam Socik\n"
			+ "UIC CS 342 Software Design Spring 2014";
			JOptionPane.showMessageDialog(null, output, "About", EXIT_ON_CLOSE);
		}
		
		// Action listener for Reset options
		if (e.getSource() == reset || e.getSource() == resetButton)
		{
			puzzle.resetBoard();	// Reset data variables
			puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
			clearMoveCount();		// Reset move counter to 0
			updateGUI();	
		}
		
		// Action listener for adding a new puzzle
		if (e.getSource() == addPuzzle)
		{
			puzzle.addUserPuzzle();
			puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
			clearMoveCount();			// Reset move counter to 0
			setMinNumOfMoves(puzzleSolver.minNumberOfMoves());
			blockButtons = new JButton[puzzle.blocks.size()];
			updateGUI();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	// Code below is for updating the board of blocks in the GUI and supporting methods
	////////////////////////////////////////////////////////////////////////////////////

	/**------------------------------------------------------------------------
	 * Update the blocks on the GUI board
	 * ------------------------------------------------------------------------*/
	public void updateGUI()
	{
		blockPanel.removeAll();
		
		ActionListener blocListener = new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				command = e.getActionCommand();	// Lets program know which block was clicked
			}
		};
		
		// Add each block to the board
		for (int i=0; i<puzzle.blocks.size(); i++)
		{	
			// Create button with ID
			String s = "" + puzzle.blocks.get(i).ID;
			blockButtons[i]= new JButton(s);
			
			// Set location and dimensions of button
			constraints.gridx = puzzle.blocks.get(i).startcol;
			constraints.gridy = puzzle.blocks.get(i).startrow;
			constraints.gridheight = puzzle.blocks.get(i).height;
			constraints.gridwidth = puzzle.blocks.get(i).width;
			
			// Make buttons fill space on grid
			constraints.weightx = constraints.weighty = 1;
			constraints.fill = GridBagConstraints.BOTH;
			
			blockButtons[i].addActionListener(blocListener);
			blockButtons[i].addMouseListener(this);
			blockPanel.add(blockButtons[i], constraints);
		}
		
		// Add the empty boxes - needed to maintain spacing
		for (int i=1; i<puzzle.rows-1; i++)
		{
			for (int j=1; j<puzzle.columns-1; j++)
			{
				if (puzzle.board[i][j] == '.');
				JButton temp = new JButton();
				
				// Set location and dimensions of button
				constraints.gridx = i;
				constraints.gridy = j;
				constraints.gridheight = 1;
				constraints.gridwidth = 1;
				
				// Make buttons fill space on grid
				constraints.weightx = constraints.weighty = 1;
				constraints.fill = GridBagConstraints.BOTH;
				blockPanel.add(temp, constraints);
			}
		}
		
		// Update the GridBagLayout
		blockPanel.revalidate();
		blockPanel.repaint();
	}
	
	/**------------------------------------------------------------------------
	 * Records start point of a click
	 * ------------------------------------------------------------------------*/
	@Override
	public void mousePressed(MouseEvent e) 
	{
		pressedX = e.getX();
		pressedY = e.getY();
	}

	/**------------------------------------------------------------------------
	 * Records endpoint of a click, uses data to calculate which direction the
	 * user wanted to move a block, calls appropriate move method. 
	 * ------------------------------------------------------------------------*/
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		releasedX = e.getX();
		releasedY = e.getY();
		
		// @see Pythagorean Theorem
		int xdistance = (int) Math.pow(releasedX-pressedX, 2);
		int ydistance = (int) Math.pow(releasedY-pressedY, 2);
		
		if (xdistance > ydistance)	// Move horizontally
		{
			if (pressedX < releasedX)	// Move right
				moveRight();	
			else	// Move left
				moveLeft();
		}
		else // Move vertically
		{
			if (pressedY < releasedY)	// Move up
				moveDown();
			else	// Move down
				moveUp();
		}
	}

	/**------------------------------------------------------------------------
	 * Moves a block one spot to the right and calls appropriate methods to 
	 * update game data
	 * ------------------------------------------------------------------------*/
	public void moveRight()
	{
		if (puzzle.blocks.get(getBlockIndex()).direction == 'v')
		{
			System.out.println("This block can only be moved\nvertically");
			return;
		}
		
		// Not needed but makes code more readable
		int startrow = puzzle.blocks.get(getBlockIndex()).startrow;
		int startcol = puzzle.blocks.get(getBlockIndex()).startcol;
		int height = puzzle.blocks.get(getBlockIndex()).height;
		int width = puzzle.blocks.get(getBlockIndex()).width;
		boolean canMoveRight = true;
		
		// Check if block can move right
		for (int i=0; i<height; i++)
		{
			if (puzzle.board[startrow+i][startcol+width] != '.')
				canMoveRight = false;
		}
		
		if (canMoveRight)
		{
			puzzle.blocks.get(getBlockIndex()).startcol += 1;
			updateGUI();
			
			// Update the board to reflect the move
			char visited[][] = new char [puzzle.rows][puzzle.columns];
			for (int i=0; i<puzzle.rows; i++)
				for (int j=0; j<puzzle.columns; j++)
					visited[i][j]= 'u';
			
			for (int i=0; i<height; i++)
			{
				for (int j=0; j<width; j++)
				{
					puzzle.board[startrow+i][startcol+1+j] = command.charAt(0);
					visited[startrow+i][startcol+1+j] = 'v';
					
					if (visited[startrow+i][startcol+j] == 'u')
						puzzle.board[startrow+i][startcol+j] = '.';
				}
			}
			
			incNumOfMoves();
			checkIfWon();
			puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
		}
	}
	
	/**------------------------------------------------------------------------
	 * Moves a block one spot to the left and calls appropriate methods to 
	 * update game data
	 * ------------------------------------------------------------------------*/
	public void moveLeft()
	{
		if (puzzle.blocks.get(getBlockIndex()).direction == 'v')
		{
			System.out.println("This block can only be moved\nvertically");
			return;
		}
		
		// Not needed but makes code more readable
		int startrow = puzzle.blocks.get(getBlockIndex()).startrow;
		int startcol = puzzle.blocks.get(getBlockIndex()).startcol;
		int height = puzzle.blocks.get(getBlockIndex()).height;
		int width = puzzle.blocks.get(getBlockIndex()).width;
		boolean canMoveLeft = true;
		
		// Check if block can move left
		for (int i=0; i<height; i++)
			if (puzzle.board[startrow+i][startcol-1] != '.')
				canMoveLeft = false;
		
		if (canMoveLeft)
		{
			puzzle.blocks.get(getBlockIndex()).startcol -= 1;
			
			updateGUI();
			
			// Update the board to reflect the move
			char visited[][] = new char [puzzle.rows][puzzle.columns];
			for (int i=0; i<puzzle.rows; i++)
				for (int j=0; j<puzzle.columns; j++)
					visited[i][j]= 'u';
						
			for (int i=0; i<height; i++)
			{
				for (int j=0; j<width; j++)
				{
					puzzle.board[startrow+i][startcol-1+j] = command.charAt(0);
					visited[startrow+i][startcol-1+j] = 'v';
					
					if (visited[startrow+i][startcol+j] == 'u')
						puzzle.board[startrow+i][startcol+j] = '.';
				}
			}
			incNumOfMoves();
			checkIfWon();
			puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
		}
	}
	
	/**------------------------------------------------------------------------
	 * Moves a block one spot up and calls appropriate methods to update game data
	 * ------------------------------------------------------------------------*/
	public void moveUp()
	{
		if (puzzle.blocks.get(getBlockIndex()).direction == 'h')
		{
			System.out.println("This block can only be moved\nhorizontally");
			return;
		}
		
		// Not needed but makes code more readable
		int startrow = puzzle.blocks.get(getBlockIndex()).startrow;
		int startcol = puzzle.blocks.get(getBlockIndex()).startcol;
		int height = puzzle.blocks.get(getBlockIndex()).height;
		int width = puzzle.blocks.get(getBlockIndex()).width;
		boolean canMoveUp = true;
		
		for (int i=0; i<width; i++)
			if (puzzle.board[startrow-1][startcol+i] != '.')
				canMoveUp = false;
		
		if (canMoveUp)
		{
			puzzle.blocks.get(getBlockIndex()).startrow -= 1;
		
			updateGUI();
			
			// Update the board to reflect the move
			char visited[][] = new char [puzzle.rows][puzzle.columns];
			for (int i=0; i<puzzle.rows; i++)
				for (int j=0; j<puzzle.columns; j++)
					visited[i][j]= 'u';
			
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					puzzle.board[startrow-1+j][startcol+i] = command.charAt(0);
					visited[startrow-1+j][startcol+i] = 'v';
					
					if (visited[startrow+j][startcol+i] == 'u')
						puzzle.board[startrow+j][startcol+i] = '.';
				}
			}
			incNumOfMoves();
			checkIfWon();
			puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
		}
	}
	
	/**------------------------------------------------------------------------
	 * Moves a block one spot down and calls appropriate methods to update game data
	 * ------------------------------------------------------------------------*/
	public void moveDown()
	{
		if (puzzle.blocks.get(getBlockIndex()).direction == 'h')
		{
			System.out.println("This block can only be moved\nhorizontally");
			return;
		}
		
		// Not needed but makes code more readable
		int startrow = puzzle.blocks.get(getBlockIndex()).startrow;
		int startcol = puzzle.blocks.get(getBlockIndex()).startcol;
		int height = puzzle.blocks.get(getBlockIndex()).height;
		int width = puzzle.blocks.get(getBlockIndex()).width;
		boolean canMoveDown = true;
		
		for (int i=0; i<width; i++)
			if (puzzle.board[startrow+height][startcol+i] != '.')
				canMoveDown = false;
		
		if (canMoveDown)
		{
			puzzle.blocks.get(getBlockIndex()).startrow += 1;

			updateGUI();
			
			// Update the board to reflect the move
			char visited[][] = new char [puzzle.rows][puzzle.columns];
			for (int i=0; i<puzzle.rows; i++)
				for (int j=0; j<puzzle.columns; j++)
					visited[i][j]= 'u';
			
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					puzzle.board[startrow+1+j][startcol+i] = command.charAt(0);
					visited[startrow+1+j][startcol+i] = 'v';
					
					if (visited[startrow+j][startcol+i] == 'u')
						puzzle.board[startrow+j][startcol+i] = '.';
				}
			}
			incNumOfMoves();
			checkIfWon();
			puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
		}
	}
	
	/**------------------------------------------------------------------------
	 * Returns the index in the list that relates to the button clicked
	 * ------------------------------------------------------------------------*/
	public int getBlockIndex()
	{
		for (int i=0; i<puzzle.blocks.size(); i++)
			if (command.charAt(0) == puzzle.blocks.get(i).ID)
				return i;
		return -1;	// Failure, should never happen
	}
	
	/**------------------------------------------------------------------------
	 * Checks if the game was won in the GUI - the game is won when the goal
	 * piece reches the right side of the board.
	 * ------------------------------------------------------------------------*/
	public void checkIfWon()
	{
		for (int i=1; i<puzzle.rows-1; i++)	// Scan rightmost column for goal piece
		{
			if (puzzle.board[i][puzzle.columns-2] == 'Z')	// Solution found
			{
				// Prompt user to keep playing
				String output = "Congradulations, you solved the puzzle!\n\nTry the next one?";
				int answer = JOptionPane.showConfirmDialog
				(null, output, "You Win!", JOptionPane.YES_NO_OPTION);
				
				// Set up next puzzle
				if (answer == JOptionPane.YES_OPTION)	// Load in the next puzzle
				{
					puzzle.loadNextPuzzle(puzzle.nextPuzzle());
					puzzleSolver = new PuzzleSolver(puzzle.rows, puzzle.columns, puzzle.board, puzzle.blocks);
					clearMoveCount();			// Reset move counter to 0
					setMinNumOfMoves(puzzleSolver.minNumberOfMoves());
					blockButtons = new JButton[puzzle.blocks.size()];
					updateGUI();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Goodbye");
			        System.exit(0);
				}
				
				return;
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}	

	@Override
	public void mouseClicked(MouseEvent e) {}
	
	/**------------------------------------------------------------------------
	 * Main creates the Jframe for the GUI
	 * ------------------------------------------------------------------------*/
	public static void main(String[] args) 
	{
		JFrame window = new Window();
		window.setTitle("Sliding Block Puzzles");
		window.setVisible(true);
		window.setSize(900, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
