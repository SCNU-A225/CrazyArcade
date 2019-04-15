package com.a225.thread;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import com.a225.model.manager.ElementManager;
import com.a225.model.manager.MoveTypeEnum;
import com.a225.model.vo.Player;

/**
 * 暂时实现键盘监听以验证Player类人物移动是否正确
 * @author newcomer02
 */

public class GameKeyListener implements KeyListener{

	private List<?> list;
	
	public void keyPressed(KeyEvent e) {
		list = ElementManager.getManager().getElementList("player");
		Player play = (Player) list.get(0);
		switch (e.getKeyCode()) {
			case 37:play.setMoveType(MoveTypeEnum.LEFT);break;
			case 38:play.setMoveType(MoveTypeEnum.TOP);break;
			case 39:play.setMoveType(MoveTypeEnum.RIGHT);break;
			case 40:play.setMoveType(MoveTypeEnum.DOWN);break;
			case 32:
				if(play.isKeepAttack())  //一次按压空格键只放一个水泡
					play.setAttack(false);
				else {
					play.setKeepAttack(true);
					play.setAttack(true);
				}
				break;
		}
	}

	
	public void keyReleased(KeyEvent e) {
		List<?> list = ElementManager.getManager().getElementList("player");
		Player play = (Player) list.get(0);
		switch (e.getKeyCode()) {
			case 37:
				if(play.getMoveType() == MoveTypeEnum.LEFT)
					play.setMoveType(MoveTypeEnum.STOP);
				break;
			case 38:
				if(play.getMoveType() == MoveTypeEnum.TOP)
					play.setMoveType(MoveTypeEnum.STOP);
				break;
			case 39:
				if(play.getMoveType() == MoveTypeEnum.RIGHT)
					play.setMoveType(MoveTypeEnum.STOP);
				break;
			case 40:
				if(play.getMoveType() == MoveTypeEnum.DOWN)
					play.setMoveType(MoveTypeEnum.STOP);
				break;
			case 32:
				play.setAttack(false);
				play.setKeepAttack(false);
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
	}

}
