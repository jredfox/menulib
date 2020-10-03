package com.jredfox.menulib.mod;

import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MLReference.id, name = MLReference.name, version = MLReference.version, clientSideOnly = true, dependencies = "required-after:evilnotchlib;after:custommainmenu")
public class MLMod {
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{	
		MLConfig.parse(event.getModConfigurationDirectory());
		MLRegistry.run();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		MenuRegistry.init();
	}

}
