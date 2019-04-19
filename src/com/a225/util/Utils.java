package com.a225.util;

import java.util.Random;

public class Utils {
	public static Random random = new Random();
	public static boolean between(int x, int a, int b) {
		if(a>b) return between(x, b, a);
		return x>a&&x<b;
	}
}
