package com.evilnotch.menulib.compat;

import java.io.File;
import java.lang.reflect.Method;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.compat.event.CMMAutoJSONRegistry;
import com.evilnotch.menulib.compat.eventhandler.CMMAutoJSONHandler;
import com.evilnotch.menulib.compat.menu.MenuCMM;
import com.evilnotch.menulib.menu.MenuRegistry;

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
		if(!ProxyCMM.isLoaded || ConfigMenu.cmmAndVanilla)
		{
			MenuRegistry.registerGuiMenu(0, GuiMainMenu.class, new ResourceLocation("mainmenu"));
		}
		if(ProxyCMM.isLoaded)
		{
			MenuRegistry.registerIMenu(ConfigMenu.cmmAndVanilla ? 1 : 0, new MenuCMM());//make sure it's the first index initially or 2d if both main menus are running
			CMMAutoJSONRegistry.registry.add(new CMMAutoJSONHandler());
		}
		
		if(ProxyTBL.isLoaded)
		{
			MenuRegistry.registerGuiMenu(ReflectionUtil.classForName("thebetweenlands.client.gui.menu.GuiBLMainMenu"), new ResourceLocation("thebetweenlands:mainmenu"));
		}
		
		if(ProxyFossil.isLoaded)
		{
			MenuRegistry.registerGuiMenu(ReflectionUtil.classForName("fossilsarcheology.client.gui.FAMainMenuGUI"), new ResourceLocation("fossil:mainmenu"));
		}
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
