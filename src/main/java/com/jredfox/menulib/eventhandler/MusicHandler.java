package com.jredfox.menulib.eventhandler;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiMainMenuBase;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.event.MusicEvent.MusicState;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicHandler {
	
	@SubscribeEvent
	public void canPlayMusic(MusicEvent e)
	{
//		if(!e.tickId.equals(MusicEvent.musicTicker) || e.state != MusicState.MENU)
//			return;
		
		if(e.gui instanceof GuiMainMenuBase)
		{
			e.canPlay = ((GuiMainMenuBase)e.gui).allowMusic;
		}
	}

}
