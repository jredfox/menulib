package com.evilnotch.menulib.eventhandler;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiMainMenuBase;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.event.MainMenuEvent.MusicEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicEventHandler {
	
	@SubscribeEvent
	public void canPlayMusic(MusicEvent e)
	{
		//only touch vanilla music here
		if(!e.isVanillaTicker)
		{
			return;
		}
		
		for(Class c : ConfigMenu.musicDeny)
		{
			if(JavaUtil.isClassExtending(c, e.gui.getClass()))
			{
				e.canPlay = false;
				return;
			}
		}
		for(Class c : ConfigMenu.musicAllow)
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
