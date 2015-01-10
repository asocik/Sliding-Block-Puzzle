/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		March 2014
 * 		CS 342 Software Design
 * 							
 * This class holds all of the necessary information for a given block. 
 * ------------------------------------------------------------------------*/
public class Block
{
	public char ID;
	public int startrow;
	public int startcol;
	public int width;
	public int height;
	public char direction;
	
	public Block(int startrow, int startcol, int width, int height, char direction, char ID) 
	{
		this.startrow = startrow;
		this.startcol = startcol;
		this.width = width;
		this.height = height;
		this.direction = direction;
		this.ID = ID;
	}
	
	// Constructor that creates a copy of the block b
	public Block(Block b)
	{
		this(b.startrow, b.startcol, b.width, b.height, b.direction, b.ID);
	}
}