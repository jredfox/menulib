package com.jredfox.menulib.event;

import com.jredfox.menulib.menu.IMenu;

/**
 * allows modders to override menu lib with ease by simply using these events
 */
public class MenuEvent {
	
	public IMenu menu;
	
	public MenuEvent(IMenu menu)
	{
		this.menu = menu;
	}
	
	public static class Close extends MenuEvent 
	{
		public Close(IMenu menu) 
		{
			super(menu);
		}
	}
	
	public static class Open extends MenuEvent 
	{
		public Open(IMenu menu) 
		{
			super(menu);
		}
	}
	
	public static class Clear extends MenuEvent 
	{
		public Clear(IMenu menu) 
		{
			super(menu);
		}
	}
	
	public static class Button extends MenuEvent
	{
		public Button(IMenu menu) 
		{
			super(menu);
		}
	}

}
