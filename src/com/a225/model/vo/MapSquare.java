package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;


public class MapSquare extends SuperElement{
	final static int pixelx = 32;//像素x
	final static int pixely = 32;//像素y
	private ImageIcon img;
	private boolean beDestoried;
	private boolean beWalked;
	private int sx,sy,dx,dy;
	
public MapSquare(int x, int y, int w, int h,boolean beDestoried,boolean beWalked){
		super(x, y, w, h);
		this.img=img;
		beDestoried=false;
		beWalked=false;
		
	}

/**
 * 地图方块类
 * @ClassName: MapSquare  
 * @Description: 地图方块VO类   
 * @author: DaXiao
 * @CreateDate: 2019年4月11日 下午23：11
 */

//0：障碍物方块，1：道路方块，2：可摧毁方块
//#01：树，02：小房子，11：绿色方块，21：红色方块
	public static MapSquare createMapSquare(List<String> data,int x, int y){
		ImageIcon img = ElementLoader.getElementLoader().getImageMap().get(data.get(0));
		int sx = Integer.parseInt(data.get(1));
		int sy = Integer.parseInt(data.get(2));
		int dx = Integer.parseInt(data.get(3));
		int dy = Integer.parseInt(data.get(4));
		int xLoc  = pixelx*x;
		int yLoc  = pixely*y;
		boolean beDestoied = data.get(5).equals("0")?false:true;
		boolean beWalked = data.get(5).equals("0")?false:true;
//		调整方块位置
		if(dy-sy == pixely) yLoc = yLoc - pixely;
//		switch (dy-sy) {
//		case pixely:
//			yLoc =yLoc - pixely;
//			break;
//		case pixely/2:
//			yLoc = yLoc - pixely/2;
//			break;
//		default:
//			break;
//		}
		MapSquare mapSquare = new MapSquare(xLoc, yLoc, dy-sy, dx-sx, beDestoied, beWalked);
		mapSquare.setPictureLoc(sx, sy, dx, dy);
		return mapSquare;
	}
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(img.getImage(), 
				getX(), getY(),                  //屏幕左上角坐标
				getX()+pixelx, getY()+pixely,    //屏幕右下角坐标
					sx, sy,    //图片左上角坐标        60 ,0
					dx, dy,    //图片右下角坐标  120,60
					null);
	}
	
	public void setPictureLoc(int sx,int sy,int dx,int dy){
		this.sx = sx;
		this.sy = sy;
		this.dx = dx;
		this.dy = dy;
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
