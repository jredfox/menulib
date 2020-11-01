package com.jredfox.menulib.eventhandler;

import com.evilnotch.lib.minecraft.tick.ITick;
import com.jredfox.menulib.event.MinecraftShutdownEvent;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class MinecraftShutdownHandler implements ITick{

	@Override
	public void tick()
	{
		if(!Minecraft.getMinecraft().running)
		{
			MinecraftForge.EVENT_BUS.post(new MinecraftShutdownEvent());
		}
	}

	@Override
	public void garbageCollect() 
	{
		
	}

	@Override
	public Phase getPhase() 
	{
		return Phase.END;
	}

}
