package com.a225.model.manager;

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
	
	public SuperElement produceElement() {
		//TODO:写工厂
		return null;
	}
}
