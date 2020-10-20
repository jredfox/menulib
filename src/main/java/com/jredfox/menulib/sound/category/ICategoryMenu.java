package com.jredfox.menulib.sound.category;

import java.util.List;

import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.sound.IMusicPlayer;

public interface ICategoryMenu {
	
	/**
	 * whether or not canTick in the menus
	 */
	public boolean canTick(IMenu menu);
	public List<IMusicPlayer> getPlayList(IMenu menu);

}
