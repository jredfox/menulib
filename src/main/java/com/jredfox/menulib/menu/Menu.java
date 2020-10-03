package com.jredfox.menulib.menu;

import java.lang.reflect.Constructor;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiBasicButton;
import com.jredfox.menulib.mod.MLConfig;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class Menu implements IMenu {
	
	static
	{
		if(!MLConfig.isLoaded)
		{
			throw new RuntimeException("ConfigMenu Must be Loaded Beofre Instantiating this class so button vars can be configurable");
		}
	}
	
	public ResourceLocation id = null;
	public Constructor ctr = null;
	public Class clazz = null;
	public GuiScreen gui;
	
	public ResourceLocation bTexture = new ResourceLocation("textures/gui/widgets.png");
	
	public static final GuiBasicButton lbutton = new GuiBasicButton(MLConfig.leftButtonId, MLConfig.leftButtonPosX, MLConfig.leftButtonPosY, MLConfig.leftButtonWidth, MLConfig.leftButtonHeight, "menulib.lbutton.name");
	public static final GuiBasicButton rbutton = new GuiBasicButton(MLConfig.rightButtonId, MLConfig.rightButtonPosX, MLConfig.rightButtonPosY, MLConfig.rightButtonWidth, MLConfig.rightButtonHeight, "menulib.rbutton.name");
	
	public static final GuiBasicButton fancyLButton = new GuiBasicButton(MLConfig.leftButtonId, MLConfig.lFButtonPosX, MLConfig.lFButtonPosY, MLConfig.lFButtonWidth, MLConfig.lFButtonHeight, "menulib.lbuttonfancy.name");
	public static final GuiBasicButton fancyRButton = new GuiBasicButton(MLConfig.rightButtonId, MLConfig.rFButtonPosX, MLConfig.rFButtonPosY, MLConfig.rFButtonWidth, MLConfig.rFButtonHeight, "menulib.rbuttonfancy.name");

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
	 * called when menu switches
	 */
	@Override
	public void onClose() {}
	
	/**
	 * called when the menu closes and goes into a sub menu
	 */
	@Override
	public void onCloseFromSub(){}

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
		GuiBasicButton button = MLConfig.fancyPage ? fancyLButton : lbutton;
		button.setButtonTexture(this.getButtonTexture());
		return button;
	}
	
	@Override
	public GuiButton getRightButton()
	{
		GuiBasicButton button = MLConfig.fancyPage ? fancyRButton : rbutton;
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
	
	@Override
	public String toString()
	{
		return this.getId().toString();
	}
	
}