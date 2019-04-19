package com.a225.util;

public class Utils {
	public static boolean between(int x, int a, int b) {
		if(a>b) return between(x, b, a);
		return x>a&&x<b;
	}
}
