package com.jredfox.menulib.mod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class MLConfig {
	
	public static boolean displayNew = true;
	/**
	 * use MenuRegistry.getMenu().getId() instead and it may lead to a null IMenu anyways
	 */
	public static ResourceLocation menuIndex;
	public static Set<ResourceLocation> order;
	public static boolean isDirty;
	public static ResourceLocation newMenu;
	
	public static void load()
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		displayNew = cfg.get("general", "displayNewMenu", displayNew).getBoolean();
		menuIndex = new ResourceLocation(cfg.get("general", "menuIndex", "").getString());
		
		//WIP menu order & custom user menus
		String[] menus = cfg.getStringList("menus", "general", new String[]{}, "format is modid:menu <full.path.to.class> = enabled");
		order = new LinkedHashSet(menus.length + 10);
		for(String s : menus)
		{
			LineArray line = new LineArray(s);
			order.add(line.getResourceLocation());
			if(!line.hasHead())
				line.setHead(true);
			if(line.hasStringMeta())
			{
				Class guiClass = ReflectionUtil.classForName(line.meta.trim());
				if(guiClass == null)
				{
					System.out.println("skipping user menu:" + line);
					continue;
				}
				IMenu menu = new Menu(line.getResourceLocation(), guiClass);
				menu.setEnabled(line.getBoolean());
				MenuRegistry.INSTANCE.user.add(menu);
			}
		}
		System.out.println(order);

		cfg.save();
	}
	
	public static void addId(ResourceLocation id)
	{
//		if(order.add(id))
//		{
//			//TODO:
//		}
	}
	
	public static void removeId(ResourceLocation id)
	{
//		if(order.remove(id))
//		{
//			//TODO:
//		}
	}
	
	public static void saveIndex()
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		menuIndex = MenuRegistry.INSTANCE.menu.getId();
		cfg.get("general", "menuIndex", "").set(menuIndex.toString());
		cfg.save();
	}
	
	/**
	 * saves the menuIndex and menu order
	 */
	public static void save()
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		
		//menuIndex
		menuIndex = MenuRegistry.INSTANCE.menu.getId();
		cfg.get("general", "menuIndex", "").set(menuIndex.toString());
		
		//menus
		String[] orderList = new String[order.size()];
		int index = 0;
		for(ResourceLocation id : order)
		{
			orderList[index] = id.toString();
			index++;
		}
		cfg.get("general", "menus", "").set(orderList);
		
		cfg.save();
	}
}
