package com.jredfox.menulib.mod;

import java.io.File;

import com.evilnotch.lib.main.loader.LoaderMain;
import com.evilnotch.lib.main.loader.LoadingStage;
import com.jredfox.menulib.compat.event.CMMJsonRegistry;
import com.jredfox.menulib.menu.MenuRegistry;

import lumien.custommainmenu.CustomMainMenu;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MLReference.id, name = MLReference.name, version = MLReference.version, clientSideOnly = true, dependencies = "required-after:evilnotchlib")
public class MLMod {
	
	static
	{
		LoaderMain.currentLoadingStage = LoadingStage.PREINIT;//evil notch lib if bugged
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{	
		MLRegistry.run();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		MLRegistry.postInit();
		MenuRegistry.init();
	}

}
