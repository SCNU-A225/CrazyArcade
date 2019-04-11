package com.a225.model.vo;

import java.awt.Graphics;

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
	
	//getters and setters
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
