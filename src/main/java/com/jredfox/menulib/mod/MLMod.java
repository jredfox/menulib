package com.jredfox.menulib.mod;

import com.evilnotch.lib.main.loader.LoaderMain;
import com.evilnotch.lib.main.loader.LoadingStage;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MLReference.id, name = MLReference.name, version = MLReference.version, clientSideOnly = true, dependencies = "required-after:evilnotchlib")
public class MLMod {
	
	static
	{
		LoaderMain.currentLoadingStage = LoadingStage.PREINIT;//fix evil notch lib
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{	
		MLRegistry.run();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MLRegistry.runInit();
	}

}
