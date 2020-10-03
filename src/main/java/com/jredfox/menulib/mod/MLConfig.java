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
	public static List<Class> musicAllow = new ArrayList();
	public static List<Class> musicDeny = new ArrayList();
	public static boolean fancyPage = false;
	public static boolean displayNewMenu = true;
	public static boolean cmmAndVanilla = false;	
	public static ResourceLocation currentMenuIndex = null;
	public static File cfgmenu = null;
	
	public static int leftButtonId = 498;
	public static int leftButtonPosX = 5;
	public static int leftButtonPosY = 5;
	public static int leftButtonWidth = 20;
	public static int leftButtonHeight = 20;
	
	public static int lFButtonPosX = 5;
	public static int lFButtonPosY = 5;
	public static int lFButtonWidth = 64;
	public static int lFButtonHeight = 20;
	
	public static int rightButtonId = 499;
	public static int rightButtonPosX = 30;
	public static int rightButtonPosY = 5;
	public static int rightButtonWidth = 20;
	public static int rightButtonHeight = 20;
	
	public static int rFButtonPosX = 74;
	public static int rFButtonPosY = 5;
	public static int rFButtonWidth = 64;
	public static int rFButtonHeight = 20;
	
	private static final String menu_comment = "format of menus is \"modid:mainmenu <class> = true/false\" changeing the menu order will change it in game";
	private static final String musicDeny_comment = "this is a blacklist of menus that extend GuiMainMenu but, have their own music";
	private static final String musicAllow_comment = "this is a whitelist of menus not extending GuiMainMenu that require vanilla music";
	
	/**
	 * load all configurations for menu lib
	 */
	public static void parse(File d) 
	{
		cfgmenu = new File(d, "menulib/menulib.cfg");
		
		Configuration config = new Configuration(cfgmenu);
		
		config.load();
		displayNewMenu = config.get("menulib","displayNewMenu",true).getBoolean();
		cmmAndVanilla = config.get("menulib", "cmmAndVanilla", cmmAndVanilla).getBoolean();
		currentMenuIndex = new ResourceLocation(config.get("menulib", "currentMenuIndex", "").getString());
		
		fancyPage = config.get("buttons","fancyButtons",false).getBoolean();
		
		leftButtonId = config.get("buttons","leftId", leftButtonId).getInt();
		leftButtonPosX = config.get("buttons", "leftPosX", leftButtonPosX).getInt();
		leftButtonPosY = config.get("buttons", "leftPosY", leftButtonPosY).getInt();
		leftButtonWidth = config.get("buttons", "leftWidth", leftButtonWidth).getInt();
		leftButtonHeight = config.get("buttons", "leftHeight", leftButtonHeight).getInt();
		
		lFButtonPosX = config.get("buttons", "leftFancyPosX", lFButtonPosX).getInt();
		lFButtonPosY = config.get("buttons", "leftFancyPosY", lFButtonPosY).getInt();
		lFButtonWidth = config.get("buttons", "leftFancyWidth", lFButtonWidth).getInt();
		lFButtonHeight = config.get("buttons", "leftFancyHeight", lFButtonHeight).getInt();
		
		rightButtonId = config.get("buttons","rightId", rightButtonId).getInt();
		rightButtonPosX = config.get("buttons", "rightPosX", rightButtonPosX).getInt();
		rightButtonPosY = config.get("buttons", "rightPosY", rightButtonPosY).getInt();
		rightButtonWidth = config.get("buttons", "rightWidth", rightButtonWidth).getInt();
		rightButtonHeight = config.get("buttons", "rightHeight", rightButtonHeight).getInt();
		
		rFButtonPosX = config.get("buttons", "rightFancyPosX", rFButtonPosX).getInt();
		rFButtonPosY = config.get("buttons", "rightFancyPosY", rFButtonPosY).getInt();
		rFButtonWidth = config.get("buttons", "rightfancyWidth", rFButtonWidth).getInt();
		rFButtonHeight = config.get("buttons", "rightFancyHeight", rFButtonHeight).getInt();

		String[] order = config.get("menulib", "menus", new String[]{""},menu_comment).getStringList();
		resetMenus(order);
		
		String[] clList = config.getStringList("classes_allowed", "music", new String[]{"lumien.custommainmenu.gui.GuiCustom"}, musicAllow_comment);
		for(String s : clList)
		{
			if(JavaUtil.toWhiteSpaced(s).isEmpty())
				continue;
			Class c = ReflectionUtil.classForName(s);
			if(c != null)
				musicAllow.add(c);
		}
		
		String[] clDenyList = config.getStringList("classes_deny", "music", new String[]{""}, musicDeny_comment);
		for(String s : clDenyList)
		{
			if(JavaUtil.toWhiteSpaced(s).isEmpty())
				continue;
			Class c = ReflectionUtil.classForName(s);
			if(c != null)
				musicDeny.add(c);
		}
		
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
		Configuration config = new Configuration(cfgmenu);
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
		Configuration config = new Configuration(cfgmenu);
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
		config.get("music", "classes_deny", new String[]{""}, musicDeny_comment);
		config.get("music", "classes_allowed", new String[]{""}, musicAllow_comment);
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
