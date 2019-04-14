package com.a225.model.vo;

import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * 地图方块类
 * @ClassName: Map  
 * @Description: 地图方块VO类   
 * @author: DaXiao
 * @CreateDate: 2019年4月11日 下午23：11
 */
public class MapSquare extends SuperElement{
	private ImageIcon img;
	private boolean beDestoried;
	private boolean beWalked;
	private int moveX;
	private int moveY;
	
public MapSquare(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img=img;
		beDestoried=false;
		beWalked=false;
	}

//
//
//	public static MapSquare createMapSquare(String s){
//		switch (s) {
//		case "01":
//			moveX=2;
//			moveY=1;
//			break;
//		case "02":
//			
//			break;
//		case "11":
//			
//			break;
//		case "21":
//	
//			break;
//		default:
//			break;
//		}
//		ImageIcon img=ElementLoad.getElementLoad().getMap().get(arrStrings[0]);
//		return new Player(x,y,w,h,img);
//		return new PlayerFire(x,y,30,30,img);
//	}
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(img.getImage(), 
				getX(), getY(),                  //屏幕左上角坐标
				getX()+getW(), getY()+getH(),    //屏幕右下角坐标
					60*moveX, 0,    //图片左上角坐标        60 ,0
					60*(moveX+1), 60,    //图片右下角坐标  120,60
//					moveX, 0,    //图片左上角坐标        60 ,0
//					moveX+60, 60,	
					null);
	}

	@Override
	public void move() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void destroy() {
		// TODO 自动生成的方法存根
		
	}

}
