package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 
 * @author Jenson
 * 所有游戏元素的父类
 */
public abstract class SuperElement {
	
	//元素坐标
	private int x;
	private int y;
	private int w;
	private int h;
	//记录是否存活
	private boolean alive; 
	
	//构造函数
	private SuperElement() {}
	public SuperElement(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		alive = true;
	}
	
	//模板类
	public void update() {
		move();
		destroy();
	}
	
	//元素展示
	public abstract void showElement(Graphics g);
	
	//元素移动
	public abstract void move();
	
	//元素销毁
	public abstract void destroy();
	
	//this pk 参数，对象具有
	public boolean crash(SuperElement se) {
		Rectangle r1 = new Rectangle(x, y, w, h);
		Rectangle r2 = new Rectangle(se.x, se.y, se.w, se.h);
		return r1.intersects(r2);//有交集范围true
	}
	
	//参数 pk 参数，比较工具
	public static boolean crash(SuperElement se1,SuperElement se2) {
		Rectangle r1 = new Rectangle(se1.x, se1.y, se1.w, se1.h);
		Rectangle r2 = new Rectangle(se2.x, se2.y, se2.w, se2.h);
		return r1.intersects(r2);//有交集范围true
	}
	
	//getters and setters
	public void setGeometry(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
}
