package com.a225.model.vo;

import java.awt.Graphics;
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
		// TODO Auto-generated method stub
//		g.drawImage(img.get(moveX).getImage(), getX(), getY(), getW(), getH(), null);
//		g.drawImage(img.get(moveX).getImage(), getX(), getY(),null);
//		g.drawImage(img.get(moveX).getImage(), getX(), getY(), 32, 32, null);
		g.drawImage(img.get(moveX).getImage(), 
				getX()-64, getY()-64, 	//ÆÁÄ»×óÉÏ½Ç×ø±ê
				getX()+getW()-64, getY()+getH()-64, 	//ÆÁÄ»ÓÒÏÂ×ø±ê
				0, 0, 				//Í¼Æ¬×óÉÏ×ø±ê
				getW(), getH(), 			//Í¼Æ¬ÓÒÏÂ×ø±ê
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
