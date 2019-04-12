package com.a225.model.vo;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;

/**
 * 水泡炸弹类
 * @author 麻瓜
 *
 */
public class Bubble extends SuperElement{
	
	private ImageIcon img;
	private int moveX;

	//构造函数
	public Bubble(int x, int y, int w, int h, ImageIcon img) {
		super(x, y, w, h);
		this.img = img;
		moveX = 0;
	}
	
	//创建气泡1
	public static Bubble createBubble(int x, int y,List<String> list) {
		//list=[Bubble,w,h]
		int w = Integer.parseInt(list.get(1));
		int h = Integer.parseInt(list.get(2));
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Bubble(x, y, w, h, imageMap.get(list.get(0)));
	}
	//创建气泡2
	public static Bubble createBubble(List<String> list){
		//list=[水泡图片，图片宽w，图片高h]
		int w = Integer.parseInt(list.get(1));
		int h = Integer.parseInt(list.get(2));
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Bubble(0, 0, w, h, imageMap.get(list.get(0)));
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.getImage(), 
				getX(), getY(), 	//屏幕左上角坐标
				getX()+getW(), getY()+getH(), 	//屏幕右下坐标
				moveX*32, 0, 				//图片左上坐标
				moveX*32+32, 46, 			//图片右下坐标
				null);
	}
	
	//重写父类模板
	@Override
	public void update() {
		super.update();
		updateImage();
	}

	//更新图片
	public void updateImage() {
		moveX++;
		moveX = moveX % 4;
	}
	
	@Override
	public void move() {
		
	}
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	//getters and setters
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}
	
}
