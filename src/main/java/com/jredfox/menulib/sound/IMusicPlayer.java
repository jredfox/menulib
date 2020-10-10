package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.event.MusicEvent.MusicState;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMusicPlayer {
	
	public ResourceLocation getId();
	public MusicState getMusicState();
    public void tick();
    
}
