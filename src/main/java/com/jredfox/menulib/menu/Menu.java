package com.jredfox.menulib.menu;

import java.lang.reflect.Constructor;

import org.apache.logging.log4j.core.util.ReflectionUtil;

import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiBasicButton;
import com.jredfox.menulib.mod.MLConfig;
import com.jredfox.menulib.mod.MLConfigButton;
import com.jredfox.menulib.sound.IMusicPlayer;
import com.jredfox.menulib.sound.MusicEmpty;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class Menu implements IMenu {
	
	public ResourceLocation id;
	public Class<? extends GuiScreen> guiClass;
	public IMusicPlayer music;
	public int frames;
	
	//objects
	protected GuiScreen gui;
	protected Constructor ctr;//cache the constructor
	protected boolean enabled = true;
	
	public Menu(ResourceLocation id, Class<? extends GuiScreen> guiClass)
	{
		this(id, guiClass, MusicEmpty.musicPlayer);
	}
	
	public Menu(ResourceLocation id, Class<? extends GuiScreen> guiClass, IMusicPlayer music)
	{
		this(id, guiClass, music, -1);
	}
	
	public Menu(ResourceLocation id, Class<? extends GuiScreen> guiClass, IMusicPlayer music, int frames)
	{
		this.id = id;
		this.guiClass = guiClass;
		try
		{
			this.ctr = ReflectionUtil.getDefaultConstructor(this.guiClass);
		}
		catch(Exception e)
		{
			
		}
		this.music = music;
		this.frames = frames;
	}
	
	@Override
	public ResourceLocation getId() 
	{
		return this.id;
	}
	
	@Override
	public Class<? extends GuiScreen> getGuiClass() 
	{
		return this.guiClass;
	}
	
	@Override
	public GuiScreen get() 
	{
		return this.gui;
	}
	
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
	public void open() {}

	@Override
	public void close() {}
	
	@Override
	public void switchMenu()
	{
		this.gui = null;
	}
	
	@Override
	public boolean isEnabled() 
	{
		return this.enabled;
	}

	@Override
	public void setEnabled(boolean enabled) 
	{
		this.enabled = enabled;
		MenuRegistry.INSTANCE.update(this);
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
	
	public static final GuiBasicButton previous = new GuiBasicButton(MLConfigButton.previousId, MLConfigButton.previousX, MLConfigButton.previousY, MLConfigButton.previousWidth, MLConfigButton.previousHeight, "menulib.previous.name");
	public static final GuiBasicButton previousFancy = new GuiBasicButton(MLConfigButton.previousId,  MLConfigButton.fPreviousX, MLConfigButton.fPreviousY, MLConfigButton.fPreviousWidth, MLConfigButton.fPreviousHeight, "menulib.previousfancy.name");
	public static final GuiBasicButton next = new GuiBasicButton(MLConfigButton.nextId,  MLConfigButton.nextX, MLConfigButton.nextY, MLConfigButton.nextWidth, MLConfigButton.nextHeight, "menulib.next.name");
	public static final GuiBasicButton nextFancy = new GuiBasicButton(MLConfigButton.nextId, MLConfigButton.fNextX, MLConfigButton.fNextY, MLConfigButton.fNextWidth, MLConfigButton.fNextHeight, "menulib.nextfancy.name");
	
	@Override
	public GuiButton getPrevious() 
	{
		return !MLConfigButton.fancyPage ? previous : previousFancy;
	}

	@Override
	public GuiButton getNext() 
	{
		return !MLConfigButton.fancyPage ? next : nextFancy;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof IMenu))
			return false;
		return this.getId().equals( ((IMenu)other).getId() );
	}
	
	@Override
	public int hashCode()
	{
		return this.getId().hashCode();
	}
	
	@Override
	public String toString()
	{
		return this.getId().toString();
	}

}
