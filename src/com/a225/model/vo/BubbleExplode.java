package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;


import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;

public class BubbleExplode extends SuperElement{
	
	private List<ImageIcon> img; //保存爆炸图片
	private int moveX;
	
	//炸弹在地图中往四个方向延申的步数
	private int up;
	private int down;
	private int left;
	private int right;


	public BubbleExplode(int x,int y, int w, int h, List<ImageIcon> imageList) {
		//参数表：坐标x，坐标y，宽w，高h，爆炸图片列表
		super(x, y, w, h);
		img = new ArrayList<>(imageList);
		moveX = 0;
		up = 0;
		down = 0;
		left = 0;
		right = 0;
		setMoveStep();
	}
	
	//创建实例
	public static BubbleExplode createExplode(int x, int y,List<String> list) {
		//list=[图片0，图片1，图片2，图片宽w，图片高h]
		int w = Integer.parseInt(list.get(3));
		int h = Integer.parseInt(list.get(4));
		List<ImageIcon> imageList = new ArrayList<>();
		Map<String, ImageIcon> imageMap = ElementLoader.getElementLoader().getImageMap();
		for(int i=0; i<3; i++) { //爆炸效果图有3张
			imageList.add(imageMap.get(list.get(i)));
		}
		return new BubbleExplode(x, y, w, h, imageList);
	}

	
	@Override
	public void showElement(Graphics g) {
		int perW = getW()/5;
		int perH = getH()/5;
		g.drawImage(img.get(moveX).getImage(), 
				getX()-left*MapSquare.PIXEL_X, getY()-up*MapSquare.PIXEL_Y, 	//屏幕左上角坐标
				getX()+(right+1)*MapSquare.PIXEL_X, getY()+(down+1)*MapSquare.PIXEL_Y, 	//屏幕右下坐标
				(2-left)*perW, (2-up)*perH, 				//图片左上坐标
				(3+right)*perW, (3+down)*perH, 			//图片右下坐标\
				null);
	}

	//更换图片，图片播完后进入消亡状态
	@Override
	public void move() {
		if(moveX<2) {
			moveX++;
		}
	}

	//爆炸效果持续0.8秒
	@Override
	public void destroy() {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				setAlive(false);
			}
		};
		timer.schedule(task, 800);
	}
	
	//判断爆炸与物体边缘冲突
	@Override
	public boolean crash(SuperElement se) {
		int bias = 0;
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
		int tpower = 2;
		for(int k=0;k<tpower;k++) {
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
		
		up = getMoveStep(mapI, mapJ, "up");
		down = getMoveStep(mapI, mapJ, "down");
		left = getMoveStep(mapI, mapJ, "left");
		right = getMoveStep(mapI, mapJ, "right");
	}

	//getters and setters
	

	public int getMoveX() {
		return moveX;
	}

	public List<ImageIcon> getImg() {
		return img;
	}

	public void setImg(List<ImageIcon> img) {
		this.img = img;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

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
