package com.evilnotch.menulib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.main.Config;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.lib.util.line.ILine;
import com.evilnotch.lib.util.line.LineArray;
import com.evilnotch.lib.util.line.config.ConfigLine;
import com.evilnotch.menulib.menu.IMenu;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigMenu {
	//class vars
	public static boolean isLoaded = false;
	public static File cfgmenu = null;
	//menu lib objects
	public static boolean fancyPage = false;
	public static boolean displayNewMenu = true;
	public static boolean cmmAndVanilla = false;
	public static ResourceLocation currentMenuIndex = null;
	public static List<LineArray> mainMenus = new ArrayList();
	public static List<Class> musicAllow = new ArrayList();
	public static List<Class> musicDeny = new ArrayList();
	//menu lib comments
	private static final String menu_comment = "format of menus is \"modid:mainmenu <class> = true/false\" changeing the menu order will change it in game";
	private static final String musicDeny_comment = "this is a blacklist of menus that extend GuiMainMenu but, have their own music";
	private static final String musicAllow_comment = "this is a whitelist of menus not extending GuiMainMenu that require vanilla music";
	//buttons
	public static int leftButtonId = 498;
	public static int rightButtonId = 499;
	public static int leftButtonPosX = 5;
	public static int leftButtonPosY = 5;
	public static int leftButtonWidth = 20;
	public static int leftButtonHeight = 20;
	public static int lFButtonPosX = 5;
	public static int lFButtonPosY = 5;
	public static int lFButtonWidth = 64;
	public static int lFButtonHeight = 20;
	public static int rightButtonPosX = 30;
	public static int rightButtonPosY = 5;
	public static int rightButtonWidth = 20;
	public static int rightButtonHeight = 20;
	public static int rFButtonPosX = 74;
	public static int rFButtonPosY = 5;
	public static int rFButtonWidth = 64;
	public static int rFButtonHeight = 20;
	
	/**
	 * load all configurations for menu lib
	 */
	public static void loadMenuLib(File d) 
	{
		if(cfgmenu == null)
			cfgmenu = new File(d, "menulib/menulib.cfg");
		
		Configuration buttons = new Configuration(new File(cfgmenu.getParentFile(), "buttons.cfg") );
		buttons.load();
		fancyPage = buttons.get("buttons","fancyButtons",false).getBoolean();
		leftButtonId = buttons.get("buttons","leftId", leftButtonId).getInt();
		rightButtonId = buttons.get("buttons","rightId", rightButtonId).getInt();
		leftButtonPosX = buttons.get("button_left", "leftPosX", leftButtonPosX).getInt();
		leftButtonPosY = buttons.get("button_left", "leftPosY", leftButtonPosY).getInt();
		leftButtonWidth = buttons.get("button_left", "leftWidth", leftButtonWidth).getInt();
		leftButtonHeight = buttons.get("button_left", "leftHeight", leftButtonHeight).getInt();
		lFButtonPosX = buttons.get("button_leftFancy", "leftFancyPosX", lFButtonPosX).getInt();
		lFButtonPosY = buttons.get("button_leftFancy", "leftFancyPosY", lFButtonPosY).getInt();
		lFButtonWidth = buttons.get("button_leftFancy", "leftFancyWidth", lFButtonWidth).getInt();
		lFButtonHeight = buttons.get("button_leftFancy", "leftFancyHeight", lFButtonHeight).getInt();
		rightButtonPosX = buttons.get("button_right", "rightPosX", rightButtonPosX).getInt();
		rightButtonPosY = buttons.get("button_right", "rightPosY", rightButtonPosY).getInt();
		rightButtonWidth = buttons.get("button_right", "rightWidth", rightButtonWidth).getInt();
		rightButtonHeight = buttons.get("button_right", "rightHeight", rightButtonHeight).getInt();
		rFButtonPosX = buttons.get("button_rightFancy", "rightFancyPosX", rFButtonPosX).getInt();
		rFButtonPosY = buttons.get("button_rightFancy", "rightFancyPosY", rFButtonPosY).getInt();
		rFButtonWidth = buttons.get("button_rightFancy", "rightfancyWidth", rFButtonWidth).getInt();
		rFButtonHeight = buttons.get("button_rightFancy", "rightFancyHeight", rFButtonHeight).getInt();
		buttons.save();
		
		Configuration config = new Configuration(cfgmenu);
		config.load();
		displayNewMenu = config.get("menulib","displayNewMenu",true).getBoolean();
		cmmAndVanilla = config.get("menulib", "cmmAndVanilla", cmmAndVanilla).getBoolean();
		currentMenuIndex = new ResourceLocation(config.get("menulib", "currentMenuIndex", "").getString());
		String[] order = config.get("menulib", "menus", new String[]{""},menu_comment).getStringList();
		setMenus(order);
		String[] clAllow = config.getStringList("classes_allowed", "music", new String[]{"lumien.custommainmenu.gui.GuiCustom"}, musicAllow_comment);
		setClasses(musicAllow, clAllow);
		String[] clDenyList = config.getStringList("classes_deny", "music", new String[]{""}, musicDeny_comment);
		setClasses(musicDeny, clDenyList);
		config.save();
		isLoaded = true;
	}
	
	private static void setClasses(List<Class> cache, String[] strList) 
	{
		cache.clear();
		for(String s : strList)
		{
			if(JavaUtil.toWhiteSpaced(s).isEmpty())
				continue;
			Class c = ReflectionUtil.classForName(s);
			if(c != null)
				cache.add(c);
		}
	}

	private static void setMenus(String[] order) 
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
	
	private static void syncComments(Configuration config) 
	{
		config.get("menulib", "menus", new String[]{""}, menu_comment);
		config.get("music", "classes_deny", new String[]{""}, musicDeny_comment);
		config.get("music", "classes_allowed", new String[]{""}, musicAllow_comment);
	}
	
	/**
	 * does the config need to save
	 */
	public static boolean isDirty = false;
	/**
	 * does the config have a new menu
	 */
	public static boolean addedMenus = false;

	/**
	 * saves the generated menus to the config with keeping configurations from the user data
	 */
	public static void saveMenusAndIndex() 
	{
		Configuration config = new Configuration(cfgmenu);
		config.load();
		List<String> list = new ArrayList();
		for(LineArray line : mainMenus)
		{
			ResourceLocation id = line.getResourceLocation();
			if(!list.contains(id))
			{
				if(!line.getBoolean())
				{
					list.add(line.toString());
				}
				else
				{
					list.add(id + (line.hasStringMeta() ? " <" + line.getMetaString() + ">" : "") );
				}
			}
		}
		setConfigIndex(config, currentMenuIndex);
		String[] strlist = JavaUtil.toStaticStringArray(list);
		Property prop = config.get("menulib", "menus", strlist, "");
		prop.set(strlist);
		syncComments(config);
		config.save();
		isDirty = false;
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
		setConfigIndex(config, loc);
		currentMenuIndex = loc;
		syncComments(config);
		config.save();
		if(Config.debug)
		{
			JavaUtil.printTime(stamp, "Saved Current Menu:");
		}
	}
	
	private static void setConfigIndex(Configuration config, ResourceLocation loc) 
	{
		Property prop_index = config.get("menulib", "currentMenuIndex", "");
		prop_index.set(loc.toString());
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
			mainMenus.add(index, new LineArray(loc.toString() + " = " + true));
			isDirty = true;
			if(!addedMenus)
				addedMenus = index == mainMenus.size();
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
			mainMenus.add(index, new LineArray(loc + " <" + clazz + ">" + " = " + enabled));
			isDirty = true;
			if(!addedMenus)
				addedMenus = index == mainMenus.size();
		}
	}
	
	public static boolean hasMenu(ResourceLocation loc) 
	{
		for(LineArray line : ConfigMenu.mainMenus)
			if(line.getResourceLocation().equals(loc))
				return true;
		return false;
	}


}
