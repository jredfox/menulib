package com.jredfox.menulib.misc;

import java.util.Comparator;
import java.util.List;

public class MLUtil {
	
	public static int capMin(int num, int min)
	{
		return num < min ? min : num;
	}
	
	/**
	 * add an object to the list inserted with logic of a custom comparator
	 */
	public static void add(List list, Comparator c, Object obj)
	{
		int addIndex = 0;
		for(int i=0; i < list.size(); i++)
		{
			Object indexObj = list.get(i);
			int compare = c.compare(obj, indexObj);
			if(compare == -1)
			{
				addIndex = i;
				break;
			}
			else if(compare == 0)
			{
				addIndex = i;
				break;
			}
			addIndex++;
		}
		list.add(addIndex, obj);
	}

}
