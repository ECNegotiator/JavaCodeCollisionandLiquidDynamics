package Collisions;

import javax.swing.JFrame;

public class Window {
	
	public Window(String title,  Main main) {
		JFrame frame = new JFrame(title);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(main);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		main.start();
		
	}

}
