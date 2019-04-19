package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.ListModel;
import javax.xml.crypto.Data;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;
import com.a225.model.manager.GameMap;
import com.a225.model.manager.MoveTypeEnum;
import com.a225.util.Point;
import com.a225.util.Utils;
import com.a225.model.manager.GameMap.SquareType;

public class Npc extends Character{
	final String DANGER_MARKER = "-1";
	
	private Random random;
	private List<ImageIcon> imgList;
	private int moveX;//记录图片索引
	private int imgW;//图片宽
	private int imgH;//图片高
	private int npcNum;//记录第几个npc，2为npcA，3为npcB,4为npcC
	private int step; //控制npc步伐节奏
	private String[][] dangerZone;
	private boolean[][] book;
	
	private Vector<MoveTypeEnum> path;

	public Npc(int x, int y, int w, int h, int imgW, int imgH,List<ImageIcon> img, int npcNum) {
		super(x, y, w, h);
		this.imgW = imgW;
		this.imgH = imgH;
		this.imgList = new ArrayList<>(img);
		this.path = new Vector<>();
		this.npcNum = npcNum;
		random = new Random();
		moveX = 0;
		step = 0;
		moveType=MoveTypeEnum.STOP;
	}
	
	public static Npc createNpc(List<String> data,int i,int j,int npcNum) {
		//data=[玩家图片，x,y,w,h]
		List<ImageIcon> imageList = 
				new ArrayList<>(ElementLoader.getElementLoader().getNpcImageList(data.get(0)));
		int x = j*MapSquare.PIXEL_X+GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y+GameMap.getBiasY();
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		int imgW = Integer.parseInt(data.get(3));
		int imgH = Integer.parseInt(data.get(4));
		return new Npc(x, y, w, h, imgW, imgH, imageList, npcNum);
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(imgList.get(moveX).getImage(),
				getX(), getY(), 
				getX()+getW(), getY()+getH(), 
				0, 0, 
				getImgW(), getImgH(), 
				null);
	}
	
	@Override
	public void update() {
		if(!dead) {
			move();
			updateImage();
			destroy();
		}
	}
	
	private void autoAttack() {
		List<SuperElement> seList = ElementManager.getManager().getElementList("player");
		for(SuperElement se:seList) {
			if (crash(se)&&getBubbleLargest()-getBubbleNum()>0) {
				addBubble();
			}
		}
			
	}
	
	private void autoAddBubble() {
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> loc = GameMap.getIJ(getX(), getY());
		Vector<MoveTypeEnum> oldPath = new Vector<>();
		oldPath.addAll(path);
		gameMap.setBlockSquareType(loc, GameMap.SquareType.BUBBLE);
		boolean find = findSafePath();
		gameMap.setBlockSquareType(loc, GameMap.SquareType.FLOOR);
		if(find&&getBubbleLargest()-getBubbleNum()>0&&Math.random()<0.1) {
			addBubble();
		} else {
			path.clear();
			path.addAll(oldPath);
		}
		return;
	}

	private String[][] getDangerZone(){
		String[][] dangerZone = new String[GameMap.getMapRows()][GameMap.getMapCols()];
		List<List<String>> mapList = GameMap.getMapList();
		for(int i=0;i<mapList.size();i++) {
			dangerZone[i] = (String[]) mapList.get(i).toArray();
		}
		List<SuperElement> bubbleList = ElementManager.getManager().getElementList("bubble");
		for(SuperElement se:bubbleList) {
			Bubble bubble = (Bubble) se;
			List<Integer> loc = GameMap.getIJ(se.getX(), se.getY());
			dangerZone[loc.get(0)][loc.get(1)] = DANGER_MARKER;
			for(int i=Math.max(loc.get(0)-bubble.getPower(),0);i<=Math.min(loc.get(0)+bubble.getPower(),GameMap.getMapRows()-1);i++)
				dangerZone[i][loc.get(1)] = DANGER_MARKER;
			for(int i=Math.max(loc.get(1)-bubble.getPower(),0);i<=Math.min(loc.get(1)+bubble.getPower(),GameMap.getMapCols()-1);i++)
				dangerZone[loc.get(0)][i] = DANGER_MARKER;
		}
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(SuperElement se:explodeList) {
			BubbleExplode explode = (BubbleExplode) se;
			List<Integer> loc = GameMap.getIJ(se.getX(), se.getY());
			dangerZone[loc.get(0)][loc.get(1)] = DANGER_MARKER;
			int up = explode.getUp();
			int down = explode.getDown();
			int left = explode.getLeft();
			int right = explode.getRight();
			for(int i=Math.max(loc.get(0)-up,0);i<=Math.min(loc.get(0)+down,GameMap.getMapRows()-1);i++)
				dangerZone[i][loc.get(1)] = DANGER_MARKER;
			for(int i=Math.max(loc.get(1)-left,0);i<=Math.min(loc.get(1)+right,GameMap.getMapCols()-1);i++)
				dangerZone[loc.get(0)][i] = DANGER_MARKER;
		}
		for(int i=0;i<GameMap.getMapRows();i++) {
			for(int j=0;j<GameMap.getMapCols();j++) {
				if(dangerZone[i][j].charAt(0)==GameMap.SquareType.BUBBLE.value()) {
					for(int k=Math.max(i-bubblePower,0);k<=Math.min(i+bubblePower,GameMap.getMapRows()-1);k++)
						dangerZone[k][j] = DANGER_MARKER;
					for(int k=Math.max(j-bubblePower,0);k<=Math.min(j+bubblePower,GameMap.getMapRows()-1);k++)
						dangerZone[i][k] = DANGER_MARKER;
				}
			}
		}
		return dangerZone;
	}
	
