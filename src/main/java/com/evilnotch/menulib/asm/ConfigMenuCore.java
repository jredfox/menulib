package com.evilnotch.menulib.asm;

import java.io.File;

import com.evilnotch.lib.asm.ConfigCore;
import com.evilnotch.lib.main.Config;
import com.evilnotch.lib.main.loader.LoaderMain;
import com.evilnotch.menulib.compat.ProxyMod;

import net.minecraftforge.common.config.Configuration;

public class ConfigMenuCore {
	
	/**
	 * do not change this value making it final screws up compilers as this will change depdening upon mc version
	 */
	public static int vanillaMainMenuLock = 30;
	
	public static boolean lockMenuFrameRate = false;
	public static int lockedMenuFrameRate = vanillaMainMenuLock;
	
	public static void loadConfig()
	{
		File dir = new File(System.getProperty("user.dir"));
		File filecfg = new File(dir, "config/menulib/menulibcore.cfg");
		Configuration cfg = new Configuration(filecfg);
		cfg.load();
		lockMenuFrameRate = cfg.get("general", "lockMenuFrameRate", lockMenuFrameRate).getBoolean();
		lockedMenuFrameRate = cfg.get("general", "lockedMenuFrameRate", lockedMenuFrameRate).getInt();
		if(lockedMenuFrameRate < vanillaMainMenuLock)
		{
			lockedMenuFrameRate = vanillaMainMenuLock;
			System.out.println("Main Menu Framerate Limit cannot be below " + vanillaMainMenuLock);
		}
		ProxyMod.flagCMMJson(dir);
		cfg.save();
	}

}
