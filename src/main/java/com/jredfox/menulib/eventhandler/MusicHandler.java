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
	
	public static final ResourceLocation vanilla = new ResourceLocation("minecraft:menu");
	@SubscribeEvent
	public void canPlayMusic(MusicEvent e)
	{
		if(!e.musicId.equals(vanilla) || e.state != MusicState.MENU)
			return;
		
		System.out.println("music firing in the menus....");
	}

}
