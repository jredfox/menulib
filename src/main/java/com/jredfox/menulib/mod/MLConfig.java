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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class MLConfig {
	
	public static boolean displayNew = true;
	public static ResourceLocation menuIndex;
	public static ResourceLocation newMenu;
	public static List<ResourceLocation> orderIds;
	public static Map<ResourceLocation, String> keepIds;
	
	public static void load()
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		displayNew = cfg.get("general", "displayNewMenu", displayNew).getBoolean();
		menuIndex = new ResourceLocation(cfg.get("general", "menuIndex", "").getString());
		
		//WIP menu order & custom user menus
		String[] menus = cfg.getStringList("menus", "general", new String[]{}, "format is modid:menu <full.path.to.class> = enabled");
		orderIds = new ArrayList(menus.length + 10);
		keepIds = new LinkedHashMap();
		for(String s : menus)
		{
			LineArray line = new LineArray(s);
			add(orderIds, line.getResourceLocation());
			if(!line.hasHead())
				line.setHead(true);
			if(line.hasStringMeta())
			{
				keepIds.put(line.getResourceLocation(), s.trim());
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

		cfg.save();
	}
	
	public static void addId(int index, ResourceLocation id)
	{
		if(add(orderIds, index, id))
		{
			newMenu = id;
//			System.out.println("newMenu:" + newMenu);
		}
	}
	
	public static boolean add(List list, Object obj)
	{
		if(!list.contains(obj))
		{
			list.add(obj);
			return true;
		}
		return false;
	}
	
	public static boolean add(List list, int index, Object obj)
	{
		if(!list.contains(obj))
		{
			list.add(index, obj);
			return true;
		}
		return false;
	}
	
	public static void saveIndex()
	{
		Configuration cfg = new Configuration(new File(MLConfigCore.menuLibHome, MLReference.id + ".cfg"));
		cfg.load();
		menuIndex = MenuRegistry.INSTANCE.getMenu().getId();
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
		menuIndex = MenuRegistry.INSTANCE.getMenu().getId();
		cfg.get("general", "menuIndex", "").set(menuIndex.toString());
		
		//menus
		String[] orderList = new String[orderIds.size()];
		int index = 0;
		for(ResourceLocation id : orderIds)
		{
			String keepId = keepIds.get(id);
			String line = keepId != null ? keepId : id.toString();
			orderList[index++] = line;
		}
		cfg.get("general", "menus", "").set(orderList);
		
		cfg.save();
	}
}
