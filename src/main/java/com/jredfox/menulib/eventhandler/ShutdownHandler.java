package com.jredfox.menulib.eventhandler;

import com.evilnotch.lib.minecraft.tick.ITick;
import com.jredfox.menulib.event.ShutdownEvent;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ShutdownHandler implements ITick{

	public static boolean hasShutdown;
	@Override
	public void tick()
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(!mc.running && !hasShutdown)
		{
			try
			{
				MinecraftForge.EVENT_BUS.post(new ShutdownEvent());
				if(!mc.running)
					hasShutdown = true;
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
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
