package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;

import net.minecraft.util.ResourceLocation;

public class MusicPlayerEmpty implements IMusicPlayer{

	public static final ResourceLocation empty = new ResourceLocation("empty");
	public static final MusicPlayerEmpty musicPlayer = new MusicPlayerEmpty();
	
	@Override
	public ResourceLocation getId() 
	{
		return empty;
	}

	@Override
	public MusicState getMusicState() 
	{
		return MusicState.NONE;
	}

	@Override
	public boolean replaceMusic() 
	{
		return false;
	}

	@Override
	public void tick() {}

}
