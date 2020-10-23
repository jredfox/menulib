package com.jredfox.menulib.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.misc.GameState;
import com.jredfox.menulib.sound.IMusicGuiHolder;
import com.jredfox.menulib.sound.IMusicPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * used to determine if music can play. Currently only supports vanilla however modders can fire this for their main menu music
 * @author jredfox
 */
public class MusicEvent extends Event {
	
	public ResourceLocation musicId;
	public boolean canPlay;
	public GameState state;
	public GuiScreen gui;
	public ISound sound;

	public MusicEvent(IMusicPlayer player, ISound sound)
	{
		this.musicId = player.getId();
		this.state = player.getGameState();
		this.gui = player instanceof IMusicGuiHolder ? ((IMusicGuiHolder)player).getGui() : null;
		this.canPlay = true;
		this.sound = sound;
	}

	/**
	 * returns if the music canPlay
	 */
	public static boolean canPlay(IMusicPlayer player, ISound sound)
	{
		MusicEvent e = new MusicEvent(player, sound);
		MinecraftForge.EVENT_BUS.post(e);
		return e.canPlay;
	}
	
	public static enum MusicState
	{
		GAME(),
		GUI(),
		MENU(),
		NONE()
	}

}
