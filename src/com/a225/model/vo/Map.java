package com.a225.model.vo;

import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

public class Map extends SuperElement{


/**
 * 地图类
 * @ClassName: Map  
 * @Description: 地图VO类   
 * @author: DaXiao
 * @CreateDate: 2019年4月11日 下午21：11
 */
	private int mapSizeW;
	private int mapSizeH;
	List<List<String>> list;
	
	public Map(int x, int y, int w, int h) {
		super(x, y, w, h);
		mapSizeW = 0;
		mapSizeH = 0;
	}
	
	@Override
	public void showElement(Graphics g) {
		
		
	}

	@Override
	public void move() {
		
		
	}

	@Override
	public void destroy() {
		
		
	}

	
}
