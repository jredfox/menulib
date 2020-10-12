package com.jredfox.menulib.mod;

import com.evilnotch.lib.minecraft.capability.registry.CapabilityRegistry;
import com.evilnotch.lib.minecraft.tick.TickRegistry;
import com.jredfox.menulib.cap.CapReg;
import com.jredfox.menulib.coremod.MLCoreConfig;
import com.jredfox.menulib.eventhandler.GuiHandler;
import com.jredfox.menulib.eventhandler.MusicHandler;
import com.jredfox.menulib.eventhandler.MusicPlayerHandler;
import com.jredfox.menulib.eventhandler.FrameHandler;
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
		registerCaps();
	}
	
	/**
	 * generic registry
	 */
	public static void register()
	{
		MenuRegistry.register(GuiMainMenu.class, new ResourceLocation("mainmenu"));
		MenuRegistry.register("fossilsarcheology.client.gui.FAMainMenuGUI", new ResourceLocation("fossil:mainmenu"));
		MenuRegistry.register("thebetweenlands.client.gui.menu.GuiBLMainMenu", new ResourceLocation("thebetweenlands:mainmenu"));
	}
	
	/**
	 * register forge and non forge events here
	 */
	public static void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new MusicHandler());
		TickRegistry.register(new MusicPlayerHandler(), Side.CLIENT);
		
		if(MLCoreConfig.debugFrames)
		{
			TickRegistry.register(new FrameHandler(), Side.CLIENT);
		}
	}
	
	public static void registerCaps()
	{
		CapabilityRegistry.registerRegistry(new CapReg());
	}

}
