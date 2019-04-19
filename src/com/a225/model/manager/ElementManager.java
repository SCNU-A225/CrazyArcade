package com.a225.model.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a225.model.loader.ElementLoader;
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
	
	//图层顺序map
	private Map<String,Integer> priorityMap;
	
	//游戏地图
	private GameMap gameMap;
	
	
	//初始化函数
	protected void init() {
		Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
		List<String> windowSize = gameInfoMap.get("windowSize");
		gameMap = new GameMap(Integer.parseInt(windowSize.get(0)),Integer.parseInt(windowSize.get(1)));
		map = new HashMap<>();
		priorityMap = new HashMap<>();
	}
	
	//初始化元素列表字典
	private void initMap() {
		map.put("player", new ArrayList<SuperElement>());//玩家
		map.put("bubble", new ArrayList<SuperElement>());//水泡
		map.put("explode",new ArrayList<SuperElement>());//水泡爆炸
		map.put("fragility", new ArrayList<SuperElement>());//可破坏方块
		map.put("floor", new ArrayList<SuperElement>());//地板
		map.put("obstacle", new ArrayList<SuperElement>());//不可破坏方块
		map.put("magicBox", new ArrayList<SuperElement>());//道具
		map.put("npc", new ArrayList<SuperElement>());
	}
	
	//初始化图层优先级字典
	private void initPriorityMap() {
		priorityMap.put("player", 50);
		priorityMap.put("npc", 45);
		priorityMap.put("bubble", 10);
		priorityMap.put("obstacle", 40);
		priorityMap.put("explode", 30);
		priorityMap.put("magicBox", 25);
		priorityMap.put("fragility", 20);
		priorityMap.put("bubble", 10);
		priorityMap.put("floor", -10);
	}
	
	//构造函数
	private ElementManager() {
		init();//初始化变量
		initMap();//初始化元素列表字典
		initPriorityMap();//初始化图层优先级字典
	}
	
	
	//图层优先级比较器
	public Comparator<String> getMapKeyComparator() {
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
	
	public void loadMap(){
		gameMap.createMap("stage1Map");
	}

	public void overGame(Boolean over) {
		if(over) {
			gameMap.clearMapALL();
			//失败动画
		} else {
			gameMap.clearMapOther();
			//恭喜动画
		}
	}


}

