/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		March 2014
 * 		CS 342 Software Design
 * 							
 * This class contains the objects and variables to read files and create
 * a puzzle. File error checking is handled as well as making sure that
 * a file creates a valid puzzle. A valid puzzle contains blocks that:
 * 		1. Do not fall outside of the board
 * 		2. Do not overlap
 * 		3. Have valid movement directions (up, down, left, right)
 * ------------------------------------------------------------------------*/
import java.util.*;
import javax.swing.*;
import java.io.*;

public class PuzzleCreator extends JFrame
{
	private static final long serialVersionUID = 1L;
	private ArrayList<String> puzzleFiles;	// List of all pre-loaded puzzles
	private int puzzleCounter;				// Counter for which puzzle to load
	private ArrayList<Block> resetBlocks;
	private char resetBoard[][];
	protected char board[][];
	protected ArrayList<Block> blocks;					
	protected int rows;			
	protected int columns;		
	
	public PuzzleCreator() 
	{
		puzzleCounter = 0;
		puzzleFiles = new ArrayList<String>();
		blocks = new ArrayList<Block>();
		
		// Add ten puzzle file names to list
		for (int i=0; i<10; i++)
			puzzleFiles.add("puzzle"+ i +".txt");
		
		loadNextPuzzle(0);
	}
	
	/**------------------------------------------------------------------------
	 * Method reads a file and creates a new puzzle from file information. 
	 * The first values read in are the rows and columns of the board, if they
	 * are invalid then the program notifies the user in stderr and exits.
	 * The method then reads in each block from the file and error checks it to 
	 * make sure that if a block were to fall outside the board, have an invalid 
	 * direction of movement, or overlap with another block, it is not added to 
	 * the board.
	 * 
	 * @param source	puzzle in puzzleFiles to load
	 * ------------------------------------------------------------------------*/
	public void loadNextPuzzle(int source) 
	{
		try		// Try to read data from file
		{
			// If all puzzles were opened
			if (puzzleCounter == 10)
			{
				String output = "Congratulations, you solved all of the puzzles\n"
							  + "Exit to close program";
				
				JOptionPane.showMessageDialog(null, output, "You win!", EXIT_ON_CLOSE);
				System.exit(0);
			}
			
			Scanner fileReader = new Scanner(new File(puzzleFiles.get(source)));
			rows = fileReader.nextInt() + 2;		// +2 for a border
			columns = fileReader.nextInt() + 2;
			
			if (rows < 1 || columns < 1)
			{
				System.err.println("Error: Check board row or column dimensions for "
								   + puzzleFiles.get(source));
				System.exit(-1);
			}
			
			// Clear out old data
			board = new char[rows][columns];	
			blocks.clear();
			
			// Fill the board with all '*'
			for (int i = 0; i < rows; i++)
				for (int j=0; j<columns; j++)
					board[i][j] = '*';
			
			boolean containsGoal = false;
			int blockCount = 49;	// ASCII value for 1
			
			// Read in each block and set it on the board
			while (fileReader.hasNext()) 
			{
				int startrow = fileReader.nextInt();
				int startcol = fileReader.nextInt();
				int width = fileReader.nextInt();
				int height = fileReader.nextInt();
				char direction = fileReader.next().charAt(0);
				
				//---------------------------------------------------------
				// Check to see if the piece falls within the puzzle boarders
				//---------------------------------------------------------
				// First set checks if block falls in valid range of rows
				// Second set calculates max possible height of a block can fit in 
				// from a given starting point and sees if the block would fit
				if (!((startrow > 0 && startrow < rows-1) && ((rows - startrow - 1) >= height)))
				{
					System.out.println("Error: block does not fit on board.\n Check rows or height of: " 
					+ startrow + " " + startcol + " " + width + " " + height + " " + direction);
					continue;	// Skip processing the rest of the piece 
				}
				
				// First set checks if block falls in valid range of columns
				// Second set calculates max possible width of a block can fit in 
				// from a given starting point and sees if the block would fit
				if (!((startcol > 0 && startcol < columns-1) && ((columns - startcol - 1) >= width)))
				{
					System.out.println("Error: block does not fit on board.\n Check columns or width of: " 
					+ startrow + " " + startcol + " " + width + " " + height + " " + direction);
					continue;	// Skip processing the rest of the piece 
				}
				
				//---------------------------------------------------------
				// Make sure that new block does not overlap old ones
				//---------------------------------------------------------
				// Check the rows
				for (int i=0; i<width; i++)
				{
					if (board[startrow][startcol+i] != '*')
					{
						System.out.println("Error: block overlap: \n" + startrow + " " 
						+ startcol + " " + width + " " + height + " " + direction);
						
						continue;	// Skip processing the rest of the piece 
					}
				}
				
				// Check the columns
				for (int i=0; i<height; i++)
				{
					if (board[startrow+i][startcol] != '*')
					{
						System.out.println("Error: block overlap: \n" + startrow + " " 
						+ startcol + " " + width + " " + height + " " + direction);
						
						continue;	// Skip processing the rest of the piece 
					}
				}
				
				//---------------------------------------------------------
				// Check if direction is valid - valid values are:
				// 'h' for horizontal
				// 'v' for vertical
				// 'b' for both
				// 'n' for none
				//---------------------------------------------------------
				if (direction == 'h' || direction == 'v' || direction == 'b' || direction == 'n')
				{
					// Intentionally left empty
				}
				else 
				{
					System.out.println("Error: Invalid direction for puzzle piece: \n" + startrow + " " 
									  + startcol + " " + width + " " + height + " " + direction);
					continue;	// Skip processing the rest of the piece 
				}
				
				//---------------------------------------------------------
				// Passed all tests so add the block to the board
				//---------------------------------------------------------
				char blockID;
				if (!containsGoal)	// If there is not a goal piece yet then add one
				{
					// Add the rows
					for (int i=0; i<width; i++)
						board[startrow][startcol+i] = 'Z';
					
					// Add the columns
					for (int i=0; i<height; i++)
						board[startrow+i][startcol] = 'Z';
					
					containsGoal = true;
					blockID = 'Z';
				}
				else
				{
					// Add the rows
					for (int i=0; i<width; i++)
						board[startrow][startcol+i] = (char) blockCount;
					
					// Add the columns
					for (int i=0; i<height; i++)
						board[startrow+i][startcol] = (char) blockCount;
					
					blockID = (char) blockCount;
					blockCount++;
					
					// If used numbers 1-9 then start using lower case letters (97 = 'a')
					if (blockCount == 58)
						blockCount = 97;
					// If used a-z then start using upper case letters (65 = 'A')
					if (blockCount == 122)
						blockCount = 65;
				}
				
				// Create a new block and add it to the list
				blocks.add(new Block(startrow, startcol, width, height, direction, blockID));
				
			} // End while (fileReader.hasNext()) 
			
			// Fill the '*' on the board with '.' - only the outer border will remain '*'
			for (int i=1; i<rows-1; i++)
			{
				for (int j=1; j<columns-1; j++)
				{
					if (board[i][j] == '*')
						board[i][j] = '.';
				}
			}
			
			//---------------------------------------------------------
			// Set up variables to allow for a reset
			//---------------------------------------------------------
			resetBoard = new char[rows][columns];
			for (int i=0; i<rows; i++)
				for(int j=0; j<columns; j++)
					resetBoard[i][j] = board[i][j]; 
			
			// Copy over the original data of each block
			resetBlocks= new ArrayList<Block>();
			for (int i=0; i<blocks.size(); i++)
			{
				Block temp = new Block(blocks.get(i));
				resetBlocks.add(temp);
			}
			
			// Print out the board
			System.out.println("Puzzle " + puzzleCounter + ":");
			for (int i = 0; i < rows; i++)
			{
				for (int j=0; j<columns; j++)
					System.out.print(board[i][j] + " ");
				
				System.out.println();
			}
			
			fileReader.close();
			
			puzzleCounter++;	// Successfully read in a puzzle to increment to later read the next one  
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Failed to open puzzle: " + puzzleFiles.get(source));
			puzzleCounter++;
			loadNextPuzzle(puzzleCounter);	// Try to load the next puzzle in the list
		}
	}
	
