package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;


import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;
import com.a225.model.manager.GameMap;

public class BubbleExplode extends SuperElement{
	//爆炸四个方向及中心的图片
	private static ImageIcon imgCenter = ElementLoader.getElementLoader().getImageMap().get("explodeCenter");
	private static ImageIcon imgUp = ElementLoader.getElementLoader().getImageMap().get("explodeUp");
	private static ImageIcon imgDown = ElementLoader.getElementLoader().getImageMap().get("explodeDown");
	private static ImageIcon imgLeft = ElementLoader.getElementLoader().getImageMap().get("explodeLeft");
	private static ImageIcon imgRight = ElementLoader.getElementLoader().getImageMap().get("explodeRight");

	
	//炸弹在地图中往四个方向延申的步数
	private int up;
	private int down;
	private int left;
	private int right;
	
	private int power;


	public BubbleExplode(int x,int y, int w, int h, int power) {
		//参数表：坐标x，坐标y，宽w，高h，爆炸图片列表
		super(x, y, w, h);
		up = 0;
		down = 0;
		left = 0;
		right = 0;
		this.power = power;
		setMoveStep();
	}
	
	//创建实例
	public static BubbleExplode createExplode(int x, int y,int power) {
		return new BubbleExplode(x, y, MapSquare.PIXEL_X, MapSquare.PIXEL_Y, power);
	}

	
	@Override
	public void showElement(Graphics g) {
		final int PIXEL_X = MapSquare.PIXEL_X;
		final int PIXEL_Y = MapSquare.PIXEL_Y;
		//分开显示
		g.drawImage(imgCenter.getImage(),
				getX(), getY(), getX()+PIXEL_X, getY()+PIXEL_Y,
				0, 0, 32, 32,
				null);
		g.drawImage(imgUp.getImage(),
				getX(), getY()-up*PIXEL_Y, getX()+PIXEL_X, getY(),
				0, 0, 32, 64,
				null);
		g.drawImage(imgDown.getImage(),
				getX(), getY()+PIXEL_Y, getX()+PIXEL_X, getY()+(down+1)*PIXEL_Y,
				0, 0, 32, 64,
				null);
		g.drawImage(imgLeft.getImage(),
				getX()-left*PIXEL_X, getY(), getX(), getY()+PIXEL_Y,
				0, 0, 64, 32,
				null);
		g.drawImage(imgRight.getImage(),
				getX()+PIXEL_X, getY(), getX()+(right+1)*PIXEL_X, getY()+PIXEL_Y,
				0, 0, 64, 32,
				null);
	}

	@Override
	public void move() {}

	//爆炸效果持续0.7秒
	@Override
	public void destroy() {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				setAlive(false);
			}
		};
		timer.schedule(task, 700);
	}
	
	//判断爆炸与物体边缘冲突
	@Override
	public boolean crash(SuperElement se) {
		int bias = 8;
		Rectangle explodeColumn = 
				new Rectangle(getX()+bias, getY()-getUp()*MapSquare.PIXEL_Y+bias, MapSquare.PIXEL_X-bias, (getUp()+getDown()+1)*MapSquare.PIXEL_Y-bias);//水泡爆炸十字纵向
		Rectangle explodeRow =  
				new Rectangle(getX()-getLeft()*MapSquare.PIXEL_X+bias, getY()+bias, (getLeft()+getRight()+1)*MapSquare.PIXEL_X-bias, MapSquare.PIXEL_Y-bias);//水泡爆炸十字横向
		Rectangle rectangle = new Rectangle(se.getX()+bias, se.getY()+bias, se.getW()-bias, se.getH()-bias);
		boolean column = explodeColumn.intersects(rectangle);
		boolean row = explodeRow.intersects(rectangle);
		return (column||row);
	}
	
	private int getMoveStep(int i, int j, String direction) {
		//计算方向改变量
		int bi = 0;
		int bj = 0;
		switch (direction) {
		case "up": bi=-1;break;
		case "down": bi=1;break;
		case "left": bj=-1;break;
		case "right": bj=1;break;
		default: break;
		}
		//获取地图
		GameMap gameMap = ElementManager.getManager().getGameMap();
		//计算step
		int step = 0;
		for(int k=0;k<power;k++) {
			i += bi;
			j += bj;
			if(gameMap.outOfBoundary(i,j)||gameMap.blockIsObstacle(i, j)) {
				break;
			} else {
				step++;
				if(gameMap.getBlockSquareType(i, j)==GameMap.SquareType.FRAGILITY) {
					break;
				}
			}
		}
		return step;
	}
	
	
	//获取爆炸范围up down left right
	public void setMoveStep() {
		int mapI = GameMap.getIJ(getX(), getY()).get(0);
		int mapJ = GameMap.getIJ(getX(), getY()).get(1);
		
		up = Math.min(getMoveStep(mapI, mapJ, "up"), power);
		down = Math.min(getMoveStep(mapI, mapJ, "down"), power);
		left = Math.min(getMoveStep(mapI, mapJ, "left"), power);
		right = Math.min(getMoveStep(mapI, mapJ, "right"), power);
	}

	//getters and setters
	public int getUp() {
		return up;
	}

	public void setUp(int up) {
		this.up = up;
	}

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	

}
