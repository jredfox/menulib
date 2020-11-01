package com.jredfox.menulib.mod;

import java.io.File;

import com.evilnotch.lib.api.ReflectionUtil;
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
import com.jredfox.menulib.eventhandler.MinecraftShutdownHandler;
import com.jredfox.menulib.eventhandler.MusicHandler;
import com.jredfox.menulib.eventhandler.MusicPlayerHandler;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.Menu;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.misc.GuiAetherii;
import com.jredfox.menulib.proxy.ModProxy;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

public class MLRegistry {
	
	public static ModProxy proxyCMM = new ModProxy("custommainmenu");
	
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
		MLConfig.load();
		MLConfigButton.load();
	}

	/**
	 * generic registry
	 */
	public static void register()
	{
		MenuRegistry.INSTANCE.register(new Menu(new ResourceLocation("mainmenu"), GuiMainMenu.class));
	}
	
	public static void registerCompat()
	{
		if(Loader.isModLoaded("fossil"))
			MenuRegistry.INSTANCE.register(new Menu(new ResourceLocation("fossil:mineshaft"), ReflectionUtil.classForName("fossilsarcheology.client.gui.FAMainMenuGUI")));
		if(Loader.isModLoaded("thebetweenlands"))
		{
			MenuRegistry.INSTANCE.register(new MenuTBL());
		}
		if(proxyCMM.isLoaded)
		{
			MenuRegistry.INSTANCE.register(0, new MenuCMM());
			CMMJsonRegistry.registry.add(new MLCMMJson());
		}
		MenuRegistry.INSTANCE.register(new Menu(new ResourceLocation("aetherii:test"), GuiAetherii.class));
	}
	
	/**
	 * register forge and non forge events here
	 */
	public static void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new MusicHandler());
		TickRegistry.register(new MusicPlayerHandler(), Side.CLIENT);
		TickRegistry.register(new MinecraftShutdownHandler(), Side.CLIENT);
		
		if(MLConfigCore.debugFrames)
		{
			TickRegistry.register(new FrameHandler(), Side.CLIENT);
		}
	}
	
	public static void registerCaps()
	{
		CapabilityRegistry.registerRegistry(new CapReg());
	}
	
	public static void runPost() 
	{
		post();
		postCompat();
	}

	public static void post()
	{
		MenuRegistry.INSTANCE.load();
	}

	public static void postCompat()
	{
		if(proxyCMM.isLoaded)
		{
			CMMJsonRegistry.checkReload();
		}
	}

}
