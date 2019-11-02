package com.evilnotch.menulib.compat.proxy;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.util.ResourceLocation;

public class ProxyFossil {
	
	public static boolean isLoaded;

	public static void register() 
	{
		if(ProxyFossil.isLoaded)
		{
			MenuRegistry.registerMenu(ReflectionUtil.classForName("fossilsarcheology.client.gui.FAMainMenuGUI"), new ResourceLocation("fossil:mainmenu"));
		}
	}

}
