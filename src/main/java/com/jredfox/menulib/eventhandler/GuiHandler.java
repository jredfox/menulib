package com.jredfox.menulib.eventhandler;

import java.util.List;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.evilnotch.lib.minecraft.event.client.ClientDisconnectEvent;
import com.jredfox.menulib.event.GuiEvent;
import com.jredfox.menulib.event.ShutdownEvent;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.MenuRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class GuiHandler {
	
	public static final GuiFakeMenu fake_menu = new GuiFakeMenu();
	public static GuiScreen old;
	public static boolean flag;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void guiOpenPre(GuiOpenEvent e)
	{
		if(!Minecraft.getMinecraft().running)
			e.setCanceled(true);//stop mods from opening gui on close when the application is closing
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
		MenuRegistry.INSTANCE.sanityCheck(menu);
		MenuRegistry.INSTANCE.open(menu);//open the menu if it's closed
		menu.openGui();
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
			MenuRegistry.INSTANCE.getMenu().closeGui();
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
		
		IMenu menu = MenuRegistry.INSTANCE.getMenu();
		List<GuiButton> li = e.getButtonList();
		GuiButton prev = menu.getPrevious();
		GuiButton next = menu.getNext();
		if(prev != null)
			li.add(prev);
		if(next != null)
			li.add(next);
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
		
		if(previous != null && previous.id == button.id)
			MenuRegistry.INSTANCE.previous();
		else if(next != null && next.id == button.id)
			MenuRegistry.INSTANCE.next();
	}
	
	@SubscribeEvent
	public void close(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
	    Minecraft.getMinecraft().addScheduledTask(() ->
	    {
	    	MenuRegistry.INSTANCE.close(MenuRegistry.INSTANCE.getMenu());
	    });
	}
	
	@SubscribeEvent
	public void shutdown(ShutdownEvent e)
	{
		IMenu menu = MenuRegistry.INSTANCE.getMenu();
		System.out.println("closing IMenu and guis:" + menu);
		if(MenuRegistry.INSTANCE.isDisplaying())
			menu.closeGui();
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if(gui != null)
		{
			gui.onGuiClosed();
		}
		MenuRegistry.INSTANCE.close(menu);
	}
}
