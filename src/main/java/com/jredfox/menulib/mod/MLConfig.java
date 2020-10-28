package com.jredfox.menulib.mod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.lib.util.line.ILine;
import com.evilnotch.lib.util.line.LineArray;
import com.evilnotch.lib.util.line.config.ConfigLine;
import com.jredfox.menulib.coremod.MLConfigCore;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class MLConfig {
	
	public static boolean displayNew = true;
	public static ResourceLocation menuIndex = null;
	
	public static void load() 
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		displayNew = cfg.get("general", "displayNewMenu", displayNew).getBoolean();
		menuIndex = new ResourceLocation(cfg.get("general", "menuIndex", "").getString());
		cfg.save();
	}
	
	public static void saveIndex()
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		cfg.get("general", "menuIndex", "").set(MenuRegistry.INSTANCE.menu.getId().toString());
		cfg.save();
	}
}
