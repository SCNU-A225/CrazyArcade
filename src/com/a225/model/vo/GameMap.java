package com.a225.model.vo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;

/**
 * 地图类
 * @ClassName: Map  
 * @Description: 地图类   
 * @author: DaXiao
 * @CreateDate: 2019年4月11日 下午21：11
 */
public class GameMap {
	private int windowW;
	private int windowH;
	private int mapRows;
	private int mapCols;
	
	private static int biasX;
	private static int biasY;
	
	private static List<List<String>> mapList;//地图
	
	//自定义方块类型对应枚举类
	public enum SquareType{
		OBSTACLE('0'),FLOOR('1'),FRAGILITY('2'),ITEM('3'),PLAYER_1('6'),PLAYER_2('7'),BUBBLE('9');
		
		private char value = 0;
		
		private SquareType(char value) {
			this.value = value;
		}
		
		public static SquareType valueOf(char c) {    //手写的从int到enum的转换函数  
	        switch (c) {  
	        case '0':  return OBSTACLE;  
	        case '1':  return FLOOR;  
	        case '2':  return FRAGILITY;  
	        case '3':  return ITEM;  
	        case '6':  return PLAYER_1;  
	        case '7':  return PLAYER_2;  
	        case '9':  return BUBBLE;  
	        default:  
	            return null;  
	        }  
	    }  
	  
	    public char value() {  
	        return this.value;  
	    }  
	}
	
	//构造函数
	public GameMap(int windowW,int windowH) {
		this.windowW = windowW;
		this.windowH = windowH;
	}
	
	//创建地板
	private void createFloor() {
		List<SuperElement> floorList = ElementManager.getManager().getElementList("floor");
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				floorList.add(MapFloor.createMapFloor(i, j));
			}
		}
	}
	
	//创建地图元素
	private void createSquare() {
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		Map<String, List<SuperElement>>elmenteMap = ElementManager.getManager().getMap();
		Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
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
				case '6':
					elmenteMap.get("player").add(Player.createPlayer(gameInfoMap.get("playerOne"), i, j));
					break;
				default:
					break;
				}
			}
		}
	}
	
	public void createMap(String pro){
		try {
			mapList = ElementLoader.getElementLoader().readMapPro(pro);
			List<String> size = ElementLoader.getElementLoader().getGameInfoMap().get("mapSize");
			mapRows = Integer.parseInt(size.get(0));
			mapCols = Integer.parseInt(size.get(1));
			biasX = (windowW-MapSquare.PIXEL_X*mapCols)/2;
			biasY = (windowH-MapSquare.PIXEL_Y*mapRows)/2;
			createFloor();
			createSquare();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取地图ij点的方块类型
	 * @param i
	 * @param j
	 * @return 方块类型
	 */
	public SquareType getBlockSquareType(int i,int j) {
		String str = mapList.get(i).get(j);
		return SquareType.valueOf(str.charAt(0));
	}
	
	/**
	 * 设置地图ij点方块类型
	 * @param i
	 * @param j
	 * @param type 方块类型
	 */
	public void setBlockSquareType(int i,int j,SquareType type) {
		mapList.get(i).set(j, type.value+"");
	}
	
	/**
	 * 判断方块是否是障碍物
	 * @param i
	 * @param j
	 * @return 是否是障碍物
	 */
	public boolean blockIsObstacle(int i,int j) {
		String type = mapList.get(i).get(j);
		if(type.charAt(0)-'0' == SquareType.OBSTACLE.value) {
			return true;
		} else {
			return false;
		}
	}
	
	//将xy转换为ij
	public List<Integer> getIJ(int x,int y){
		List<Integer> list = new ArrayList<>();
		list.add((x-biasX)/MapSquare.PIXEL_X);
		list.add((y-biasY)/MapSquare.PIXEL_Y);
		return list;
	}
	
	//将ij转换为xy
	public List<Integer> getXY(int i,int j){
		List<Integer> list = new ArrayList<>();
		list.add(j*MapSquare.PIXEL_X+biasX);
		list.add(i*MapSquare.PIXEL_Y+biasY);
		return list;
	}
	
	public void clearMap() {
		ElementManager.getManager().getElementList("obstacle").clear();
		ElementManager.getManager().getElementList("fragility").clear();
		ElementManager.getManager().getElementList("floor").clear();
		ElementManager.getManager().getElementList("player").clear();
		ElementManager.getManager().getElementList("explode").clear();
	}

	public static List<List<String>> getMapList(){
		return mapList; 
	}
	public static int getBiasX() {
		return biasX;
	}
	public static int getBiasY() {
		return biasY;
	}
	
}
