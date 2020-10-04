package com.jredfox.menulib.eventhandler;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiMainMenuBase;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.event.MusicMenuEvent;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicHandler {
	
	@SubscribeEvent
	public void canPlayMusic(MusicMenuEvent e)
	{
		//only touch vanilla music here
		if(!e.vanillaTicker)
		{
			return;
		}
		
		for(Class c : MLConfig.musicDeny)
		{
			if(JavaUtil.isClassExtending(c, e.gui.getClass()))
			{
				e.canPlay = false;
				return;
			}
		}
		for(Class c : MLConfig.musicAllow)
		{
			if(JavaUtil.isClassExtending(c, e.gui.getClass()))
			{
				e.canPlay = true;
				break;
			}
		}
		
		if(e.gui instanceof GuiMainMenuBase)
		{
			e.canPlay = ((GuiMainMenuBase)e.gui).allowMusic;
		}
//		System.out.println("e.canPlay:" + e.canPlay);
	}

}
