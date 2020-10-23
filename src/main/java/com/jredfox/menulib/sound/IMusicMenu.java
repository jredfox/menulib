package com.jredfox.menulib.sound;

import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.misc.GameState;

public interface IMusicMenu extends IMusicGuiHolder {
	
	/**
	 * a reference to the IMenu object for the IMusic
	 */
	public IMenu getMenu();
	
	@Override
	public default GameState getGameState()
	{
		return GameState.MENU;
	}

}
