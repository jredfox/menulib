package com.jredfox.menulib.compat.eventhandler;

import org.ralleytn.simple.json.JSONObject;

import com.jredfox.menulib.compat.event.ICMMJsonEvent;
import com.jredfox.menulib.mod.MLConfigButton;

public class MLCMMJson implements ICMMJsonEvent{

	@Override
	public void fire(JSONObject json) 
	{
		JSONObject buttons = json.getJSONObject("buttons");
		JSONObject left = new JSONObject();
    	
		left.put("text", MLConfigButton.fancyPage ? "menulib.previousfancy.name" : "menulib.previous.name");
    	left.put("posX", MLConfigButton.fancyPage ? MLConfigButton.fPreviousX : MLConfigButton.previousX);
    	left.put("posY", MLConfigButton.fancyPage ? MLConfigButton.fPreviousY : MLConfigButton.previousY);
    	left.put("width", MLConfigButton.fancyPage ? MLConfigButton.fPreviousWidth : MLConfigButton.previousWidth);
    	left.put("height", MLConfigButton.fancyPage ? MLConfigButton.fPreviousHeight : MLConfigButton.previousHeight);
    	left.put("alignment", "top_left");
    	left.put("wrappedButton", MLConfigButton.previousId);
    	buttons.put("menulibPrevious", left);
    	
    	JSONObject right = new JSONObject();
    	right.put("text", MLConfigButton.fancyPage ? "menulib.nextfancy.name": "menulib.next.name");
    	right.put("posX", MLConfigButton.fancyPage ? MLConfigButton.fNextX : MLConfigButton.nextX);
    	right.put("posY", MLConfigButton.fancyPage ? MLConfigButton.fNextY : MLConfigButton.nextY);
    	right.put("width", MLConfigButton.fancyPage ? MLConfigButton.fNextWidth : MLConfigButton.nextWidth);
    	right.put("height", MLConfigButton.fancyPage ? MLConfigButton.fNextHeight : MLConfigButton.nextHeight);
    	right.put("alignment", "top_left");
    	right.put("wrappedButton", MLConfigButton.nextId);
    	buttons.put("menulibNext", right);
	}

}
