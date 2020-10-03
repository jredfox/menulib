package com.jredfox.menulib.compat.proxy;

import java.io.File;
import java.lang.reflect.Method;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.compat.event.CMMReg;
import com.jredfox.menulib.compat.eventhandler.MLCompat;
import com.jredfox.menulib.compat.menu.MenuCMM;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.mod.MLConfig;

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
			CMMReg.fire(json);
			JavaUtil.saveJSON(json, ProxyCMM.cmmJson, false);
			refreshMenus();
			System.out.println("Done Hooking CMM Auto JSON Support. To Regenerate support delete the CMM JSON File!");
		}
	}
	
	public static void refreshMenus()
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
			if(!MLConfig.cmmAndVanilla)
			{
				MenuRegistry.removeMenu(new ResourceLocation("mainmenu"));
				MenuRegistry.registerMenu(0, new MenuCMM());
			}
			else
			{
				MenuRegistry.registerMenu(1, new MenuCMM());
			}
			
			//register the handler for CMM json support
			CMMReg.registry.add(new MLCompat());
		}
	}
}
