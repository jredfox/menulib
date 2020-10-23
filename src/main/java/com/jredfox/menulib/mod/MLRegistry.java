package com.jredfox.menulib.mod;

import java.io.File;

import com.evilnotch.lib.minecraft.capability.registry.CapabilityRegistry;
import com.evilnotch.lib.minecraft.tick.TickRegistry;
import com.jredfox.menulib.cap.CapReg;
import com.jredfox.menulib.compat.event.CMMJsonRegistry;
import com.jredfox.menulib.compat.eventhandler.MLCMMJson;
import com.jredfox.menulib.compat.menu.MenuCMM;
import com.jredfox.menulib.compat.menu.MenuTBL;
import com.jredfox.menulib.coremod.MLConfigCore;
import com.jredfox.menulib.eventhandler.FrameHandler;
import com.jredfox.menulib.eventhandler.GuiHandler;
import com.jredfox.menulib.eventhandler.MusicHandler;
import com.jredfox.menulib.eventhandler.MusicPlayerHandler;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

public class MLRegistry {
	
	public static void run()
	{
		loadConfigs();
		register();
		registerCompat();
		registerEvents();
		registerCaps();
	}
	
	public static void loadConfigs() 
	{
		MLConfig.load(new File(MLConfigCore.getHome(), "config"));
		MLConfigButton.load();
	}

	/**
	 * generic registry
	 */
	public static void register()
	{
		MenuRegistry.register(GuiMainMenu.class, new ResourceLocation("mainmenu"));
	}
	
	public static void registerCompat()
	{
		if(Loader.isModLoaded("fossil"))
			MenuRegistry.register("fossilsarcheology.client.gui.FAMainMenuGUI", new ResourceLocation("fossil:mineshaft"));
		if(Loader.isModLoaded("thebetweenlands"))
			MenuRegistry.register(new MenuTBL());
		if(Loader.isModLoaded("custommainmenu"))
		{
			MenuRegistry.register(new MenuCMM());
			CMMJsonRegistry.registry.add(new MLCMMJson());
		}
	}
	
	/**
	 * register forge and non forge events here
	 */
	public static void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new MusicHandler());
		TickRegistry.register(new MusicPlayerHandler(), Side.CLIENT);
		
		if(MLConfigCore.debugFrames)
		{
			TickRegistry.register(new FrameHandler(), Side.CLIENT);
		}
	}
	
	public static void registerCaps()
	{
		CapabilityRegistry.registerRegistry(new CapReg());
	}

}
