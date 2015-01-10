/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		March 2014
 * 		CS 342 Software Design
 * 							
 * This class searches a puzzle for the shortest/quickest solution
 * ------------------------------------------------------------------------*/
import java.util.*;

public class PuzzleSolver
{
	private Move hint;	
	private Snapshot solution;				
	private ArrayList<Snapshot> queue;		// Snapshots to process
	private HashSet<String> prevConfigs;	// Previous configurations of the board
	private int rows;
	private int columns;
	private ArrayList<Block> blocks;
	
	/**------------------------------------------------------------------------
	 * Subclass to hold a snapshot of the board and the list
	 * of moves it took to get there.
	 * ------------------------------------------------------------------------*/
	private class Snapshot
	{
		public char board[][];			// Current board
		public ArrayList<Move> moves;	// Moves it took to get this board
		public ArrayList<Block> snapshotBlocks;
		
		public Snapshot(char b[][])
		{
			this.board = copyBoard(b);
			moves = new ArrayList<Move>();
			snapshotBlocks = new ArrayList<Block>();
			
			// Copy list of blocks
			for (int i=0; i<blocks.size(); i++)
			{
				Block temp = new Block(blocks.get(i));
				this.snapshotBlocks.add(temp);
			}
			solution = null;
		}
		
		// Constructor for a snapshot that implements a move
		public Snapshot(Snapshot old, char dir, int value, int updateBlock)
		{
			this.board = copyBoard(old.board); 	// Board will be updated in appropriate move method
			
			// Copy list of moves
			this.moves = new ArrayList<Move>(old.moves.size());
			for (int i=0; i<old.moves.size(); i++)
			{
				Move temp = new Move(old.moves.get(i));
				this.moves.add(temp);
			}
			
			Move move = new Move(dir, value, updateBlock);
			this.moves.add(move);	// Add new move
			
			// Copy list of blocks
			snapshotBlocks = new ArrayList<Block>();
			for (int i=0; i<old.snapshotBlocks.size(); i++)
			{
				Block temp = new Block(old.snapshotBlocks.get(i));
				this.snapshotBlocks.add(temp);
			}
			
			// Update the block moved
			if (dir == 'v')
				this.snapshotBlocks.get(updateBlock).startrow += value;
			if (dir == 'h')
				this.snapshotBlocks.get(updateBlock).startcol += value;
		}
	}
	
	/**------------------------------------------------------------------------
	 * Subclass to hold information about a move
	 * ------------------------------------------------------------------------*/
	private class Move
	{
		public char direction;	
		public int value;		// -1 for left/down, +1 for right/up
		public int blockID;		// Which block was moved
		
		public Move(char direction, int value, int ID)
		{
			this.direction = direction;
			this.value = value;
			this.blockID = ID;
		}
		
		// Constructor that creates a copy of the move parameter
		public Move (Move m)
		{
			this(m.direction, m.value, m.blockID);
		}
	}
	
	/**------------------------------------------------------------------------
	 * Tries to move each block in every possible way to fund a solution
	 * ------------------------------------------------------------------------*/
	public PuzzleSolver(int rows, int columns, char board[][], ArrayList<Block> blocks)
	{
		this.rows = rows;
		this.columns = columns;
		this.blocks = blocks;
		
		prevConfigs = new HashSet<String>();
		hint = null;
		queue = new ArrayList<Snapshot>();
		Snapshot snapshot = new Snapshot(board);
		queue.add(snapshot);
		addToPrevConfig(board);		// Add starting board to configurations list
		while (!queue.isEmpty())	// While the queue is not empty
		{
			//---------------------------------------------------------
			// Check to see if the goal piece is on the right end of
			// board - solution was found
			//---------------------------------------------------------
			snapshot = queue.get(0);	// Get snapshot on top of the queue
			queue.remove(0);
			
			for (int i=1; i<rows-1; i++)	// Scan rightmost column for goal piece
			{
				if (snapshot.board[i][columns-2] == 'Z')	// Solution found
				{
					solution = snapshot;
					hint = snapshot.moves.get(0);
					break;
				}
			}
			
			// If hint is not null then a solution was found - break out of search loop
			if (hint != null)
				break;
			
			//---------------------------------------------------------
			// No solution found yet so move each block in every possible 
			// way and add each new snapshot to the queue
			//---------------------------------------------------------
			for (int i=0; i<blocks.size(); i++)
			{
				char movement = blocks.get(i).direction;
				
				// Move block based on movement specification
				switch (movement)
				{
					case 'h':
						horizontalMove(snapshot, i);
						break;
						
					case 'v':
						verticalMove(snapshot, i);
						break;
						
					case 'b':
						horizontalMove(snapshot, i);
						verticalMove(snapshot, i);
						break;
						
					default:	// No movement
						break;
				}
			}
		} // End while (!queue.isEmpty())
		
		// No solution was found
		if (solution == null)
			System.out.println("Puzzle has no solution");
		
	}
	
