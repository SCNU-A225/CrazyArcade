package com.a225.model.vo;

import java.util.List;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;

/**
 * 地图可破坏物体类
 * @ClassName: MapFragility  
 * @Description:    
 * @author: WeiXiao
 * @CreateDate: 2019年4月13日 下午6:31:49
 */
public class MapFragility extends MapSquare{

	public MapFragility(int i, int j, ImageIcon img, int sx, int sy, int dx, int dy, int scaleX, int scaleY) {
		super(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
	}
	
	public static MapFragility createMapFragility(List<String> data,int i, int j) {
		ImageIcon img = ElementLoader.getElementLoader().getImageMap().get(data.get(0));
		int sx = Integer.parseInt(data.get(1));
		int sy = Integer.parseInt(data.get(2));
		int dx = Integer.parseInt(data.get(3));
		int dy = Integer.parseInt(data.get(4));
		int scaleX = Integer.parseInt(data.get(6));
		int scaleY = Integer.parseInt(data.get(7));
		MapFragility mapMapFragility = new MapFragility(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
		return mapMapFragility;
	}
	
	@Override
	public void update() {
		destroy();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if(!isAlive()) {
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> list = GameMap.getIJ(getX(), getY());
			gameMap.setBlockSquareType(list.get(0), list.get(1), GameMap.SquareType.FLOOR);
		}
	}

}
