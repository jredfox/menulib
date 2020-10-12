package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;
import com.jredfox.menulib.menu.IMenu;

import net.minecraft.client.gui.GuiScreen;

public interface IMusicMenu extends IMusicGui {
	
	/**
	 * a reference to the IMenu object for the IMusic
	 */
	public IMenu getMenu();
	
	@Override
	public default MusicState getMusicState()
	{
		return MusicState.MENU;
	}

}
