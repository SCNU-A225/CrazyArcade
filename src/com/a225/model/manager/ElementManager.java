package com.a225.model.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a225.model.loader.ElementLoader;
import com.a225.model.vo.Player;
import com.a225.model.vo.SuperElement;

/**
 * 
 * @author Jenson
 * 元素管理器
 * 单例模式
 */
public class ElementManager {
	
	//元素管理器单例
	private static ElementManager elementManager;
	static {
		elementManager = new ElementManager();
	}
	
	//元素的Map集合
	Map<String, List<SuperElement>> map;
	
	//初始化函数
	protected void init() {
		map = new HashMap<>();
	}
	
	//构造函数
	private ElementManager() {
		init();
		//初始化player的list
		map.put("player", new ArrayList<>());
		map.put("bubble", new ArrayList<>());
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

	public void loadElement() {
		// TODO Auto-generated method stub
		map.get("player").add(ElementFactory.getElementFactory().produceElement("playerOne"));
	}

}
