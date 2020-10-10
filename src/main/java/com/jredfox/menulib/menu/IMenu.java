package com.jredfox.menulib.menu;

import com.jredfox.menulib.coremod.MLCoreConfig;
import com.jredfox.menulib.sound.IMusicPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMenu {
	/**
	 * called right after menu closes
	 */
	public void close();
	/**
	 * called when the menu opens
	 */
	public void open();
	/**
	 * this creates a new gui called when a new main menu gets created(opened)
	 */
	public GuiScreen create();
	/**
	 * grabs current gui populated from createGui()
	 */
	public GuiScreen get();
	/**
	 * get the gui class
	 */
	public Class<? extends GuiScreen> getGuiClass();
	/**
	 * this is the resource location to identify your main menu
	 */
	public ResourceLocation getId();
	/**
	 * setting this to above -1 will result in a fixed framerate rather then whatever the game values are set to
	 */
	public int getFrames();
	/**
	 * return null for no music
	 */
	public IMusicPlayer getMusic();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getLeft();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getRight();

}
