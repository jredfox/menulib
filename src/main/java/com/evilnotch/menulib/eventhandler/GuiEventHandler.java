package com.evilnotch.menulib.eventhandler;

import java.util.List;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.menu.IMenu;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventHandler {
	
	/**
	 * set the gui to something mods are never going to be looking at
	 */
	public static GuiScreen cachedGui = null;
	public static final GuiFakeMenu fake_menu = new GuiFakeMenu();
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOpenPre(GuiOpenEvent e)
	{
		GuiScreen gui = e.getGui();
		if(gui == null)
		{
			return;
		}
		if(!(gui instanceof GuiMainMenu) && !MenuRegistry.containsMenu(gui.getClass() ))
		{
			return;
		}
		cachedGui = gui;
		e.setGui(fake_menu);
	}
	
	/**
	 * set gui after mods are stopping looking for the main screen
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGuiOpen(GuiOpenEvent e)
	{
		GuiScreen gui = e.getGui();
		if(gui == null)
		{
			return;
		}
		if(!(gui instanceof GuiFakeMenu))
		{
			return;
		}
		boolean flag = MenuRegistry.getCurrentGui() == cachedGui && cachedGui != null;
		GuiScreen replacedGui = flag ? cachedGui : MenuRegistry.createCurrentGui();
		e.setGui(replacedGui);
		IMenu menu = MenuRegistry.getCurrentMenu();
		if(!flag)
		{
			menu.onOpen();
		}
		else
		{
			menu.onOpenFromSub();
		}
	}
	
	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post e)
	{
		GuiScreen gui = e.getGui();
		if(gui == null)
		{
			return;
		}
		if(MenuRegistry.getMenuSize() > 1)
		{
			Class clazz = gui.getClass();
			if(!MenuRegistry.containsMenu(clazz))
			{
				return;
			}
			IMenu menu = MenuRegistry.getCurrentMenu();
			List<GuiButton> li = e.getButtonList();
			GuiButton lbutton = menu.getLeftButton();
			GuiButton rbutton = menu.getRightButton();
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
		
		if(e.getButton().id == ConfigMenu.leftButtonId)
		{
			MenuRegistry.advancePreviousMenu();
			ConfigMenu.saveMenuIndex();//keep the save index separately for more options on modders
		}
		else if(e.getButton().id == ConfigMenu.rightButtonId)
		{
			MenuRegistry.advanceNextMenu();
			ConfigMenu.saveMenuIndex();
		}
	}

}
