package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;
import com.a225.model.manager.MoveTypeEnum;


/**
 * 玩家类
 * @ClassName: Player  
 * @Description: 玩家VO类   
 * @author: WeiXiao
 * @CreateDate: 2019年4月11日 下午5:10:20
 */
public class Player extends SuperElement{
	
	private ImageIcon img;
	private MoveTypeEnum moveType;
	private int moveX;
	private int moveY;
	private boolean attack;//记录攻击状态，默认为false
	private boolean keepAttack;//记录是否为一直按着攻击键，实现一次按键只放一个水泡
	private int playerNum;//记录第几个玩家，0为玩家一，1为玩家二
	private int bubbleNum;//记录玩家已经放了多少个炸弹
	private int bubbleLargest;//玩家最多可以放多少个炸弹，初始值为3
	private static int INIT_SPEED = 4; //初始移动速度
	private int speed;//移动速度
	
	//构造函数
	public Player(int x, int y, int w, int h, ImageIcon img, int playerNum) {
		super(x, y, w, h);
		this.img = img;
		this.playerNum = playerNum;
		moveType = MoveTypeEnum.STOP;
		moveX = 0;
		moveY = 0;
		attack = false;
		keepAttack = false;
		bubbleNum = 0;
		bubbleLargest = 3;
		speed = INIT_SPEED;
	}
	
	public static Player createPlayer(List<String> list,int playerNum) {
		//list = [PlayerA,x,y,w,h]
		int x = Integer.parseInt(list.get(1));
		int y = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Player(x, y, w, h, imageMap.get(list.get(0)),playerNum);
	}
	
	public static Player createPlayer(List<String> data,int i,int j,int playerNum) {
		int x = j*MapSquare.PIXEL_X+GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y+GameMap.getBiasY();
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Player(x, y, w, h, imageMap.get(data.get(0)),playerNum);
	}

	//展示人物图片
	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.getImage(), 
				getX(), getY(), 	//屏幕左上角坐标
				getX()+getW(), getY()+getH(), 	//屏幕右下坐标
				moveX*100+27, moveY*100+43, 				//图片左上坐标
				moveX*100+72, moveY*100+99, 			//图片右下坐标
				null);
	}

	//移动
	@Override
	public void move() {
		int tx = getX();
		int ty = getY();
		

		switch(moveType) {
		case TOP: ty-=speed;break;
		case LEFT: tx-=speed;break;
		case RIGHT: tx+=speed;break;
		case DOWN: ty+=speed;break;
		case STOP:
		default:
			break;
		}
		
		boolean det1 = crashDetection(tx, ty, ElementManager.getManager().getElementList("obstacle"));
		boolean det2 = crashDetection(tx, ty, ElementManager.getManager().getElementList("fragility"));
		if(det1&&det2) {
			setX(tx);
			setY(ty);			
		}
	}
	
	/**
	 * 碰撞检测+平滑移动
	 * @param tx
	 * @param ty
	 * @param list
	 * @return 是否碰撞
	 */
	private boolean crashDetection(int tx, int ty, List<SuperElement> list){
		int bias = 1;//判断碰撞偏差值
		int THRESHOLD = 25;//平滑移动阈值
		Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
		Random random = new Random();
		
		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX()+bias, se.getY()+bias, se.getW()-bias, se.getH()-bias);
			if(playerRect.intersects(elementRect)) {//如果碰撞
				switch(moveType) {//判断方向
				case TOP:
				case DOWN:
					int width=Math.min(getX()+getW(),se.getX()+se.getW())-Math.max(getX(), se.getX());
					if(width>THRESHOLD) break;//超过阈值不做平滑处理
					if(getX()<se.getX()) {
						for(int i=0;i<width;i++) {
							if(random.nextBoolean())
								setX(getX()-1);
						}
					} else {
						for(int i=0;i<width;i++) {
							if(random.nextBoolean())
								setX(getX()+1);
						}
					}
					break;
				case LEFT:
				case RIGHT:
					int height=Math.min(getY()+getH(),se.getY()+se.getH())-Math.max(getY(), se.getY());
					if(height>THRESHOLD) break;
					if(getY()<se.getY()) {
						for(int i=0;i<height;i++) {
							if(random.nextBoolean())
								setY(getY()-1);
						}
					} else {
						for(int i=0;i<height;i++) {
							if(random.nextBoolean())
								setY(getY()+1);
						}
					}
					break;
				default:break;
				}
				return false;
			}
		}
		return true;
	}
	
	
	
	//重写父类模板
	@Override
	public void update() {
		super.update();
		addBubble();
		updateImage();
	}
	
	//更新图片
	public void updateImage() {
		if(moveType==MoveTypeEnum.STOP){
			return;
		}
		
		moveX = ++moveX%4;
		
		switch (moveType) {
		case TOP:moveY = 3;break;
		case LEFT:moveY = 1;break;
		case RIGHT:moveY = 2;break;
		case DOWN:moveY = 0;break;
		default:break;
		}
	}
	
	//添加气泡
	public void addBubble() {
		List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX()+getW()/2, getY()+getH()/2));
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(loc.get(0), loc.get(1));
		if(attack && bubbleNum<bubbleLargest &&  //判断是否为攻击状态，当前的炸弹数小于上限值，当前位置没有炸弹
				gameMap.getBlockSquareType(maplist.get(0), maplist.get(1))!=GameMap.SquareType.BUBBLE) {

			List<SuperElement> list = 
					ElementManager.getManager().getElementList("bubble");
			list.add(Bubble.createBubble(loc.get(0), loc.get(1), ElementLoader.getElementLoader().getGameInfoMap().get("bubble"),playerNum));
			attack = false;
			bubbleNum++;
		}
	}
	
//	改变一段时间的移动速度,传入速度需要提升的倍数和持续的时间（秒）
	public void changeSpeed(double times,int lastTime) {
		{
			
			Timer timer = new Timer(true);
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					setSPEED(INIT_SPEED);
					System.out.println(getSPEED());
				}
			};
		
			if(getSPEED() == INIT_SPEED*times) speed = (int)(getSPEED()*times);//设置速度
			
			timer.schedule(task,lastTime*1000);
			

		}
			}

	@Override
	public void destroy() {
		
	}
	
	
	//gettes and setters
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public MoveTypeEnum getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveTypeEnum moveType) {
		this.moveType = moveType;
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getMoveY() {
		return moveY;
	}

	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}

	public boolean isAttack() {
		return attack;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public boolean isKeepAttack() {
		return keepAttack;
	}

	public void setKeepAttack(boolean keepAttack) {
		this.keepAttack = keepAttack;
	}
	
	public int getPlayerNum() {
		return this.playerNum;
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

	public int getSPEED() {
		return speed;
	}

	public void setSPEED(int speed) {
		this.speed = speed;
	}
	
	

}
