package com.jredfox.menulib.coremod;

import java.io.File;

import com.jredfox.menulib.mod.MLReference;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

public class MLConfigCore {
	
	public static boolean debugFrames = false;//print the framerate for debug info
	
	public static void loadConfig()
	{
		File filecfg = new File(getHome(), "config/" + MLReference.id + "/" + MLReference.id + "core.cfg");
		Configuration cfg = new Configuration(filecfg);
		cfg.load();
		debugFrames = cfg.get("general", "debugFrames", debugFrames).getBoolean();
		cfg.save();
	}
	
	/**
	 * Launch.minecraftHome is null in deob and is valid in ob
	 */
	public static File getHome()
	{
		return Launch.minecraftHome != null ? Launch.minecraftHome : new File(System.getProperty("user.dir"));
	}

}
