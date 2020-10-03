package com.jredfox.menulib.coremod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class MLCoreConfig {
	
	public static int vFrames = 30;//vanilla default framerate do not modify
	public static int frames = vFrames;//the locked main menu framerate if locked menu frames is enabled
	public static boolean lockFrames = false;
	public static boolean debugFrames = false;//print the framerate for debug info
	
	public static void loadConfig()
	{
		File dir = new File(System.getProperty("user.dir"));
		File filecfg = new File(dir, "config/menulib/menulibcore.cfg");
		Configuration cfg = new Configuration(filecfg);
		
		cfg.load();
		lockFrames = cfg.get("general", "lockMenuFrames", lockFrames).getBoolean();
		frames = cfg.get("general", "lockedMenuFrames", vFrames).getInt();
		debugFrames = cfg.get("general","debugFrames", debugFrames).getBoolean();
		cfg.save();
		
//		ProxyCMM.flagCMMJson(dir);
	}

}
