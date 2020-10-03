package com.jredfox.menulib.mod;

import com.evilnotch.lib.minecraft.tick.TickRegistry;
import com.jredfox.menulib.compat.proxy.ProxyMod;
import com.jredfox.menulib.eventhandler.GuiHandler;
import com.jredfox.menulib.eventhandler.MusicHandler;
import com.jredfox.menulib.eventhandler.TickHandler;
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

@Mod(modid = MLReference.id, name = MLReference.name, version = MLReference.version, clientSideOnly = true, dependencies = "required-after:evilnotchlib;after:custommainmenu")
public class MLMod {
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{	
		MLConfig.loadConfig(event.getModConfigurationDirectory());
		MLRegistry.register();
		MLRegistry.registerEvents();
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
