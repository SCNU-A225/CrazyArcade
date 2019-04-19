package com.a225.model.vo;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import com.a225.model.manager.MoveTypeEnum;

/**
 * 角色类
 * @ClassName: Character  
 * @Description:为玩家和电脑的父类，保存玩家与电脑共有的属性和方法    
 * @author: WeiXiao
 * @CreateDate: 2019年4月18日 下午5:27:20
 */
public class Character extends SuperElement{
	public final static int INIT_SPEED = 4; //初始移动速度
	
	protected boolean dead;//记录是否存活
	protected MoveTypeEnum moveType;
	protected int speed;//移动速度
	protected int speedItemCount;//生效中的加速卡数量
	protected int bubblePower;//炮弹威力
	protected int bubbleNum;//记录玩家已经放了多少个炸弹
	protected int bubbleLargest;//玩家最多可以放多少个炸弹，初始值为3
	public int score;

	public Character(int x, int y, int w, int h) {
		super(x, y, w, h);
		moveType = MoveTypeEnum.STOP;
		speedItemCount = 0;
		bubblePower = 2;
		bubbleNum = 0;
		bubbleLargest = 1;
		speed = INIT_SPEED;
		score = 0;
		dead = false;
	}
	
	//	改变一段时间的移动速度,传入速度需要提升的倍数和持续的时间（秒）
	public void changeSpeed(double times,int lastTime) {
		speed = (int)(speed*times);
		Timer timer = new Timer(true);
		speedItemCount++;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				speedItemCount--;
				if(speedItemCount==0) {
					speed = INIT_SPEED;					
				}
			}
		};
		timer.schedule(task,lastTime*1000);
	}

	@Override
	public void showElement(Graphics g) {}

	@Override
	public void move() {}

	@Override
	public void destroy() {}
	

	
	
	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public MoveTypeEnum getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveTypeEnum moveType) {
		this.moveType = moveType;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeedItemCount() {
		return speedItemCount;
	}

	public void setSpeedItemCount(int speedItemCount) {
		this.speedItemCount = speedItemCount;
	}

	public int getBubblePower() {
		return bubblePower;
	}

	public void setBubblePower(int bubblePower) {
		this.bubblePower = bubblePower;
	}

	public int getBubbleNum() {
		return bubbleNum;
	}

	public void setBubbleNum(int bubbleNum) {
		this.bubbleNum = bubbleNum;
	}

	public int getBubbleLargest() {
		return bubbleLargest;
	}

	public void setBubbleLargest(int bubbleLargest) {
		this.bubbleLargest = bubbleLargest;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
