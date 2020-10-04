package com.jredfox.menulib.event;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class MusicMenuEvent extends MusicEvent{
	
	public GuiScreen gui;
	public MusicMenuEvent(Class tickerClass, MusicTicker.MusicType type, GuiScreen screen)
	{
		super(tickerClass, type);
		this.gui = screen;
		this.canPlay = this.gui instanceof GuiMainMenu;
	}

}