	private boolean BFS(int di,int dj) {
		Queue<Point> queue = new LinkedList<>();
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> loc = GameMap.getIJ(getX(), getY());
		Point tPoint = new Point(loc.get(0), loc.get(1));
		int next[][] = {{0,-1},{-1,0},{0,1},{1,0}};
		queue.add(tPoint);
		while(!queue.isEmpty()) {
			Point fPoint = queue.poll();
			book[fPoint.i][fPoint.j] = true;
			if (di==fPoint.i && dj==fPoint.j) {
				path = fPoint.path;
				return true;
			}
			int ti,tj;
			for(int i=0;i<4;i++) {
				ti = fPoint.i+next[i][0];
				tj = fPoint.j+next[i][1];
				List<Integer> tloc = new ArrayList<>();
				tloc.add(ti);tloc.add(tj);
				if(!book[ti][tj]&&!dangerZone[ti][tj].equals(DANGER_MARKER)&&!gameMap.blockIsObstacle(ti, tj)) {
					tPoint = new Point(ti, tj);
					tPoint.path.addAll(fPoint.path);
					tPoint.path.addElement(MoveTypeEnum.values()[i]);
					queue.add(tPoint);
				}
			}
		}
		return false;
	}
	
	private void findPath() {
		dangerZone = getDangerZone();
		book = new boolean[GameMap.getMapRows()][GameMap.getMapCols()];
		GameMap gameMap = ElementManager.getManager().getGameMap();
		int di=0,dj=0;
		do {
			di = (int) (Math.random()*GameMap.getMapRows());
			dj = (int) (Math.random()*GameMap.getMapCols());				
		}while(dangerZone[di][dj].equals(DANGER_MARKER) || gameMap.blockIsObstacle(di, dj));
		BFS(di,dj);
	}
	
	private boolean findSafePath() {
		book = new boolean[GameMap.getMapRows()][GameMap.getMapCols()];
		dangerZone = getDangerZone();
		Queue<Point> queue = new LinkedList<>();
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> loc = GameMap.getIJ(getX(), getY());
		Point tPoint = new Point(loc.get(0), loc.get(1));
		int next[][] = {{0,-1},{-1,0},{0,1},{1,0}};
		queue.add(tPoint);
		while(!queue.isEmpty()) {
			Point fPoint = queue.poll();
			book[fPoint.i][fPoint.j] = true;
			if (!dangerZone[fPoint.i][fPoint.j].equals(DANGER_MARKER)
					&& gameMap.getBlockSquareType(fPoint.i, fPoint.j)!=GameMap.SquareType.BUBBLE) {//此处判定有误
				path.clear();
				path.addAll(fPoint.path);
				return true;
			}
			int ti,tj;
			for(int i=0;i<4;i++) {
				ti = fPoint.i+next[i][0];
				tj = fPoint.j+next[i][1];
				List<Integer> tloc = new ArrayList<>();
				tloc.add(ti);tloc.add(tj);
				if(!book[ti][tj]&&gameMap.blockIsWalkable(tloc)) {
					tPoint = new Point(ti, tj);
					tPoint.path.addAll(fPoint.path);
					tPoint.path.addElement(MoveTypeEnum.values()[i]);
					queue.add(tPoint);
				}
			}
		}
		return false;
	}
	
	//判断前面是否能走过去
	public boolean judgeForward(MoveTypeEnum m) {
		GameMap gameMap = ElementManager.getManager().getGameMap();
		boolean go = false;
		List<Integer> ijList = GameMap.getIJ(getX(), getY());
		//暂时判断前面是地板即可前进，TODO判断前面是Bubble
		switch(m) {
		case LEFT: if(gameMap.getBlockSquareType(ijList.get(0), ijList.get(1)-1)==SquareType.FLOOR) go = true;
			break;  
		case RIGHT: if(gameMap.getBlockSquareType(ijList.get(0), ijList.get(1)+1)==SquareType.FLOOR) go = true;
			break;
		case TOP: if(gameMap.getBlockSquareType(ijList.get(0)-1, ijList.get(1))==SquareType.FLOOR) go = true;
			break;  
		case DOWN: if(gameMap.getBlockSquareType(ijList.get(0)+1, ijList.get(1))==SquareType.FLOOR) go = true;
			break;
		case STOP: go=true;
			break;
		}
		return go;
	}
	
