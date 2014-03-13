/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		March 2014
 * 		CS 342 Software Design
 * 							
 * This class creates the JFrame and calls the window class to set up the GUI.
 * ------------------------------------------------------------------------*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JFrame;

public class SlidingBlockPuzzle extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException
	{
		JFrame window = new Window();
		window.setTitle("Sliding Block Puzzles");
		window.setVisible(true);
		window.setSize(800, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		
		String s1;
		String s2;
		
		// set up the buffered reader to read from the keyboard
		BufferedReader br = new BufferedReader (new FileReader ("proj3a.data"));
		
		s1 = br.readLine();
		
		System.out.println ("The line is " + s1);
		System.out.println ("The line has " + s1.length() + " characters");
		
		System.out.println ();
		System.out.println ("Breaking the line into tokens we get:");
		
		int numTokens = 0;
		StringTokenizer st = new StringTokenizer (s1);
		
		while (st.hasMoreTokens())
	   {
		    s2 = st.nextToken();
		    numTokens++;
		    System.out.println ("    Token " + numTokens + " is: " + s2);
	   }
		
		
	   br.close();
		
		System.out.println("Output will be printed here");	// Look at GUI
	}
}