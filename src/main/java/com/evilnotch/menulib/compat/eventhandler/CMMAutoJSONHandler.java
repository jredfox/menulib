package com.evilnotch.menulib.compat.eventhandler;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.compat.event.CMMAutoJSON;

public class CMMAutoJSONHandler implements CMMAutoJSON{

	@Override
	public void fireEvent(JSONObject json) 
	{
		JSONObject buttonElements = json.getJSONObject("buttons");
		JSONObject left = new JSONObject();
    	
		left.put("text", ConfigMenu.fancyPage ? "previous" : "<");
    	left.put("posX", 5);
    	left.put("posY", 5);
    	left.put("width", 20);
    	left.put("height", 20);
    	left.put("alignment", "top_left");
    	left.put("wrappedButton", ConfigMenu.leftButtonId);
    	buttonElements.put("menulibLeft", left);
    	
    	JSONObject right = new JSONObject();
    	right.put("text", ConfigMenu.fancyPage ? "next" : ">");
    	right.put("posX", 30);
    	right.put("posY", 5);
    	right.put("width", 20);
    	right.put("height", 20);
    	right.put("alignment", "top_left");
    	right.put("wrappedButton", ConfigMenu.rightButtonId);
    	buttonElements.put("menulibRight", right);
	}

}
