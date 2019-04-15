package com.a225.thread;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Stack;
import com.a225.main.GameController;
import com.a225.model.manager.ElementManager;
import com.a225.model.manager.MoveTypeEnum;
import com.a225.model.vo.Player;

/**
 * 暂时实现键盘监听以验证Player类人物移动是否正确
 * @author newcomer02
 */

public class GameKeyListener implements KeyListener{

	private List<?> list;
	private Stack<Integer> p1PressStack = new Stack<>();
	private Stack<Integer> p2PressStack = new Stack<>();
	
	public void keyPressed(KeyEvent e) {
		list = ElementManager.getManager().getElementList("player");
		Player player1 = (Player) list.get(0);
		
		int code = e.getKeyCode();
		switch (code) {
		case 10:
			if(player1.isKeepAttack())
				player1.setAttack(false);
			else {
				player1.setKeepAttack(true);
				player1.setAttack(true);
			}
			break;
		case 37:
		case 38:
		case 39:
		case 40:
			if(!p1PressStack.contains(code)) {
				p1PressStack.push(code);
			}
			player1.setMoveType(MoveTypeEnum.codeToMoveType(code));
			break;
		default:
			break;
		}
		if(GameController.isTwoPlayer()) {
			Player player2 = (Player) list.get(1);
			switch (code) {
			case 32:
				player2.setAttack(true);
			case 65:
			case 87:
			case 68:
			case 83:
				if(!p2PressStack.contains(code)) {
					p2PressStack.push(code);
				}
				player2.setMoveType(MoveTypeEnum.codeToMoveType(code));
				break;
			
			default:
				break;
			}
		}
	}

	
	public void keyReleased(KeyEvent e) {
		List<?> list = ElementManager.getManager().getElementList("player");
		int code = e.getKeyCode();
		Player player1 = (Player) list.get(0);
		switch (code) {
		case 10:
			player1.setAttack(false);
			player1.setKeepAttack(false);
			break;
		case 37:
		case 38:
		case 39:
		case 40:
			if(p1PressStack.peek()!=code) {
				p1PressStack.remove(new Integer(code));
			} else {
				p1PressStack.pop();
				if(p1PressStack.size()==0) {
					player1.setMoveType(MoveTypeEnum.STOP);
				} else {
					player1.setMoveType(MoveTypeEnum.codeToMoveType(p1PressStack.peek()));
				}
			}
			break;
		default:
			break;
		}
		if(GameController.isTwoPlayer()) {
			Player player2 = (Player) list.get(1);
			switch (code) {
			case 32:
				player2.setAttack(false);
				break;
			case 65:
			case 87:
			case 68:
			case 83:
				if(p2PressStack.peek()!=code) {
					p2PressStack.remove(new Integer(code));
				} else {
					p2PressStack.pop();
					if(p2PressStack.size()==0) {
						player2.setMoveType(MoveTypeEnum.STOP);
					} else {
						player2.setMoveType(MoveTypeEnum.codeToMoveType(p2PressStack.peek()));
					}
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
	}

}
