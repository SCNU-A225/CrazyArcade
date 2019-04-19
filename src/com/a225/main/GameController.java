package com.a225.main;

/**
 * 游戏控制信息类
 * @ClassName: GameController  
 * @Description:    
 * @author: WeiXiao
 * @CreateDate: 2019年4月12日 上午9:13:13
 */
public class GameController {
	private static boolean gameRunning = false;
	private static boolean twoPlayer;
	private static int npcNum;
	
	public static boolean isGameRunning() {
		return gameRunning;
	}
	public static void setGameRunning(boolean gameRunning) {
		GameController.gameRunning = gameRunning;
	}
	public static boolean isTwoPlayer() {
		return twoPlayer;
	}
	public static void setTwoPlayer(boolean twoPlayer) {
		GameController.twoPlayer = twoPlayer;
	}
	public static int getNpcNum() {
		return npcNum;
	}
	public static void setNpcNum(int npcNum) {
		GameController.npcNum = npcNum;
	}
}
