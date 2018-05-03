package com.song.worm;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements InputKey.InputMethod, Observer{
	GameModel gameModel;
	int[][] field;
	public GamePanel() {
		super();
		this.setBackground(Color.black);
		gameModel = new GameModel();
		gameModel.add((Observer)this);
		field = gameModel.getField();
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(!gameModel.isGameOver()) {
						Thread.sleep(gameModel.getSpeed());
						gameModel.tern();
					}
					System.out.println("Game Ovepr");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		thread.start();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);

		g.setColor(Color.gray);
		g.drawString("SCORE: ", 0, 20);
		g.setColor(Color.gray);
		g.drawString(gameModel.getScore()+"", 50, 20);

		g.setColor(Color.gray);
		g.drawString("SPEED: ", 400, 20);
		g.drawString(gameModel.getSpeed()+"", 450, 20);
		
		for(int i =0;i<25;i++) {
			for(int j=0;j<25;j++) {
				System.out.print(field[i][j]);
				if(field[i][j]==1) {
					g.setColor(Color.gray);
					g.fillRect(j*20, i*20+25, 15, 15);
				}else if(field[i][j]==2) {
					g.setColor(Color.green);
					g.fillRect(j*20, i*20+25, 15, 15);
				}else if(field[i][j]==3) {
					g.setColor(Color.YELLOW);
					g.fillRect(j*20, i*20+25, 15, 15);
				}
			}
			System.out.println();
		}
		
	}

	@Override
	public void LeftKeyPressed() {
		if(gameModel.getDirection().equals(GameModel.Direction.RIGHT))return;
		if(gameModel.isPermitRotation())
			gameModel.setDirection(GameModel.Direction.LEFT);
	}

	@Override
	public void RightKeyPressed() {
		if(gameModel.getDirection().equals(GameModel.Direction.LEFT))return;
		if(gameModel.isPermitRotation())
			gameModel.setDirection(GameModel.Direction.RIGHT);
	}

	@Override
	public void UpKeyPressed() {
		if(gameModel.getDirection().equals(GameModel.Direction.DOWN))return;
		if(gameModel.isPermitRotation())
			gameModel.setDirection(GameModel.Direction.UP);
	}

	@Override
	public void DownKeyPressed() {
		if(gameModel.getDirection().equals(GameModel.Direction.UP))return;
		if(gameModel.isPermitRotation())
			gameModel.setDirection(GameModel.Direction.DOWN);
	}

	@Override
	public void update() {
		System.out.println("GamePanel: update");
		field = gameModel.getField();
		repaint();
	}
	
	
}
