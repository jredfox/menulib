package com.jredfox.menulib.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.lib.util.line.LineArray;
import com.jredfox.menulib.event.MenuRegistryEvent;
import com.jredfox.menulib.eventhandler.GuiHandler;
import com.jredfox.menulib.misc.MLUtil;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class MenuRegistry
{
	public IMenu menu;
	public boolean isOpen;
	public int index;
	public IMenu previous;
	public boolean isLoaded;
	public List<IMenu> registry = new ArrayList();
	public List<IMenu> menus = new ArrayList(0);
	protected List<Class<? extends GuiScreen>> menuBases = new ArrayList(3);
	public Minecraft mc = Minecraft.getMinecraft();
	public static MenuRegistry INSTANCE = new MenuRegistry();
	
	public void register(IMenu menu)
	{
	    this.remove(menu);
		this.registry.add(menu);
	}
	
	public void register(int index, IMenu menu)
	{
		this.remove(menu);
		this.registry.add(index, menu);
	}
	
	public void remove(IMenu menu)
	{
		this.registry.remove(menu);
	}

	public IMenu getMenu()
	{
		return this.menu;
	}
	
	public int getIndex()
	{
		return this.index;
	}

	public GuiScreen getGui()
	{
		return this.getMenu().get();
	}
	
	public GuiScreen getGuiOpen()
	{
		return this.getGui() != null ? this.getGui() : this.getMenu().create();
	}
	
	/**
	 * should the gui event handler replace the current gui on open
	 */
	public boolean shouldReplace(GuiScreen gui)
	{
		return gui instanceof GuiMainMenu || this.isMenu(gui);
	}

	public boolean isMenu(GuiScreen gui)
	{
		return gui != null && isMenu(gui.getClass());
	}
	
	public boolean isMenu(Class<? extends GuiScreen> clazz)
	{
		for(Class<? extends GuiScreen> base : this.menuBases)
			if(JavaUtil.isClassExtending(base, clazz))
				return true;
		return false;
	}

	public IMenu getMenu(ResourceLocation id)
	{
		for(IMenu m : this.registry)
			if(m.getId().equals(id))
				return m;
		return null;
	}

	public boolean isBrowsable() 
	{
		return this.menus.size() > 1;
	}
	
	public void previous()
	{
		IMenu menu = this.getPrevious();
		if(menu == null)
			menu = this.getLast();
		this.setMenu(menu);
	}

	public void next()
	{
		IMenu menu = this.getNext();
		if(menu == null)
			menu = this.getFirst();
		this.setMenu(menu);
	}
	
	public void setMenu(IMenu nextMenu) 
	{
		this.setMenu(nextMenu, true);
	}
	
	public void setMenu(IMenu nextMenu, boolean display) 
	{
		this.sanityCheck(nextMenu);
		if(this.isMenuIndex(nextMenu))
			return;
		IMenu old = this.menu;
		if(this.isDisplaying())
		{
			old.closeGui();//close the old gui
		}
		this.menu = nextMenu;
		this.syncChange(this.menu);
		if(old != null)
		{
			this.close(old);
		}
		this.previous = old;
		this.open(this.menu);
		
		//save config
		if(!this.menu.getId().equals(MLConfig.menuIndex))
			MLConfig.setDirtyIndex(true);
		MLConfig.save();
		
		//display menu
		if(display)
			this.display();
	}

	public void sanityCheck(IMenu nextMenu)
	{
		if(!nextMenu.isEnabled())
			throw new RuntimeException("cannot set index to a disabled IMenu:" + nextMenu.getId());
		else if(!this.menus.contains(nextMenu))
			throw new RuntimeException("cannot set index to an unregistered IMenu:" + nextMenu.getId());
	}

	/**
	 * stops all sounds and displays a fake gui menu so an IMenu will be displayed
	 */
	public void display()
	{
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		Minecraft.getMinecraft().displayGuiScreen(GuiHandler.fake_menu);
	}
	
	public boolean isDisplaying()
	{
		return this.menu != null && this.menu.get() != null && this.menu.get() == this.mc.currentScreen;
	}
	
	public boolean isDisplaying(IMenu menu)
	{
		return this.menu == menu && menu.get() != null && menu.get() == this.mc.currentScreen;
	}
	
	/**
	 * return if your IMenu is MenuRegistry.menu
	 */
	public boolean isMenuIndex(IMenu menu) 
	{
		return this.menu == menu;
	}
	
	/**
	 * @return return true if non null and enabled
	 */
	public boolean isEnabledSafe(IMenu menu)
	{
		return menu != null && menu.isEnabled();
	}
	
	public IMenu getFirst() 
	{
		return this.menus.get(0);
	}
	
	public IMenu getLast() 
	{
		return this.menus.get(this.menus.size() - 1);
	}
	
	public IMenu getPrevious()
	{
		int index = this.getIndex() - 1;
		return index == -1 ? null : this.menus.get(index);
	}
	
	public IMenu getNext()
	{
		int index = this.getIndex() + 1;
		return index == this.menus.size() ? null : this.menus.get(index);
	}
	
	public IMenu getNearestMenu() 
	{
		IMenu m = this.getPrevious();
		if(m == null)
			m = this.getNext();
		return m;
	}
	
	public static Comparator menuComparator = new Comparator<IMenu>()
	{
		@Override
		public int compare(IMenu m1, IMenu m2)
		{
			return ((Integer)MenuRegistry.INSTANCE.registry.indexOf(m1)).compareTo(MenuRegistry.INSTANCE.registry.indexOf(m2));
		}
	};
	
	/**
	 * sync enabling/disabling a menu
	 */
	public void update(IMenu menu)
	{
		if(!this.isLoaded)
			return;
		if(!menu.isEnabled())
		{
			IMenu nearest = this.getNearestMenu();
			boolean remove = this.menus.remove(menu);
			if(!remove)
				return;
			
			this.syncChange(menu);
			MLConfig.setDirtyOrder(true);
			
			if(this.isDisplaying(menu))
				this.setMenu(nearest);//this sets the config dirty, syncs any changes
			else if(this.isMenuIndex(menu))
				this.setMenu(nearest, false);//if your in the sub menus switch out of it
		}
		else if(!this.menus.contains(menu))
		{
			MLUtil.add(this.menus, menuComparator, menu);
			this.syncChange(menu);
			MLConfig.setDirtyOrder(true);
		}
		MLConfig.save();
	}

	/**
	 * sync the buttons & index data
	 */
	public void syncChange(IMenu menu) 
	{
		this.syncIndex();
		this.syncButton(menu.getPrevious());
		this.syncButton(menu.getNext());
	}
	
	/**
	 * call this if you added / removed a menu from menus or even switched a menu
	 */
	public void syncIndex() 
	{
		this.index = this.menus.indexOf(this.menu);
	}
	
	/**
	 * sync the button visibility and being enabled to whether or not the main menu is browsable
	 */
	public void syncButton(GuiButton b) 
	{
		if(b == null)
			return;
		boolean enabled = this.isBrowsable();
		b.enabled = enabled;
		b.visible = enabled;
	}
	
	public void load()
	{
		long ms = System.currentTimeMillis();
		MinecraftForge.EVENT_BUS.post(new MenuRegistryEvent());
		this.syncConfig();
		this.cacheMenuBases();
		this.populateMenus();
		this.setInitMenu();
		this.isLoaded = true;
		JavaUtil.printTime(ms, "Done loading MenuRegistry:");
	}

	public void cacheMenuBases()
	{
		this.menuBases.clear();
		for(IMenu menu : this.registry)
			for(Class<? extends GuiScreen> c : menu.getGuiClasses())
				if(!this.isMenu(c))
					this.menuBases.add(c);
	}

	/**
	 * syncs the config order then syncs the registry to itself in the new list
	 */
	public void syncConfig() 
	{
		MLConfig.registerUserMenus();
		List<IMenu> list = new ArrayList(this.registry.size());
		
		//sync config order
		for(String s : MLConfig.menu_order)
		{
			LineArray line = new LineArray(s);
			if(!line.hasHead())
				line.setHead(true);
			ResourceLocation id = line.getResourceLocation();
			IMenu menu = this.getMenu(id);
			if(menu == null)
			{
				System.out.println("skipping invalid menu:" + id);
				MLConfig.setDirtyOrder(true);
				continue;
			}
			menu.setEnabled(line.getBoolean());
			list.add(menu);
		}
		
		//sync registry to the new list
		for(IMenu m : this.registry)
		{
			if(!list.contains(m))
			{
				list.add(this.getRealtiveIndex(m, list), m);
				MLConfig.setNewMenu(m);
				MLConfig.setDirtyOrder(true);
			}
		}
		this.registry = list;
	}
	
	/**
	 * guesses the best insertion order based on the left menu of the registry then gets it from a new list
	 */
	protected int getRealtiveIndex(IMenu m, List<IMenu> compare) 
	{
		int index = this.registry.indexOf(m) - 1;
		if(index == -1)
			return 0;
		IMenu pair = this.registry.get(index);
		return compare.indexOf(pair) + 1;
	}

	public void populateMenus() 
	{
		this.menus = new ArrayList(this.registry.size());
		for(IMenu m : this.registry)
			if(m.isEnabled())
				this.menus.add(m);
	}

	public void setInitMenu() 
	{
		IMenu cfgIndex = this.getMenu(MLConfig.menuIndex);
		IMenu newMenu = MLConfig.displayNew && MLConfig.newMenu != null ? this.getMenu(MLConfig.newMenu) : null;
		IMenu menu = newMenu != null ? newMenu : (this.isEnabledSafe(cfgIndex) ? cfgIndex : this.getFirst());
		this.setMenu(menu, false);
	}
	
    //WIP IMenu Events______________________
	public void open(IMenu menu)
	{
		if(this.isOpen)
			return;
		menu.open();
		this.isOpen = true;
	}
	
	public void close(IMenu menu)
	{
		if(!this.isOpen)
			return;
		menu.close();
		this.isOpen = false;
	}
	
	public void openGui(IMenu menu)
	{
		
	}
	
	public void closeGui(IMenu menu)
	{
		
	}
	
	public void clear(IMenu menu)
	{
		
	}
	
	public void switchMenu(IMenu oldMenu, IMenu newMenu)
	{
		
	}
	
	public void SelectMenu(IMenu menu)
	{
		
	}
}