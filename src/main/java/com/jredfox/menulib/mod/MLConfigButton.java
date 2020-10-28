package com.jredfox.menulib.mod;

import java.io.File;

import com.jredfox.menulib.coremod.MLConfigCore;

import net.minecraftforge.common.config.Configuration;

public class MLConfigButton {
	
	public static boolean fancyPage = false;
	public static int previousId = 498;
	public static int nextId = 499;
	
	public static int previousX = 5;
	public static int previousY = 5;
	public static int previousWidth = 20;
	public static int previousHeight = 20;
	public static int fPreviousX = 5;
	public static int fPreviousY = 5;
	public static int fPreviousWidth = 64;
	public static int fPreviousHeight = 20;
	
	public static int nextX = 30;
	public static int nextY = 5;
	public static int nextWidth = 20;
	public static int nextHeight = 20;
	public static int fNextX = 74;
	public static int fNextY = 5;
	public static int fNextWidth = 64;
	public static int fNextHeight = 20;
	
	public static void load()
	{
		Configuration config = new Configuration(new File(MLConfigCore.menuLibHome, "buttons.cfg"));
		config.load();
		fancyPage = config.get("general","fancyPage",false).getBoolean();
		previousId = config.get("general","previousId", previousId).getInt();
		nextId = config.get("general","nextId", nextId).getInt();
		
		previousX = config.get("previous", "x", previousX).getInt();
		previousY = config.get("previous", "y", previousY).getInt();
		previousWidth = config.get("previous", "width", previousWidth).getInt();
		previousHeight = config.get("previous", "height", previousHeight).getInt();
		fPreviousX = config.get("previous_fancy", "x", fPreviousX).getInt();
		fPreviousY = config.get("previous_fancy", "y", fPreviousY).getInt();
		fPreviousWidth = config.get("previous_fancy", "width", fPreviousWidth).getInt();
		fPreviousHeight = config.get("previous_fancy", "height", fPreviousHeight).getInt();
		
		nextX = config.get("next", "x", nextX).getInt();
		nextY = config.get("next", "y", nextY).getInt();
		nextWidth = config.get("next", "width", nextWidth).getInt();
		nextHeight = config.get("next", "height", nextHeight).getInt();
		fNextX = config.get("next_fancy", "x", fNextX).getInt();
		fNextY = config.get("next_fancy", "y", fNextY).getInt();
		fNextWidth = config.get("next_fancy", "width", fNextWidth).getInt();
		fNextHeight = config.get("next_fancy", "height", fNextHeight).getInt();
		config.save();
	}

}
