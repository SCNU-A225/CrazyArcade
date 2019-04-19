package com.a225.thread;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.a225.frame.OverJPanel;
import com.a225.main.GameStart;
import com.a225.model.manager.ElementManager;
import com.a225.model.vo.Bubble;
import com.a225.model.vo.BubbleExplode;
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
	private boolean running; //表示当前关卡是否在进行
	private boolean over = false; //表示游戏是否结束，结束返回开始菜单
	private static int sleepTime = 20; //runGame刷新时间
	//倒计时变量
	private static int allTime = 600*1000; //10分钟

	
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
		GameStart.changeJPanel("over");
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
		allTime = 600*1000;
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
			allTime = allTime - sleepTime;
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
		int surviveP = 0;
		int winner = 2;//0为玩家1，1为玩家2，2为电脑获胜
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		for(SuperElement se:playerList) {
			if(!((Player)se).isDead()) {
				surviveP++;
			}
		}
		for(SuperElement npc:npcList) {
			if(!((Npc)npc).isDead()) {
				allDead = false;
			}
		}
		
		//玩家失败
		if(surviveP==0||(allTime<=0 && !allDead)) {
			running = false;
			over = true;
			OverJPanel.getResult().setText("defeated");
		}
		//玩家胜利
		if(allDead&&surviveP==1) {
			running = false;
			over = true;
			for(SuperElement se:playerList) {
				if(!((Player)se).isDead()) {
					surviveP++;
					winner = ((Player)se).getPlayerNum();
				}
			}
			OverJPanel.getResult().setText("player "+(winner+1)+" win");
		}
		
		//时间到，两个玩家都活着
		if(allTime<=0&&surviveP==2&&allDead) {
			running = false;
			over = true;
			int score1 = ((Player)playerList.get(0)).score;
			int score2 = ((Player)playerList.get(0)).score;
			if(score1==score2) {
				OverJPanel.getResult().setText("defeated");
			}
			else if(score1>score2)
			{
				OverJPanel.getResult().setText("player 1 win");
			}
			else {
				OverJPanel.getResult().setText("player 2 win");
			}
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
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(int i=0; i<npcList.size(); i++) {
			for(int j=0; j<explodeList.size(); j++) {
				if(explodeList.get(j).crash(npcList.get(i))){
					Npc npc = (Npc) npcList.get(i);
					npc.setDead(true);
					npc.setX(-100);
					npc.setY(-100);
					BubbleExplode e = (BubbleExplode)explodeList.get(j);
					if(e.getPlayerNum()<2)//目前只有玩家计分
						((Player)playerList.get(e.getPlayerNum())).setScore(((Player)playerList.get(e.getPlayerNum())).getScore()+50);
				}
			}
		}
	}
	
	//障碍物与炸弹碰撞判断
	private void fragilityBoom() {
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> explodes = ElementManager.getManager().getElementList("explode");
		List<SuperElement> fragility = ElementManager.getManager().getElementList("fragility");
		for(int i=0; i<fragility.size(); i++) {
			for(int j=0; j<explodes.size(); j++) {
				if(explodes.get(j).crash(fragility.get(i))) {
					MapFragility mapFragility = (MapFragility)fragility.get(i);
					mapFragility.setDestoried(true);
					BubbleExplode e = (BubbleExplode)explodes.get(j);
					if(e.getPlayerNum()<2)//目前只有玩家计分
						((Player)playerList.get(e.getPlayerNum())).setScore(((Player)playerList.get(e.getPlayerNum())).getScore()+10);
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
					((Player)playerList.get(i)).setScore(((Player)playerList.get(i)).getScore()+30);
				}
				
			}
		}
	}
	
	//runGame调用，加入拓展
	public void linkGame() {}
	

	public static int getAllTime() {
		return allTime;
	}


}
