package com.a225.frame;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GameFrame  extends JFrame{
	
	private KeyListener keyListener; //游戏按键
	private MouseListener mouseListener; //游戏设计使用鼠标点击
	private JPanel jPanel; //画板
	
	public GameFrame() {
		init();
	}

//	初始化
	protected void init() {
		// TODO Auto-generated method stub
		this.setTitle("CrazyArcade");
		this.setSize(1600, 1080);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
//	绑定监听
	public void addListener() {
		if(keyListener!=null)
			this.addKeyListener(keyListener);
		if(mouseListener!=null)
			this.addMouseListener(mouseListener);
	}
	
//	画板注入
	public void addJPanel() {
		if(jPanel!=null)
			this.add(jPanel);
	}
	
//	窗体启动
	public void start() {
		//线程启动
		//界面刷新线程启动
		
		this.setVisible(true);
	}
	
	
//	getter and setter
	public KeyListener getKeyListener() {
		return keyListener;
	}

	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	public MouseListener getMouseListener() {
		return mouseListener;
	}

	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	public JPanel getjPanel() {
		return jPanel;
	}

	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}
	
}
