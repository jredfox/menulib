package com.jredfox.menulib.mod;

import com.evilnotch.lib.minecraft.tick.TickRegistry;
import com.jredfox.menulib.eventhandler.GuiHandler;
import com.jredfox.menulib.eventhandler.MusicHandler;
import com.jredfox.menulib.eventhandler.TickHandler;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

public class MLRegistry {
	
	public static void run()
	{
		register();
		registerEvents();
	}
	
	/**
	 * register forge and non forge events here
	 */
	public static void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new MusicHandler());
		
		if(MLConfig.debugFrameRate)
		{
			TickRegistry.register(new TickHandler(), Side.CLIENT);
		}
	}
	
	/**
	 * generic registry
	 */
	public static void register()
	{
		MenuRegistry.register(GuiMainMenu.class, new ResourceLocation("mainmenu"));
		MenuRegistry.register("fossilsarcheology.client.gui.FAMainMenuGUI", new ResourceLocation("fossil:mainmenu"));
//		MenuRegistry.register("thebetweenlands.client.gui.menu.GuiBLMainMenu", new ResourceLocation("thebetweenlands:mainmenu"));
	}

}
