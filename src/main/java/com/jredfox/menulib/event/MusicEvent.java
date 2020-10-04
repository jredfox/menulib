package com.jredfox.menulib.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * used to determine if music can play. Currently only supports vanilla however modders can fire this for their main menu music
 * @author jredfox
 */
public class MusicEvent extends Event {
	
	public boolean canPlay;
	public Class instanceClass;
	public MusicTicker.MusicType type;
	public boolean vanillaTicker;
	
	public MusicEvent(Class tickerClass, MusicTicker.MusicType type)
	{
		this.instanceClass = tickerClass;
		this.canPlay = true;
		this.type = type;
		this.vanillaTicker = this.instanceClass.equals(MusicTicker.class);
	}
	
	/**
	 * fires the MusicEvent
	 * @return if the music canPlay
	 */
	public static boolean fire(Class tickerClass, MusicTicker.MusicType type)
	{
		MusicEvent e = type != type.MENU ? new MusicEvent(tickerClass, type) : new MusicMenuEvent(tickerClass, type, Minecraft.getMinecraft().currentScreen);
		MinecraftForge.EVENT_BUS.post(e);
		return e.canPlay;
	}

}
