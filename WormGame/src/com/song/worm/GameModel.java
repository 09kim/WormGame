package com.song.worm;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameModel implements Publisher{
	private final static int MAXSIZE = 100;
	private int[][] field = new int[25][25];
	private int hx,hy,ex,ey,size,score,speed;
	private int[][] bodyXY = new int[2][MAXSIZE];
	private Direction direction;
	private boolean gameOver,permitRotation;
	private ArrayList<Observer> obs;
	enum Direction{
		CENTER,LEFT,UP,RIGHT,DOWN
	}
	
	
	public GameModel() {
		this.hx=12;
		this.hy=12;
		this.size=0;
		this.score = 0;
		this.gameOver = false;
		this.direction = Direction.RIGHT;
		this.obs = new ArrayList<>();
		this.permitRotation = true;
		this.speed = 800;
		
		init();
		makeEat();
		init();
	}
	
	private void init(){
		for(int i=0;i<25;i++) {
			for(int j=0;j<25;j++) {
				if(i==0||i==24||j==0||j==24)field[i][j] = 1;
				else field[i][j] = 0;
			}
		}
		
		field[hx][hy] = 2;
		for(int i=0;i<size;i++) {
			field[bodyXY[0][i]][bodyXY[1][i]] = 2;
		}
		field[ex][ey] = 3;
		
		if(isGameOver())makeGameOver();
	}
	
	public void tern() {
		switch (direction) {
		case LEFT:
			if(field[hx][hy-1]==1 || field[hx][hy-1]==2) {
				gameOver = true;
				break;
			}else if(field[hx][hy-1]==0) {
				move();
			}else if(field[hx][hy-1]==3) {
				eat();
			}
			hy-=1;
			break;
		case RIGHT:
			if(field[hx][hy+1]==1 || field[hx][hy+1]==2) {
				gameOver = true;
				break;
			}else if(field[hx][hy+1]==0) {
				move();
			}else if(field[hx][hy+1]==3) {
				eat();
			}
			hy+=1;
			break;
		case UP:
			if(field[hx-1][hy]==1 || field[hx-1][hy]==2) {
				gameOver = true;
				break;
			}else if(field[hx-1][hy]==0) {
				move();
			}else if(field[hx-1][hy]==3) {
				eat();
			}
			hx-=1;
			break;
		case DOWN:
			if(field[hx+1][hy]==1 || field[hx+1][hy]==2) {
				gameOver = true;
				break;
			}else if(field[hx+1][hy]==0) {
				move();
			}else if(field[hx+1][hy]==3) {
				eat();
			}
			hx+=1;
			break;

		default:
			break;
		}
		permitRotation = true;
		init();
		notifyOb();
	}
	
	//먹이 만들기
	private void makeEat() {
		Random rand = new Random();
		do {
			this.ex = rand.nextInt(23)+1;
			this.ey = rand.nextInt(23)+1;
		}
		while((field[ex][ey]!=0));
		System.out.println("eatxt: "+ ex + ", " +ey);
	}
	
	//먹이를 먹었을 때
	private void eat() {
		plusScore(100);
		if(size>=MAXSIZE)return;
		if(size>=1){
			bodyXY[0][size] = bodyXY[0][size-1];
			bodyXY[1][size] = bodyXY[1][size-1];
		}
		size++;
		controlDifficulty();
		move();
		makeEat();
	}
	
	//그냥 움직일 때
	private void move() {
		if(size==0)return;
		else if(size>=2){
			for(int i=size-1;i>0;i--) {
				bodyXY[0][i] = bodyXY[0][i-1];
				bodyXY[1][i] = bodyXY[1][i-1];
			}
		}
		bodyXY[0][0] = hx;
		bodyXY[1][0] = hy;
	}
	
	private void controlDifficulty() {
		if(size==2)setSpeed(650);
		if(size==4)setSpeed(550);
		if(size==6)setSpeed(450);
		if(size==12)setSpeed(380);
		if(size==16)setSpeed(340);
		if(size==25)setSpeed(300);
		if(size==30)setSpeed(270);
		if(size==40)setSpeed(240);
		if(size==60)setSpeed(220);
		if(size==70)setSpeed(200);
		if(size==80)setSpeed(190);
		if(size==90)setSpeed(180);
	}
	
	private void makeGameOver() {
		
		int[][] gameoverField = 
			   {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,1,1,1,1,0,0,1,1,1,0,0,1,1,0,1,1,0,1,1,1,1,1,0},
				{0,1,0,0,0,0,0,1,0,0,0,1,0,1,0,1,0,1,0,1,0,0,0,0,0},
				{0,1,0,0,1,1,0,1,1,1,1,1,0,1,0,1,0,1,0,1,1,1,1,1,0},
				{0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0},
				{0,0,1,1,1,1,0,1,0,0,0,1,0,1,0,0,0,1,0,1,1,1,1,1,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,1,1,1,0,0,1,0,0,0,1,0,1,1,1,1,1,0,1,1,1,1,0,0},
				{0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,1,0,0,0,1,0},
				{0,1,0,0,0,1,0,1,0,0,0,1,0,1,1,1,1,1,0,1,1,1,1,1,0},
				{0,1,0,0,0,1,0,0,1,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0},
				{0,0,1,1,1,0,0,0,0,1,0,0,0,1,1,1,1,1,0,1,0,0,0,1,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		
		field = gameoverField;
		
		for(int i=0;i<25;i++) {
			for(int j=0;j<25;j++) {
				if(i==0||i==24||j==0||j==24)field[i][j] = 3;
			}
		}
	}
	
	private void plusScore(int point) {
		score += point;
	}

	public int[][] getField() {
		return field;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public int getSpeed() {
		return speed;
	}

	public int getScore() {
		return score;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isGameOver() {
		return gameOver;
	}
	
	public boolean isPermitRotation() {
		return permitRotation;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
		this.permitRotation = false;
	}

	@Override
	public void add(Observer ob) {
		obs.add(ob);
	}

	@Override()
	public void remove(Observer ob) {
		int i = -1; 
		i = obs.indexOf(ob);
		if(i>=0)obs.remove(i);
	}

	@Override
	public void notifyOb() {
		if(obs==null)return;
		Iterator<Observer> its = obs.iterator();
		while(its.hasNext()) {
			its.next().update();
		}
	}
	
	
	
}
