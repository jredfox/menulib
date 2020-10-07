package com.jredfox.menulib.eventhandler;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiMainMenuBase;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicHandler {
	
	public static final ResourceLocation vanilla = new ResourceLocation("minecraft:music");
	@SubscribeEvent
	public void canPlayMusic(MusicEvent e)
	{
		if(!e.tickId.equals(vanilla) || e.type != e.type.MENU)
			return;
		
		if(e.gui instanceof GuiMainMenuBase)
		{
			e.canPlay = ((GuiMainMenuBase)e.gui).allowMusic;
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
				return;
			}
		}
	}

}
