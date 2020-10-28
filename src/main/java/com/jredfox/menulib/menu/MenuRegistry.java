package com.jredfox.menulib.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.evilnotch.lib.main.loader.LoaderMain;
import com.evilnotch.lib.main.loader.LoadingStage;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.compat.util.CMMUtil;
import com.jredfox.menulib.eventhandler.GuiHandler;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class MenuRegistry
{
	public IMenu menu;
	public int index;
	public IMenu previous;
	public boolean isLoaded;
	public List<IMenu> registry = new ArrayList();
	public List<IMenu> user = new ArrayList();//stored seperatly so it can be removed when reloading
	public List<IMenu> menus = new ArrayList();
	public Minecraft mc = Minecraft.getMinecraft();
	public static MenuRegistry INSTANCE = new MenuRegistry();
	
	public void register(IMenu menu)
	{
		this.registry.remove(menu);
		this.registry.add(menu);
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
		MLConfig.saveIndex();
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
	
	//START WIP CODE________________________________
	
	public void load()
	{
		this.menus.clear();
		this.registry.addAll(this.user);
		
		for(IMenu m : this.registry)
			if(m.isEnabled())
				this.menus.add(m);
		
		//config menuIndex
		IMenu cfgIndex = this.getMenu(MLConfig.menuIndex);
		IMenu menu = cfgIndex != null && cfgIndex.isEnabled() ? cfgIndex : this.getFirst();
		if(cfgIndex != menu)
			System.out.println("menuIndex" + MLConfig.menuIndex + " is null or disabled setting it to:" + menu);
		this.setMenuDirect(menu);
		this.isLoaded = true;
	}

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
				this.syncIndex();//needs to sync the index even when it's removed because setting the men
				this.setMenu(prevMenu);
			}
			else
			{
				this.menus.remove(menu);
				this.syncChange(menu);
			}
		}
		else if(!this.menus.contains(menu))
		{
			this.menus.add(this.getAddedIndex(menu), menu);
			this.syncChange(menu);
		}
	}
	
	public boolean isDisplaying(IMenu menu)
	{
		return menu.get() != null && menu.get() == this.mc.currentScreen;
	}
	
	/**
	 * grabs the index from the registry and then assigns it to the enabled menu list
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