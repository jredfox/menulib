package com.evilnotch.menulib.compat.event;

import java.util.ArrayList;
import java.util.List;

import org.ralleytn.simple.json.JSONObject;

public class CMMAutoJSONRegistry {
	
	/**
	 * register your CMMAutoJSon Event Handlers in pre init as it fires during init
	 */
	public static List<CMMAutoJSON> registry = new ArrayList(0);
	
	public static void fireCMMAutoJSON(JSONObject json)
	{
		for(CMMAutoJSON cmm : registry)
			cmm.fireEvent(json);
	}

}
