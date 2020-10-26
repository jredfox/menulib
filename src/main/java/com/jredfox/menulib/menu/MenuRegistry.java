package com.jredfox.menulib.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jredfox.menulib.eventhandler.GuiHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class MenuRegistry
{
	public IMenu menu;
	public List<IMenu> registry = new ArrayList();
	public List<IMenu> menus = new ArrayList();
	public List<IMenu> user = new ArrayList();
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
	
	//START WIP CODE________________________________
	
	public void load()
	{
		this.menus.clear();
		for(IMenu m : this.registry)
		{
			if(m.isEnabled())
			{
				this.menus.add(m);
			}
		}
		this.menu = this.menus.get(0);
	}
	
	/**
	 * if the menu is disabled and displaying will update the the nearest left menu if none is found the nearest right menu
	 * if the menu is enabled and not on the menu list it will re-add them based on the configed index
	 */
	public void update(IMenu menu)
	{
		if(!menu.isEnabled())
		{
			if(this.isDisplaying(menu))
			{
				IMenu index = this.getPrevious();
				if(index == null)
					index = this.getNext();
				this.menus.remove(menu);
				this.setMenu(index);
			}
			else
			{
				this.menus.remove(menu);
			}
		}
		else if(!this.menus.contains(menu))
		{
			this.menus.add(this.getAddedIndex(menu), menu);
		}
	}
	
	public int getAddedIndex(IMenu index)
	{
		for(int i = this.registry.indexOf(index) - 1; i >= 0 ; i--)
		{
			IMenu m = this.menus.get(i);
			if(m.isEnabled())
				return i + 1;
		}
		return 0;
	}

	public boolean isDisplaying(IMenu menu)
	{
		return menu.get() == this.mc.currentScreen;
	}
	
	public void setMenu(IMenu nextMenu) 
	{
		this.sanityCheck(nextMenu);
		this.close(this.menu);
		this.switchMenu(this.menu);
		this.menu = nextMenu;
		this.display();
	}
	
	public void sanityCheck(IMenu nextMenu)
	{
		if(!nextMenu.isEnabled())
			throw new RuntimeException("cannot set index to a disabled IMenu:" + nextMenu.getId());
		else if(!this.menus.contains(nextMenu))
			throw new RuntimeException("an unregistred IMenu has been attempted to be set to the current menu:" + nextMenu.getId());
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
	
	public IMenu getPrevious()
	{
		int index = this.menus.indexOf(this.menu) - 1;
		return index == -1 ? null : this.menus.get(index);
	}
	
	public IMenu getNext()
	{
		int index = this.menus.indexOf(this.menu) + 1;
		return index == this.menus.size() ? null : this.menus.get(index);
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
	
}