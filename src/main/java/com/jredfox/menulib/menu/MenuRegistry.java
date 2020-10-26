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
	public List<IMenu> coded = new ArrayList();
	public List<IMenu> menus = new ArrayList();
	public List<IMenu> user = new ArrayList();
	public Minecraft mc = Minecraft.getMinecraft();
	public static MenuRegistry INSTANCE = new MenuRegistry();
	
	public void register(IMenu menu)
	{
		this.coded.remove(menu);
		this.coded.add(menu);
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
		return this.size() > 1;
	}
	
	//START WIP CODE________________________________
	public int size()
	{
		int size = 0;
		for(IMenu menu : this.menus)
			if(menu.isEnabled())
				size++;
		return size;
	}

	public void load()
	{
		this.menus.clear();
		this.menus.addAll(this.coded);
		this.loadUser();
		this.menu = this.getFirst();
	}

	public void loadUser()
	{
		this.user.clear();
	}
	
	/**
	 * if the menu is disabled and on display it will set the display previous and if not available the next
	 */
	public void update()
	{
		if(this.isDisplaying() && !this.menu.isEnabled())
		{
			IMenu index = this.getPrevious();
			if(index == null)
				index = this.getNext();
			this.setMenu(index);
		}
	}
	
	public boolean isDisplaying()
	{
		return this.menu != null &&  this.menu.get() == this.mc.currentScreen;
	}
	
	public int getIndex() 
	{
		return this.menus.indexOf(this.menu);
	}
	
	public void setMenu(IMenu nextMenu) 
	{
		if(!nextMenu.isEnabled())
			throw new RuntimeException("cannot set index to a disabled IMenu:" + nextMenu.getId());
		else if(!this.menus.contains(nextMenu))
			throw new RuntimeException("an unregistred IMenu has been attempted to be set to the current menu:" + nextMenu.getId());
		this.close(this.menu);
		this.switchMenu(this.menu);
		this.menu = nextMenu;
		this.display();
	}
	
	public void next()
	{
		IMenu index = this.getNext();
		if(index == null)
			index = this.getFirst();
		this.setMenu(index);
	}
	
	public void previous()
	{
		IMenu index = this.getPrevious();
		if(index == null)
			index = this.getLast();
		this.setMenu(index);
	}
	
	public void display()
	{
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		Minecraft.getMinecraft().displayGuiScreen(GuiHandler.fake_menu);
	}

	public IMenu getPrevious() 
	{
		for(int i = this.getIndex() - 1; i >= 0 ; i--)
		{
			IMenu menu = this.menus.get(i);
			if(menu.isEnabled())
				return menu;
		}
		return null;
	}
	
	public IMenu getNext() 
	{
		for(int i = this.getIndex() + 1; i < this.menus.size() ; i++)
		{
			IMenu menu = this.menus.get(i);
			if(menu.isEnabled())
				return menu;
		}
		return null;
	}
	
	public IMenu getLast() 
	{
		for(int i = this.menus.size() - 1; i >= 0; i--)
		{
			IMenu menu = this.menus.get(i);
			if(menu.isEnabled())
				return menu;
		}
		return null;
	}

	public IMenu getFirst()
	{
		for(IMenu m : this.menus)
			if(m.isEnabled())
				return m;
		return null;
	}
	
}