	/**------------------------------------------------------------------------
	 * Method prompts the user for a filename to add as a puzzle and checks if
	 * the file exists. If it does, then the file is used to create a new puzzle.
	 * ------------------------------------------------------------------------*/
	public void addUserPuzzle()
	{
		String fileName = JOptionPane.showInputDialog
		("Add one of you own puzzles, just enter the filename of your puzzle.\n\n"
		+ "Make sure the file is in the same location as the other puzzle files.");
		
		// If the user canceled or they left the dialog box empty then return
		if (fileName == null || fileName.length() == 0)
			return;
		
		File userfile = new File(fileName);
		if (!userfile.exists())
		{
			System.out.println("Error: could not find " + fileName);
			return;
		}
		else 
			puzzleFiles.add(fileName);
		
		puzzleCounter--;	// Don't skip pre-loaded puzzles
		
		// Read the puzzle file and create the board
		loadNextPuzzle(puzzleFiles.size()-1);
	}
	
	/**------------------------------------------------------------------------
	 * @return index of next puzzle to load for list
	 * ------------------------------------------------------------------------*/
	public int nextPuzzle()
	{
		return puzzleCounter;
	}
	
	/**------------------------------------------------------------------------
	 * Restores data values to when they were originally read in from a file
	 * ------------------------------------------------------------------------*/
	public void resetBoard()
	{
		for (int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = resetBoard[i][j]; 
		
		// Copy over the original data of each block
		blocks.removeAll(blocks);
		for (int i=0; i<resetBlocks.size(); i++)
		{
			Block temp = new Block(resetBlocks.get(i));
			blocks.add(temp);
		}
	}
}
