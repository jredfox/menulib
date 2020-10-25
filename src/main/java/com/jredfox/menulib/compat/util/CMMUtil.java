package com.jredfox.menulib.compat.util;

import java.lang.reflect.Method;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.main.loader.LoaderMain;
import com.jredfox.menulib.proxy.ModProxy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.Loader;

public class CMMUtil {
	
	public static Class<? extends GuiScreen> guiCustom = ReflectionUtil.classForName("lumien.custommainmenu.gui.GuiCustom");
	public static Class customMainMenu = ReflectionUtil.classForName("lumien.custommainmenu.CustomMainMenu");
	public static Object INSTANCE = ReflectionUtil.getObject(null, customMainMenu, "INSTANCE");
	public static Method reload = ReflectionUtil.getMethod(customMainMenu, "reload");
	public static Method getGui = ReflectionUtil.getMethod(ReflectionUtil.classForName("lumien.custommainmenu.configuration.Config"), "getGUI", String.class);
	
	public static void reload()
	{
		invoke(reload, INSTANCE);
	}
	
	public static Object getConfig()
	{
		return ReflectionUtil.getObject(INSTANCE, customMainMenu, "config");
	}
	
	public static GuiScreen getGui() 
	{
		Object config = getConfig();
		return (GuiScreen) invoke(getGui, config, "mainmenu");
	}
	
	/**
	 * here cause evil notch lib is bugged
	 */
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
