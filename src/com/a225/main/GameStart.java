package com.a225.main;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.a225.frame.BeginJPanel;
import com.a225.frame.GameFrame;
import com.a225.frame.GameJPanel;
import com.a225.model.loader.ElementLoader;
import com.a225.thread.GameKeyListener;
import com.a225.thread.GameThread;

/**
 * 游戏启动入口
 * @ClassName: GameStart  
 * @Description:  
 * @author: WeiXiao
 * @CreateDate: 2019年4月8日 下午4:17:37
 */
public class GameStart {
	private static GameFrame gameFrame;
	private static BeginJPanel beginJPanel;
	private static GameKeyListener keyListener;

	//游戏启动入口
	public static void main(String[] args) {
		// 资源加载
		try {
			ElementLoader.getElementLoader().readGamePro();
			ElementLoader.getElementLoader().readImagePro();
			ElementLoader.getElementLoader().readCharactorsPro();
			ElementLoader.getElementLoader().readBubblePro();
			ElementLoader.getElementLoader().readSquarePro();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 窗体加载（自动化……）
		gameFrame = new GameFrame();
		keyListener = new GameKeyListener();
		gameFrame.setKeyListener(keyListener);
		gameFrame.addListener();
		
		beginJPanel = new BeginJPanel("img/bg/title.png");
		gameFrame.setjPanel(beginJPanel);
		gameFrame.addJPanel();
		// 监听加载
		
		// 界面显示
		gameFrame.setVisible(true);
	}
	
	public static void changeJPanel(boolean gamePanel){
		if(GameController.isGameRunning()==gamePanel) return;
		gameFrame.setVisible(false);
		if(gamePanel){
			GameController.setGameRunning(true);
			gameFrame.setjPanel(new GameJPanel());
			gameFrame.addJPanel();
			gameFrame.start();
		} else {
			GameController.setGameRunning(false);
			gameFrame.setjPanel(beginJPanel);
			gameFrame.addJPanel();
		}
		//System.out.println(gamePanel);
		gameFrame.setVisible(false);
		gameFrame.setVisible(true);
		
		
		
		//gameFrame.start();
		
	}

}
