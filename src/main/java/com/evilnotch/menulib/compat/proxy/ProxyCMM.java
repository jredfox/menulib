package com.evilnotch.menulib.compat.proxy;

import java.io.File;
import java.lang.reflect.Method;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.compat.event.CMMAutoJSONRegistry;
import com.evilnotch.menulib.compat.eventhandler.CMMAutoJSONHandler;
import com.evilnotch.menulib.compat.menu.MenuCMM;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;

public class ProxyCMM {

	public static boolean isLoaded;
	public static boolean flagCMMJson;
	public static File cmmJson = null;
	
	public static void init()
	{
		if(ProxyCMM.isLoaded && ProxyCMM.flagCMMJson)
		{
			JSONObject json = JavaUtil.getJson(ProxyCMM.cmmJson);
			CMMAutoJSONRegistry.fireCMMAutoJSON(json);
			JavaUtil.saveJSON(json, ProxyCMM.cmmJson, false);
			refreshCMM();
			System.out.println("Done Hooking CMM Auto JSON Support. To Regenerate support delete the CMM JSON File!");
		}
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
	

	public static void flagCMMJson(File rootdir) 
	{
		cmmJson = new File(rootdir, "config/CustomMainMenu/mainmenu.json");
		flagCMMJson = !cmmJson.exists();
	}

	public static void register()
	{
		if(ProxyCMM.isLoaded)
		{
			if(!ConfigMenu.cmmAndVanilla)
			{
				MenuRegistry.removeMenu(new ResourceLocation("mainmenu"));
				MenuRegistry.registerIMenu(0, new MenuCMM());
			}
			else
			{
				MenuRegistry.registerIMenu(1, new MenuCMM());
			}
			
			//register the handler for CMM json support
			CMMAutoJSONRegistry.registry.add(new CMMAutoJSONHandler());
		}
	}
}
