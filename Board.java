import java.io.*;
import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Board extends JPanel {

	int height, width, numberOfBombs;
	Clock z;
	boolean bombs[][];
	JButton[][] buttons;
	
	int dw[] = { 0, 0, 1,  1, 1, -1, -1, -1};
	int dk[] = {-1, 1, 0, -1, 1,  1, -1,  0};
	
	
	Icon bomb = new ImageIcon(this.getClass().getResource("/img/bomb.GIF"));
	Icon bombFlag = new ImageIcon(this.getClass().getResource("/img/flag.GIF"));
	Icon numbers[] = {   new ImageIcon(this.getClass().getResource("/img/button.GIF")),
						new ImageIcon(this.getClass().getResource("/img/one.GIF")),
					    new ImageIcon(this.getClass().getResource("/img/two.GIF")),
					    new ImageIcon(this.getClass().getResource("/img/tree.GIF")),
						new ImageIcon(this.getClass().getResource("/img/four.GIF")),
						new ImageIcon(this.getClass().getResource("/img/five.GIF")),
						new ImageIcon(this.getClass().getResource("/img/six.GIF")),
						new ImageIcon(this.getClass().getResource("/img/seven.GIF")),
						new ImageIcon(this.getClass().getResource("/img/eight.GIF"))
			};		
	

	
	
	Board(int initialHeight, int initialWidth, int initialNumberOfBombs, Clock initialClock) {
	
	  height = initialHeight;
	  width = initialWidth;
	  numberOfBombs = initialNumberOfBombs;
	  z = initialClock;
	  JButton[][] buttons = new JButton[height][width];
	  	  	  
	  setPreferredSize(new Dimension(30*width,30*height));	
	  setLayout(new GridLayout(height,width));
	  	  
	  randomBombDistribution();
	  
	  	  
	  for (int row=0; row<height; row++)
		  for(int column=0; column < width; column++)
		  {
			  buttons[row][column] = new JButton();
			  buttons[row][column].addMouseListener(new ButtonHandling(row,column,bombs[row][column]));
			  add(buttons[row][column]);
		  }	  
	  this.buttons = buttons;
	  
	}
	
	
	public static void printBoard(int width, int height, boolean[][] bombs){
	  String s = "";
	  for(int i = 0; i<height; i++){
	    for(int j = 0; j<width; j++){
	      s = s + " " + bombs[i][j];
	    }
	   System.out.println(s); 
	   s = "";
	  }
	}
	
	
	void randomBombDistribution(){
	  this.bombs = new boolean[height][width];
		
	  Random r = new Random();
	  int numberOfBombs1 = numberOfBombs;
	  while(numberOfBombs1!=0) {
		  int row = r.nextInt(height);
		  int column = r.nextInt(width);
		  if(!bombs[row][column]) {
			  this.bombs[row][column]=true;
			  numberOfBombs1-=1;
		  }
	  }
	}

	
	boolean isOutOfRange(int w, int k) {
	    if (w >= this.height || k >= this.width) {
		    return true;
	    }
	    if (w < 0 || k < 0) {
		    return true;
	    }
	    return false;
	}
	
	
	int howManyBombsAround(int w, int k) {
	  int numberOfBombs=0;
	  for(int i = 0; i<dw.length; i++) {
		  if (!isOutOfRange(w+dw[i],k+dk[i])) {
			  if(bombs[w+dw[i]][k+dk[i]]) {
				  numberOfBombs+=1;
			  }
		  }
	  }
	  return numberOfBombs;
	}
	
	boolean isUncovered(int w, int k) {
		return buttons[w][k].getIcon()!=null;
	}
	boolean isFlag(int w, int k) {
		return buttons[w][k].isEnabled() && buttons[w][k].getIcon()!=null;
	}
	
	boolean isVictory() {
			for(int i=0; i < this.height; i++) {
				for(int j=0; j < this.width; j++) {
					if(bombs[i][j] && !isFlag(i,j)) {return false;}
					if(!bombs[i][j] && isFlag(i,j)) {return false;}
					if(!bombs[i][j] && !isUncovered(i,j)) {return false;}
				}
			}
	  return true;
	}
	
	
	public void gameOver() {
		String tmp; 
		if(isVictory()) {tmp = "Game over: Good job !!\n";}
		else { tmp = "Game over: Try again! \n" ;}
		JOptionPane.showMessageDialog(this,"\n" + tmp);
	}
	
	
	void uncover(int w, int k) {
		Icon icon;
		if(bombs[w][k]) { icon = bomb; }
		else {
			int number = howManyBombsAround(w,k);
			icon = numbers[number];
		}
		buttons[w][k].setIcon(icon);
		buttons[w][k].setDisabledIcon(icon);
		buttons[w][k].setEnabled(false);
	}
	
	void visit(int w, int k) {
		if(buttons[w][k].isEnabled()) {
			int number = howManyBombsAround(w,k);
			uncover(w,k);
			if(number==0) {
				for(int i = 0; i<dw.length; i++ ) {
					if (!isOutOfRange(w+dw[i],k+dk[i])) {
						visit(w+dw[i],k+dk[i]);
					}	
				}
			}
		}
	}
	
	
	class ButtonHandling extends MouseAdapter
		{
			final int row;
			final int column;
			final boolean bomb;

			ButtonHandling(int w, int k, boolean m)
			{
				row = w;
				column = k;
				bomb = m;
			}
			public void mouseClicked(MouseEvent e)
			{
				if(!z.isRunning()) { z.start();}
				if (buttons[row][column].isEnabled()) {
					if (e.getButton()==MouseEvent.BUTTON1 && !isUncovered(row, column) )
					{
						if (bombs[row][column]) {
							uncover(row, column);
							for(int i = 0; i < height; i++){
								for(int j = 0; j < width; j++) {
									if(!isUncovered(i,j)) {
										uncover(i,j);
									}
								}
							}
							z.stop();
							gameOver();
							System.exit(0);
							
						}	
						else {
							int nbr = howManyBombsAround(row,column);
							if(nbr==0) {
								visit(row,column);
							}
							else {
								uncover(row,column);
							}
						}
					}
					else if (e.getButton()==MouseEvent.BUTTON3)
					{
						if(buttons[row][column].getIcon()==null) {buttons[row][column].setIcon(bombFlag); }
						else { buttons[row][column].setIcon(null);}
						
					}
					if(isVictory()) {
						z.stop();
						gameOver();
						System.exit(0);}
				}
			}
		}

	public static void main(String[]  args){
	
	}
}








