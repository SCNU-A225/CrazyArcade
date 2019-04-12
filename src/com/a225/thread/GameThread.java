package com.a225.thread;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.a225.main.GameStart;
import com.a225.model.manager.ElementManager;
import com.a225.model.vo.SuperElement;

/**
 * 游戏线程控制
 * @author 麻瓜
 *
 */
public class GameThread extends Thread{
	private boolean twoPlayer = false;
	
	public void run() {
		while(true){
			

			while(GameStart.gameRuning) {
				GameStart.beginLock = false;
				
				try {
					sleep(6000);
					break;
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
				//加载地图
				//显示人物，流程，自动化
				runGame();
				//结束本地图
				
			}
			if(!GameStart.beginLock)
				GameStart.changeJPanel();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		
	}
	
	//加载地图
	private void loadElement() {}
	
	//显示人物，游戏流程，自动化
	private void runGame() {
		while(true) {
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
			
			//控制runGame进程
			try {	
				sleep(50);
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	//runGame调用，加入拓展
	public void linkGame() {}
	
	//关卡结束
	private void overGame() {}

	public void setTwoPlayer(boolean twoPlayer) {
		this.twoPlayer = twoPlayer;
	}
	
	

}
