package com.evilnotch.menulib;

import com.evilnotch.lib.minecraft.tick.ITick;
import com.evilnotch.lib.minecraft.tick.TickRegistry;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class TickTest implements ITick{

	@Override
	public void tick() 
	{
		if(TickRegistry.isRightTickClient(20))
		{
			System.out.println("Client FrameRate:" + Minecraft.getMinecraft().getLimitFramerate());
		}
	}

	@Override
	public void garbageCollect() {}

	@Override
	public Phase getPhase() {
		return Phase.END;
	}

}
