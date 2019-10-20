package com.evilnotch.menulib.menu;

import java.lang.reflect.Constructor;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiBasicButton;
import com.evilnotch.menulib.ConfigMenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class Menu implements IMenu {
	
	static
	{
		if(!ConfigMenu.isLoaded)
		{
			throw new RuntimeException("ConfigMenu Must be Loaded Beofre Instantiating this class so button vars can be configurable");
		}
	}
	
	public ResourceLocation id = null;
	public Constructor ctr = null;
	public Class clazz = null;
	public GuiScreen gui;
	
	public ResourceLocation bTexture = new ResourceLocation("textures/gui/widgets.png");
	
	public static final GuiBasicButton lbutton = new GuiBasicButton(ConfigMenu.leftButtonId, ConfigMenu.leftButtonPosX, ConfigMenu.leftButtonPosY, ConfigMenu.leftButtonWidth, ConfigMenu.leftButtonHeight, "menulib.lbutton.name");
	public static final GuiBasicButton rbutton = new GuiBasicButton(ConfigMenu.rightButtonId, ConfigMenu.rightButtonPosX, ConfigMenu.rightButtonPosY, ConfigMenu.rightButtonWidth, ConfigMenu.rightButtonHeight, "menulib.rbutton.name");
	
	public static final GuiBasicButton fancyLButton = new GuiBasicButton(ConfigMenu.leftButtonId, ConfigMenu.lFButtonPosX, ConfigMenu.lFButtonPosY, ConfigMenu.lFButtonWidth, ConfigMenu.lFButtonHeight, "menulib.lbuttonfancy.name");
	public static final GuiBasicButton fancyRButton = new GuiBasicButton(ConfigMenu.rightButtonId, ConfigMenu.rFButtonPosX, ConfigMenu.rFButtonPosY, ConfigMenu.rFButtonWidth, ConfigMenu.rFButtonHeight, "menulib.rbuttonfancy.name");

	public Menu(Class<? extends GuiScreen> clazz, ResourceLocation id)
	{
		this.clazz = clazz;
		try 
		{
			this.ctr = clazz.getConstructor();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		this.id = id;
	}

	/**
	 * for clearing open gl special effects on close for stubborn guis
	 */
	@Override
	public void onClose() {}

	/**
	 * do special and or rnd effects to your gui and open gl via open
	 */
	@Override
	public void onOpen() {}
	
	@Override
	public void onOpenFromSub() {}

	@Override
	public ResourceLocation getId() 
	{
		return this.id;
	}

	@Override
	public Class<? extends GuiScreen> getGuiClass() 
	{
		return this.clazz;
	}

	@Override
	public ResourceLocation getButtonTexture() 
	{
		return this.bTexture;
	}
	
	@Override
	public void setButtonTexture(ResourceLocation loc)
	{
		this.bTexture = loc;
	}
	
	@Override
	public GuiButton getLeftButton() 
	{
		GuiBasicButton button = ConfigMenu.fancyPage ? fancyLButton : lbutton;
		button.setButtonTexture(this.getButtonTexture());
		return button;
	}
	
	@Override
	public GuiButton getRightButton()
	{
		GuiBasicButton button = ConfigMenu.fancyPage ? fancyRButton : rbutton;
		button.setButtonTexture(this.getButtonTexture());
		return button;
	}

	@Override
	public GuiScreen createGui() 
	{
		try
		{
			this.gui = (GuiScreen) ctr.newInstance();
			return this.getGui();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public GuiScreen getGui() 
	{
		return this.gui;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof IMenu))
			return false;
		return this.getId().equals(((IMenu)obj).getId());
	}
	
}
