package com.jredfox.menulib.compat.menu;

import java.lang.reflect.Method;

import com.evilnotch.lib.api.ReflectionUtil;
import com.jredfox.menulib.menu.Menu;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MenuCMM extends Menu{
	
	public static Class<? extends GuiScreen> guiCustom = ReflectionUtil.classForName("lumien.custommainmenu.gui.GuiCustom");
	public static Class customMainMenu = ReflectionUtil.classForName("lumien.custommainmenu.CustomMainMenu");
	public static Object INSTANCE = ReflectionUtil.getObject(null, customMainMenu, "INSTANCE");
	public static Method reload = ReflectionUtil.getMethod(customMainMenu, "reload");
	public static Method getGui = ReflectionUtil.getMethod(ReflectionUtil.classForName("lumien.custommainmenu.configuration.Config"), "getGUI", String.class);
	
	public MenuCMM()
	{
		super(new ResourceLocation("cmm:mainmenu"), guiCustom);
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
	
	public void syncGui() 
	{
		Object config = getConfig();
		this.gui = (GuiScreen) invoke(getGui, config, "mainmenu");
	}
	
	public static void reload()
	{
		invoke(reload, INSTANCE);
	}
	
	public static Object getConfig()
	{
		return ReflectionUtil.getObject(INSTANCE, customMainMenu, "config");
	}
	
	public static Object invoke(Method method, Object instance, Object... params)
	{
		try
		{
			return method.invoke(instance, params);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		return null;
	}

}
