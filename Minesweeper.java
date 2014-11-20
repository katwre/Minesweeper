import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

/**
* @author Katarzyna Wreczycka
*/


public class Minesweeper extends JFrame {
  Board board;
  Container c;
  Clock z;
    
  Minesweeper(int height, int width, int numberOfBombs){
  
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setSize(30*width+50,32*height+105);
	setMinimumSize(new Dimension(30*width+50,32*height+105));
	c = getContentPane();
	c.setLayout(new FlowLayout());
	c.setBackground(new Color(0,204,204));
		
	// add clock
	z = new Clock();
	z.setPreferredSize(new Dimension(50,50));
	c.add(z);
	
	// add board
	Board board = new Board(height, width, numberOfBombs, z);
	add(board);

	setVisible(true);	
	
  }
  
  public static void main(String[]  args){
  
    final int height = Integer.parseInt(args[0]);
    final int width = Integer.parseInt(args[1]);
    final int numberOfBombs = Integer.parseInt(args[2]);
        
    new Minesweeper(height, width, numberOfBombs);
 
  }
}

