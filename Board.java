import java.awt.List;

/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		March 2014
 * 		CS 342 Software Design
 * 							
 * Board objects holds a snapshot of the board and a list of moves it took 
 * to get to the current position.
 * ------------------------------------------------------------------------*/
import java.util.*;

public class Board 
{
	private char board[][];
	private ArrayList<String> moves;
	
	public Board()
	{
		
		
	}
	
	public void printMoves()
	{
		
	}
	
	public char[][] getBoard()
	{
		return board;
	}
	
	public ArrayList<String> getMoves()
	{
		return moves;
	}
}
