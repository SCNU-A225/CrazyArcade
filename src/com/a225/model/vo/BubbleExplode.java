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
				getX()-2*MapSquare.PIXEL_X, getY()-2*MapSquare.PIXEL_Y, 	//屏幕左上角坐标
				getX()+3*MapSquare.PIXEL_X, getY()+3*MapSquare.PIXEL_Y, 	//屏幕右下坐标
				(2-getLeft())*perW, (2-getUp())*perH, 				//图片左上坐标
				(3+getRight())*perW, (3+getDown())*perH, 			//图片右下坐标\
				null);
//		g.drawImage(img.get(moveX).getImage(), 
//				getX()-2*MapSquare.PIXEL_X, getY()-2*MapSquare.PIXEL_Y, 	//屏幕左上角坐标
//				getX()+3*MapSquare.PIXEL_X, getY()+3*MapSquare.PIXEL_Y, 	//屏幕右下坐标
//				0, 0, 				//图片左上坐标
//				getW(), getH(), 			//图片右下坐标\
//				null);
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
		Rectangle explodeColumn = 
				new Rectangle(getX(), getY()-getUp()*MapSquare.PIXEL_Y, MapSquare.PIXEL_X, (getUp()+getDown()+1)*MapSquare.PIXEL_Y);//水泡爆炸十字纵向
		Rectangle explodeRow = 
				new Rectangle(getX()-getLeft()*MapSquare.PIXEL_X, getY(), (getLeft()+getRight()+1)*MapSquare.PIXEL_X, MapSquare.PIXEL_Y);//水泡爆炸十字横向
		Rectangle rectangle = new Rectangle(se.getX(), se.getY(), se.getW(), se.getH());
		boolean column = explodeColumn.intersects(rectangle);
		boolean row = explodeRow.intersects(rectangle);
		return (column||row);
	}
	
	//获取爆炸范围up down left right
	public void setMoveStep() {
		List<List<String>> mapList = GameMap.getMapList();
		int mapJ = (getX()-GameMap.getBiasX())/MapSquare.PIXEL_X;
		int mapI = (getY()-GameMap.getBiasY())/MapSquare.PIXEL_Y;
		
		int mapH = mapList.size();
		int mapW = mapList.get(0).size();
		int[][] map = new int[mapH][mapW];
		for(int i=0; i<mapH; i++) {
			for(int j=0; j<mapW; j++) {
				map[i][j]=Integer.parseInt(mapList.get(i).get(j));
				System.out.print(map[i][j]+" ");
			}
			System.out.println();
		}
		//up
		switch(mapI-1) {
		case -1: setUp(0);break;
		case 0: 
			if(map[0][mapJ]>=20) {
				setUp(1);
			}else {
				setUp(0);
			}
			break;
		default:
			if(map[mapI-1][mapJ]>=20) {
				if(map[mapI-2][mapJ]>=20) {
					setUp(2);
				}else {
					setUp(1);
				}
			}else {
				setUp(0);
			}
		}
		
		//left
		switch(mapJ-1) {
		case -1: setLeft(0);break;
		case 0:
			if(map[mapI][0]>=20) {
				setLeft(1);
			}else {
				setLeft(0);
			}
			break;
		default:
			if(map[mapI][mapJ-1]>=20) {
				if(map[mapI][mapJ-2]>=20) {
					setLeft(2);
				}else {
					setLeft(1);
				}
			}else {
				setLeft(0);
			}
		}
		
		//down
		if(mapI==mapH) {
			setDown(0);
		}else if (mapI+1==mapH) {
			if(map[mapI+1][mapJ]>=20)
				setDown(1);
			else
				setDown(0);
		}
		else {
			if(map[mapI+1][mapJ]>=20) {
				if(map[mapI+2][mapJ]>=20)
					setDown(2);
				else
					setDown(1);
			}else {
				setDown(0);
			}
		}
		
		//right
		if(mapJ==mapW) {
			setRight(0);
		}else if (mapJ+1==mapW) {
			if(map[mapI][mapJ+1]>=20)
				setRight(1);
			else
				setRight(0);
		}else {
			if(map[mapI][mapJ+1]>=20) {
				if(map[mapI][mapJ+2]>=20)
					setRight(2);
				else 
					setRight(1);
			}else {
				setRight(0);
			}
		}
		
		System.out.println("mapI"+mapI+" mapJ"+mapJ);
		System.out.println("up"+getUp()+" down"+getDown()+" left"+getLeft()+" right"+getRight());
		
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
