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
	public List<IMenu> menus = new ArrayList();
	public static MenuRegistry INSTANCE = new MenuRegistry();
	
	public void register(IMenu menu)
	{
		this.menus.remove(menu);
		this.menus.add(menu);
	}

	public IMenu getMenu()
	{
		return this.menu;
	}
	
	public void update()
	{
		if(!this.menu.isEnabled())
		{
			this.previous();
		}
	}

	public GuiScreen getGui()
	{
		return this.getMenu().get();
	}
	
	public GuiScreen getGuiOpen()
	{
		return this.getGui() != null ? this.getGui() : this.getMenu().create();
	}
	
	public void next()
	{
		int index = this.getIndex() + 1;
		if(index == this.menus.size())
			index = 0;
		this.setMenu(index);
	}
	
	public void previous()
	{
		int index = this.getIndex() - 1;
		if(index == -1)
			index = this.menus.size() - 1;
		this.setMenu(index);
	}
	
	public int getIndex() 
	{
		return this.menus.indexOf(this.menu);
	}
	
	public void setMenu(int index) 
	{
		this.close(this.menu);
		this.switchMenu(this.menu);
		this.menu = this.menus.get(index);
		this.display();
	}
	
	public void display()
	{
		Minecraft.getMinecraft().getSoundHandler().stopSounds();
		Minecraft.getMinecraft().displayGuiScreen(GuiHandler.fake_menu);
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

	public void load()
	{
		this.menu = this.getFirst();
	}
	
	public IMenu getFirst()
	{
		for(IMenu m : this.menus)
			if(m.isEnabled())
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
	
}