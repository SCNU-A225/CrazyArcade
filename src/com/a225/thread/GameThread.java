package com.a225.thread;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.a225.main.GameStart;
import com.a225.model.manager.ElementManager;
import com.a225.model.vo.MagicBox;
import com.a225.model.vo.MapFragility;
import com.a225.model.vo.Npc;
import com.a225.model.vo.Player;
import com.a225.model.vo.SuperElement;

/**
 * 游戏线程控制
 * @author 麻瓜
 *
 */
public class GameThread extends Thread{
	private boolean running;//表示当前关卡是否在进行
	private boolean over = false;//表示游戏是否结束，结束返回开始菜单
	
	@Override
	public void run() {
		while(!over) {
			running = true;//当前关卡正在进行
			//加载元素
			loadElement();
			//显示人物，流程，自动化
			runGame();
			//结束当前关
			overGame(over);
		}
		GameStart.changeJPanel(false);
	}
	
	//加载元素
	private void loadElement() {
		ElementManager.getManager().loadMap();//加载地图及其元素
	}
	
	/**
	 * 关卡结束
	 * 如果over为真则游戏失败返回界面，否则进入下一关
	 * @param over
	 */
	private void overGame(Boolean over) {
		ElementManager.getManager().overGame(over);
	}
	
	//显示人物，游戏流程，自动化
	private void runGame() {
		while(running) {
			Map<String, List<SuperElement>> map = ElementManager.getManager().getMap();
			Set<String> set = map.keySet();
			for(String key:set) {
				List<SuperElement> list = map.get(key);
				for(int i=list.size()-1; i>=0; i--) {
					list.get(i).update();
					if(!list.get(i).isAlive())
						list.remove(i);
				}
			}
			
			//添加游戏的流程控制linkGame()?
			
			//玩家与炸弹碰撞死亡
			playerBoom();
			//可破坏物与炸弹碰撞
			fragilityBoom();
			//电脑与炸弹碰撞死亡
			npcBoom();
			//玩家与道具碰撞效果
			playerMagicBox();
			//检测是否玩家全部死亡
			defeat();
			
			//控制runGame进程
			try {	
				sleep(20);
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	private void defeat() {
		boolean allDead = true;
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		for(SuperElement se:playerList) {
			if(!((Player)se).isDead()) {
				allDead = false;
			}
		}
		if(allDead) {
			running = false;
			over = true;
		}
	}
	
	//玩家与炸弹碰撞判断
	private void playerBoom() {
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(int i=0; i<playerList.size(); i++) {
			for(int j=0; j<explodeList.size(); j++) {
				if(explodeList.get(j).crash(playerList.get(i))){
					Player player = (Player) playerList.get(i);
					player.setDead(true);
					player.setX(-100);
					player.setY(-100);
				}
			}
		}
		
	}
	//npc与炸弹碰撞判断
	private void npcBoom() {
		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(int i=0; i<npcList.size(); i++) {
			for(int j=0; j<explodeList.size(); j++) {
				if(explodeList.get(j).crash(npcList.get(i))){
					Npc npc = (Npc) npcList.get(i);
					npc.setDead(true);
					npc.setX(-100);
					npc.setY(-100);
				}
			}
		}
	}
	
	//障碍物与炸弹碰撞判断
	private void fragilityBoom() {
		List<SuperElement> explodes = ElementManager.getManager().getElementList("explode");
		List<SuperElement> fragility = ElementManager.getManager().getElementList("fragility");
		for(int i=0; i<fragility.size(); i++) {
			for(int j=0; j<explodes.size(); j++) {
				if(explodes.get(j).crash(fragility.get(i))) {
					MapFragility mapFragility = (MapFragility)fragility.get(i);
					mapFragility.setDestoried(true);
				}
			}
		}
	}
	
	//玩家与道具碰撞判断
	private void playerMagicBox() {
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
		for(int i=0; i<playerList.size(); i++) {
			for(int j=magicBoxList.size()-1; j>=0; j--) {
				if(magicBoxList.get(j).crash(playerList.get(i))){
					MagicBox magicBox = (MagicBox) magicBoxList.get(j);
					magicBox.setPlayer(i);//谁吃方块
					magicBox.setEaten(true);//方块被吃
				}
				
			}
		}
	}
	
	//runGame调用，加入拓展
	public void linkGame() {}
	
	

}
