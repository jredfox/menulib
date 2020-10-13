package com.jredfox.menulib.sound;

import com.evilnotch.lib.minecraft.tick.TickRegistry;
import com.jredfox.menulib.event.MusicEvent.MusicState;

import net.minecraft.util.ResourceLocation;

public class MusicEmpty implements IMusicPlayer{

	public static final ResourceLocation empty = new ResourceLocation("menulib:empty");
	public static final MusicEmpty musicPlayer = new MusicEmpty();
	
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
	public boolean shouldReplace() 
	{
		return false;
	}

	@Override
	public void tick() {}

}
