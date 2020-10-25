package com.jredfox.menulib.coremod.gen;

import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.misc.NumberUtil;

import net.minecraft.client.settings.GameSettings;

public class Methods {
	
	/**
	 * if locked will be what the user configures else will be capped at 30 or higher depending on the game settings
	 */
	public GameSettings gameSettings;
	public int getMenuFrames() 
    {
		IMenu menu = MenuRegistry.INSTANCE.getMenu();
    	int frames = menu != null ? menu.getFrames() : -1;
    	return frames > -1 ? frames : NumberUtil.capMin(this.gameSettings.limitFramerate, 30);
	}

}
