package com.a225.model.vo;

import java.awt.Graphics;
import javax.swing.ImageIcon;

import com.a225.model.manager.ElementManager;

/**
 * 地图方块类
 * @ClassName: MapSquare  
 * @Description: 地图方块VO类   
 * @author: DaXiao
 * @CreateDate: 2019年4月11日 下午23：11
 */
public class MapSquare extends SuperElement{
	public static int pixelx = 64;//单位像素x
	public static int pixely = 64;//单位像素y
	private ImageIcon img;
	private int sx,sy,dx,dy;
	
	public MapSquare(int i, int j ,ImageIcon img, int sx, int sy, int dx, int dy, int scaleX,int scaleY) {
		super((j-scaleX+1)*pixelx+ElementManager.getManager().getGameMap().getBiasX(), 
				(i-scaleY+1)*pixely+ElementManager.getManager().getGameMap().getBiasY(), 
				pixelx*scaleX, pixely*scaleY);
		this.img = img;
		this.setPictureLoc(sx, sy, dx, dy);
	}
	
	@Override
	public final void showElement(Graphics g) {
		g.drawImage(img.getImage(), 
				getX(), getY(),                  //屏幕左上角坐标
				getX()+getW(), getY()+getH(),    //屏幕右下角坐标
				sx, sy,    //图片左上角坐标
				dx, dy,    //图片右下角坐标
				null);
	}
	
	public void setPictureLoc(int sx,int sy,int dx,int dy){
		this.sx = sx;
		this.sy = sy;
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void move() {}

	@Override
	public void destroy() {}
	
}
