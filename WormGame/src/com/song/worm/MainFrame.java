package com.song.worm;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	final static int Width = 504;
	final static int Height = 560;
	
	public MainFrame() throws HeadlessException {
		super();
		
		GamePanel gamePanel = new GamePanel();
		gamePanel.setSize(Width,Height);
		add(gamePanel);
		
		this.addKeyListener(new InputKey(gamePanel));
		this.requestFocus();
		
		setSize(Width, Height);
		setLocation(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setResizable(false);
		setVisible(true);
	}
	
	

}
