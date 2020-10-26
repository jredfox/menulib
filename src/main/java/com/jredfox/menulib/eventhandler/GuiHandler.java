package com.jredfox.menulib.eventhandler;

import java.util.List;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiBasicButton;
import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.event.GuiEvent;
import com.jredfox.menulib.event.MenuEvent;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.Menu;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.mod.MLConfig;
import com.jredfox.menulib.mod.MLConfigButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiHandler {
	
	public static final GuiFakeMenu fake_menu = new GuiFakeMenu();
	public static GuiScreen old;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void guiOpenPre(GuiOpenEvent e)
	{
		old = Minecraft.getMinecraft().currentScreen;
		if(!MenuRegistry.INSTANCE.shouldReplace(e.getGui()))
			return;
		e.setGui(fake_menu);
	}
	
	/**
	 * set gui after mods are stopping looking for the main screen
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void guiOpen(GuiOpenEvent e)
	{	
		if(!(e.getGui() instanceof GuiFakeMenu))
			return;
		IMenu menu = MenuRegistry.INSTANCE.getMenu();
		if(!menu.isEnabled())
			throw new RuntimeException("cannot display a disabled IMenu:" + menu.getId() + " why are you not using MenuRegistry methods?");
		else if(!MenuRegistry.INSTANCE.menus.contains(menu))
			throw new RuntimeException("an unregistred IMenu has been attempted to be set to the current menu:" + menu.getId());
		MenuRegistry.INSTANCE.open(menu);
		e.setGui(MenuRegistry.INSTANCE.getGuiOpen());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void fireClose(GuiOpenEvent e)
	{
		if(old != null && old != e.getGui())
			MinecraftForge.EVENT_BUS.post(new GuiEvent.Close(old, e.getGui()));
	}
	
	@SubscribeEvent
	public void menuClose(GuiEvent.Close event)
	{
		if(MenuRegistry.INSTANCE.getGui() == event.gui)
		{
			MenuRegistry.INSTANCE.close(MenuRegistry.INSTANCE.getMenu());
		}
	}
	
	/**
	 * add left and right button to this gui if it's not already added
	 */
	@SubscribeEvent
	public void guiInit(GuiScreenEvent.InitGuiEvent.Post e)
	{
		if(e.getGui() != MenuRegistry.INSTANCE.getGui())
			return;
		if(MenuRegistry.INSTANCE.isBrowsable())
		{
			IMenu menu = MenuRegistry.INSTANCE.getMenu();
			List<GuiButton> li = e.getButtonList();
			GuiButton prev = menu.getPrevious();
			GuiButton next = menu.getNext();
			if(prev != null)
				li.add(prev);
			if(next != null)
				li.add(next);
		}
	}
	
	@SubscribeEvent
	public void buttonClick(GuiScreenEvent.ActionPerformedEvent.Pre e)
	{
		if(e.getGui() != MenuRegistry.INSTANCE.getGui())
			return;
		
		IMenu menu = MenuRegistry.INSTANCE.getMenu();
		GuiButton button = e.getButton();
		GuiButton previous = menu.getPrevious();
		GuiButton next = menu.getNext();
		
		//modders may have a custom button id so support whatever button id they may have
		if(previous != null && previous.id == button.id)
		{
			MenuRegistry.INSTANCE.previous();
			MLConfig.saveMenuIndex();//keep the save separate so modders can switch multiple times without lag
		}
		else if(next != null && next.id == button.id)
		{
			MenuRegistry.INSTANCE.next();
			MLConfig.saveMenuIndex();
		}
	}

}
