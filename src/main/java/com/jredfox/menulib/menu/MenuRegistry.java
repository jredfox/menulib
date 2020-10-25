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

	public int getIndex() 
	{
		return this.menus.indexOf(this.menu);
	}
	
	public void setMenu(int index) 
	{
		IMenu nextMenu = this.menus.get(index);
		if(!nextMenu.isEnabled())
		{
			throw new RuntimeException("cannot set index to a disabled IMenu:" + nextMenu.getId());
		}
		this.close(this.menu);
		this.switchMenu(this.menu);
		this.menu = nextMenu;
		this.display();
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
	
	/**
	 * should the gui event handler replace the current gui on open
	 */
	public boolean shouldReplace(GuiScreen gui)
	{
		return gui instanceof GuiMainMenu || this.isMenu(gui);
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
	
	public int size()
	{
		int size = 0;
		for(IMenu menu : this.menus)
			if(menu.isEnabled())
				size++;
		return size;
	}

	//START WIP CODE________________________________
	public void load()
	{
		this.menus.clear();
		this.menus.addAll(this.coded);
		this.loadUser();
		this.menu = this.menus.get(this.getFirstIndex());
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
		if(this.menu != null && !this.menu.isEnabled() && this.mc.currentScreen == this.getGui())
		{
			int index = this.getPreviousIndex();
			if(index == -1)
				index = this.getNextIndex();
			this.setMenu(index);
		}
	}
	
	public void next()
	{
		int index = this.getNextIndex();
		if(index == -1)
			index = this.getFirstIndex();
		this.setMenu(index);
	}
	
	public void previous()
	{
		int index = this.getPreviousIndex();
		if(index == -1)
			index = this.getLastIndex();
		this.setMenu(index);
	}
	
	public void display()
	{
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		Minecraft.getMinecraft().displayGuiScreen(GuiHandler.fake_menu);
	}

	public int getPreviousIndex() 
	{
		for(int i = this.getIndex() - 1; i >= 0 ; i--)
		{
			if(this.menus.get(i).isEnabled())
				return i;
		}
		return -1;
	}
	
	public int getNextIndex() 
	{
		for(int i = this.getIndex() + 1; i < this.menus.size() ; i++)
		{
			if(this.menus.get(i).isEnabled())
				return i;
		}
		return -1;
	}
	
	public int getLastIndex() 
	{
		for(int i = this.menus.size() - 1; i >= 0; i--)
		{
			if(this.menus.get(i).isEnabled())
				return i;
		}
		return -1;
	}

	public int getFirstIndex()
	{
		for(IMenu m : this.menus)
			if(m.isEnabled())
				return this.menus.indexOf(m);
		return -1;
	}
	
}