package com.jredfox.menulib.compat.event;

import java.util.ArrayList;
import java.util.List;

import org.ralleytn.simple.json.JSONObject;

public class CMMReg {
	
	/**
	 * register your CMMAutoJSon Event Handlers in pre init as it fires during init
	 */
	public static List<ICMMJson> registry = new ArrayList(0);
	
	/**
	 * fires all registered ICMMSupport interfaces
	 */
	public static void fire(JSONObject json)
	{
		for(ICMMJson cmm : registry)
			cmm.fire(json);
	}

}
