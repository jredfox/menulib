package com.jredfox.menulib.misc;

public class NumberUtil {
	
	public static int range(int num, int min, int max)
	{
		if(num < min)
			num = min;
		else if(num > max)
			num = max;
		return num;
	}
	
	public static int capMin(int num, int min)
	{
		if(num < min)
			num = min;
		return num;
	}

}
