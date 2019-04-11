package com.a225.main;

import java.io.IOException;

import javax.swing.JFrame;

import com.a225.frame.GameFrame;
import com.a225.frame.GameJPanel;
import com.a225.model.loader.ElementLoader;
import com.a225.thread.GameKeyListener;

/**
 * 游戏启动入口
 * @ClassName: GameStart  
 * @Description:  
 * @author: WeiXiao
 * @CreateDate: 2019年4月8日 下午4:17:37
 */
public class GameStart {

	//游戏启动入口
	public static void main(String[] args) {
		// 资源加载
		try {
			ElementLoader.getElementLoader().readGamePro();
			ElementLoader.getElementLoader().readImagePro();
			ElementLoader.getElementLoader().readCharactorsPro();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 窗体加载（自动化……）
		GameFrame gameFrame = new GameFrame();
		GameJPanel gameJPanel = new GameJPanel();
		GameKeyListener gameListener = new GameKeyListener();
		gameFrame.setKeyListener(gameListener);
		gameFrame.setjPanel(gameJPanel);
		gameFrame.addJPanel();
		// 监听加载
		gameFrame.addListener();
		// 游戏开始
		gameFrame.start();
	}

}
