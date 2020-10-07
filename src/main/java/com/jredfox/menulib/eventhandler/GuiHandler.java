package com.jredfox.menulib.eventhandler;

import java.util.List;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
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
		GuiScreen gui = e.getGui();
		
		//return from method if gui is null
		if(gui == null || !MenuRegistry.isReplaceable(gui))
		{
			return;
		}
		e.setGui(fake_menu);
	}
	
	/**
	 * set gui after mods are stopping looking for the main screen
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGuiOpen(GuiOpenEvent e)
	{
		GuiScreen gui = e.getGui();
		if(!(gui instanceof GuiFakeMenu))
		{
			return;
		}
		IMenu menu = MenuRegistry.getCurrentMenu();
		//temporary line of code so it works this is not the proper way to open a menu. if it came from a sub menu it shouldn't create a new one each time
		e.setGui(menu.create());
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
			GuiButton lbutton = menu.getLeft();
			GuiButton rbutton = menu.getRight();
			if(lbutton != null && !li.contains(lbutton))
			{
				li.add(lbutton);
			}
			if(rbutton != null && !li.contains(rbutton))
			{
				li.add(rbutton);
			}
		}
	}
	
	@SubscribeEvent
	public void guiButtonClick(GuiScreenEvent.ActionPerformedEvent.Pre e)
	{
		GuiScreen gui = e.getGui();
		if(gui == null || !MenuRegistry.containsMenu(gui.getClass()))
		{
			return;
		}
		
		//advance previous or next menu based upon the button
		int buttonId = e.getButton().id;
		if(buttonId == MLConfig.leftButtonId)
		{
			MenuRegistry.advancePreviousMenu();
			MLConfig.saveMenuIndex();//keep the save index separately for more options on modders
		}
		else if(buttonId == MLConfig.rightButtonId)
		{
			MenuRegistry.advanceNextMenu();
			MLConfig.saveMenuIndex();
		}
	}

}
