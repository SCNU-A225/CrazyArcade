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
		String url = "img/Characters/Done_body16001_walk.png";
		return new Player(0, 0, 50, 60, new ImageIcon(url));
	}

	//展示人物图片
	@Override
	public void showElement(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(img.getImage(), 
				getX(), getY(), //屏幕左上角坐标
				getX()+getW(), getY()+getH(), //屏幕右下角坐标
				60*moveX, 0, //图片左上角坐标60,0
				60*(moveX+1), 60, //图片右下角坐标120,60
				null);
	}

	//移动
	@Override
	public void move() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		super.update();
		updateImage();
	}
	
	//更新图片
	public void updateImage() {
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
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
