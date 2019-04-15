package com.a225.model.vo;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;

public class MagicBox extends MapSquare{

	private boolean eaten;//被吃掉消失。
	private int moveX;
	static Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
	static String type;
	
	public MagicBox(int x, int y, ImageIcon img, 
			int sx, int sy, int dx,int dy, int scaleX, int scaleY) {
		super(x, y, img, sx, sy, dx, dy, scaleX, scaleY);
		moveX = 0;
		eaten = false;
	}
	
	public static MagicBox createMagicBox(int x,int y) {
//		下生成随机数
		Random rd = new Random();
		int letter = rd.nextInt(8)+1;
		type = "3" + letter;
		List<String> data = typeMap.get(type);
		int sx = Integer.parseInt(data.get(1));
		int sy = Integer.parseInt(data.get(2));
		int dx = Integer.parseInt(data.get(3));
		int dy = Integer.parseInt(data.get(4));
		int scaleX = Integer.parseInt(data.get(6));
		int scaleY = Integer.parseInt(data.get(7));
		ImageIcon img = ElementLoader.getElementLoader().getImageMap().get(data.get(0));
		MagicBox magicBox = new MagicBox(x, y, img, sx, sy, dx, dy, scaleX, scaleY);
		return magicBox;
	}
	
	@Override
	public void update() {
		// TODO 自动生成的方法存根
		super.update();
		updateImage();
	}
	
//	切换图片
	public void updateImage() {
		if(eaten) return;
		moveX = ++moveX%3;
		int sx = moveX*getW()/2;
		int sy = Integer.parseInt(typeMap.get(type).get(2));
		int dx = (moveX+1)*getW()/2;
		int dy = Integer.parseInt(typeMap.get(type).get(4));
		setPictureLoc(sx, sy, dx, dy);
	}

	@Override
	public void destroy() {
		// TODO 自动生成的方法存根
		if(eaten){			
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> list = gameMap.getIJ(getX(), getY());
			gameMap.setBlockSquareType(list.get(0), list.get(1), GameMap.SquareType.FLOOR);
			System.out.println("被吃啦");
//			得到buff
			
			
			eaten = false;
			setAlive(false);
		}
		
	}
//	人物接触道具
	public boolean isEaten() {
		return eaten;
	}

	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}
	
}
