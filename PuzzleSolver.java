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
	private char board[][] = {	{'*','*','*','*','*','*'},
								{'*','1','2','.','.','*'},
								{'*','3','.','4','4','*'},
								{'*','z','z','5','.','*'},
								{'*','6','6','5','7','*'},
								{'*','*','*','*','*','*'}};
	private char movement[] = {'b','b','b','b','b','b','b'};
	
	
	/*
	 * class variables:
	 * hint string
	 * solved board object (includes list of steps included for hint)
	 * queue of board objects
	 * 
	 * 
	 * Constructor(initial board):
	 * create the queue - add the first board
	 * 
	 * while the queue is not empty
	 * 		check if the goal piece on is at the end (use board on top of the queue)
	 * 			if so store solved board and break
	 * 		
	 * 		for (i=0; i<#blocks; i++)
	 * 		{
	 * 			check movement
	 * 				move left right
	 * 				move up down
	 * 				call method to see is board is already on queue
	 * 					if not then add it to the queue
	 * 					else skip it
	 * 		}
	 * 
	 * 		reset current board to top of the queue
	 * 
	 * 
	 * 
	 * 
	 * Methods needed for this class:
	 * 1.compare board with boards in queue(board)
	 * 2.move function (piece, left/right +-, up/down+-)
	 * 
	 *
	 */
	
	
	
}
