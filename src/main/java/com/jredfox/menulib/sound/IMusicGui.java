package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;

import net.minecraft.client.gui.GuiScreen;

public interface IMusicGui extends IMusicPlayer{
	
	public GuiScreen getGui();
	
}
