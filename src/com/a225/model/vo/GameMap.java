package com.a225.model.vo;

import java.awt.Graphics;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;

public class GameMap {


/**
 * 地图类
 * @ClassName: Map  
 * @Description: 地图类   
 * @author: DaXiao
 * @CreateDate: 2019年4月11日 下午21：11
 */
	private int mapSizeW;
	private int mapSizeH;
	//List<List<String>> list;
	
	public GameMap(String pro) {
		createMap(pro);//mapA.pro
	}
	
	private void createMap(String pro){
		try {
			Map<String, List<String>> squareTypeMap = ElementLoader.getElementLoader().getSquareTypeMap();
			ElementLoader.getElementLoader().readMapPro(pro);
			List<List<String>> mapList = ElementLoader.getElementLoader().getMapList();
			for (int i = 0; i < mapList.size(); i++) {
				for (int j = 0; j < mapList.get(i).size(); j++) {
					String type = mapList.get(i).get(j);
					MapSquare.createMapSquare(squareTypeMap.get(type), i, j);
				}
			}
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
//	public static GameMap createGameMap(List<List<String>> list) {
//		
//		
//		
//		return null;
//		
//	}


	
}
