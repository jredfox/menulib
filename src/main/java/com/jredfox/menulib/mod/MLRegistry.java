package com.jredfox.menulib.mod;

import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;

public class MLRegistry {
	
	public static void registerEvents()
	{
		
	}
	
	public static void register()
	{
		MenuRegistry.registerMenu(GuiMainMenu.class, new ResourceLocation("mainmenu"));
	}

}
