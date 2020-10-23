package com.jredfox.menulib.sound;

import com.jredfox.menulib.misc.GameState;

import net.minecraft.util.ResourceLocation;

public interface IMusicPlayer {
	/**
	 * the music players id so mods can override it in event handlers or replace it
	 */
	public ResourceLocation getId();
	/**
	 * the intended music state
	 */
	public GameState getGameState();
	/**
	 * called every tick use this to calculate when to start playing music
	 */
    public void tick();
    
    //_____________START EXPERIMENTAL CODE HERE______________________________
    
    public ResourceLocation getCategory();
    public boolean stopPrevious();
    public boolean shouldReplace();
    public int maxPlayCount();
    
}
    
