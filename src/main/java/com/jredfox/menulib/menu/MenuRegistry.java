package com.jredfox.menulib.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.lib.util.line.LineArray;
import com.jredfox.menulib.eventhandler.GuiHandler;
import com.jredfox.menulib.misc.MLUtil;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MenuRegistry
{
	public IMenu menu;
	public int index;
	public IMenu previous;
	public boolean isLoaded;
	public List<IMenu> registry = new ArrayList();
	public List<IMenu> menus = new ArrayList(0);
	protected Map<ResourceLocation, Integer> temp = new HashMap(0);
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
		this.temp.put(menu.getId(), index);
	}
	
	public void remove(IMenu menu)
	{
		this.registry.remove(menu);
		this.temp.remove(menu.getId());
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
		if(gui == null)
			return false;
		
		for(IMenu m : this.menus)
		{
			if(m.getGuiClass().equals(gui.getClass()))
				return true;
		}
		return false;
	}
	
	public IMenu getMenu(ResourceLocation id)
	{
		for(IMenu m : this.registry)
			if(m.getId().equals(id))
				return m;
		return null;
	}

	public void open(IMenu menu) 
	{
		menu.open();
	}

	public void close(IMenu menu) 
	{
		menu.close();
	}
	
	public void switchMenu(IMenu menu)
	{
		menu.switchMenu();
	}
	
	public void clear(IMenu menu)
	{
		menu.clear();
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
		this.sanityCheck(nextMenu);
		this.close(this.menu);
		this.switchMenu(this.menu);
		this.previous = this.menu;
		this.setMenuDirect(nextMenu);
		MLConfig.setDirtyIndex(true);
		MLConfig.save();
		this.display();
	}
	
	/**
	 * do not use unless you know what you are doing
	 */
	public void setMenuDirect(IMenu menu) 
	{
		this.menu = menu;
		this.syncChange(menu);
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
	
	public void load()
	{
		new LineArray("a");
		long ms = System.currentTimeMillis();
		this.syncConfig();
		this.populateMenus();
		this.setInitMenu();
		MLConfig.save();
		this.isLoaded = true;
		JavaUtil.printTime(ms, "Done loading MenuRegistry:");
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
				Integer index = this.temp.get(m.getId());
				if(index != null)
				{
					list.add(index, m);
				}
				else
				{
					list.add(m);
				}
				MLConfig.setNewMenu(m.getId());
				MLConfig.setDirtyOrder(true);
			}
		}
		this.temp.clear();
		this.registry = list;
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
		IMenu menu = cfgIndex != null && cfgIndex.isEnabled() ? cfgIndex : this.getFirst();
		IMenu newMenu = MLConfig.displayNew && MLConfig.newMenu != null ? this.getMenu(MLConfig.newMenu) : null;
		if(newMenu != null)
			menu = newMenu;
		
		if(!menu.getId().equals(MLConfig.menuIndex))
			MLConfig.setDirtyIndex(true);
		this.setMenuDirect(menu);
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
			if(this.isDisplaying(menu))
			{
				IMenu prevMenu = this.getPrevious();
				if(prevMenu == null)
					prevMenu = this.getNext();
				this.menus.remove(menu);
				this.syncIndex();//sync index to -1
				MLConfig.setDirtyOrder(true);
				this.setMenu(prevMenu);//this sets the config dirty, syncs any changes
			}
			else
			{
				boolean removed = this.menus.remove(menu);
				if(removed)
				{
					this.syncChange(menu);
					MLConfig.setDirtyOrder(true);
				}
			}
		}
		else if(!this.menus.contains(menu))
		{
			MLUtil.add(this.menus, menuComparator, menu);
			this.syncChange(menu);
			MLConfig.setDirtyOrder(true);
		}
		MLConfig.save();
	}
	
	public boolean isDisplaying(IMenu menu)
	{
		return menu.get() != null && menu.get() == this.mc.currentScreen;
	}
	
	/**
	 * the algorithm is flawed doesn't work in every case
	 */
	public int getAddedIndex(IMenu index)
	{
		for(int i = this.registry.indexOf(index) - 1; i >= 0 ; i--)
		{
			IMenu m = this.registry.get(i);
			if(m.isEnabled())
				return this.menus.indexOf(m) + 1;
		}
		return 0;
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
	
}