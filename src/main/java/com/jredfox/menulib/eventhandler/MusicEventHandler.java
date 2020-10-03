package com.jredfox.menulib.eventhandler;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiMainMenuBase;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.event.MenuMusicEvent;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicEventHandler {
	
	@SubscribeEvent
	public void canPlayMusic(MenuMusicEvent e)
	{
		//only touch vanilla music here
		if(!e.isVanillaTicker)
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
	}

}
