package com.jredfox.menulib.mod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.lib.util.line.ILine;
import com.evilnotch.lib.util.line.LineArray;
import com.evilnotch.lib.util.line.config.ConfigLine;
import com.jredfox.menulib.coremod.MLConfigCore;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.Menu;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class MLConfig {
	
	public static boolean displayNew = true;
	public static ResourceLocation menuIndex;
	public static ResourceLocation newMenu;
	
	public static String[] menu_order;
	public static String[] menu_user;
	private static final String comment_order = "configure order here. to disable a menu append \"= false\"";
	private static final String comment_user = "modid:menu <full.path.to.class>";
	
	public static boolean isDirty;
	
	public static void load()
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		displayNew = cfg.get("general", "displayNewMenu", displayNew).getBoolean();
		menuIndex = new ResourceLocation(cfg.get("general", "menuIndex", "").getString());
		menu_order = cfg.getStringList("menus_order", "general", new String[]{}, comment_order);
		menu_user =  cfg.getStringList("menus_user", "general", new String[]{}, comment_user);
		cfg.save();
	}
	
	public static void saveIndex()
	{
		if(!isDirty)
			return;
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		saveIndex(cfg);
		syncComments(cfg);
		cfg.save();
		setDirty(false);
	}

	/**
	 * saves the menuIndex and menu order
	 */
	public static void save()
	{
		if(!isDirty)
			return;
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		saveIndex(cfg);
		
		String[] orderList = new String[MenuRegistry.INSTANCE.registry.size()];
		int index = 0;
		for(IMenu menu : MenuRegistry.INSTANCE.registry)
			orderList[index++] = "" + menu.getId() + (!menu.isEnabled() ? " = false" : "");
		cfg.get("general", "menus_order", new String[]{}).set(orderList);
		syncComments(cfg);
		cfg.save();
		setDirty(false);
	}
	
	private static void saveIndex(Configuration cfg)
	{
		menuIndex = MenuRegistry.INSTANCE.getMenu().getId();
		cfg.get("general", "menuIndex", "").set(menuIndex.toString());
	}
	
	private static void syncComments(Configuration cfg)
	{
		cfg.get("general", "menus_order", new String[]{}).setComment(comment_order);
		cfg.get("general", "menus_user", new String[]{}).setComment(comment_user);
	}
	
	public static void registerUserMenus()
	{
		for(String s : menu_user)
		{
			LineArray line = new LineArray(s);
			ResourceLocation id = line.getResourceLocation();
			Class<? extends GuiScreen> guiClass = ReflectionUtil.classForName(line.getMetaString());
			if(guiClass == null)
			{
				System.out.println("skipping:" + line);
				continue;
			}
			IMenu menu = new Menu(id, guiClass);
			MenuRegistry.INSTANCE.register(menu);
		}
	}
	
	public static void setDirty(boolean dirty)
	{
		isDirty = dirty;
	}

	/**
	 * sets the config dirty and sets the newMenu to the new id
	 */
	public static void setNewMenu(ResourceLocation id) 
	{
		MLConfig.newMenu = id;
		MLConfig.setDirty(true);
	}
}
