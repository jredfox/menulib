package com.jredfox.menulib.coremod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class MLCoreConfig {
	
	public static int vMenuFrames = 30;//vanilla menu framerate
	public static boolean lockFrames = false;
	
	public static void loadConfig()
	{
		File dir = new File(System.getProperty("user.dir"));
		File filecfg = new File(dir, "config/menulib/menulibcore.cfg");
		Configuration cfg = new Configuration(filecfg);
		
		cfg.load();
		lockFrames = cfg.get("general", "lockMenuFrameRate", lockFrames).getBoolean();
		cfg.save();
		
//		ProxyCMM.flagCMMJson(dir);
	}

}
