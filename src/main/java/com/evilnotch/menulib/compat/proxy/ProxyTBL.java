package com.evilnotch.menulib.compat.proxy;

import com.evilnotch.lib.api.ReflectionUtil;

public class ProxyTBL {
	
	public static boolean isLoaded;
	public static Class tbl_musicHandler = null;
	public static Object tbl_instance = null;
	
	
	public static void menuChange() 
	{
		if(ProxyTBL.isLoaded)
		{
			ReflectionUtil.setObject(tbl_instance, false, tbl_musicHandler, "hasBlMainMenu");//sets it to false to garentee it will not play till the next cik
		}
	}
	
	public static void cacheData()
	{
		if(ProxyTBL.isLoaded)
		{
			tbl_musicHandler =  ReflectionUtil.classForName("thebetweenlands.client.handler.MusicHandler");
			tbl_instance = ReflectionUtil.getObject(null, tbl_musicHandler, "INSTANCE");
		}
	}

}
