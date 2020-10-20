package com.jredfox.menulib.eventhandler;

import java.util.List;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiHandler {
	
	public static final GuiFakeMenu fake_menu = new GuiFakeMenu();
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOpenPre(GuiOpenEvent e)
	{
		if(!MenuRegistry.isReplaceable(e.getGui()))
			return;
		e.setGui(fake_menu);
	}
	
	/**
	 * set gui after mods are stopping looking for the main screen
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGuiOpen(GuiOpenEvent e)
	{
		if(!(e.getGui() instanceof GuiFakeMenu))
			return;
		IMenu menu = MenuRegistry.getCurrentMenu();
		e.setGui(menu.create());//TODO:
		menu.open();
	}
	
	/**
	 * add left and right button to this gui if it's not already added
	 */
	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post e)
	{
		if(e.getGui() != MenuRegistry.getCurrentGui())
			return;
		if(MenuRegistry.hasButtons())
		{
			IMenu menu = MenuRegistry.getCurrentMenu();
			List<GuiButton> li = e.getButtonList();
			GuiButton prev = menu.getPrevious();
			GuiButton next = menu.getNext();
			if(prev != null && !li.contains(prev))
				li.add(prev);
			if(next != null && !li.contains(next))
				li.add(next);
		}
	}
	
	@SubscribeEvent
	public void guiButtonClick(GuiScreenEvent.ActionPerformedEvent.Pre e)
	{
		if(e.getGui() != MenuRegistry.getCurrentGui())
			return;
		
		IMenu menu = MenuRegistry.getCurrentMenu();
		GuiButton button = e.getButton();
		GuiButton previous = menu.getPrevious();
		GuiButton next = menu.getNext();
		
		//modders may have a custom button id so support whatever button id they may have
		if(previous != null && previous.id == button.id)
		{
			MenuRegistry.advancePreviousMenu();
			MLConfig.saveMenuIndex();//keep the save separate so modders can switch multiple times without lag
		}
		else if(next != null && next.id == button.id)
		{
			MenuRegistry.advanceNextMenu();
			MLConfig.saveMenuIndex();
		}
	}

}
