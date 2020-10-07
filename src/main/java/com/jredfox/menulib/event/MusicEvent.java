package com.jredfox.menulib.event;

import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * used to determine if music can play. Currently only supports vanilla however modders can fire this for their main menu music
 * @author jredfox
 */
public class MusicEvent extends Event {
	
	public ResourceLocation tickId;
	public boolean canPlay = true;
	public MusicState type;
	public GuiScreen gui;
	public ISound sound;

	public MusicEvent(ResourceLocation tickId, ISound sound)
	{
		this.gui = Minecraft.getMinecraft().currentScreen;
		this.type = getType(this.gui);
		this.canPlay = this.type == MusicState.MENU ? (MenuRegistry.getCurrentGui() instanceof GuiMainMenu) : true;
		this.tickId = tickId;
		this.sound = sound;
	}
	
	public MusicState getType(GuiScreen gui) 
	{
		return Minecraft.getMinecraft().world == null ? MusicState.MENU : gui != null ? MusicState.GAMEGUI : MusicState.GAME;
	}

	/**
	 * fires the MusicEvent
	 * @return if the music canPlay
	 */
	public static boolean fire(ResourceLocation id, ISound sound)
	{
		MusicEvent e = new MusicEvent(id, sound);
		MinecraftForge.EVENT_BUS.post(e);
		return e.canPlay;
	}
	
	public enum MusicState
	{
		GAME(),
		GAMEGUI(),
		MENU()
	}

}
