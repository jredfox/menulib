package com.jredfox.menulib.menu;

import java.util.Set;

import com.jredfox.menulib.sound.IMusicPlayer;
import com.jredfox.menulib.sound.IMusicPlayerHolder;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMenu extends IMusicPlayerHolder{
	/**
	 * this is the resource location to identify your main menu
	 */
	public ResourceLocation getId();
	/**
	 * get the gui class
	 */
	public Class<? extends GuiScreen> getGuiClass();
	/**
	 * grabs current gui populated from createGui()
	 */
	public GuiScreen get();
	/**
	 * this creates a new gui and sets the gui field
	 */
	public GuiScreen create();
	/**
	 * called when the menu opens
	 */
	public void open();
	/**
	 * called when the menu closes
	 */
	public void close();
	/**
	 * fired when your IMenu gets switched
	 */
	public void switchMenu();
	/**
	 * is your menu enabled
	 */
	public boolean isEnabled();
	/**
	 * make your menu enabled or disabled
	 */
	public void setEnabled(boolean enabled);
	/**
	 * setting this to above -1 will result in a fixed framerate rather then whatever the game values are set to
	 */
	public int getFrames();
	/**
	 * return MusicPlayerEmpty.musicPlayer for no music
	 */
	public IMusicPlayer getMusicPlayer();
	/**
	 * do not return a new button each getter call as IMenus need a direct link to sync browsing button data for enabling/disabling
	 */
	public GuiButton getPrevious();
	/**
	 * do not return a new button each getter call as IMenus need a direct link to sync browsing button data for enabling/disabling
	 */
	public GuiButton getNext();
}
