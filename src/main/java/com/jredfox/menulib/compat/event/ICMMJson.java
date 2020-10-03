package com.jredfox.menulib.compat.event;

import org.ralleytn.simple.json.JSONObject;

/**
 * fires if and only if cmm hasn't created a json object before
 */
public interface ICMMJson {
	
	public void fire(JSONObject json);

}
