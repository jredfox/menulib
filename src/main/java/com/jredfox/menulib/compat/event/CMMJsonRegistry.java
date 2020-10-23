package com.jredfox.menulib.compat.event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.lib.util.JavaUtil;

public class CMMJsonRegistry {
	
	/**
	 * register your CMMAutoJSon Event Handlers in pre init as it fires during init
	 */
	public static List<ICMMJsonEvent> registry = new ArrayList(0);
	
	public static void fire(File mainmenu)
	{
		JSONObject json = JavaUtil.getJson(mainmenu);
		for(ICMMJsonEvent cmm : registry)
		{
			cmm.fire(json);
		}
		JavaUtil.saveJSON(json, mainmenu, true);
	}

}
