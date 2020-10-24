package com.jredfox.menulib.compat.menu;

import java.lang.reflect.Method;

import com.evilnotch.lib.api.ReflectionUtil;
import com.jredfox.menulib.compat.util.CMMUtil;
import com.jredfox.menulib.menu.Menu;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MenuCMM extends Menu{

	public MenuCMM()
	{
		super(new ResourceLocation("cmm:mainmenu"), CMMUtil.guiCustom);
	}
	
	@Override
	public void open()
	{
		if(this.gui != null)
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
		CMMUtil.reload();
	}

	public void syncGui() 
	{
		this.gui = CMMUtil.getGui();
	}

}
