package com.jredfox.menulib.sound.category;

import java.util.List;

import com.jredfox.menulib.sound.IMusicPlayer;

import net.minecraft.client.gui.GuiScreen;

public interface ICategoryGui extends ICategory {
	
	/**
	 * whether or not it canTick if the MusicState is MusicState.GUI
	 */
	public boolean canTick(GuiScreen gui, boolean inGame);
	public List<IMusicPlayer> getPlayList(GuiScreen gui, boolean inGame);

}
