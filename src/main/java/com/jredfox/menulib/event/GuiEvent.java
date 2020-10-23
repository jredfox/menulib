package com.jredfox.menulib.event;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GuiEvent extends Event {
	
	public GuiScreen gui;
	
	public GuiEvent(GuiScreen gui)
	{
		this.gui = gui;
	}
	
	@Cancelable
	public static class Open extends GuiEvent
	{
		public final GuiScreen old;
		
		public Open(GuiScreen gui, GuiScreen old)
		{
			super(gui);
			this.old = old;
		}
	}
	
	public static class Close extends GuiEvent
	{
		public GuiScreen screen;
		public Close(GuiScreen closing, GuiScreen screen)
		{
			super(closing);
			this.screen = screen;
		}
	}

}
