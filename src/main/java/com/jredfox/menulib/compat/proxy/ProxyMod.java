package com.jredfox.menulib.compat.proxy;

import java.io.File;
import java.lang.reflect.Method;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.compat.event.CMMReg;
import com.jredfox.menulib.compat.eventhandler.MLCompat;
import com.jredfox.menulib.compat.menu.MenuCMM;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class ProxyMod {
	
	public static void preInit() 
	{
		isModsLoaded();
		register();
		cacheData();
	}
	
	public static void init()
	{
		ProxyCMM.init();
	}
	
	public static void isModsLoaded()
	{
		ProxyCMM.isLoaded = Loader.isModLoaded("custommainmenu");
		ProxyTBL.isLoaded = Loader.isModLoaded("thebetweenlands");
		ProxyFossil.isLoaded = Loader.isModLoaded("fossil");
	}

	public static void register()
	{	
		ProxyCMM.register();
		ProxyTBL.register();
		ProxyFossil.register();
	}
	
	/**
	 * yes I am fixing mods music
	 */
	public static void menuChange()
	{
		ProxyTBL.menuChange();
	}
	
	public static void cacheData() 
	{
		ProxyTBL.cacheData();
	}


}
