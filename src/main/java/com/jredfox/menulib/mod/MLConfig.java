package com.jredfox.menulib.mod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.lib.util.line.ILine;
import com.evilnotch.lib.util.line.LineArray;
import com.evilnotch.lib.util.line.config.ConfigLine;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class MLConfig {
	
	public static boolean isLoaded = false;
	public static List<LineArray> mainMenus = new ArrayList();
	public static boolean displayNewMenu = true;
	public static ResourceLocation currentMenuIndex = null;
	
	public static File cfgRoot;
	public static File cfgMenu;
	private static final String menu_comment = "format of menus is \"modid:mainmenu <class> = true/false\" changeing the menu order will change it in game";
	
	/**
	 * load all configurations for menu lib
	 */
	public static void loadConfigs(File d) 
	{
		cfgRoot = new File(d, MLReference.id);
		cfgMenu = new File(cfgRoot, MLReference.id + ".cfg");
		load();
		MLConfigButton.load();
	}
	
	private static void load() 
	{
		Configuration config = new Configuration(cfgMenu);
		config.load();
		
		displayNewMenu = config.get("menulib","displayNewMenu",true).getBoolean();
		currentMenuIndex = new ResourceLocation(config.get("menulib", "currentMenuIndex", "").getString());

		String[] order = config.get("menulib", "menus", new String[]{""},menu_comment).getStringList();
		resetMenus(order);
		
		config.save();
		isLoaded = true;
	}

	private static void resetMenus(String[] order) 
	{
		mainMenus.clear();
		for(String s : order)
		{
			if(s == null || JavaUtil.toWhiteSpaced(s).equals(""))
				continue;
			LineArray line = new LineArray(s);
			if(!line.hasHead())
				line.setHead(true);
			mainMenus.add(line);
		}
	}
	/**
	 * does the config need to save
	 */
	public static boolean isDirty = false;
	/**
	 * does the config have a new menu
	 */
	public static boolean addedMenus = false;

	public static void saveMenusAndIndex() 
	{
		Configuration config = new Configuration(cfgMenu);
		config.load();
		
		List<String> list = new ArrayList();
		for(LineArray line : mainMenus)
		{
			if(!list.contains(line.getResourceLocation().toString()))
			{
				if(!line.getBoolean())
					list.add(line.toString());
				else
				{
					String s = line.getMetaString();
					if(!s.isEmpty())
						s = " <" + s + ">";
					list.add(line.getResourceLocation() + s);
				}
			}
		}
		
		setConfigIndex(config, currentMenuIndex);
		
		//fix comment dissapearing
		String[] strlist = JavaUtil.toStaticStringArray(list);
		Property prop = config.get("menulib", "menus", strlist,"");
		prop.set(strlist);
		
		syncComments(config);
		
		config.save();
		isDirty = false;
	}

	private static void setConfigIndex(Configuration config,ResourceLocation loc) 
	{
		Property prop_index = config.get("menulib", "currentMenuIndex", "");
		prop_index.set(loc.toString());
	}

	public static void saveMenuIndex()
	{
		saveMenuIndex(MenuRegistry.getCurrentMenu().getId());
	}
	
	public static void saveMenuIndex(ResourceLocation loc) 
	{
		long stamp = System.currentTimeMillis();
		Configuration config = new Configuration(cfgMenu);
		config.load();
		setConfigIndex(config,loc);
		currentMenuIndex = loc;
		syncComments(config);
		config.save();
		if(com.evilnotch.lib.main.Config.debug)
		{
			JavaUtil.printTime(stamp, "Saved Current Menu:");
		}
	}
	
	private static void syncComments(Configuration config) 
	{
		config.get("menulib", "menus", new String[]{""}, menu_comment);
	}

	/**
	 * don't call this till after the config has loaded
	 */
	public static void saveMenuToConfig(ResourceLocation loc) 
	{
		if(!hasMenu(loc))
		{
			mainMenus.add(new LineArray(loc.toString() + " = " + true));
			isDirty = true;
			addedMenus = true;
		}
	}
	
	/**
	 * don't call this till after the config has loaded
	 */
	public static void saveMenuToConfig(int index, ResourceLocation loc) 
	{
		if(!hasMenu(loc))
		{
			if(!addedMenus)
				addedMenus = index == mainMenus.size();
			isDirty = true;
			
			mainMenus.add(index, new LineArray(loc.toString() + " = " + true));
		}
	}
	
	/**
	 *  don't call this till after the config is loaded. Use this method if you choose not to register the IMenu directly. 
	 *  Currently only in use for the betweenlands to show people they can input custom entries
	 */
	public static void saveMenuToConfig(ResourceLocation loc, String clazz, boolean enabled) 
	{
		if(!hasMenu(loc))
		{
			mainMenus.add(new LineArray(loc + " <" + clazz + ">" + " = " + enabled));
			isDirty = true;
			addedMenus = true;
		}
	}
	
	/**
	 *  don't call this till after the config is loaded. Use this method if you choose not to register the IMenu directly. 
	 *  Currently only in use for the betweenlands to show people they can input custom entries
	 */
	public static void saveMenuToConfig(int index, ResourceLocation loc, String clazz, boolean enabled) 
	{
		if(!hasMenu(loc))
		{
			if(!addedMenus)
				addedMenus = index == mainMenus.size();
			isDirty = true;
			
			mainMenus.add(index, new LineArray(loc + " <" + clazz + ">" + " = " + enabled));
		}
	}
	
	public static boolean hasMenu(ResourceLocation loc) 
	{
		for(LineArray line : MLConfig.mainMenus)
			if(line.getResourceLocation().equals(loc))
				return true;
		return false;
	}


}