	private boolean judgeStop(List<Integer> loc) {
		GameMap gameMap = ElementManager.getManager().getGameMap();
		int i = loc.get(0);
		int j = loc.get(1);
		if((dangerZone[i+1][j].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i+1, j))
				&&(dangerZone[i-1][j].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i-1, j))
				&&(dangerZone[i][j+1].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i, j+1))
				&&(dangerZone[i][j-1].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i, j-1))
				&&!dangerZone[i][j].equals(DANGER_MARKER)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void move() {
		if(step==MapSquare.PIXEL_X/Character.INIT_SPEED) {
			step=0;
			//autoAddBubble();
			autoAttack();
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> loc = GameMap.getIJ(getX(), getY());
			dangerZone = getDangerZone();
			if(dangerZone[loc.get(0)][loc.get(1)].equals(DANGER_MARKER)) {
				findSafePath();
			} else if(path.isEmpty()) {
				if(judgeStop(loc)) {
					path.add(MoveTypeEnum.STOP);
				} else {
					while(path.isEmpty()) {
						findPath();
					}
				}
			}
			if(path.size()>0) {
				moveType = path.firstElement();
				path.removeElementAt(0);				
			}
			
			if(!judgeForward(moveType)) {
				gameMap.setBlockSquareType(loc, GameMap.SquareType.BUBBLE);
				boolean find = findSafePath();
				gameMap.setBlockSquareType(loc, GameMap.SquareType.FLOOR);
				if(find) {
					addBubble();
					moveType = path.firstElement();
					path.removeElementAt(0);
				}
				else {
					findPath();
				}
			}
		}
		int tx = getX();
		int ty = getY();
		switch(moveType) {
		case LEFT:tx-=speed;break;
		case RIGHT:tx+=speed;break;
		case TOP:ty-=speed;break;
		case DOWN:ty+=speed;break;
		default:
			break;
		}
		boolean det1 = crashDetection(tx, ty, ElementManager.getManager().getElementList("obstacle"));
		boolean det2 = crashDetection(tx, ty, ElementManager.getManager().getElementList("fragility"));
		boolean det3 = bubbleCrashDetection(tx, ty, ElementManager.getManager().getElementList("bubble"));
		
		if(det1&&det2&&det3) {
			setX(tx);
			setY(ty);
			step++;
		} else {
			//moveType = randomOrient();
		}
		
	}
	
	private boolean bubbleCrashDetection(int tx, int ty, List<SuperElement> list) {
		for(SuperElement se:list) {
			switch(moveType) {
			case TOP: 
			case DOWN:
				if(Utils.between(getBottomBound(), se.getTopBound(), se.getBottomBound())
						||Utils.between(getTopBound(), se.getTopBound(), se.getBottomBound())
						||(getBottomBound()==se.getBottomBound()&&getTopBound()==se.getTopBound())) {
					return true;
				}
				break;
			case LEFT:
			case RIGHT:
				if(Utils.between(getLeftBound(), se.getLeftBound(), se.getRightBound())
						||Utils.between(getRightBound(), se.getLeftBound(), se.getRightBound())
						||(getLeftBound()==se.getLeftBound()&&getRightBound()==se.getRightBound())) {
					return true;
				}
				break;
			default:
				break;
			}
		}
		return crashDetection(tx, ty, list);
	}
	
	private void updateImage() {
		switch(moveType) {
		case STOP: moveX = 0;break;
		case LEFT: moveX = 1;break;
		case RIGHT: moveX = 2;break;
		case TOP: moveX = 3;break;
		case DOWN: moveX = 0;break;
		}
	}
	
	private boolean crashDetection(int tx, int ty, List<SuperElement> list){
		Rectangle npcRect = new Rectangle(tx, ty, getW(), getH());
		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX(), se.getY(), se.getW(), se.getH());
			if(npcRect.intersects(elementRect)) {//如果碰撞
				return false;
			}
		}
		return true;
	}


	//随机获得一个方向
	private MoveTypeEnum randomOrient() {
		MoveTypeEnum moveTypeEnum[] = {MoveTypeEnum.LEFT,MoveTypeEnum.RIGHT,MoveTypeEnum.TOP,MoveTypeEnum.DOWN};
		
		return moveTypeEnum[random.nextInt(moveTypeEnum.length)];
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	//添加气泡
	public void addBubble() {
		List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX()+getW()/2, getY()+getH()/2));
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(loc.get(0), loc.get(1));
		if( bubbleNum<bubbleLargest &&  //当前的炸弹数小于上限值，当前位置没有炸弹
				gameMap.getBlockSquareType(maplist.get(0), maplist.get(1))!=GameMap.SquareType.BUBBLE) {

			List<SuperElement> list = 
					ElementManager.getManager().getElementList("bubble");
			list.add(Bubble.createBubble(loc.get(0), loc.get(1), ElementLoader.getElementLoader().getGameInfoMap().get("bubble"),npcNum+2,getBubblePower()));
			bubbleNum++;
		}
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getNpcNum() {
		return npcNum;
	}

	public void setNpcNum(int npcNum) {
		this.npcNum = npcNum;
	}

	public int getImgW() {
		return imgW;
	}

	public void setImgW(int imgW) {
		this.imgW = imgW;
	}

	public int getImgH() {
		return imgH;
	}

	public void setImgH(int imgH) {
		this.imgH = imgH;
	}
	
	

}
