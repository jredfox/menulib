package com.jredfox.menulib.menu;

import java.lang.reflect.Constructor;

import org.apache.logging.log4j.core.util.ReflectionUtil;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiBasicButton;
import com.jredfox.menulib.mod.MLConfig;
import com.jredfox.menulib.sound.IMusicPlayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class Menu implements IMenu {
	
	public ResourceLocation id;
	public Class<? extends GuiScreen> guiClass;
	public IMusicPlayer music;
	public int frames;
	
	//objects
	public GuiScreen gui;
	public Constructor ctr;//cache the constructor
	
	public Menu(ResourceLocation id, Class<? extends GuiScreen> guiClass)
	{
		this(id, guiClass, null);
	}
	
	public Menu(ResourceLocation id, Class<? extends GuiScreen> guiClass, IMusicPlayer music)
	{
		this(id, guiClass, music, -1);
	}
	
	public Menu(ResourceLocation id, Class<? extends GuiScreen> guiClass, IMusicPlayer music, int frames)
	{
		this.id = id;
		this.guiClass = guiClass;
		this.music = music;
		this.frames = frames;
		this.ctr = ReflectionUtil.getDefaultConstructor(this.guiClass);
	}

	@Override
	public void close() {}

	@Override
	public void open() {}

	@Override
	public GuiScreen create() 
	{
		try
		{
			this.gui = (GuiScreen) this.ctr.newInstance();
			return this.get();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public GuiScreen get() 
	{
		return this.gui;
	}

	@Override
	public Class<? extends GuiScreen> getGuiClass() 
	{
		return this.guiClass;
	}

	@Override
	public ResourceLocation getId() 
	{
		return this.id;
	}
	
	@Override
	public IMusicPlayer getMusicPlayer() 
	{
		return this.music;
	}

	@Override
	public int getFrames() 
	{
		return this.frames;
	}
	
	public static final GuiBasicButton previous = new GuiBasicButton(MLConfig.leftButtonId, MLConfig.leftButtonPosX, MLConfig.leftButtonPosY, MLConfig.leftButtonWidth, MLConfig.leftButtonHeight, "menulib.previous.name");
	public static final GuiBasicButton next = new GuiBasicButton(MLConfig.rightButtonId,  MLConfig.rightButtonPosX, MLConfig.rightButtonPosY, MLConfig.rightButtonWidth, MLConfig.rightButtonHeight, "menulib.next.name");
	public static final GuiBasicButton previousFancy = new GuiBasicButton(MLConfig.leftButtonId,  MLConfig.lFButtonPosX, MLConfig.lFButtonPosY, MLConfig.lFButtonWidth, MLConfig.lFButtonHeight, "menulib.previousFancy.name");
	public static final GuiBasicButton nextFancy = new GuiBasicButton(MLConfig.rightButtonId, MLConfig.rFButtonPosX, MLConfig.rFButtonPosY, MLConfig.rFButtonWidth, MLConfig.rFButtonHeight, "menulib.nextFancy.name");

	static
	{
		if(!MLConfig.isLoaded)
			throw new RuntimeException("MLConfig cannot be hotloaded!");
	}
	
	@Override
	public GuiButton getPrevious() 
	{
		return !MLConfig.fancyPage ? previous : previousFancy;
	}

	@Override
	public GuiButton getNext() 
	{
		return !MLConfig.fancyPage ? next : nextFancy;
	}

}
