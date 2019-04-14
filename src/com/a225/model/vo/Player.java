package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
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
	
	private static final int SPEED = 5;
	
	//构造函数
	public Player(int x, int y, int w, int h, ImageIcon img) {
		super(x, y, w, h);
		this.img = img;
		moveType = MoveTypeEnum.STOP;
		moveX = 0;
		moveY = 0;
		attack = false;
		keepAttack = false;
	}
	
	public static Player createPlayer(List<String> list) {
		//list = [PlayerA,x,y,w,h]
		int x = Integer.parseInt(list.get(1));
		int y = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Player(x, y, w, h, imageMap.get(list.get(0)));
	}
	
	public static Player createPlayer(List<String> data,int i,int j) {
		int x = j*MapSquare.PIXEL_X+ElementManager.getManager().getGameMap().getBiasX();
		int y = i*MapSquare.PIXEL_Y+ElementManager.getManager().getGameMap().getBiasY();
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new Player(x, y, w, h, imageMap.get(data.get(0)));
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
		int bias = SPEED;

		switch(moveType) {
		case TOP: ty-=SPEED;break;
		case LEFT: tx-=SPEED;break;
		case RIGHT: tx+=SPEED;break;
		case DOWN: ty+=SPEED;break;
		case STOP:
		default:
			break;
		}
		
		Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
		List<SuperElement> list = ElementManager.getManager().getElementList("obstacle");
		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX()+bias, se.getY()+bias, se.getW()-bias, se.getH()-bias);
			if(playerRect.intersects(elementRect)) {
				System.out.println(tx+" "+ty+" "+"b:"+se.getX()+" "+se.getY());
				return;
			}
		}
		list = ElementManager.getManager().getElementList("fragility");
		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX()+bias, se.getY()+bias, se.getW()-bias, se.getH()-bias);
			if(playerRect.intersects(elementRect)) {
				System.out.println(tx+" "+ty+" "+"b:"+se.getX()+" "+se.getY());
				return;
			}
		}
		setX(tx);
		setY(ty);
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
		if(attack) {
			List<SuperElement> list = 
					ElementManager.getManager().getElementList("bubble");
			list.add(Bubble.createBubble(getX(), getY(), ElementLoader.getElementLoader().getGameInfoMap().get("bubble")));
			attack = false;
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
	
	

}
