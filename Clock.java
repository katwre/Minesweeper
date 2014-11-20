import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;


public class Clock extends JLabel {
	long start_time;
	long end_time;
	Timer t;
	Clock() {
		super("0"); 
			
		setFont(new Font("Dialog", Font.PLAIN, 40));
		setBounds(new Rectangle (25, 0, 50, 25));
		setHorizontalAlignment (SwingConstants.CENTER);
		setHorizontalTextPosition (SwingConstants.CENTER);
		
		t = new Timer(1000, new ActionListener() { //tu timer jest zastopowany
			public void actionPerformed(ActionEvent e) {
				end_time = System.currentTimeMillis();
				setText(""+((end_time - start_time)/1000));			
			}
		});
	}
	void start() {
		start_time = System.currentTimeMillis();
		setText("0");
		t.start();			
	}
	
	void stop() {
		t.stop();
	}
	
	boolean isRunning() {
		return t.isRunning();
	}
}