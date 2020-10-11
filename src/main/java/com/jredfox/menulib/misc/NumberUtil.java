package com.jredfox.menulib.misc;

public class NumberUtil {
	
	public static int capMin(int num, int min)
	{
		if(num < min)
			num = min;
		return num;
	}

}
