package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.text.html.parser.TagElement;

import com.a225.model.loader.ElementLoader;

public class BubbleExplode extends SuperElement{
	
	private List<ImageIcon> img; //±£´æ±¬Õ¨Í¼Æ¬
	private int moveX;


	public BubbleExplode(int x,int y, int w, int h, List<ImageIcon> imageList) {
		//²ÎÊý±í£º×ø±êx£¬×ø±êy£¬¿íw£¬¸ßh£¬±¬Õ¨Í¼Æ¬ÁÐ±í
		super(x, y, w, h);
		img = new ArrayList<>(imageList);
		moveX = 0;
	}
	
	//´´½¨ÊµÀý
	public static BubbleExplode createExplode(int x, int y,List<String> list) {
		//list=[Í¼Æ¬0£¬Í¼Æ¬1£¬Í¼Æ¬2£¬Í¼Æ¬¿íw£¬Í¼Æ¬¸ßh]
		int w = Integer.parseInt(list.get(3));
		int h = Integer.parseInt(list.get(4));
		List<ImageIcon> imageList = new ArrayList<>();
		Map<String, ImageIcon> imageMap = ElementLoader.getElementLoader().getImageMap();
		for(int i=0; i<3; i++) { //±¬Õ¨Ð§¹ûÍ¼ÓÐ3ÕÅ
			imageList.add(imageMap.get(list.get(i)));
		}
		return new BubbleExplode(x, y, w, h, imageList);
	}

	
	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.get(moveX).getImage(), 
				getX()-2*MapSquare.PIXEL_X, getY()-2*MapSquare.PIXEL_Y, 	//ÆÁÄ»×óÉÏ½Ç×ø±ê
				getX()+3*MapSquare.PIXEL_X, getY()+3*MapSquare.PIXEL_Y, 	//ÆÁÄ»ÓÒÏÂ×ø±ê
				0, 0, 				//Í¼Æ¬×óÉÏ×ø±ê
				getW(), getH(), 			//Í¼Æ¬ÓÒÏÂ×ø±ê\
				null);
	}

	//¸ü»»Í¼Æ¬£¬Í¼Æ¬²¥Íêºó½øÈëÏûÍö×´Ì¬
	@Override
	public void move() {
		if(moveX<2) {
			moveX++;
		}
	}

	//±¬Õ¨Ð§¹û³ÖÐø0.8Ãë
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
	
	//ÅÐ¶Ï±¬Õ¨ÓëÎïÌå±ßÔµ³åÍ»
	@Override
	public boolean crash(SuperElement se) {
		Rectangle explodeColumn = 
				new Rectangle(getX(), getY()-2*MapSquare.PIXEL_Y, MapSquare.PIXEL_X, 5*MapSquare.PIXEL_Y);//Ë®ÅÝ±¬Õ¨Ê®×Ö×ÝÏò
		Rectangle explodeRow = 
				new Rectangle(getX()-2*MapSquare.PIXEL_X, getY(), 5*MapSquare.PIXEL_X, MapSquare.PIXEL_Y);//Ë®ÅÝ±¬Õ¨Ê®×ÖºáÏò
		Rectangle rectangle = new Rectangle(se.getX(), se.getY(), se.getW(), se.getH());
		boolean column = explodeColumn.intersects(rectangle);
		boolean row = explodeRow.intersects(rectangle);
		return (column||row);
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


}