	/**------------------------------------------------------------------------
	 * Tries to move a block left or right on the board. If it is a valid move 
	 * then a new sanpshot is created and added to the end queue.
	 * 
	 * @param current		current snapshot whose block is being moved
	 * @param block			block to move
	 * ------------------------------------------------------------------------*/
	public void horizontalMove(Snapshot current, int block)
	{
		boolean canMoveRight = true;
		boolean canMoveLeft = true;
		
		// Don't need these but makes code more readable
		char ID = current.snapshotBlocks.get(block).ID;
		int startrow = current.snapshotBlocks.get(block).startrow;
		int startcol = current.snapshotBlocks.get(block).startcol;
		int height = current.snapshotBlocks.get(block).height;		
		int width = current.snapshotBlocks.get(block).width;
		
		// Try to move right or left on the board
		for (int i=0; i<height; i++)
		{
			if (current.board[startrow+i][startcol+width] != '.')
				canMoveRight = false;
			if (current.board[startrow+i][startcol-1] != '.')
				canMoveLeft = false;
		}
		
		if (canMoveRight)
		{
			Snapshot right = new Snapshot(current, 'h', 1, block);
					
			// Update the board to reflect the move - move the block
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			for (int i=0; i<height; i++)
			{
				for (int j=0; j<width; j++)
				{
					right.board[startrow+i][startcol+1+j] = ID;
					visited[startrow+i][startcol+1+j] = 'v';
					
					if (visited[startrow+i][startcol+j] == 'u')
						right.board[startrow+i][startcol+j] = '.';
				}
			}
			
			// Make sure an identical board is not current on the queue - if not add new snapshot
			if (addToPrevConfig(right.board) == true)
				queue.add(right);
		}
		
		if (canMoveLeft)
		{
			Snapshot left = new Snapshot(current, 'h', -1, block);
			
			// Update the board to reflect the move - move the block
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			for (int i=0; i<height; i++)
			{
				for (int j=0; j<width; j++)
				{
					left.board[startrow+i][startcol-1+j] = ID;
					visited[startrow+i][startcol-1+j] = 'v';
					
					if (visited[startrow+i][startcol+j] == 'u')
						left.board[startrow+i][startcol+j] = '.';
				}
			}
						
			// Make sure an identical board is not current on the queue - if not add new snapshot
			if (addToPrevConfig(left.board) == true)
				queue.add(left);
		}
	}
	
	/**------------------------------------------------------------------------
	 * Tries to move a block up or down on the board. If it is a valid move 
	 * then a new sanpshot is created and added to the end queue.
	 * 
	 * @param current		current snapshot whose block is being moved
	 * @param block			block to move
	 * ------------------------------------------------------------------------*/
	public void verticalMove(Snapshot current, int block)
	{
		boolean canMoveUp = true;
		boolean canMoveDown = true;
		
		// Don't need these but makes code more readable
		char ID = current.snapshotBlocks.get(block).ID;
		int startrow = current.snapshotBlocks.get(block).startrow;
		int startcol = current.snapshotBlocks.get(block).startcol;
		int width = current.snapshotBlocks.get(block).width;		
		int height = current.snapshotBlocks.get(block).height;
		
		// Try to move right or left on the board
		for (int i=0; i<width; i++)
		{
			if (current.board[startrow-1][startcol+i] != '.')
				canMoveUp = false;
			if (current.board[startrow+height][startcol+i] != '.')
				canMoveDown = false;
		}
		
		if (canMoveUp)
		{
			Snapshot up = new Snapshot(current, 'v', -1, block);
			
			// Update the board to reflect the move - move the block
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					up.board[startrow-1+j][startcol+i] = ID;
					visited[startrow-1+j][startcol+i] = 'v';
					
					if (visited[startrow+j][startcol+i] == 'u')
						up.board[startrow+j][startcol+i] = '.';
				}
			}
				
