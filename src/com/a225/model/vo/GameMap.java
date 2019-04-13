package com.a225.model.vo;

import java.awt.Graphics;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementFactory;
import com.a225.model.manager.ElementManager;

public class GameMap {


/**
 * 地图类
 * @ClassName: Map  
 * @Description: 地图类   
 * @author: DaXiao
 * @CreateDate: 2019年4月11日 下午21：11
 */
	private int windowW;
	private int windowH;
	private int mapRows;
	private int mapCols;
	
	private int biasX;
	private int biasY;
	
	public GameMap() {}
	public GameMap(int windowW,int windowH) {
		this.windowW = windowW;
		this.windowH = windowH;
	}
	
	private void createFloor() {
		List<SuperElement> floorList = ElementManager.getManager().getElementList("floor");
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				floorList.add(MapFloor.createMapFloor(i, j));
			}
		}
	}
	
	private void createSquare() {
		List<List<String>> mapList = ElementLoader.getElementLoader().getMapList();
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		Map<String, List<SuperElement>>elmenteMap = ElementManager.getManager().getMap();
		for (int i = 0; i < mapRows; i++) {
			for (int j = 0; j < mapCols; j++) {
				String type = mapList.get(i).get(j);
				switch (type.charAt(0)) {
				case '0':
					elmenteMap.get("obstacle").add(MapObstacle.createMapObstacle(typeMap.get(type), i, j));
					break;
				case '2': 
					elmenteMap.get("fragility").add(MapFragility.createMapFragility(typeMap.get(type), i, j));
					break;
				default:
					break;
				}
				
			}
		}
	}
	
	public void createMap(String pro){
		try {
			ElementLoader.getElementLoader().readMapPro(pro);
			List<String> size = ElementLoader.getElementLoader().getGameInfoMap().get("mapSize");
			mapRows = Integer.parseInt(size.get(0));
			mapCols = Integer.parseInt(size.get(1));
			biasX = (windowW-MapSquare.pixelx*mapCols)/2;
			biasY = (windowH-MapSquare.pixely*mapRows)/2;
			
			createFloor();
			createSquare();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clearMap() {
		ElementManager.getManager().getElementList("obstacle").clear();
		ElementManager.getManager().getElementList("fragility").clear();
	}

	public List<List<String>> getMapList(){
		return ElementLoader.getElementLoader().getMapList(); 
	}
	public int getBiasX() {
		return biasX;
	}
	public int getBiasY() {
		return biasY;
	}
	
}
