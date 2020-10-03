package com.jredfox.menulib.compat.eventhandler;

import org.ralleytn.simple.json.JSONObject;

import com.jredfox.menulib.compat.event.ICMMJson;
import com.jredfox.menulib.mod.MLConfig;

public class MLCompat implements ICMMJson{

	@Override
	public void fire(JSONObject json) 
	{
		JSONObject buttonElements = json.getJSONObject("buttons");
		JSONObject left = new JSONObject();
    	
		left.put("text", MLConfig.fancyPage ? "menulib.lbuttonfancy.name" : "menulib.lbutton.name");
    	left.put("posX", MLConfig.fancyPage ? MLConfig.lFButtonPosX : MLConfig.leftButtonPosX);
    	left.put("posY", MLConfig.fancyPage ? MLConfig.lFButtonPosY : MLConfig.leftButtonPosY);
    	left.put("width", MLConfig.fancyPage ? MLConfig.lFButtonWidth : MLConfig.leftButtonWidth);
    	left.put("height", MLConfig.fancyPage ? MLConfig.lFButtonHeight : MLConfig.leftButtonHeight);
    	left.put("alignment", "top_left");
    	left.put("wrappedButton", MLConfig.leftButtonId);
    	buttonElements.put("menulibLeft", left);
    	
    	JSONObject right = new JSONObject();
    	right.put("text", MLConfig.fancyPage ? "menulib.rbuttonfancy.name": "menulib.rbutton.name");
    	right.put("posX", MLConfig.fancyPage ? MLConfig.rFButtonPosX : MLConfig.rightButtonPosX);
    	right.put("posY", MLConfig.fancyPage ? MLConfig.rFButtonPosY : MLConfig.rightButtonPosY);
    	right.put("width", MLConfig.fancyPage ? MLConfig.rFButtonWidth : MLConfig.rightButtonWidth);
    	right.put("height", MLConfig.fancyPage ? MLConfig.rFButtonHeight : MLConfig.rightButtonHeight);
    	right.put("alignment", "top_left");
    	right.put("wrappedButton", MLConfig.rightButtonId);
    	buttonElements.put("menulibRight", right);
	}

}
