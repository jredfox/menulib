package com.jredfox.menulib.menu;

import com.jredfox.menulib.sound.IMusicPlayer;
import com.jredfox.menulib.sound.IMusicPlayerHolder;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMenu extends IMusicPlayerHolder{
	/**
	 * called right after menu closes
	 */
	public void close();
	/**
	 * called when the menu opens
	 */
	public void open();
	/**
	 * this creates a new gui and sets the gui field
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
	 * return MusicPlayerEmpty.musicPlayer for no music
	 */
	public IMusicPlayer getMusicPlayer();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getPrevious();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getNext();

}
