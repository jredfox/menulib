package com.evilnotch.menulib.event;

import com.evilnotch.menulib.menu.IMenu;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MainMenuEvent extends Event{
	
	public IMenu menu;
	
	public MainMenuEvent(IMenu menu)
	{
		this.menu = menu;
	}
	
	/**
	 * fired when {@link IMenu#onOpen()} gets called
	 */
	@Cancelable
	public static class Open extends MainMenuEvent
	{
		public Open(IMenu menu)
		{
			super(menu);
		}
	}
	
	/**
	 * fired when {@link IMenu#onOpenFromSub()} gets called
	 */
	@Cancelable
	public static class OnOpenFromSub extends MainMenuEvent
	{
		public OnOpenFromSub(IMenu menu) 
		{
			super(menu);
		}
	}
	
	
	/**
	 * fired when {@link IMenu#onClose()} gets called
	 */
	@Cancelable
	public static class Close extends MainMenuEvent
	{
		public Close(IMenu menu)
		{
			super(menu);
		}
	}
	
	/**
	 * fired when {@link IMenu#onCloseFromSub()} gets called
	 */
	@Cancelable
	public static class OnCloseFromSub extends MainMenuEvent
	{
		public OnCloseFromSub(IMenu menu)
		{
			super(menu);
		}
	}
	
	/**
	 * fired when {@link MenuRegistry#advanceNextMenu()} switches indexes
	 */
	@Cancelable
	public static class AdvancedNext extends MainMenuEvent
	{
		public int currentIndex;
		public int nextIndex;
		
		public AdvancedNext()
		{
			super(MenuRegistry.getCurrentMenu());
			this.currentIndex = MenuRegistry.indexMenu;
			this.nextIndex = MenuRegistry.getNext(this.currentIndex);
		}
	}
	
	/**
	 * fired when {@link MenuRegistry#advancePreviousMenu()} gets called
	 */
	@Cancelable
	public static class AdvancedPrevious extends MainMenuEvent
	{
		public int currentIndex;
		public int nextIndex;
		
		public AdvancedPrevious()
		{
			super(MenuRegistry.getCurrentMenu());
			this.currentIndex = MenuRegistry.indexMenu;
			this.nextIndex = MenuRegistry.getPrevious(this.currentIndex);
		}
	}
	
	public static class ChooseMenu extends MainMenuEvent
	{
		public ChooseMenu(IMenu menu)
		{
			super(menu);	
		}
	}

}
