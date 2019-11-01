package com.evilnotch.menulib.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.main.Config;
import com.evilnotch.lib.main.loader.LoaderMain;
import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.evilnotch.lib.util.line.LineArray;
import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.compat.proxy.ProxyMod;
import com.evilnotch.menulib.event.MainMenuEvent;
import com.evilnotch.menulib.event.MainMenuEvent.MusicEvent;
import com.evilnotch.menulib.eventhandler.GuiEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class MenuRegistry {
	
	protected static List<IMenu> menus = new ArrayList();
	protected static boolean isInit;
	protected static Map<ResourceLocation,Integer> tempMenus = new HashMap();
	public static int indexMenu = 0;
	protected static IMenu currentMenu = null;
	
	public static void registerIMenu(IMenu menu)
	{
		menus.remove(menu);
		menus.add(menu);
		tempMenus.put(menu.getId(), -1);
	}
	
	public static void registerIMenu(int index, IMenu menu)
	{
		menus.remove(menu);
		menus.add(menu);
		tempMenus.put(menu.getId(), index);
	}
	
	/**
	 * Returns menu so you can manipulate data after adding one
	 * to make an IMenu method use register IMenu
	 * or simply make a new Menu instance(Menu implements IMenu) and override what you need 
	 */
	public static IMenu registerGuiMenu(Class<? extends GuiScreen> guiClazz,ResourceLocation id)
	{
		IMenu menu = new Menu(guiClazz, id);
		menus.remove(menu);
		menus.add(menu);
		tempMenus.put(id, -1);
		return menu;
	}
	
	public static IMenu registerGuiMenu(int index, Class<? extends GuiScreen> guiClazz,ResourceLocation id)
	{
		IMenu menu = new Menu(guiClazz,id);
		menus.remove(menu);
		menus.add(menu);
		tempMenus.put(id, index);
		return menu;
	}
	
	/**
	 * used by button guis to advanced current gui to the next gui
	 */
	public static void advanceNextMenu()
	{
		MainMenuEvent.AdvancedNext event = new MainMenuEvent.AdvancedNext();
		if(MinecraftForge.EVENT_BUS.post(event))
			return;
		setMenu(event.nextIndex);
		ProxyMod.menuChange();
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		Minecraft.getMinecraft().displayGuiScreen(GuiEventHandler.fake_menu);
		ConfigMenu.saveMenuIndex();
	}
	
	/**
	 * used by button guis to advanced current gui to the next gui
	 */
	public static void advancePreviousMenu()
	{
		MainMenuEvent.AdvancedPrevious event = new MainMenuEvent.AdvancedPrevious();
		if(MinecraftForge.EVENT_BUS.post(event))
			return;
		setMenu(event.nextIndex);
		ProxyMod.menuChange();
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		Minecraft.getMinecraft().displayGuiScreen(GuiEventHandler.fake_menu);
		ConfigMenu.saveMenuIndex();
	}
	
	public static int getNext(int index) 
	{
		if(index + 1 == MenuRegistry.getMenus().size() )
			return 0;
		index++;
		return index;
	}
	
	public static int getPrevious(int index) 
	{
		if( (index -1) == -1)
			return menus.size()-1;
		index--;
		return index;
	}
	/**
	 * creates a new gui from the current IMenu
	 */
	public static GuiScreen createCurrentGui()
	{
		IMenu menu = getCurrentMenu();
		GuiScreen screen = menu.createGui();
		return screen;
	}
	/**
	 * gets the current gui from the current IMenu without creating a new one for custom stuffs
	 */
	public static GuiScreen getCurrentGui()
	{
		IMenu menu = getCurrentMenu();
		GuiScreen screen = menu.getGui();
		return screen;
	}
	
	public static IMenu getCurrentMenu()
	{
		return currentMenu;
	}
	
	public static List<IMenu> getMenus() 
	{
		return menus;
	}
	
	public static int getMenuSize() 
	{
		return menus.size();
	}
	
	public static boolean containsMenu(Class clazz) 
	{
		for(int i=0;i<menus.size();i++)
		{
			if(menus.get(i).getGuiClass().equals(clazz))
				return true;
		}
		return false;
	}
	
	public static void removeMenu(ResourceLocation loc)
	{
		Iterator<IMenu> it = menus.iterator();
		while(it.hasNext())
		{
			IMenu m = it.next();
			if(m.getId().equals(loc))
			{
				it.remove();
				break;
			}
		}
		tempMenus.remove(loc);
	}
	
	/**
	 * re-order the menus list also skip any menus that are disabled
	 */
	public static void init() 
	{
		if(isInit)
			return;
		mergeTemp();
		reorderLists();
		checkConfig();
		setConfigIndex();
		isInit = true;
	}

	public static void mergeTemp() 
	{
		for(Map.Entry<ResourceLocation, Integer> map : tempMenus.entrySet())
		{
			int index = map.getValue();
			if(index == -1)
				ConfigMenu.saveMenuToConfig(map.getKey());
			else
				ConfigMenu.saveMenuToConfig(index, map.getKey());
		}
		tempMenus.clear();
	}

	public static void setConfigIndex() 
	{
		setMenu(ConfigMenu.currentMenuIndex);
	}

	public static void setMenu(int i) 
	{
		indexMenu = i;
		currentMenu = menus.get(i);
	}

	public static boolean setMenu(ResourceLocation loc) 
	{
		int index = getIndex(loc);
		if(index == -1)
		{
			System.out.println("null menu when trying to set index:" + loc);
			return false;
		}
		setMenu(index);
		return true;
	}
	
	public static boolean setMenu(IMenu menu)
	{
		return setMenu(menu.getId());
	}

	public static void checkConfig() 
	{
		if(ConfigMenu.isDirty)
		{
			if(ConfigMenu.displayNewMenu && ConfigMenu.addedMenus)
			{
				ConfigMenu.currentMenuIndex = menus.get(menus.size()-1).getId();//when adding a new menu display it
			}
			ConfigMenu.saveMenusAndIndex();
		}
	}

	public static void reorderLists() 
	{
		List<IMenu> list = new ArrayList<IMenu>();
		Iterator<LineArray> it = ConfigMenu.mainMenus.iterator();
		while(it.hasNext())
		{
			LineArray line = it.next();
			if(!line.getBoolean())
			{
				continue;
			}
			ResourceLocation loc = line.getResourceLocation();
			if(line.hasStringMeta())
			{
				Class c = ReflectionUtil.classForName(line.getMetaString());
				if(c == null)
				{
					System.out.println("null class when parsing menu for:" + line.getMetaString());
					it.remove();
					ConfigMenu.isDirty = true;
					continue;
				}
				IMenu menu = new Menu(c,loc);
				if(!list.contains(menu))
					list.add(menu);
			}
			else
			{
				IMenu menu = getMenu(loc);
				if(menu == null)
				{
					System.out.println("null menu when parsing found for:" + loc);
					it.remove();
					ConfigMenu.isDirty = true;
					continue;
				}
				if(!list.contains(menu))
					list.add(menu);
			}
		}
		menus = list;
		
		if(Config.debug)
			System.out.println("ConfigMenu.isDirty:" + ConfigMenu.isDirty);
		
		//more optimized then setting then saving the config twice
		if(!hasMenu(ConfigMenu.currentMenuIndex))
		{
			ResourceLocation loc = menus.get(0).getId();
			System.out.println("null currentIndex found:" + ConfigMenu.currentMenuIndex + " setting currentIndex to 0:" + loc);
			ConfigMenu.currentMenuIndex = loc;
			ConfigMenu.isDirty = true;
		}
	}
	
	public static boolean hasMenu(ResourceLocation loc)
	{
		return getMenu(loc) != null;
	}

	public static IMenu getMenu(ResourceLocation loc) 
	{
		for(IMenu menu : menus)
		{
			if(menu.getId().equals(loc))
				return menu;
		}
		return null;
	}

	public static int getIndex(ResourceLocation loc) 
	{
		for(int i=0;i<menus.size();i++)
		{
			IMenu menu = menus.get(i);
			if(menu.getId().equals(loc))
				return i;
		}
		return -1;
	}
	
	/**
	 * should buttons be added to IMenus
	 */
	public static boolean hasButtons()
	{
		return MenuRegistry.getMenuSize() > 1;
	}
	
	/**
	 * is this gui replaceable for IMenus
	 */
	public static boolean isReplaceable(GuiScreen gui)
	{
		if(gui == null)
			return false;
		return gui instanceof GuiMainMenu || gui instanceof GuiFakeMenu || containsMenu(gui.getClass() );
	}
	
	public static boolean hasInit()
	{
		return isInit;
	}
	
	public static void refresh()
	{
		isInit = false;
		init();
	}
	
	/**
	 * use this to fire the event
	 */
	public static boolean canPlayMusic(GuiScreen screen, Class instance)
	{
		MusicEvent e = new MusicEvent(screen, instance);
		MinecraftForge.EVENT_BUS.post(e);
		return e.canPlay;
	}

	/**
	 * Get the gui from the IMenu with null handeling
	 */
	public static GuiScreen getGui(IMenu menu) 
	{
		return menu != null ? menu.getGui() : null;
	}
}
