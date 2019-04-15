package com.a225.model.manager;

public enum MoveTypeEnum {
	LEFT,TOP,RIGHT,DOWN,STOP;
	
	public static MoveTypeEnum codeToMoveType(int code) {
		switch (code) {
		case 37:
		case 65:
			return MoveTypeEnum.LEFT;
		case 38:
		case 87:
			return MoveTypeEnum.TOP;
		case 39:
		case 68:
			return MoveTypeEnum.RIGHT;
		case 40:
		case 83:
			return MoveTypeEnum.DOWN;
		default:
			return MoveTypeEnum.STOP;
		}
	}
}
