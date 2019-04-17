package com.a225.model.vo;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;

/**
 * 水泡炸弹类
 * @author 麻瓜
 *
 */
public class Bubble extends SuperElement{
	
	private ImageIcon img;
	private int moveX;
	private int playerNum;//表示对应玩家的炸弹，0为玩家一，1为玩家二，2为npcA，3为npcB，4为npcC

	//构造函数
	public Bubble(int x, int y, int w, int h, ImageIcon img, int playerNum) {
		super(x, y, w, h);
		this.img = img;
		this.playerNum = playerNum;
		moveX = 0;
		//地图对应位置设置为障碍物，不能通过
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(x, y);
		gameMap.setBlockSquareType(maplist.get(0), maplist.get(1), GameMap.SquareType.BUBBLE);
	}
	
	//创建气泡1
	public static Bubble createBubble(int x, int y,List<String> list,int playerNum) {
		//list=[Bubble,w,h]
		int w = Integer.parseInt(list.get(1));
		int h = Integer.parseInt(list.get(2));
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Bubble(x, y, w, h, imageMap.get(list.get(0)),playerNum);
	}
	//创建气泡2
	public static Bubble createBubble(List<String> list,int playerNum){
		//list=[水泡图片，图片宽w，图片高h]
		int w = Integer.parseInt(list.get(1));
		int h = Integer.parseInt(list.get(2));
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Bubble(0, 0, w, h, imageMap.get(list.get(0)),playerNum);
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.getImage(), 
				getX(), getY(), 	//屏幕左上角坐标
				getX()+MapSquare.PIXEL_X, getY()+MapSquare.PIXEL_Y, 	//屏幕右下坐标
				moveX*getW(), 0, 				//图片左上坐标
				(moveX+1)*getW(), 46, 			//图片右下坐标
				null);
	}
	
	//重写父类模板
	@Override
	public void update() {
		super.update();
		updateImage();
	}

	//更新图片
	public void updateImage() {
		moveX++;
		moveX = moveX % 4;
	}
	
	//使用计时器，2.5秒改变Alive状态
	@Override
	public void move() {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				setAlive(false);
			}
		};
		timer.schedule(task, 2500);
	}
	

	@Override
	public void destroy() {
		if(!isAlive()) {	//显示爆炸效果，加入ExplodeBubble
			List<SuperElement> list = 
					ElementManager.getManager().getElementList("explode");
			list.add(BubbleExplode.createExplode(getX(), getY(), ElementLoader.getElementLoader().getGameInfoMap().get("explode")));
			
			//将地图位置设为floor
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> maplist = GameMap.getIJ(getX(), getY());
			gameMap.setBlockSquareType(maplist.get(0), maplist.get(1), GameMap.SquareType.FLOOR);
			
			//改变炸弹玩家已经放在炸弹数bubbleNum
			if(playerNum<2) {
				List<SuperElement> list2 = ElementManager.getManager().getElementList("player");
				Player player = (Player) list2.get(playerNum);
				player.setBubbleNum(player.getBubbleNum()-1);
			}
			else {
				List<SuperElement> list2 = ElementManager.getManager().getElementList("npc");
				Npc npc = (Npc) list2.get(playerNum-2);
				npc.setBubbleNum(npc.getBubbleNum()-1);
			}
		}
	}

	//getters and setters
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}
	
	public int getPlayerNum() {
		return this.playerNum;
	}
}
