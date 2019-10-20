package com.evilnotch.menulib.compat;

import java.io.File;
import java.lang.reflect.Method;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.menulib.compat.event.CMMAutoJSONRegistry;
import com.evilnotch.menulib.compat.menu.MenuCMM;

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
}
