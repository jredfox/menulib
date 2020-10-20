package com.jredfox.menulib.coremod;

import java.io.File;

import com.jredfox.menulib.mod.MLReference;

import net.minecraftforge.common.config.Configuration;

public class MLCoreConfig {
	
	public static boolean debugFrames = false;//print the framerate for debug info
	
	public static void loadConfig()
	{
		File dir = new File(System.getProperty("user.dir"));
		File filecfg = new File(dir, "config/" + MLReference.id + "/" + MLReference.id + "core.cfg");
		Configuration cfg = new Configuration(filecfg);
		cfg.load();
		debugFrames = cfg.get("general", "debugFrames", debugFrames).getBoolean();
		cfg.save();
	}

}
