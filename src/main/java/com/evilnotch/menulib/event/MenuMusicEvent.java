package com.evilnotch.menulib.event;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * used to determine if music can play. Currently only supports vanilla however modders can fire this for their main menu music
 * @author jredfox
 */
public class MenuMusicEvent extends Event {
	
	public GuiScreen gui;
	public boolean canPlay;
	public Class instanceClass;
	public boolean isVanillaTicker;
	
	public MenuMusicEvent(GuiScreen screen, Class instance)
	{
		this.gui = screen;
		this.canPlay = this.gui instanceof GuiMainMenu;
		this.instanceClass = instance;
		this.isVanillaTicker = this.instanceClass.equals(MusicTicker.class);
	}
	
	/**
	 * use this to fire the event
	 */
	public static MenuMusicEvent fireMusicEvent(GuiScreen screen, Class instance)
	{
		MenuMusicEvent e = new MenuMusicEvent(screen, instance);
		MinecraftForge.EVENT_BUS.post(e);
		return e;
	}

}
