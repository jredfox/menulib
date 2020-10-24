package com.jredfox.menulib.compat.event;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ralleytn.simple.json.JSONObject;
import org.ralleytn.simple.json.JSONParseException;

import com.evilnotch.lib.main.loader.LoaderMain;
import com.evilnotch.lib.main.loader.LoadingStage;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.compat.menu.MenuCMM;
import com.jredfox.menulib.compat.util.CMMUtil;
import com.jredfox.menulib.mod.MLConfig;

public class CMMJsonRegistry {
	
	/**
	 * register your CMMAutoJSon Event Handlers in pre init as it fires during init
	 */
	public static List<ICMMJsonEvent> registry = new ArrayList(0);
	public static boolean reloadFlag;
			
	public static void fire(File mainmenu)
	{
		if(LoaderMain.currentLoadingStage.ordinal() < LoadingStage.POSTINIT.ordinal())
		{
			reloadFlag = true;
			return;
		}
		JSONObject json = getJson(mainmenu);
		for(ICMMJsonEvent cmm : registry)
		{
			cmm.fire(json);
		}
		JavaUtil.saveJSON(json, mainmenu, true);
	}
	
	public static void checkReload()
	{
		if(CMMJsonRegistry.reloadFlag)
		{
			File mainmenu = new File(MLConfig.cfgRoot.getParent(), "CustomMainMenu/mainmenu.json");
			CMMJsonRegistry.fire(mainmenu);
			CMMUtil.reload();
			CMMJsonRegistry.reloadFlag = false;
		}
	}
	
	/**
	 * here cause evil notch lib is bugged
	 */
	public static JSONObject getJson(File armor) 
	{
		FileReader reader = null;
	    try 
	    {
	    	reader = new FileReader(armor);
	        JSONObject json = (JSONObject)JavaUtil.jsonParser.parse((Reader)(reader));
	        return json;
	    } 
	    catch (Exception var2) 
	    {
	       var2.printStackTrace();
	       return null;
	    }
	    finally
	    {
	    	if(reader != null)
	    	{
	    		try 
	    		{
					reader.close();
				} 
	    		catch (IOException e) 
	    		{
					e.printStackTrace();
				}
	    	}
	    }
	}

}
