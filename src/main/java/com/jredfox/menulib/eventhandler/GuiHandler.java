package com.jredfox.menulib.eventhandler;

import java.awt.datatransfer.ClipboardOwner;
import java.util.List;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiBasicButton;
import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.evilnotch.lib.minecraft.event.client.ClientDisconnectEvent;
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
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

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
		MenuRegistry.INSTANCE.sanityCheck(menu);
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
		
		//modders may have a custom button id so support whatever button id they may have
		if(previous != null && previous.id == button.id)
		{
			MenuRegistry.INSTANCE.previous();
		}
		else if(next != null && next.id == button.id)
		{
			MenuRegistry.INSTANCE.next();
		}
	}
	
	@SubscribeEvent
	public void guiNullifyer(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
	    Minecraft.getMinecraft().addScheduledTask(() ->
	    {
	    	MenuRegistry.INSTANCE.getMenu().clear();
	    });
	}

}
