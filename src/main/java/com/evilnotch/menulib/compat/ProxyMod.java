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
	
	public static void isModsLoaded()
	{
		ProxyCMM.isLoaded = Loader.isModLoaded("custommainmenu");
		ProxyTBL.isLoaded = Loader.isModLoaded("thebetweenlands");
		ProxyFossil.isLoaded = Loader.isModLoaded("fossil");
	}
	
	public static void init()
	{
		if(ProxyCMM.isLoaded && ProxyCMM.flagCMMJson)
		{
			JSONObject json = JavaUtil.getJson(ProxyCMM.cmmJson);
	    	CMMAutoJSONRegistry.fireCMMAutoJSON(json);
			JavaUtil.saveJSON(json, ProxyCMM.cmmJson, false);
			refreshCMM();
			System.out.println("Done Hooking CMM Auto JSON Support for first time use");
		}
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
		if(ProxyTBL.isLoaded)
		{
			ReflectionUtil.setObject(tbl_instance, false, tbl_musicHandler, "hasBlMainMenu");//sets it to false to garentee it will not play till the next cik
		}
	}
	
	private static void cacheData() 
	{
		if(ProxyTBL.isLoaded)
		{
			tbl_musicHandler =  ReflectionUtil.classForName("thebetweenlands.client.handler.MusicHandler");
			tbl_instance = ReflectionUtil.getObject(null, tbl_musicHandler, "INSTANCE");
		}
	}

	public static void flagCMMJson(File rootdir) 
	{
		cmmJson = new File(rootdir, "config/CustomMainMenu/mainmenu.json");
		flagCMMJson = !cmmJson.exists();
	}
	
	public static void refreshCMM()
	{
		try 
		{
			Method refresh = MenuCMM.cmm.getDeclaredMethod("reload");
			refresh.setAccessible(true);
			refresh.invoke(MenuCMM.modInstance);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
