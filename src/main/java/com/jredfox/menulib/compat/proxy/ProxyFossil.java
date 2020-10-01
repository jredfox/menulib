package com.jredfox.menulib.compat.proxy;

import com.evilnotch.lib.api.ReflectionUtil;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.util.ResourceLocation;

public class ProxyFossil {
	
	public static boolean isLoaded;

	public static void register() 
	{
		if(ProxyFossil.isLoaded)
		{
			MenuRegistry.registerGuiMenu(ReflectionUtil.classForName("fossilsarcheology.client.gui.FAMainMenuGUI"), new ResourceLocation("fossil:mainmenu"));
		}
	}

}
