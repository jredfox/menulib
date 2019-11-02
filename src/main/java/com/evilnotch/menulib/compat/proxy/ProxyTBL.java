package com.evilnotch.menulib.compat.proxy;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.util.ResourceLocation;

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

	public static void register() 
	{
		if(ProxyTBL.isLoaded)
		{
			MenuRegistry.registerMenu(ReflectionUtil.classForName("thebetweenlands.client.gui.menu.GuiBLMainMenu"), new ResourceLocation("thebetweenlands:mainmenu"));
		}
	}

}
