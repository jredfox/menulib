package com.jredfox.menulib.eventhandler;

import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.misc.GameState;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicHandler {
	
	public static final ResourceLocation vanilla = new ResourceLocation("minecraft:menu");
	@SubscribeEvent
	public void canPlayMusic(MusicEvent e)
	{
		if(!e.musicId.equals(vanilla) || e.state != GameState.MENU)
			return;
		
		System.out.println("music firing in the menus....");
	}

}
