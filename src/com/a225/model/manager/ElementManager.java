package com.a225.model.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.a225.model.loader.ElementLoader;
import com.a225.model.vo.GameMap;
import com.a225.model.vo.MapFloor;
import com.a225.model.vo.SuperElement;

/**
 * 元素管理器
 * 单例模式
 * @author Jenson
 */
public class ElementManager {
	//元素管理器单例
	private static ElementManager elementManager;
	static {
		elementManager = new ElementManager();
	}
	
	//元素的Map集合
	private Map<String, List<SuperElement>> map;
	
	//游戏地图
	private GameMap gameMap;
	
	//初始化函数
	protected void init() {
		Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
		List<String> windowSize = gameInfoMap.get("windowSize");
		gameMap = new GameMap(Integer.parseInt(windowSize.get(0)),Integer.parseInt(windowSize.get(1)));
		map = new HashMap<>();
	}
	
	//构造函数
	private ElementManager() {
		init();
		//初始化player的list
		map.put("player", new ArrayList<SuperElement>());//玩家
		map.put("bubble", new ArrayList<SuperElement>());//水泡
		map.put("explode",new ArrayList<SuperElement>());//水泡爆炸
		map.put("fragility", new ArrayList<SuperElement>());
		map.put("floor", new ArrayList<SuperElement>());
		map.put("obstacle", new ArrayList<SuperElement>());
	}
	
	
	//键值比较器
	public Comparator<String> getMapKeyComparator() {
		Map<String, Integer> priorityMap = new HashMap<>();
		priorityMap.put("player", 50);
		priorityMap.put("bubble", 10);
		priorityMap.put("explode", 30);
		priorityMap.put("fragility", 20);
		priorityMap.put("floor", -10);
		priorityMap.put("obstacle", 40);
		return new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int p1 = priorityMap.get(o1);
				int p2 = priorityMap.get(o2);
				if(p1 > p2) {
					return 1;
				} else if(p1 < p2) {
					return -1;
				} else {
					return 0;
				}
			}
		};
	}
	
	//图层透视比较器	线程不安全
	public Comparator<SuperElement> getElementComparator() {
		return new Comparator<SuperElement>() {
			@Override
			public int compare(SuperElement o1, SuperElement o2) {
				if(o1 instanceof MapFloor) return -1;//地板永远最先显示
	
				int loc1 = o1.getY()+o1.getH();
				int loc2 = o2.getY()+o2.getH();
				if(loc1 > loc2) {
					return 1;
				} else if(loc1 < loc2) {
					return -1;
				} else {
					return 0;
				}
			}
		};
	}
	
	//获得map集合
	public Map<String, List<SuperElement>> getMap(){
		return map;
	}
	
	//得到元素list
	public List<SuperElement> getElementList(String key){
		return map.get(key);
	}
	
	//元素管理器入口
	public static ElementManager getManager() {
		return elementManager;
	}
	
	//获取游戏地图类
	public GameMap getGameMap() {
		return gameMap;
	}

	public void loadElement() {
		// TODO Auto-generated method stub
		//map.get("player").add(ElementFactory.getElementFactory().produceElement("playerOne"));
		
	}
	
	public void loadMap(){
		gameMap.createMap("stage1Map");
	}

	public void overGame() {
		gameMap.clearMap();
	}

}

