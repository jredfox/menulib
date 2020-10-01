package com.jredfox.menulib.main;

import com.evilnotch.lib.minecraft.tick.TickRegistry;
import com.jredfox.menulib.compat.proxy.ProxyMod;
import com.jredfox.menulib.eventhandler.GuiEventHandler;
import com.jredfox.menulib.eventhandler.MusicEventHandler;
import com.jredfox.menulib.eventhandler.Tick;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.modid,name = Reference.name, version = Reference.version, clientSideOnly = true, dependencies = "required-after:evilnotchlib;after:custommainmenu")
public class MenuLib {
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{	
		ConfigMenu.loadMenuLib(event.getModConfigurationDirectory());
		MenuRegistry.registerGuiMenu(0, GuiMainMenu.class, new ResourceLocation("mainmenu"));
		ProxyMod.preInit();
		MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
		MinecraftForge.EVENT_BUS.register(new MusicEventHandler());
		if(ConfigMenu.debugFrameRate)
		{
			TickRegistry.register(new Tick(), Side.CLIENT);
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ProxyMod.init();
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		MenuRegistry.init();
	}

}
