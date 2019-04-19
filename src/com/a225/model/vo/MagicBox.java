package com.a225.model.vo;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;
import com.a225.model.manager.GameMap;

public class MagicBox extends MapSquare{
	private static Random rd = new Random();
	private boolean eaten;//被吃掉消失。
	private int moveX;//图片变换
	private String type;//道具类型
	private int player;//哪个player获得道具
	
	static Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
	
	public MagicBox(int i, int j, ImageIcon img, 
			int sx, int sy, int dx,int dy, int scaleX, int scaleY, String type) {
		super(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
		moveX = 0;
		eaten = false;
		this.type = type;
	}
	
	// 现有各数出现概率
	static Map<String, Integer> keyChanceMap = new HashMap<String, Integer>();
    static {
        keyChanceMap.put("1", 20);// rate = 0.2 
        keyChanceMap.put("3", 5); // rate = 0.05
        keyChanceMap.put("4", 20);// rate = 0.2  
        keyChanceMap.put("5", 30);// rate = 0.3  
        keyChanceMap.put("7", 10);// rate = 0.1  
        keyChanceMap.put("8", 20);// rate = 0.2
        keyChanceMap.put("9", 5); // rate = 0.05
    } 
    public static String chanceSelect(Map<String, Integer> keyChanceMap) {
        if (keyChanceMap == null || keyChanceMap.size() == 0)
            return null;

        Integer sum = 0;
        for (Integer value : keyChanceMap.values()) {
            sum += value;
        }
        // 从1开始
        Integer rand = new Random().nextInt(sum) + 1;

        for (Map.Entry<String, Integer> entry : keyChanceMap.entrySet()) {
            rand -= entry.getValue();
            // 选中
            if (rand <= 0) {
                String item = entry.getKey();
                return item;
            }
        }
        return null;
    }

	public static MagicBox createMagicBox(int i,int j) {
    	String letter = "0";
    	letter = chanceSelect(keyChanceMap);
		String boxtype = "3" + letter;
		System.out.println(boxtype);
		List<String> data = typeMap.get(boxtype);
		int sx = Integer.parseInt(data.get(1));
		int sy = Integer.parseInt(data.get(2));
		int dx = Integer.parseInt(data.get(3));
		int dy = Integer.parseInt(data.get(4));
		int scaleX = Integer.parseInt(data.get(6));
		int scaleY = Integer.parseInt(data.get(7));
		ImageIcon img = ElementLoader.getElementLoader().getImageMap().get(data.get(0));
		MagicBox magicBox = new MagicBox(i, j, img, sx, sy, dx, dy, scaleX, scaleY, boxtype);
		return magicBox;
	}
	
//	重写crash方法，缩小碰撞体积
	@Override
	public boolean crash(SuperElement se) {
		Rectangle r1 = new Rectangle(getX()+getW()/4, getY()+getH()/4, getW()/2, getH()/2);
		Rectangle r2 = new Rectangle(se.getX()+se.getW()/4, se.getY()+se.getH()/4, se.getW()/2, se.getH()/2);
		return r1.intersects(r2);//有交集范围true
	}
	
	@Override
	public void update() {
		// TODO 自动生成的方法存根
		super.update();
		updateImage();
	}
	
//	切换图片
	public void updateImage() {
		if(eaten) return;
		if(++moveX>=40)
			moveX = 0;
		int sx = (moveX/10)*32;
		int sy = Integer.parseInt(typeMap.get(type).get(2));
		int dx = (moveX/10+1)*32;
		int dy = Integer.parseInt(typeMap.get(type).get(4));
		setPictureLoc(sx, sy, dx, dy);
	}


	@Override
	public void destroy() {
		if(eaten){	
//			将被摧毁方块设置为地板
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> list = GameMap.getIJ(getX(), getY());
			gameMap.setBlockSquareType(list.get(0), list.get(1), GameMap.SquareType.FLOOR);
//			得到buff
			List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
			Player player = (Player) playerList.get(this.getPlayer());
			switch (type) {
			case "31": //使移动反向 10s
				player.changeDirection(10);//传入方向改变的持续时间（秒）
				break;
			case "33": //增加生命值
				player.setHealthPoint(1);//传入增加的生命值个数
				break;
			case "34": //增加移动速度 10s
				player.changeSpeed(2,10);//传入移速增加倍数和持续时间（秒）
				break;
			case "35": //气泡个数增加
				player.setBubbleLargest(player.getBubbleLargest()+1);
				break;	
			case "37": //其他玩家停止5s
				player.setOtherStop(5);
				break;	
			case "38": //威力增加
				player.bubbleAddPower();//传入方向改变的持续时间（秒）
				break;	
			case "39" ://无敌5s
				player.setUnstoppable(5);//传入方向改变的持续时间（秒）
				break;	
			default:

				break;
			}
			
			eaten = false;
			setAlive(false);
		}
		
	}
//	人物接触道具
	public boolean isEaten() {
		return eaten;
	}

	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}
	
	
}
