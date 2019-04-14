package com.a225.model.manager;

import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.w3c.dom.NamedNodeMap;

import com.a225.model.loader.ElementLoader;
import com.a225.model.vo.Bubble;
import com.a225.model.vo.Player;
import com.a225.model.vo.SuperElement;

/**
 * 元素工厂类
 * @ClassName: ElementFactory  
 * @Description: 用于构建对象进入元素管理器   
 * @author: WeiXiao
 * @CreateDate: 2019年4月11日 下午5:07:57
 */
public class ElementFactory {
	private static ElementFactory elementFactory;
	
	//构造函数
	private ElementFactory() {}
	
	public static ElementFactory getElementFactory() {
		if(elementFactory == null) {
			elementFactory = new ElementFactory();
		}
		return elementFactory;
	}
	
	public SuperElement produceElement(String name) {
		//TODO:写工厂
		Map<String, List<String>> gameInfoMap = 
				ElementLoader.getElementLoader().getGameInfoMap();//获取资源加载器的游戏信息字典

		switch(name) {
		case "playerOne":
			return Player.createPlayer(gameInfoMap.get(name));
		case "playerTwo":
			return Player.createPlayer(gameInfoMap.get(name));
//		case "bubble":
//			return Bubble.createBubble(gameInfoMap.get(name));
		}
		return null;
	}
}
