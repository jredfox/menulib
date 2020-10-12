package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;

import net.minecraft.client.gui.GuiScreen;

public interface IMusicGuiHolder extends IMusicPlayer{
	
	public GuiScreen getGui();
	
}
