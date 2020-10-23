package com.jredfox.menulib.sound;

import com.jredfox.menulib.misc.GameState;

import net.minecraft.util.ResourceLocation;

public class MusicEmpty implements IMusicPlayer{

	public static final ResourceLocation empty = new ResourceLocation("menulib:empty");
	public static final ResourceLocation none = new ResourceLocation("menulib:none");
	public static final MusicEmpty musicPlayer = new MusicEmpty();
	
	@Override
	public ResourceLocation getId() 
	{
		return empty;
	}

	@Override
	public GameState getGameState() 
	{
		return GameState.NONE;
	}

	@Override
	public boolean shouldReplace() 
	{
		return false;
	}

	@Override
	public void tick() {}

	@Override
	public ResourceLocation getCategory() {
		return none;
	}

	@Override
	public boolean stopPrevious() {
		return false;
	}

	@Override
	public int maxPlayCount() {
		return 1;
	}

}
