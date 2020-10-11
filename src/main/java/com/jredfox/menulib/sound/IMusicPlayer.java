package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.event.MusicEvent.MusicState;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMusicPlayer {
	/**
	 * the music players id so mods can override it in event handlers or replace it
	 */
	public ResourceLocation getId();
	/**
	 * the intended music state
	 */
	public MusicState getMusicState();
	/**
	 * if true will replace current music player 
	 * if false it will not play the music until the other one has finished playing
	 */
	public boolean replaceMusic();
	/**
	 * called every tick use this to calculate when to start playing music
	 */
    public void tick();
    
}
