package com.jredfox.menulib.compat.eventhandler;

import org.ralleytn.simple.json.JSONObject;

import com.jredfox.menulib.compat.event.CMMAutoJSON;
import com.jredfox.menulib.main.ConfigMenu;

public class CMMAutoJSONHandler implements CMMAutoJSON{

	@Override
	public void fireEvent(JSONObject json) 
	{
		JSONObject buttonElements = json.getJSONObject("buttons");
		JSONObject left = new JSONObject();
    	
		left.put("text", ConfigMenu.fancyPage ? "menulib.lbuttonfancy.name" : "menulib.lbutton.name");
    	left.put("posX", ConfigMenu.fancyPage ? ConfigMenu.lFButtonPosX : ConfigMenu.leftButtonPosX);
    	left.put("posY", ConfigMenu.fancyPage ? ConfigMenu.lFButtonPosY : ConfigMenu.leftButtonPosY);
    	left.put("width", ConfigMenu.fancyPage ? ConfigMenu.lFButtonWidth : ConfigMenu.leftButtonWidth);
    	left.put("height", ConfigMenu.fancyPage ? ConfigMenu.lFButtonHeight : ConfigMenu.leftButtonHeight);
    	left.put("alignment", "top_left");
    	left.put("wrappedButton", ConfigMenu.leftButtonId);
    	buttonElements.put("menulibLeft", left);
    	
    	JSONObject right = new JSONObject();
    	right.put("text", ConfigMenu.fancyPage ? "menulib.rbuttonfancy.name": "menulib.rbutton.name");
    	right.put("posX", ConfigMenu.fancyPage ? ConfigMenu.rFButtonPosX : ConfigMenu.rightButtonPosX);
    	right.put("posY", ConfigMenu.fancyPage ? ConfigMenu.rFButtonPosY : ConfigMenu.rightButtonPosY);
    	right.put("width", ConfigMenu.fancyPage ? ConfigMenu.rFButtonWidth : ConfigMenu.rightButtonWidth);
    	right.put("height", ConfigMenu.fancyPage ? ConfigMenu.rFButtonHeight : ConfigMenu.rightButtonHeight);
    	right.put("alignment", "top_left");
    	right.put("wrappedButton", ConfigMenu.rightButtonId);
    	buttonElements.put("menulibRight", right);
	}

}
