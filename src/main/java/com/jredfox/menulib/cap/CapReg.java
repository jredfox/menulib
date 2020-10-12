package com.jredfox.menulib.cap;

import com.evilnotch.lib.minecraft.capability.CapContainer;
import com.evilnotch.lib.minecraft.capability.registry.CapRegWorld;
import com.jredfox.menulib.sound.IMusicPlayer;
import com.jredfox.menulib.sound.IMusicPlayerHolder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CapReg extends CapRegWorld {
	
	public static ResourceLocation musicPlayerWorld = new ResourceLocation("menulib:music_player");
	
	@Override
	public void register(World world, CapContainer container) 
	{
		container.registerCapability(musicPlayerWorld, new CapabilityMusic(world));
	}
}
