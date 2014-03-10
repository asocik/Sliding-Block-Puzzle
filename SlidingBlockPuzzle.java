/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		March 2014
 * 		CS 342 Software Design
 * 							
 * This class creates the JFrame and calls the window class to set up the GUI.
 * ------------------------------------------------------------------------*/
import javax.swing.JFrame;

public class SlidingBlockPuzzle extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) 
	{
		JFrame window = new Window();
		window.setTitle("Sliding Block Puzzles");
		window.setVisible(true);
		window.setSize(800, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		System.out.println("Output will be printed here");	// Look at GUI
	}
}

// ajhskfadskjlfhaskfhajkdsfhadskjlfhkadfhjadskjfhakjd