			// Make sure an identical board is not current on the queue - if not add new snapshot
			if (addToPrevConfig(up.board) == true)
				queue.add(up);
		}
		
		if (canMoveDown)
		{
			Snapshot down = new Snapshot(current, 'v', 1, block);
			
			// Update the board to reflect the move - move the block
			char visited[][] = new char [rows][columns];
			for (int i=0; i<rows; i++)
				for (int j=0; j<columns; j++)
					visited[i][j]= 'u';	 
			
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					down.board[startrow+1+j][startcol+i] = ID;
					visited[startrow+1+j][startcol+i] = 'v';
					
					if (visited[startrow+j][startcol+i] == 'u')
						down.board[startrow+j][startcol+i] = '.';
				}
			}
						
			// Make sure an identical board is not current on the queue - if not add new snapshot
			if (addToPrevConfig(down.board) == true)
				queue.add(down);
		}
	}
	
	/**------------------------------------------------------------------------
	 *  Make a copy of the board
	 * 
	 * @param original
	 * @return copy
	 * ------------------------------------------------------------------------*/
	public char[][] copyBoard(char original[][] )
	{
		char copy[][] = new char[rows][columns];
		
		for (int i = 0; i < copy.length; i++) 
			for (int j = 0; j < copy.length; j++) 
				copy[i][j] = original[i][j];
		
		return copy;
	}
	
	/**------------------------------------------------------------------------
	 * Checks to see if an equivalent board exists in the queue by comparing
	 * the key board with previous configurations.
	 * 
	 * @param key
	 * ------------------------------------------------------------------------*/
	public boolean addToPrevConfig(char key[][])
	{	
		String s = "";	// Convert board to a string
		
		for (int i=0; i<rows; i++)
			for (int j = 0; j < columns; j++) 
				s += key[i][j];
		
		// Hashsets do not allow duplicate values - duplicate strings would not be added (false returned)
		boolean ret = prevConfigs.add(s);
		return ret;
	}
	
	/**------------------------------------------------------------------------
	 * Prints out a hint to the user
	 * ------------------------------------------------------------------------*/
	public void printHint()
	{
		int id = hint.blockID;
		
		if (id == 0)
			System.out.print("Hint: Move block Z one spot ");
		else if (id >= 10 && id <=35)	// Lower case letters
			System.out.print("Hint: Move block " + (char)(id+87) + " one spot ");
		else if (id >=36 && id <= 60)	// Upper case letters		
			System.out.print("Hint: Move block " + (char)(id+29) + " one spot ");
		else	// Numbers
			System.out.print("Hint: Move block " + id + " one spot ");
		
		if (hint.direction == 'v')
		{
			if (hint.value == -1)
				System.out.print("up\n");
			else if (hint.value == 1)
				System.out.print("down\n");
		}
		else if (hint.direction == 'h')
		{
			if (hint.value == -1)
				System.out.print("left\n");
			else if (hint.value == 1)
				System.out.print("right\n");
		}
	}
	
	/**------------------------------------------------------------------------
	 * Prints out a list of moves to the solution and the final board
	 * ------------------------------------------------------------------------*/
	public void printSolution()
	{
		for (int i=0; i<solution.moves.size(); i++)
		{
			int id = solution.moves.get(i).blockID;
			System.out.print(i+1 + ". ");
			
			if (id == 0)
				System.out.print("Move block Z one spot ");
			else if (id >= 10 && id <=35)	// Lower case letters
				System.out.print("Move block " + (char)(id+87) + " one spot ");
			else if (id >=36 && id <= 60)	// Upper case letters			
				System.out.print("Move block " + (char)(id+29) + " one spot ");
			else	// Numbers
				System.out.print("Move block " + id + " one spot ");
			
			if (solution.moves.get(i).direction == 'v')
			{
				if (solution.moves.get(i).value == -1)
					System.out.print("up\n");
				else if (solution.moves.get(i).value == 1)
					System.out.print("down\n");
				
			}
			else if (solution.moves.get(i).direction == 'h')
			{
				if (solution.moves.get(i).value == -1)
					System.out.print("left\n");
				else if (solution.moves.get(i).value == 1)
					System.out.print("right\n");
			}
		}
		
		// Print out the solved board
		System.out.println("\nSolved board:");
		for (int i = 0; i < rows; i++)
		{
			for (int j=0; j<columns; j++)
				System.out.print(solution.board[i][j] + " ");
			
			System.out.println();
		}
	}
	
	/**------------------------------------------------------------------------
	 * @return The number of moves taken to solve the puzzle
	 * ------------------------------------------------------------------------*/
	public int minNumberOfMoves()
	{
		return solution.moves.size();
	}
}
