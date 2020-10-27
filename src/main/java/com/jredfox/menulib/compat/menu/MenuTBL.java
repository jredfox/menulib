package com.jredfox.menulib.compat.menu;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.minecraft.basicmc.client.gui.GuiBasicButton;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.Menu;
import com.jredfox.menulib.mod.MLConfigButton;
import com.jredfox.menulib.sound.IMusicPlayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MenuTBL extends Menu{
	
	public static Class tbl_musicHandler = ReflectionUtil.classForName("thebetweenlands.client.handler.MusicHandler");
	public static Object tbl_instance = ReflectionUtil.getObject(null, tbl_musicHandler, "INSTANCE");
	
	public MenuTBL()
	{
		super(new ResourceLocation("tbl:swamp"), ReflectionUtil.classForName("thebetweenlands.client.gui.menu.GuiBLMainMenu"));
	}

	@Override
	public void switchMenu()
	{
		super.switchMenu();//set the gui to null
		ReflectionUtil.setObject(tbl_instance, false, tbl_musicHandler, "hasBlMainMenu");
	}
}
