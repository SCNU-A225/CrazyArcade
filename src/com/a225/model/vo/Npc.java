package com.a225.model.vo;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.MoveTypeEnum;

public class Npc extends SuperElement{
	
	private List<ImageIcon> img;
	private MoveTypeEnum moveType;//开始为stop
	private int moveX;//记录图片索引
	private int imgW;//图片宽
	private int imgH;//图片高
	private int npcNum;//记录第几个npc，0为npcA，1为npcB,2为npcC
	private int bubbleNum;//记录已经放了多少个炸弹
	private int bubbleLargest;//玩家最多可以放多少个炸弹，初始值为3
	
	private int speed = 8;

	public Npc(int x, int y, int w, int h, int imgW, int imgH,List<ImageIcon> img, int npcNum) {
		super(x, y, w, h);
		this.imgW = imgW;
		this.imgH = imgH;
		this.img = new ArrayList<>(img);
		moveType = MoveTypeEnum.DOWN;
		moveX = 0;
		this.npcNum = npcNum;
		bubbleNum = 0;
		bubbleLargest = 3;
	}
	
	public static Npc createNpc(List<String> data,int i,int j,int npcNum) {
		//data=[玩家图片，x,y,w,h]
		List<ImageIcon> imageList = 
				new ArrayList<>(ElementLoader.getElementLoader().getNpcImageList(data.get(0)));
		int x = j*MapSquare.PIXEL_X+GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y+GameMap.getBiasY();
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		int imgW = Integer.parseInt(data.get(3));
		int imgH = Integer.parseInt(data.get(4));
		return new Npc(x, y, w, h, imgW, imgH, imageList, npcNum);
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(getImg().get(moveX).getImage(),
				getX(), getY(), 
				getX()+getW(), getY()+getH(), 
				0, 0, 
				getImgW(), getImgH(), 
				null);
	}
	
	@Override
	public void update() {
		super.update();
		updateImage();
	}
	
	public void updateImage() {
		switch(moveType) {
		case STOP: moveX = 0;break;
		case LEFT: moveX = 1;break;
		case RIGHT: moveX = 2;break;
		case TOP: moveX = 3;break;
		case DOWN: moveX = 0;break;
		}
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

	public List<ImageIcon> getImg() {
		return img;
	}

	public void setImg(List<ImageIcon> img) {
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

	public int getNpcNum() {
		return npcNum;
	}

	public void setNpcNum(int npcNum) {
		this.npcNum = npcNum;
	}

	public int getBubbleNum() {
		return bubbleNum;
	}

	public void setBubbleNum(int bubbleNum) {
		this.bubbleNum = bubbleNum;
	}

	public int getBubbleLargest() {
		return bubbleLargest;
	}

	public void setBubbleLargest(int bubbleLargest) {
		this.bubbleLargest = bubbleLargest;
	}

	public int getImgW() {
		return imgW;
	}

	public void setImgW(int imgW) {
		this.imgW = imgW;
	}

	public int getImgH() {
		return imgH;
	}

	public void setImgH(int imgH) {
		this.imgH = imgH;
	}
	
	

}
