package com.a225.model.vo;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.a225.model.manager.MoveTypeEnum;

public class Player extends SuperElement{
	
	private ImageIcon img;
	private MoveTypeEnum moveType;
	private int moveX;
	private int moveY;
	
	//构造函数
	public Player(int x, int y, int w, int h, ImageIcon img) {
		super(x, y, w, h);
		this.img = img;
		moveType = MoveTypeEnum.STOP;
		moveX = 0;
		moveY = 0;
	}
	
	public static Player createPlayer(String str) {
		int x = 0;
		int y = 0;
		int w = 50;
		int h = 60;
		String url = "img/Characters/Done_body16001_walk.png";
		return new Player(x, y, w, h, new ImageIcon(url));
	}

	//展示人物图片
	@Override
	public void showElement(Graphics g) {
		
		g.drawImage(img.getImage(), 
				getX(), getY(), 	//屏幕左上角坐标
				getX()+getW(), getY()+getH(), 	//屏幕右下坐标
				moveX*100+25, moveY*100+40, 				//图片左上坐标
				moveX*100+75, moveY*100+100, 			//图片右下坐标
				null);
	}

	//移动
	@Override
	public void move() {
		
		switch(moveType) {
		case TOP: setY(getY()-5);break;
		case LEFT: setX(getX()-5);break;
		case RIGHT: setX(getX()+5);break;
		case DOWN: setY(getY()+5);break;
		case STOP:
		}

	}
	
	//重写父类模板
	@Override
	public void update() {
		
		super.update();
		updateImage();
	}
	
	//更新图片
	public void updateImage() {
		if(moveType==MoveTypeEnum.STOP){
			return ;
		}
		
		moveX++;
		if(moveX>3)
			moveX = 0;
		switch (moveType) {
		case TOP:moveY = 3;break;
		case LEFT:moveY = 1;break;
		case RIGHT:moveY = 2;break;
		case DOWN:moveY = 0;break;
		default:break;
		}
	}

	@Override
	public void destroy() {
		
		
	}
	
	
	//gettes and setters
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public MoveTypeEnum getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveTypeEnum moveType) {
		this.moveType = moveType;
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getMoveY() {
		return moveY;
	}

	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}
	
	

}
