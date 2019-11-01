package com.evilnotch.menulib.eventhandler;

import java.util.List;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.event.MainMenuEvent;
import com.evilnotch.menulib.menu.IMenu;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventHandler {
	/**
	 * the last IMenu opened
	 */
	public static IMenu lastMenu;
	public static final GuiFakeMenu fake_menu = new GuiFakeMenu();
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOpenPre(GuiOpenEvent e)
	{
		if(!MenuRegistry.hasInit())
		{
			System.out.println("GuiOpenEvent firing before MenuRegistry has been initialized returning!");
			return;
		}
		GuiScreen gui = e.getGui();
		boolean isReplaceable = MenuRegistry.isReplaceable(gui);
		GuiScreen compare = isReplaceable ? MenuRegistry.getCurrentGui() : gui;
		GuiScreen lastGui = MenuRegistry.getGui(lastMenu);
		if(isReplaceable && lastGui != compare && lastGui != null)
		{
			lastMenu.onClose();
		}
		else if(lastGui == Minecraft.getMinecraft().currentScreen && lastGui != null)
		{
			lastMenu.onCloseFromSub();
		}
		
		//return from method if gui is null or not a main menu
		if(gui == null || !isReplaceable)
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
		boolean sub = MenuRegistry.getGui(lastMenu) == menu.getGui() && lastMenu != null;
		if(!sub)
		{
			MainMenuEvent.Open open = new MainMenuEvent.Open(menu);
			menu = open.menu;//in case it gets replaced
			gui = menu.createGui();
			if(!MinecraftForge.EVENT_BUS.post(open))
			{
				menu.onOpen();
			}
		}
		else
		{
			MainMenuEvent.OnOpenFromSub onOpenFromSub = new MainMenuEvent.OnOpenFromSub(menu);
			if(!MinecraftForge.EVENT_BUS.post(onOpenFromSub))
			{
				menu.onOpenFromSub();
			}
			gui = menu.getGui();
		}
		lastMenu = menu;
		e.setGui(gui);
	}
	
	/**
	 * add left and right button to this gui if it's not already added
	 */
	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post e)
	{
		GuiScreen gui = e.getGui();
		if(gui == null || !MenuRegistry.containsMenu(gui.getClass() ))
		{
			return;
		}
		if(MenuRegistry.hasButtons())
		{
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
		IMenu menu = MenuRegistry.getCurrentMenu();
		int bId = e.getButton().id;
		if(bId == menu.getLeftButton().id)
		{
			MenuRegistry.advancePreviousMenu();
		}
		else if(bId == menu.getRightButton().id)
		{
			MenuRegistry.advanceNextMenu();
		}
	}

}
