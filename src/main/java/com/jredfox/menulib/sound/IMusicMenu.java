package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;

import net.minecraft.client.gui.GuiScreen;

public interface IMusicMenu extends IMusicGui{
	
	@Override
	public default MusicState getMusicState()
	{
		return MusicState.MENU;
	}

}
