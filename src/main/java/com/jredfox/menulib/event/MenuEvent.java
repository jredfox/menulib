package com.jredfox.menulib.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jredfox.menulib.menu.IMenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * allows modders to override menu lib with ease by simply using these events
 */
public class MenuEvent extends Event{
	
	public IMenu menu;
	
	public MenuEvent(IMenu menu)
	{
		this.menu = menu;
	}
	
	public static class Open extends MenuEvent 
	{
		public Open(IMenu menu) 
		{
			super(menu);
		}
	}
	
	public static class Close extends MenuEvent 
	{
		public Close(IMenu menu) 
		{
			super(menu);
		}
	}
	
	public static class SwitchMenu extends MenuEvent 
	{
		public SwitchMenu(IMenu menu) 
		{
			super(menu);
		}
	}
	
	public static class SelectMenu extends MenuEvent 
	{
		public SelectMenu(IMenu menu) 
		{
			super(menu);
		}
	}

}
