package com.jredfox.menulib.compat;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiFakeMenu;
import com.jredfox.menulib.menu.Menu;

import lumien.custommainmenu.CustomMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MenuCMM extends Menu{

	public MenuCMM()
	{
		super(new ResourceLocation("cmm:menu"), GuiFakeMenu.class);
	}
	
	@Override
	public void open()
	{
		this.syncGui();
	}
	
	@Override
	public GuiScreen create()
	{
		this.reload();
		this.syncGui();
		return this.get();
	}

	public void reload()
	{
		CustomMainMenu.INSTANCE.reload();
	}

	public void syncGui() 
	{
		this.gui = CustomMainMenu.INSTANCE.config.getGUI("mainmenu");
	}

}
