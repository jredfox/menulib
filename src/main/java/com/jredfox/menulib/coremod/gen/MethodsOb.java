package com.jredfox.menulib.coremod.gen;

import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.misc.MLUtil;

import net.minecraft.client.settings.GameSettings;

public class MethodsOb {
	
	/**
	 * if locked will be what the user configures else will be capped at 30 or higher depending on the game settings
	 */
	public GameSettings field_71474_y;
	public int getMenuFrames() 
    {
		IMenu menu = MenuRegistry.INSTANCE.getMenu();
    	int frames = menu != null ? menu.getFrames() : -1;
    	return frames > -1 ? frames : MLUtil.capMin(this.field_71474_y.limitFramerate, 30);
	}

}
