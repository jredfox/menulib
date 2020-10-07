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
	public void close() {}
	
	/**
	 * called when the menu closes and goes into a sub menu
	 */
	@Override
	public void closeSub(){}

	/**
	 * do special and or rnd effects to your gui and open gl via open
	 */
	@Override
	public void open() {}
	
	@Override
	public void openSub() {}

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
	public GuiButton getLeft() 
	{
		GuiBasicButton button = MLConfig.fancyPage ? fancyLButton : lbutton;
		return button;
	}
	
	@Override
	public GuiButton getRight()
	{
		GuiBasicButton button = MLConfig.fancyPage ? fancyRButton : rbutton;
		return button;
	}

	@Override
	public GuiScreen create() 
	{
		try
		{
			this.gui = (GuiScreen) ctr.newInstance();
			return this.get();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public GuiScreen get() 
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
